package pers.like.framework.main.network.resource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import okhttp3.Headers;
import pers.like.framework.main.network.Params;
import retrofit2.Call;

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
    public final Call call;
    @Nullable
    public final DATA data;
    @NonNull
    public final Status status;
    @Nullable
    public final String message;
    @NonNull
    public final Params params;
    @Nullable
    public final Headers headers;

    public final int code;

    public Resource(@Nullable Call call, @NonNull Params params, @Nullable Headers headers, int code, @Nullable DATA data, @NonNull Status status, @Nullable String message) {
        this.call = call;
        this.params = params;
        this.headers = headers;
        this.code = code;
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static <DATA> Resource<DATA> success(Call call, Params params, DATA data, Headers headers) {
        return new Resource<>(call, params, headers, 0, data, Status.SUCCESS, null);
    }

    public static <DATA> Resource<DATA> failed(Call call, Params params, int code, String message, Headers headers) {
        return new Resource<>(call, params, headers, code, null, Status.FAILED, message);
    }

    public static <DATA> Resource<DATA> loading(Call call, Params params, DATA data) {
        return new Resource<>(call, params, null, 0, data, Status.LOADING, null);
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
