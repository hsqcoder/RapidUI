package pers.like.framework.main.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Type;

@SuppressWarnings("unused")
public class Cache {

    private static final String SP_KEY = "CACHE";

    private SharedPreferences mSharedPreference;

    public Cache(Context context) {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void put(String key, String value) {
        mSharedPreference.edit().putString(key, value).apply();
    }

    public void put(String key, int value) {
        mSharedPreference.edit().putInt(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreference.edit().putBoolean(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreference.edit().putFloat(key, value).apply();
    }

    public void put(String key, long value) {
        mSharedPreference.edit().putLong(key, value).apply();
    }

    public void put(String key, Object object) {
        mSharedPreference.edit().putString(key, JsonUtils.toJson(object)).apply();
    }

    public void remove(String key) {
        mSharedPreference.edit().remove(key).apply();
    }

    public int intValue(String key) {
        return intValue(key, 0);
    }

    public int intValue(String key, int defValue) {
        return mSharedPreference.getInt(key, defValue);
    }

    public boolean booValue(String key) {
        return booValue(key, false);
    }

    public boolean booValue(String key, boolean defValue) {
        return mSharedPreference.getBoolean(key, defValue);
    }

    public float floatValue(String key) {
        return floatValue(key, 0f);
    }

    public float floatValue(String key, float defValue) {
        return mSharedPreference.getFloat(key, defValue);
    }

    public long longValue(String key) {
        return longValue(key, 0L);
    }

    public long longValue(String key, long defValue) {
        return mSharedPreference.getLong(key, defValue);
    }

    public String string(String key) {
        return string(key, "");
    }

    public String string(String key, String defValue) {
        return mSharedPreference.getString(key, defValue);
    }


    public <T> T get(String key, Type clazz) {
        String json = mSharedPreference.getString(key, null);
        if (json == null) {
            return null;
        }
        T t = null;
        try {
            t = JsonUtils.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
