package pers.like.framework.sample.base.component;

import android.text.TextUtils;

import pers.like.framework.main.network.transform.DataConverter;
import pers.like.framework.main.network.transform.DataWrapper;
import pers.like.framework.main.util.JsonUtils;

/**
 * @author Like
 */
public class DoubanDataConverter implements DataConverter {

    private String REGEX = "subjects";

    @Override
    public DataWrapper convert(String json) {
        if (!TextUtils.isEmpty(json) && json.contains(REGEX)) {
            DoubanWrapper wrapper = JsonUtils.fromJson(json, DoubanWrapper.class);
            DataWrapper dataWrapper = new DataWrapper();
            if (wrapper.code != null) {
                dataWrapper.setSuccessful(false);
                dataWrapper.setMessage(wrapper.msg);
                dataWrapper.setData(null);
            } else {
                dataWrapper.setSuccessful(true);
                dataWrapper.setData(JsonUtils.fromJson(json, Object.class));
            }
            return dataWrapper;

        }
        return null;
    }

    public static class DoubanWrapper {
        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
