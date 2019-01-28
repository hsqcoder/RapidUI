package pers.like.framework.main.network.resource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pers.like.framework.main.network.Params;

/**
 * @author Like
 */
public class Resource<DATA> {

    public enum Status {
        /**
         * 加载成功
         */
        SUCCESS,
        /**
         * 加载中
         */
        LOADING,
        /**
         * 加载失败
         */
        FAILED
    }

    @Nullable
    public final DATA data;
    @NonNull
    public final Status status;
    @Nullable
    public final String message;
    @NonNull
    public final Params params;

    public final int code;

    public Resource(@NonNull Params params, int code, @Nullable DATA data, @NonNull Status status, @Nullable String message) {
        this.params = params;
        this.code = code;
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static <DATA> Resource<DATA> success(Params params, DATA data) {
        return new Resource<>(params, 0, data, Status.SUCCESS, null);
    }

    public static <DATA> Resource<DATA> failed(Params params, int code, String message) {
        return new Resource<>(params, code, null, Status.FAILED, message);
    }

    public static <DATA> Resource<DATA> loading(Params params, DATA data) {
        return new Resource<>(params, 0, data, Status.LOADING, null);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "data=" + data +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
