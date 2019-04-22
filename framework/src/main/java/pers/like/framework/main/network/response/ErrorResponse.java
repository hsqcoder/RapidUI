package pers.like.framework.main.network.response;

import okhttp3.Headers;
import retrofit2.Call;

/**
 * @author Like
 */
public class ErrorResponse<DATA> extends Response<DATA> {

    ErrorResponse(Call call, int code, String message, Headers headers) {
        this.call = call;
        this.code = code;
        this.message = message;
        this.headers = headers;
    }

}
