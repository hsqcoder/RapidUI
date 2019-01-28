package pers.like.framework.main.network;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pers.like.framework.main.BuildConfig;
import pers.like.framework.main.network.interceptor.CommonParamsInterceptor;
import pers.like.framework.main.network.interceptor.HeaderInterceptor;
import pers.like.framework.main.network.transform.BaseCallAdapterFactory;
import pers.like.framework.main.network.transform.ConverterFactory;
import pers.like.framework.main.util.BuildConfigUtil;
import pers.like.framework.main.util.Logger;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

/**
 * @author like
 */
public class Network {

    private static Network mInstance;
    private Retrofit mRetrofit;
    private NetworkConfigService networkConfigService;

    public static Network instance() {
        if (mInstance == null) {
            synchronized (Network.class) {
                if (mInstance == null) {
                    mInstance = new Network();
                }
            }
        }
        return mInstance;
    }

    public <T> T create(Class<T> clazz) {
        return this.mRetrofit.create(clazz);
    }

    private Network() {
        networkConfigService = ARouter.getInstance().navigation(NetworkConfigService.class);
        if (networkConfigService == null) {
            throw new IllegalStateException("NetworkConfigService 未配置");
        }
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(networkConfigService.url())
                .addConverterFactory(ConverterFactory.create())
                .client(client());
        builder.addCallAdapterFactory(new BaseCallAdapterFactory());
        this.mRetrofit = builder.build();
    }

    private OkHttpClient client() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfigUtil.isDebug()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        SSLSocketFactory sslSocketFactory = null;
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{x509TrustManager};
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(networkConfigService.connectTimeout(), TimeUnit.SECONDS)
                .readTimeout(networkConfigService.readTimeout(), TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .hostnameVerifier((s, sslSession) -> true)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                        this.cookieStore.put(HttpUrl.parse(url.host()), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                        List<Cookie> cookies = this.cookieStore.get(HttpUrl.parse(url.host()));
                        return cookies != null ? cookies : new ArrayList();
                    }
                });
        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new CommonParamsInterceptor());
        for (Interceptor interceptor : networkConfigService.interceptorList()) {
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(logging);
        return builder.build();
    }


}
