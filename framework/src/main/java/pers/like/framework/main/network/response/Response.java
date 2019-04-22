package pers.like.framework.main.network.response;

import android.support.annotation.NonNull;
import android.text.TextUtils;


import okhttp3.Headers;
import retrofit2.Call;

/**
 * @author like
 */
@SuppressWarnings("ALL")
public class Response<DATA> {

    protected Call call;
    protected int code;
    protected String message;
    protected DATA data;
    protected boolean successful;
    protected Headers headers;

    public static <DATA> ErrorResponse<DATA> create(@NonNull Call call, Throwable throwable) {
        if (!TextUtils.isEmpty(throwable.getMessage())) {
            return new ErrorResponse<>(call, -1, "网络连接出错，请稍后重试!", null);
        } else {
            return new ErrorResponse<>(call, -1, "系统连接超时，请稍后重试!", null);
        }
    }

    public static <DATA> Response<DATA> create(@NonNull Call call, @NonNull retrofit2.Response<Response<DATA>> response) {
        if (response.isSuccessful()) {
            Response<DATA> responseInner = response.body();
            if (responseInner == null) {
                return new EmptyResponse<>(call);
            }
            if (responseInner.successful) {
                return new SuccessResponse<>(call, responseInner.data, response.headers());
            } else {
                return new ErrorResponse<>(call, responseInner.code, responseInner.message, response.headers());
            }
        } else {
            String errorMessage = "Unknown error!";
            if (response.code() == 404) {
                errorMessage = "code[404],找不到服务地址";
            } else if (response.code() == 502) {
                errorMessage = "code[502],服务器正在维护";
            } else {
                errorMessage = "code[999],系统连接超时";
            }
            return new ErrorResponse<>(call, response.code(), errorMessage, response.headers());
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DATA getData() {
        return data;
    }

    public void setData(DATA data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Response<DATA> setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }
}
