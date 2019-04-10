package pers.like.framework.main.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import pers.like.framework.main.network.transform.DataConverter;

/**
 * 对框架内Network模块的参数进行配置
 *
 * @author like
 */
@SuppressWarnings("unused")
public interface NetworkConfigService extends IProvider {

    Map<String, String> NONE_MAP = new HashMap<>();
    List<Interceptor> NONE_LIST = new ArrayList<>();

    /**
     * 网络框架baseUrl
     *
     * @return url
     */
    @NonNull
    String url();

    /**
     * 若启用stomp模块，配置stompUrl
     *
     * @return stompUrl
     */
    @Nullable
    default String stompUrl() {
        return null;
    }

    /**
     * 是否允许使用stomp
     *
     * @return 是否允许
     */
    default boolean enableStomp() {
        return false;
    }

    /**
     * 连接超时时间 （秒）
     *
     * @return 连接超时时间
     */
    default int connectTimeout() {
        return 10;
    }

    /**
     * 读取超时时间（秒）
     *
     * @return 读取超时时间
     */
    default int readTimeout() {
        return 10;
    }

    /**
     * 数据转换器列表
     * 用于适配不同API返回的不同类型的数据格式如{code,message,data},{status,err_msg,obj}等
     *
     * @return dataConverterList
     */
    @NonNull
    default List<DataConverter> dataConverterList() {
        return Collections.singletonList(DataConverter.DEFAULT);
    }

    /**
     * 拦截器列表
     *
     * @return interceptorList
     */
    @NonNull
    default List<Interceptor> interceptorList() {
        return NONE_LIST;
    }

    /**
     * 公共参数
     *
     * @return commonParams
     */
    @NonNull
    default Map<String, String> commonParams() {
        return NONE_MAP;
    }

    /**
     * stomp公共参数
     *
     * @return commonStompParams
     */
    @NonNull
    default Map<String, String> commonStompParams() {
        return NONE_MAP;
    }

    /**
     * 公共消息头
     *
     * @return commonHeaders
     */
    @NonNull
    default Map<String, String> commonHeaders() {
        return NONE_MAP;
    }

    /**
     * stomp公共消息头
     *
     * @return commonStompHeaders
     */
    @NonNull
    default Map<String, String> commonStompHeaders() {
        return NONE_MAP;
    }

    /**
     * 日志级别
     *
     * @return 日志级别
     */
    default HttpLoggingInterceptor.Level logLevel() {
        return HttpLoggingInterceptor.Level.BASIC;
    }

}
