package pers.like.framework.main.network.resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.network.Params;
import pers.like.framework.main.network.response.EmptyResponse;
import pers.like.framework.main.network.response.ErrorResponse;
import pers.like.framework.main.network.response.Response;
import pers.like.framework.main.network.response.SuccessResponse;

/**
 * @author Like
 */
@SuppressWarnings("All")
public abstract class BaseNetworkResource<DataType> {

    private BaseExecutor mExecutor;

    private final MediatorLiveData<Resource<DataType>> mResult = new MediatorLiveData<>();

    public BaseNetworkResource(@NonNull BaseExecutor executor, @NonNull Params params) {
        this.mExecutor = executor;
        this.mResult.setValue(Resource.loading(null, params, null));
        LiveData<Response<DataType>> responseLiveData = call(params);
        mResult.addSource(responseLiveData, response -> {
            mResult.removeSource(responseLiveData);
            if (response instanceof SuccessResponse) {
                mExecutor.diskIO().execute(() -> {
                    cache(processResponse((SuccessResponse<DataType>) response));
                    SuccessResponse<DataType> success = (SuccessResponse<DataType>) response;
                    mExecutor.mainThread().execute(() -> mResult.setValue(Resource.success(response.getCall(), params, success.getData(), response.getHeaders())));
                });
            } else if (response instanceof ErrorResponse) {
                onFetchFailed();
                mResult.setValue(Resource.failed(response.getCall(), params, response.getCode(), response.getMessage(), response.getHeaders()));
            } else if (response instanceof EmptyResponse) {
                mExecutor.mainThread().execute(() -> mResult.setValue(Resource.failed(null, params, -1, "未知错误", null)));
            }
        });
    }

    /**
     * 缓存网络结果志数据库
     *
     * @param item 网络返回值
     */
    @WorkerThread
    protected void cache(@NonNull DataType item) {
    }

    @WorkerThread
    protected DataType processResponse(SuccessResponse<DataType> response) {
        return response.getData();
    }

    /**
     * 从网络拉取数据
     *
     * @return 网络返回值
     */
    @NonNull
    @MainThread
    protected abstract LiveData<Response<DataType>> call(@NonNull Params params);

    /**
     * 拉取数据失败回调
     */
    @MainThread
    protected void onFetchFailed() {
    }

    public LiveData<Resource<DataType>> liveData() {
        return mResult;
    }

}
