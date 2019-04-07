package pers.like.framework.main.component.download;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author like
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DownloadCenter {

    private static final OkHttpClient CLIENT;

    static {
        CLIENT = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static Call download(String url, DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.failed();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.processResponse(response);
            }
        });
        return call;
    }

}
