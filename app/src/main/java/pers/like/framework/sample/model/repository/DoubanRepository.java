package pers.like.framework.sample.model.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.network.Params;
import pers.like.framework.main.network.resource.BaseNetworkResource;
import pers.like.framework.main.network.resource.Resource;
import pers.like.framework.main.network.response.Response;
import pers.like.framework.main.util.Cache;
import pers.like.framework.sample.model.network.DoubanApi;
import pers.like.framework.sample.model.pojo.douban.DoubanListWrapper;
import pers.like.framework.sample.model.pojo.douban.Movie;

/**
 * @author Like
 */
public class DoubanRepository {


    private BaseExecutor executor;
    private DoubanApi doubanApi;
    private Cache cache;

    public DoubanRepository(DoubanApi doubanApi, Cache cache, BaseExecutor executor) {
        this.doubanApi = doubanApi;
        this.cache = cache;
        this.executor = executor;
    }

    public LiveData<Resource<DoubanListWrapper<Movie>>> search(Params params) {
        return new BaseNetworkResource<DoubanListWrapper<Movie>>(executor, params) {
            @NonNull
            @Override
            protected LiveData<Response<DoubanListWrapper<Movie>>> call(@NonNull Params params) {
                return doubanApi.search(params.get());
            }
        }.liveData();
    }

}
