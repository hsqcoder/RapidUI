package pers.like.framework.main.network.transform;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import pers.like.framework.main.network.response.Response;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;

/**
 * @author Like
 */
@SuppressWarnings("WeakerAccess")
public class BaseCallAdapter<DATA> implements CallAdapter<Response<DATA>, LiveData<Response<DATA>>> {

    private Type mResponseType;

    public BaseCallAdapter(Type responseType) {
        mResponseType = responseType;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public LiveData<Response<DATA>> adapt(@NonNull final Call<Response<DATA>> call) {
        return new LiveData<Response<DATA>>() {

            private AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<Response<DATA>>() {
                        @Override
                        public void onResponse(@NonNull Call<Response<DATA>> call, @NonNull retrofit2.Response<Response<DATA>> response) {
                            postValue(Response.create(response));
                        }

                        @Override
                        public void onFailure(@NonNull Call<Response<DATA>> call, @NonNull Throwable t) {
                            postValue(Response.<DATA>create(t));
                        }
                    });
                }
            }
        };
    }

}
