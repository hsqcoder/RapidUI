package pers.like.framework.main.network.stomp;

import android.annotation.SuppressLint;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.Callback;
import pers.like.framework.main.network.NetworkConfigService;
import pers.like.framework.main.network.stomp.client.StompClient;
import pers.like.framework.main.network.stomp.client.StompMessage;
import pers.like.framework.main.util.Logger;
import pers.like.framework.main.util.StringUtil;

/**
 * @author Like
 */
@SuppressLint("CheckResult")
@SuppressWarnings("unused")
public class SocketClient {

    private final String TAG = "stomp";

    private BaseExecutor mExecutor;
    private StompClient mClient;
    private Disposable mLifeCycleDisposable;
    private boolean isConnected = false;
    private Hashtable<String, Call> cachedTopic = new Hashtable<>();
    private NetworkConfigService networkConfigService;

    public SocketClient(BaseExecutor executor) {
        this.mExecutor = executor;
        networkConfigService = ARouter.getInstance().navigation(NetworkConfigService.class);
    }

    public void connect() {
        if (networkConfigService.enableStomp()) {
            mExecutor.networkIO().execute(this::connectInternal);
        }
    }

    private synchronized boolean connectInternal() {
        if (isConnected) {
            return true;
        }
        CountDownLatch latch = new CountDownLatch(1);
        mClient = Stomp.over(Stomp.Provider.JWS, networkConfigService.stompUrl() + "?" +
                StringUtil.map2url(networkConfigService.commonStompParams()), networkConfigService.commonStompHeaders());
        mLifeCycleDisposable = mClient.lifecycle().subscribe(lifecycleEvent -> {
            if (lifecycleEvent.getType() != LifecycleEvent.Type.OPENED) {
                isConnected = false;
                dispose(mLifeCycleDisposable);
                if (lifecycleEvent.getCode() != 1000) {
                    retry();
                }
            } else {
                isTrying = false;
                latch.countDown();
                reTopic();
            }
        });
        mClient.connect();
        try {
            isConnected = latch.await(3, TimeUnit.SECONDS);
            return isConnected;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void reTopic() {
        if (!cachedTopic.isEmpty()) {
            for (String topic : cachedTopic.keySet()) {
                Call call = cachedTopic.get(topic);
                if (call != null) {
                    call.disposable = mClient.topic(topic).subscribe(stompMessage -> mExecutor.mainThread().execute(() -> {
                        for (Callback<String> callbackItem : call.callbackList) {
                            if (callbackItem != null) {
                                callbackItem.call(stompMessage.getPayload());
                            }
                        }
                    }));
                }
            }
        }
    }

    public void disconnect() {
        isTrying = false;
        if (mClient != null && mClient.isConnected()) {
            mClient.disconnect();
        }
    }

    private void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private int retryTimes = 0;
    private boolean isTrying = false;

    private void retry() {
        if (isTrying) {
            return;
        }
        isTrying = true;
        mExecutor.networkIO().execute(() -> {
            while (!connectInternal() && isTrying) {
                retryTimes++;
                Logger.e(TAG, "webSocket尝试连接第< " + retryTimes + " >次");
                try {
                    Thread.sleep(500 * retryTimes);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            retryTimes = 0;
        });
    }

    public void topic(String topic, Callback<String> callback) {
        if (topic == null || callback == null) {
            return;
        }
        if (cachedTopic.containsKey(topic)) {
            Call call = cachedTopic.get(topic);
            if (call == null) {
                throw new NullPointerException("CachedTopic List should not contains a void-Call");
            }
            if (!call.callbackList.contains(callback)) {
                call.callbackList.add(callback);
            }
        } else {
            List<Callback<String>> callbackList = new ArrayList<>();
            callbackList.add(callback);
            final Call call = new Call();
            call.callbackList = callbackList;
            cachedTopic.put(topic, call);
            if (isConnected) {
                call.disposable = mClient.topic(topic).subscribe(stompMessage -> {
                    mExecutor.mainThread().execute(() -> {
                        for (Callback<String> callbackItem : call.callbackList) {
                            if (callbackItem != null) {
                                callbackItem.call(stompMessage.getPayload());
                            }
                        }
                    });
                });
            } else {
                connect();
            }
        }
    }

    public void unTopic(String topic, Callback<String> callback) {
        if (topic == null || callback == null || !cachedTopic.containsKey(topic)) {
            return;
        }
        Call call = cachedTopic.get(topic);
        if (call == null || call.callbackList == null || !call.callbackList.contains(callback)) {
            return;
        }
        call.callbackList.remove(callback);
        if (call.callbackList.isEmpty()) {
            cachedTopic.remove(topic);
            dispose(call.disposable);
            connect();
        }
    }

    public Flowable<StompMessage> topicSimply(String topic) {
        connect();
        return mClient.topicSimply(topic);
    }

    public Completable send(String topic, String message) {
        connect();
        return mClient.send(topic, message);
    }

    class Call {
        Disposable disposable;
        List<Callback<String>> callbackList;
    }
}
