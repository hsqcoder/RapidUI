package pers.like.framework.main.network.stomp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import pers.like.framework.main.util.Logger;

/**
 */
abstract class AbstractConnectionProvider implements ConnectionProvider {

    private static final String TAG = "stomp";

    @NonNull
    private final PublishSubject<LifecycleEvent> mLifecycleStream;
    @NonNull
    private final PublishSubject<String> mMessagesStream;

    AbstractConnectionProvider() {
        mLifecycleStream = PublishSubject.create();
        mMessagesStream = PublishSubject.create();
    }

    @NonNull
    @Override
    public Observable<String> messages() {
        Logger.e("STEP", "messages");
        return mMessagesStream.startWith(initSocket().toObservable());
    }

    /**
     * Simply close socket.
     * <p>
     * For example:
     * <pre>
     * webSocket.close();
     * </pre>
     */
    abstract void rawDisconnect();

    @Override
    public Completable disconnect() {
        Logger.e("STEP", "disconnect");
        return Completable.fromAction(this::rawDisconnect);
    }

    private Completable initSocket() {
        Logger.e("STEP", "initSocket");
        return Completable.fromAction(this::createWebSocketConnection);
    }

    @Override
    public Completable setHeartbeat(int ms) {
        return Completable.complete();
    }

    /**
     * Most important method: connects to websocket and notifies program of messages.
     * <p>
     * See implementations in OkHttpConnectionProvider and WebSocketsConnectionProvider.
     */
    abstract void createWebSocketConnection();

    @NonNull
    @Override
    public Completable send(String stompMessage) {
        return Completable.fromCallable(() -> {
            if (getSocket() == null) {
                throw new IllegalStateException("Not connected yet");
            } else {
                Logger.e(TAG, " \n" + stompMessage);
                rawSend(stompMessage);
                return null;
            }
        });
    }

    /**
     * Just a simple message send.
     * <p>
     * For example:
     * <pre>
     * webSocket.send(stompMessage);
     * </pre>
     *
     * @param stompMessage message to send
     */
    abstract void rawSend(String stompMessage);

    /**
     * Get socket object.
     * Used for null checking; this object is expected to be null when the connection is not yet established.
     * <p>
     * For example:
     * <pre>
     * return webSocket;
     * </pre>
     */
    @Nullable
    abstract Object getSocket();

    void emitLifecycleEvent(@NonNull LifecycleEvent lifecycleEvent) {
        Logger.e(TAG, " \n" + lifecycleEvent.getType().name());
        mLifecycleStream.onNext(lifecycleEvent);
    }

    void emitMessage(String stompMessage) {
        Logger.e(TAG, " \n" + stompMessage);
        mMessagesStream.onNext(stompMessage);
    }

    @NonNull
    @Override
    public Observable<LifecycleEvent> lifecycle() {
        return mLifecycleStream;
    }
}
