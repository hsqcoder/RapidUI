package pers.like.framework.main.network.resource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * @author like0
 */
public abstract class BaseCacheResource<ResultType> {

    private final MediatorLiveData<ResultType> mResult = new MediatorLiveData<>();

    public BaseCacheResource() {
        final LiveData<ResultType> dbSource = loadFromCache();
        mResult.addSource(dbSource, resultType -> {
            mResult.removeSource(dbSource);
            mResult.setValue(dbSource.getValue());
        });
    }

    /**
     * 从数据库拉取缓存
     *
     * @return 缓存对象
     */
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromCache();

    public LiveData<ResultType> liveData() {
        return mResult;
    }

}
