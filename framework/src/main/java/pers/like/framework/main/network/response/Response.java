package pers.like.framework.main.network.response;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.ResponseBody;

/**
 * @author like
 */
@SuppressWarnings("ALL")
public class Response<DATA> {

    protected int code;
    protected String message;
    protected DATA data;
    protected boolean successful;
    protected Headers headers;

    public static <DATA> ErrorResponse<DATA> create(Throwable throwable) {
        if (!TextUtils.isEmpty(throwable.getMessage())) {
            return new ErrorResponse<>(-1, "网络连接出错，请稍后重试!");
        } else {
            return new ErrorResponse<>(-1, "未知错误");
        }
    }

    public static <DATA> Response<DATA> create(@NonNull retrofit2.Response<Response<DATA>> response) {
        if (response.isSuccessful()) {
            Response<DATA> responseInner = response.body();
            if (responseInner == null) {
                return new EmptyResponse<>();
            }
            if (responseInner.successful) {
                return new SuccessResponse<>(responseInner.data, response.headers());
            } else {
                return new ErrorResponse<>(responseInner.code, responseInner.message);
            }
        } else {
            try {
                String errorMessage = "Unknown error!";
                ResponseBody body = response.errorBody();
                if (body != null) {
                    errorMessage = body.string();
                }
                return new ErrorResponse<>(response.code(), errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new EmptyResponse<>();
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
}
