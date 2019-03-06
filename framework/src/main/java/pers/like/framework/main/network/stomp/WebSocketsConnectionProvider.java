package pers.like.framework.main.network.stomp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import pers.like.framework.main.util.Logger;


/**
 * Created by Like
 */
class WebSocketsConnectionProvider extends AbstractConnectionProvider {

    private static final String TAG = "stomp";

    private final String mUri;
    @NonNull
    private final Map<String, String> mConnectHttpHeaders;

    private WebSocketClient mWebSocketClient;
    private boolean haveConnection, connected = false;
    private TreeMap<String, String> mServerHandshakeHeaders;
    private Disposable mHeartbeatDisposable;

    WebSocketsConnectionProvider(String uri, @Nullable Map<String, String> connectHttpHeaders) {
        mUri = uri;
        mConnectHttpHeaders = connectHttpHeaders != null ? connectHttpHeaders : new HashMap<>();
    }

    @Override
    public void rawDisconnect() {
        try {
            mWebSocketClient.closeBlocking();
        } catch (InterruptedException e) {
            Logger.e(TAG, "Thread interrupted while waiting for Websocket closing: ");
            throw new RuntimeException(e);
        }
    }

    @Override
    void createWebSocketConnection() {
        if (haveConnection) {
            return;
        }
        mWebSocketClient = new WebSocketClient(URI.create(mUri), new Draft_6455(), mConnectHttpHeaders, 0) {

            @Override
            public void onWebsocketHandshakeReceivedAsClient(WebSocket conn, ClientHandshake request, @NonNull ServerHandshake response) {
                mServerHandshakeHeaders = new TreeMap<>();
                Iterator<String> keys = response.iterateHttpFields();
                while (keys.hasNext()) {
                    String key = keys.next();
                    mServerHandshakeHeaders.put(key, response.getFieldValue(key));
                }
            }

            @Override
            public void onOpen(@NonNull ServerHandshake handshakeData) {
                connected = true;
                LifecycleEvent openEvent = new LifecycleEvent(LifecycleEvent.Type.OPENED);
                openEvent.setHandshakeResponseHeaders(mServerHandshakeHeaders);
                emitLifecycleEvent(openEvent);
                mHeartbeatDisposable = Observable.interval(30, TimeUnit.SECONDS).subscribe(a -> {
                    if (connected) {
                        send("\n");
                    }
                });
            }

            @Override
            public void onMessage(String message) {
                emitMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                connected = false;
                Logger.e(TAG, "OnClose: code=" + code + " reason=" + reason + " remote=" + remote);
                emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.CLOSED, reason, code));
                if (mHeartbeatDisposable != null && !mHeartbeatDisposable.isDisposed()) {
                    mHeartbeatDisposable.dispose();
                }
            }

            @Override
            public void onError(Exception ex) {
                connected = false;
                Logger.e(TAG, "OnError");
                haveConnection = false;
                emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.ERROR, ex, -1));
                if (mHeartbeatDisposable != null && !mHeartbeatDisposable.isDisposed()) {
                    mHeartbeatDisposable.dispose();
                }
            }
        };

        if (mUri.startsWith("wss")) {
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, null, null);
                SSLSocketFactory factory = sc.getSocketFactory();
                mWebSocketClient.setSocket(factory.createSocket());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mWebSocketClient.connect();
        haveConnection = true;
    }

    @Override
    void rawSend(String stompMessage) {
        try {
            if (connected) {
                mWebSocketClient.send(stompMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("stomp:[" + stompMessage + "] send failed!\n" + e.getMessage());
        }
    }

    @Override
    Object getSocket() {
        return mWebSocketClient;
    }
}
