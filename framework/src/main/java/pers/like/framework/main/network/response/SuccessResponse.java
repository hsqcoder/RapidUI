package pers.like.framework.main.network.response;

import okhttp3.Headers;
import retrofit2.Call;

/**
 * @author Like
 */
@SuppressWarnings("unused")
public class SuccessResponse<DATA> extends Response<DATA> {

    private DATA data;

    SuccessResponse(Call call, DATA data, Headers headers) {
        this.call = call;
        this.data = data;
        this.headers = headers;
    }

    @Override
    public DATA getData() {
        return data;
    }

    @Override
    public void setData(DATA data) {
        this.data = data;
    }

}
