package pers.like.framework.main.ui.component;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pers.like.framework.main.network.Params;
import pers.like.framework.main.network.resource.Resource;
import pers.like.framework.main.util.Logger;

/**
 * @author like
 */
public class BaseObserver<T> implements Observer<Resource<T>> {

    @Override
    public void onChanged(@Nullable Resource<T> resource) {
        if (resource != null) {
            switch (resource.status) {
                case SUCCESS:
                    if (resource.data != null) {
                        success(resource.params, resource.data);
                    } else {
                        emptyData(resource.params);
                    }
                    complete(resource.params);
                    break;
                case FAILED:
                    failed(resource.params, resource.code, resource.message);
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

    protected void success(@NonNull Params params, @NonNull T data) {
    }

    protected void emptyData(@NonNull Params params) {
        Logger.e("BaseObserver", "emptyData - " + params.toString());
    }

    protected void failed(@NonNull Params params, int code, String message) {
    }

    protected void loading(@NonNull Params params, @Nullable T data) {
    }

    protected void complete(@NonNull Params params) {
    }
}
