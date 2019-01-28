package pers.like.framework.main.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;


/**
 * @author like
 */
@SuppressWarnings("ALL")
public class JsonUtils {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final boolean DEFAULT_EXCLUDES_FIELDS_WITHOUT_EXPOSE = false;
    public static final String EMPTY_JSON = "{}";
    public static final String EMPTY_JSON_ARRAY = "[]";

    public static String toJson(Object target) {
        return toJson(target, null, false, null, null, false);
    }

    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, false, null, null, false);
    }

    public static String toJson(Object target, Type targetType, Double version) {
        return toJson(target, targetType, false, version, null, false);
    }

    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, null, null, excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, version, null, excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, null, null, excludesFieldsWithoutExpose);
    }

    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version, String datePattern, boolean excludesFieldsWithoutExpose) {
        if (target == null) {
            return EMPTY_JSON;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls) {
            builder.serializeNulls();
        }
        if (version != null) {
            builder.setVersion(version.doubleValue());
        }
        if (TextUtils.isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        return toJson(target, targetType, builder);
    }

    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if (target == null) {
            return EMPTY_JSON;
        }
        Gson gson;
        if (builder == null) {
            gson = new Gson();
        } else {
            gson = builder.create();
        }
        String result = EMPTY_JSON;
        if (targetType != null) {
            return gson.toJson(target, targetType);
        }
        try {
            return gson.toJson(target);
        } catch (Exception e) {
            e.printStackTrace();
            if ((target instanceof Collection) || (target instanceof Iterator) || (target instanceof Enumeration) || target.getClass().isArray()) {
                return EMPTY_JSON_ARRAY;
            }
            return result;
        }
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        return (T) fromJson(json, (Class) cls, null);
    }

    public static <T> T fromJson(String json, Type type) {
        return fromJson(json, type, null);
    }

    public static <T> T fromJson(String json, TypeToken<T> token) {
        return (T) fromJson(json, (TypeToken) token, null);
    }

    public static <T> T fromJson(String json, Class<T> cls, String datePattern) {
        T t = null;
        if (!TextUtils.isEmpty(json)) {
            GsonBuilder builder = new GsonBuilder();
            if (TextUtils.isEmpty(datePattern)) {
                datePattern = DEFAULT_DATE_PATTERN;
            }
            builder.setDateFormat(datePattern);
            Gson gson = builder.create();
            try {
                t = (T) gson.fromJson(json, (Class) cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static <T> T fromJson(String json, Type type, String datePattern) {
        T t = null;
        if (!TextUtils.isEmpty(json)) {
            GsonBuilder builder = new GsonBuilder();
            if (TextUtils.isEmpty(datePattern)) {
                datePattern = DEFAULT_DATE_PATTERN;
            }
            builder.setDateFormat(datePattern);
            try {
                t = builder.create().fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        return fromJson(json, token.getType(), datePattern);
    }

}
