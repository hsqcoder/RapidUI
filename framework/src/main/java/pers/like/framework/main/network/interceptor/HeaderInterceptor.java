package pers.like.framework.main.network.interceptor;

import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import pers.like.framework.main.network.NetworkConfigService;

/**
 * @author like
 */
@SuppressWarnings("unused")
public class HeaderInterceptor implements Interceptor {

    private NetworkConfigService networkConfigService;

    public HeaderInterceptor() {
        this.networkConfigService = ARouter.getInstance().navigation(NetworkConfigService.class);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Map<String, String> headers = networkConfigService.commonHeaders();
        if (headers.isEmpty()) {
            return chain.proceed(request);
        }
        Request.Builder builder = request.newBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.removeHeader(entry.getKey()).addHeader(entry.getKey(), entry.getValue());
        }
        return chain.proceed(builder.build());
    }

}
