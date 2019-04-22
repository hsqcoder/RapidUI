package pers.like.framework.main.network.response;


import retrofit2.Call;

/**
 * @author Like
 */
public class EmptyResponse<DATA> extends Response<DATA> {

    public EmptyResponse(Call call) {
        this.call = call;
    }

}
