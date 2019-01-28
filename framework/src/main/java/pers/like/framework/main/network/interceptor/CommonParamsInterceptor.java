package pers.like.framework.main.network.interceptor;


import com.alibaba.android.arouter.launcher.ARouter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import pers.like.framework.main.network.NetworkConfigService;
import pers.like.framework.main.util.JsonUtils;

/**
 * @author like
 */
@SuppressWarnings("unused")
public class CommonParamsInterceptor implements Interceptor {

    private final static String GET = "GET";
    private final static String POST = "POST";
    private NetworkConfigService networkConfigService;

    public CommonParamsInterceptor() {
        this.networkConfigService = ARouter.getInstance().navigation(NetworkConfigService.class);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Map<String, String> params = networkConfigService.commonParams();
        if (params.isEmpty()) {
            return chain.proceed(request);
        }
        Request.Builder newRequestBuild;
        if (POST.equals(request.method())) {
            RequestBody oldBody = request.body();
            StringBuilder postBodyString = new StringBuilder();
            if (oldBody instanceof FormBody) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }
                newRequestBuild = request.newBuilder();
                RequestBody formBody = formBodyBuilder.build();
                postBodyString = new StringBuilder(bodyToString(request.body()));
                postBodyString.append((postBodyString.length() > 0) ? "&" : "").append(bodyToString(formBody));
                newRequestBuild.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString.toString()));
            } else if (oldBody instanceof MultipartBody) {
                MultipartBody oldBodyMultipart = (MultipartBody) oldBody;
                List<MultipartBody.Part> oldPartList = oldBodyMultipart.parts();
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);

                for (MultipartBody.Part part : oldPartList) {
                    builder.addPart(part);
                    postBodyString.append(bodyToString(part.body())).append("\n");
                }

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), JsonUtils.toJson(entry));
                    builder.addPart(requestBody);
                }
                newRequestBuild = request.newBuilder();
                newRequestBuild.post(builder.build());
            } else {
                newRequestBuild = request.newBuilder();
            }
        } else {
            HttpUrl.Builder commonParamsUrlBuilder = request.url()
                    .newBuilder()
                    .scheme(request.url().scheme())
                    .host(request.url().host());

            for (Map.Entry<String, String> entry : params.entrySet()) {
                commonParamsUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            newRequestBuild = request.newBuilder()
                    .method(request.method(), request.body())
                    .url(commonParamsUrlBuilder.build());

        }
        return chain.proceed(newRequestBuild.build());
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null) {
                request.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
