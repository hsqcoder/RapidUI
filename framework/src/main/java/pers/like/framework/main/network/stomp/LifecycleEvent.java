package pers.like.framework.main.network.stomp;

import android.support.annotation.Nullable;

import java.util.TreeMap;

/**
 * @author Like
 */
@SuppressWarnings("unused")
public class LifecycleEvent {

    public enum Type {
        /**
         * OPENED
         */
        OPENED, CLOSED, ERROR
    }

    private final Type mType;
    @Nullable
    private Exception mException;
    @Nullable
    private String mMessage;

    private int code;

    private TreeMap<String, String> handshakeResponseHeaders = new TreeMap<>();

    public LifecycleEvent(Type type) {
        mType = type;
    }

    public LifecycleEvent(Type type, @Nullable Exception exception, int code) {
        mType = type;
        mException = exception;
        this.code = code;
    }

    public LifecycleEvent(Type type, @Nullable String message, int code) {
        mType = type;
        mMessage = message;
        this.code = code;
    }

    public Type getType() {
        return mType;
    }

    public Exception getException() {
        return mException;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setHandshakeResponseHeaders(TreeMap<String, String> handshakeResponseHeaders) {
        this.handshakeResponseHeaders = handshakeResponseHeaders;
    }

    public TreeMap<String, String> getHandshakeResponseHeaders() {
        return handshakeResponseHeaders;
    }

    public int getCode() {
        return code;
    }
}
