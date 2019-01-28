package pers.like.framework.main.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import pers.like.framework.main.util.JsonUtils;

/**
 * @author Like
 */
@SuppressWarnings("unused")
public class Params {

    @NonNull
    private final Map<String, Object> params;

    public Params() {
        this.params = new HashMap<>();
    }

    public Params(String key, Object value) {
        this.params = new HashMap<>();
        this.params.put(key, value);
    }

    public Params put(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public Params put(Map<String, Object> param) {
        if (param != null) {
            this.params.putAll(param);
        }
        return this;
    }

    public Params put(Params params) {
        if (params != null) {
            this.put(params.get());
        }
        return this;
    }


    public Map<String, Object> get() {
        return this.params;
    }

    public String string(String key) {
        if (!this.params.containsKey(key)) {
            return "";
        }
        return (String) this.params.get(key);
    }

    public int intValue(String key) {
        if (!this.params.containsKey(key)) {
            return 0;
        }
        return (int) this.params.get(key);
    }

    public float floatValue(String key) {
        if (!this.params.containsKey(key)) {
            return 0f;
        }
        return (float) this.params.get(key);
    }

    public boolean boo(String key) {
        return this.params.containsKey(key) && (boolean) this.params.get(key);
    }

    @Nullable
    public Object get(String key) {
        return this.params.get(key);
    }

    public void remove(String key) {
        this.params.remove(key);
    }

    public String json() {
        return JsonUtils.toJson(this.params);
    }

    @NonNull
    @Override
    public String toString() {
        return this.params.toString();
    }
}
