package pers.like.framework.main.component.download;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author like0
 */
public class DownloadCenter {

    private static final OkHttpClient mClient;

    static {
        mClient = new OkHttpClient();
    }

    public static void download(String url, DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.failed();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.processResponse(response);
            }
        });
    }

}
