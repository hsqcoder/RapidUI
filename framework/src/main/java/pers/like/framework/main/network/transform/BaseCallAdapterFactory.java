package pers.like.framework.main.network.transform;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import pers.like.framework.main.network.response.Response;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * @author Like
 */
@SuppressWarnings("WeakerAccess")
public class BaseCallAdapterFactory extends CallAdapter.Factory {

    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (CallAdapter.Factory.getRawType(returnType) != LiveData.class) {
            return null;
        }
        Type type = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);

        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("resource must be parameterized");
        }
        if (CallAdapter.Factory.getRawType(type) != Response.class) {
            throw new IllegalArgumentException("type must be a resource");
        }
        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) type);
        return new BaseCallAdapter(bodyType);
    }

}
