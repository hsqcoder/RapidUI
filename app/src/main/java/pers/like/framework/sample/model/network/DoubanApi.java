package pers.like.framework.sample.model.network;

import android.arch.lifecycle.LiveData;

import java.util.Map;

import pers.like.framework.main.network.response.Response;
import pers.like.framework.sample.model.pojo.douban.DoubanListWrapper;
import pers.like.framework.sample.model.pojo.douban.Movie;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Like
 */
public interface DoubanApi {

    /**
     * 搜索豆瓣电影
     *
     * @param params 名字，分页参数
     * @return 电影列表
     */
    @FormUrlEncoded
    @POST("https://api.douban.com/v2/movie/search")
    public LiveData<Response<DoubanListWrapper<Movie>>> search(@FieldMap Map<String, Object> params);

}
