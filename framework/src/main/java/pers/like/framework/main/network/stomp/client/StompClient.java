package pers.like.framework.main.network.stomp.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import pers.like.framework.main.network.stomp.ConnectionProvider;
import pers.like.framework.main.network.stomp.LifecycleEvent;
import pers.like.framework.main.network.stomp.StompHeader;
import pers.like.framework.main.util.Logger;

/**
 * @author like0
 */
@SuppressWarnings("ALL")
public class StompClient {

    private static final String TAG = StompClient.class.getSimpleName();

    public static final String SUPPORTED_VERSIONS = "1.1,1.0";
    public static final String DEFAULT_ACK = "auto";

    private final String tag = "stomp";
    private final ConnectionProvider mConnectionProvider;
    private ConcurrentHashMap<String, String> mTopics;
    private boolean mConnected;
    private boolean isConnecting;
    private boolean legacyWhitespace;

    private PublishSubject<StompMessage> mMessageStream;
    private ConcurrentHashMap<String, Flowable<StompMessage>> mStreamMap;
    private final BehaviorSubject<Boolean> mConnectionStream;
    private Disposable mLifecycleDisposable;
    private Disposable mMessagesDisposable;
    private Disposable mHeartbeatDisposable;
    private int heartbeat = 30000;

    public StompClient(ConnectionProvider connectionProvider) {
        mConnectionProvider = connectionProvider;
        mMessageStream = PublishSubject.create();
        mStreamMap = new ConcurrentHashMap<>();
        mConnectionStream = BehaviorSubject.createDefault(false);
    }

    public void setHeartbeat(int ms) {
        heartbeat = ms;
        mConnectionProvider.setHeartbeat(ms).subscribe();
    }

    public synchronized void connect() {
        if (mConnected || isConnecting) {
            return;
        }
        isConnecting = true;
        mLifecycleDisposable = mConnectionProvider.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            List<StompHeader> headers = new ArrayList<>();
                            headers.add(new StompHeader(StompHeader.VERSION, SUPPORTED_VERSIONS));
                            headers.add(new StompHeader(StompHeader.HEART_BEAT, "60000,60000"));
                            mConnectionProvider.send(new StompMessage(StompCommand.CONNECT, headers, null).compile(legacyWhitespace)).subscribe();
                            break;
                        case CLOSED:
                            setConnected(false);
                            isConnecting = false;
                            break;
                        case ERROR:
                            setConnected(false);
                            isConnecting = false;
                            break;
                        default:
                    }
                });

        mMessagesDisposable = mConnectionProvider.messages()
                .map(StompMessage::from)
                .doOnNext(this::callSubscribers)
                .filter(msg -> msg.getStompCommand().equals(StompCommand.CONNECTED))
                .subscribe(stompMessage -> {
                    setConnected(true);
                    isConnecting = false;
                });
