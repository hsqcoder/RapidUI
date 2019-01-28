package pers.like.framework.main.network.resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.network.Params;
import pers.like.framework.main.network.response.EmptyResponse;
import pers.like.framework.main.network.response.ErrorResponse;
import pers.like.framework.main.network.response.Response;
import pers.like.framework.main.network.response.SuccessResponse;
import pers.like.framework.main.util.JsonUtils;
import pers.like.framework.main.util.Logger;

/**
 * @author Like
 */
@SuppressWarnings({"ALL"})
public abstract class BaseNetworkCacheResource<ResultType, RequestType> {

    private BaseExecutor mExecutor;

    private final MediatorLiveData<Resource<ResultType>> mResult = new MediatorLiveData<>();

    public BaseNetworkCacheResource(BaseExecutor appExecutor, @NonNull Params params) {
        this.mExecutor = appExecutor;
        final LiveData<ResultType> dbSource = cache();
        mResult.setValue(Resource.loading(params, dbSource.getValue()));
        mResult.addSource(dbSource, resultType -> {
            mResult.removeSource(dbSource);
            if (shouldFetch(resultType)) {
                fetchFromNetwork(dbSource, params);
            } else {
                mResult.addSource(dbSource, resultType1 -> mResult.setValue(Resource.success(params, resultType1)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource, @NonNull Params params) {
        //获取云端数据
        LiveData<Response<RequestType>> responseLiveData = call(params);
        //将数据库缓存显示到UI
        mResult.addSource(dbSource, resultType -> mResult.setValue(Resource.loading(params, resultType)));
        //观察网络请求结果
        mResult.addSource(responseLiveData, requestTypeKlyResponse -> {
            //网络返回时移除本地数据源
            mResult.removeSource(responseLiveData);
            mResult.removeSource(dbSource);
            //请求成功，将数据缓存到DB，从DB展示数据到UI
            if (requestTypeKlyResponse instanceof SuccessResponse) {
                mExecutor.diskIO().execute(() -> {
                    cache(processResponse((SuccessResponse<RequestType>) requestTypeKlyResponse));
                    mExecutor.mainThread().execute(() -> mResult.addSource(cache(), resultType -> mResult.setValue(Resource.success(params, resultType))));
                });
                //请求失败，提示错误，并从数据库获取数据到UI
            } else if (requestTypeKlyResponse instanceof ErrorResponse) {
                onFetchFailed();
                mResult.addSource(dbSource, resultType -> mResult.setValue(Resource.failed(params, requestTypeKlyResponse.getCode(), requestTypeKlyResponse.getMessage())));
                //请求为空，直接取数据库缓存更新UI
            } else if (requestTypeKlyResponse instanceof EmptyResponse) {
                mExecutor.mainThread().execute(() -> mResult.addSource(cache(), resultType -> mResult.setValue(Resource.success(params, resultType))));
            }
        });
    }

    /**
     * 缓存网络结果志数据库
     *
     * @param item 网络返回值
     */
    @WorkerThread
    protected void cache(@NonNull RequestType item) {
        Logger.e("Network", "Cache:" + JsonUtils.toJson(item));
    }

    @WorkerThread
    protected RequestType processResponse(SuccessResponse<RequestType> response) {
        return response.getData();
    }

    /**
     * 判断缓存是否可用，如不可用启动网络拉取数据
     *
     * @param data 缓存数据
     * @return 是否应该从网络拉取数据
     */
    @MainThread
    protected boolean shouldFetch(@Nullable ResultType data) {
        return true;
    }

    /**
     * 从数据库拉取缓存
     *
     * @return 缓存对象
     */
    @NonNull
    @MainThread
    protected LiveData<ResultType> cache() {
        return new LiveData<ResultType>() {
        };
    }

    /**
     * 从网络拉取数据
     *
     * @return 网络返回值
     */
    @NonNull
    @MainThread
    protected abstract LiveData<Response<RequestType>> call(Params params);

    /**
     * 拉取数据失败回调
     */
    @MainThread
    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> liveData() {
        return mResult;
    }

}
