package pers.like.framework.sample.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pers.like.framework.main.network.NetworkConfigService;
import pers.like.framework.main.network.transform.DataConverter;
import pers.like.framework.main.util.AppUtil;
import pers.like.framework.sample.base.component.DoubanDataConverter;

/**
 * @author like
 */
@SuppressWarnings("unused")
@Route(path = "/system/network/config", name = "网络配置")
public class HsqNetworkConfigServiceImpl implements NetworkConfigService {

    private HsqUserSystem userSystem;
    private Map<String, String> commonHeaders = new HashMap<>();
    private Map<String, String> commonParams = new HashMap<>();
    private List<DataConverter> dataConverters = new ArrayList<>();

    @Override
    public void init(Context context) {
        userSystem = HsqAppUtil.component(context).userSystem();
        commonHeaders.put("Hsq-Agent", getUserAgent(context));
        //解析默认api
        dataConverters.add(DataConverter.DEFAULT);
        //解析豆瓣api
        dataConverters.add(new DoubanDataConverter());
    }

    @NonNull
    @Override
    public String url() {
        return "http://www.Like0809.github.com/";
    }

    @Override
    public boolean enableStomp() {
        return false;
    }

    @NonNull
    @Override
    public List<DataConverter> dataConverterList() {
        return dataConverters;
    }

    @NonNull
    @Override
    public Map<String, String> commonParams() {
        String token = userSystem.token();
        if (!TextUtils.isEmpty(token)) {
            commonParams.put("token", userSystem.token());
        } else {
            commonParams.remove("token ");
        }
        return commonParams;
    }

    @NonNull
    @Override
    public Map<String, String> commonStompParams() {
        String token = userSystem.token();
        if (!TextUtils.isEmpty(token)) {
            commonParams.put("token", userSystem.token());
        } else {
            commonParams.remove("token");
        }
        return commonParams;
    }

    @NonNull
    @Override
    public Map<String, String> commonHeaders() {
        return commonHeaders;
    }

    @NonNull
    @Override
    public Map<String, String> commonStompHeaders() {
        return commonHeaders;
    }

    private String getUserAgent(Context context) {
        String userAgent;
        try {
            userAgent = WebSettings.getDefaultUserAgent(context);
        } catch (Exception e) {
            userAgent = System.getProperty("http.agent");
        }
        if (userAgent == null) {
            userAgent = "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        sb.append(" hsq.version/").append(AppUtil.versionName(context));
        return sb.toString();
    }

}