//        mHeartbeatDisposable = Observable.interval(heartbeat, TimeUnit.MILLISECONDS).subscribe(a -> {
//            if (mConnected) {
//                Logger.e(TAG, "heartbeat PING!");
//                List<StompHeader> headers = new ArrayList<>();
//                headers.add(new StompHeader(StompHeader.DESTINATION, "/"));
//                mConnectionProvider.send(new StompMessage(StompCommand.SEND, headers, "\n").compile()).subscribe();
//            }
//        });
    }

    private void setConnected(boolean connected) {
        mConnected = connected;
        mConnectionStream.onNext(mConnected);
    }

    public void reconnect() {
        disconnectCompletable().subscribe(() -> connect(), e -> Logger.e(tag, "Disconnect error"));
    }

    public Completable send(String destination) {
        return send(destination, null);
    }

    public Completable send(String destination, String data) {
        return send(new StompMessage(StompCommand.SEND,
                Collections.singletonList(new StompHeader(StompHeader.DESTINATION, destination)), data));
    }

    public Completable send(@NonNull StompMessage stompMessage) {
        Completable completable = mConnectionProvider.send(stompMessage.compile(legacyWhitespace));
        CompletableSource connectionComplete = mConnectionStream
                .filter(isConnected -> isConnected)
                .firstOrError().toCompletable();
        return completable.startWith(connectionComplete);
    }

    private void callSubscribers(StompMessage stompMessage) {
        mMessageStream.onNext(stompMessage);
    }

    public Flowable<LifecycleEvent> lifecycle() {
        return mConnectionProvider.lifecycle().toFlowable(BackpressureStrategy.BUFFER);
    }

    public void disconnect() {
        disconnectCompletable().subscribe(() -> {
        }, e -> Logger.e(tag, "Disconnect error"));
    }

    public Completable disconnectCompletable() {
        if (mLifecycleDisposable != null) {
            mLifecycleDisposable.dispose();
        }
        if (mMessagesDisposable != null) {
            mMessagesDisposable.dispose();
        }
        return mConnectionProvider.disconnect().doOnComplete(() -> setConnected(false));
    }

    public Flowable<StompMessage> topic(String destinationPath) {
        return topic(destinationPath, null);
    }

    public Flowable<StompMessage> topic(@NonNull String destPath, List<StompHeader> headerList) {
        if (destPath == null) {
            return Flowable.error(new IllegalArgumentException("Topic path cannot be null"));
        } else if (!mStreamMap.containsKey(destPath)) {
            mStreamMap.put(destPath, mMessageStream
                    .filter(msg -> matches(destPath, msg))
                    .toFlowable(BackpressureStrategy.BUFFER)
                    .doOnSubscribe(disposable -> subscribePath(destPath, headerList).subscribe())
                    .doFinally(() -> {
                        try {
                            unsubscribePath(destPath).subscribe();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    })
                    .share()
            );
        }
        return mStreamMap.get(destPath);
    }

    public Flowable<StompMessage> topicSimply(String destinationPath) {
        if (destinationPath == null) {
            return Flowable.error(new IllegalArgumentException("Topic path cannot be null"));
        } else {
            return mMessageStream
                    .filter(msg -> matches(destinationPath, msg))
                    .toFlowable(BackpressureStrategy.BUFFER)
                    .doOnSubscribe(disposable -> subscribePathSimply(destinationPath).subscribe())
                    .share();
        }
    }

    private Completable subscribePathSimply(String destinationPath) {
        String topicId = UUID.randomUUID().toString();
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(StompHeader.ID, topicId));
        headers.add(new StompHeader(StompHeader.DESTINATION, destinationPath));
        headers.add(new StompHeader(StompHeader.ACK, DEFAULT_ACK));
        return send(new StompMessage(StompCommand.SUBSCRIBE, headers, null));
    }

    public void setLegacyWhitespace(boolean legacyWhitespace) {
        this.legacyWhitespace = legacyWhitespace;
    }

    private boolean matches(String path, StompMessage msg) {
        String dest = msg.findHeader(StompHeader.DESTINATION);
        if (dest == null) {
            return false;
        }
        boolean ret = path.equals(dest);
        return ret;
    }

    private Completable subscribePath(String destinationPath, @Nullable List<StompHeader> headerList) {
        String topicId = UUID.randomUUID().toString();
        if (mTopics == null) {
            mTopics = new ConcurrentHashMap<>();
        }
        if (mTopics.containsKey(destinationPath)) {
            Logger.e(TAG, "Attempted to subscribe to already-subscribed path!");
            return Completable.complete();
        }
        mTopics.put(destinationPath, topicId);
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(StompHeader.ID, topicId));
        headers.add(new StompHeader(StompHeader.DESTINATION, destinationPath));
        headers.add(new StompHeader(StompHeader.ACK, DEFAULT_ACK));
        if (headerList != null) {
            headers.addAll(headerList);
        }
        return send(new StompMessage(StompCommand.SUBSCRIBE, headers, null));
    }


    private Completable unsubscribePath(String dest) {
        mStreamMap.remove(dest);
        String topicId = mTopics.get(dest);
        mTopics.remove(dest);
        if (TextUtils.isEmpty(topicId) || !isConnected()) {
            return Completable.complete();
        }
        return send(new StompMessage(StompCommand.UNSUBSCRIBE, Collections.singletonList(new StompHeader(StompHeader.ID, topicId)), null));
    }

    public boolean isConnected() {
        return mConnected;
    }

    public boolean isConnecting() {
        return isConnecting;
    }
}
