package pers.like.framework.main.network.transform;

import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import pers.like.framework.main.network.NetworkConfigService;
import pers.like.framework.main.network.response.Response;
import pers.like.framework.main.util.JsonUtils;
import retrofit2.Converter;

/**
 * @author like
 */
public class BaseConverter<T> implements Converter<ResponseBody, Response<T>> {
    private final TypeAdapter<T> adapter;
    private NetworkConfigService networkConfigService;

    BaseConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
        this.networkConfigService = ARouter.getInstance().navigation(NetworkConfigService.class);
    }

    @Override
    public Response<T> convert(@NonNull ResponseBody value) throws IOException {
        String data = value.string();
        Response<T> response = new Response<>();
        for (DataConverter converter : networkConfigService.dataConverterList()) {
            DataWrapper dataWrapper = converter.convert(data);
            if (dataWrapper != null) {
                response.setCode(dataWrapper.getCode());
                response.setMessage(dataWrapper.getMessage());
                response.setSuccessful(dataWrapper.isSuccessful());
                try {
                    if (dataWrapper.getData() == null) {
                        response.setData(null);
                    } else {
                        response.setData(adapter.fromJson(JsonUtils.toJson(dataWrapper.getData())));
                    }
                } catch (Exception e) {
                    response.setData(null);
                    response.setCode(-1);
                    response.setSuccessful(false);
                    response.setMessage("接口数据解析失败，可能是您的APP未升级到最新版");
                }
                return response;
            }
        }
        throw new IllegalStateException("未设置数据转换器");
    }

}