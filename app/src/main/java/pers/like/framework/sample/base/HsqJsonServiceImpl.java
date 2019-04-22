package pers.like.framework.sample.base;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;

import java.lang.reflect.Type;

import pers.like.framework.main.util.JsonUtils;

/**
 * @author like
 */
@Route(path = "/service/json")
@SuppressWarnings("unused")
public class HsqJsonServiceImpl implements SerializationService {

    @Override
    public <T> T json2Object(String json, Class<T> clazz) {
        return JsonUtils.fromJson(json, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return JsonUtils.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        return JsonUtils.fromJson(input, clazz);
    }

    @Override
    public void init(Context context) {

    }
}
