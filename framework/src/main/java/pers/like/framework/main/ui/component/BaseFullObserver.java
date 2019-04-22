package pers.like.framework.main.ui.component;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import okhttp3.Headers;
import pers.like.framework.main.network.Params;
import pers.like.framework.main.network.resource.Resource;
import pers.like.framework.main.util.Logger;
import retrofit2.Call;

/**
 * @author like
 */
public class BaseFullObserver<T> implements Observer<Resource<T>> {

    @Override
    public void onChanged(@Nullable Resource<T> resource) {
        if (resource != null) {
            switch (resource.status) {
                case SUCCESS:
                    if (resource.data != null) {
                        success(resource.call, resource.params, resource.headers, resource.data);
                    } else {
                        emptyData(resource.params, resource.headers);
                    }
                    complete(resource.params);
                    break;
                case FAILED:
                    failed(resource.call, resource.params, resource.headers, resource.code, resource.message);
                    complete(resource.params);
                    break;
                case LOADING:
                    loading(resource.params, resource.data);
                    break;
                default:
                    throw new IllegalStateException("Resource状态异常");
            }
        }
    }

    protected void success(@Nullable Call call, @NonNull Params params, @Nullable Headers headers, @NonNull T data) {
    }

    protected void emptyData(@NonNull Params params, @Nullable Headers headers) {
        Logger.e("BaseObserver", "emptyData - " + params.toString());
    }

    protected void failed(@Nullable Call call, @NonNull Params params, @Nullable Headers headers, int code, String message) {
    }

    protected void loading(@NonNull Params params, @Nullable T data) {
    }

    protected void complete(@NonNull Params params) {
    }
}
