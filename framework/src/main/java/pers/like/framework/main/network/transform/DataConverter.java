package pers.like.framework.main.network.transform;

import pers.like.framework.main.util.JsonUtils;

/**
 * @author like
 */
public interface DataConverter {
    /**
     * 转换网络请求结果为包装类
     *
     * @param json 请求结果
     * @return 包装类
     */
    DataWrapper convert(String json);

    DataConverter DEFAULT = new DataConverter() {

        private final String REGEX = "\\{\"code\":-?\\d*,\"message\":\"[\\s\\S]*?\\}";

        @Override
        public DataWrapper convert(String json) {
            DataWrapper result = null;
            if (json.matches(REGEX)) {
                result = JsonUtils.fromJson(json, DataWrapper.class);
            }
            if (result != null) {
                result.setSuccessful(result.getCode() == 0);
            }
            return result;
        }
    };

}
