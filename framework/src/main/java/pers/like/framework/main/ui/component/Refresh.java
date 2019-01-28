package pers.like.framework.main.ui.component;


import android.support.annotation.NonNull;

import pers.like.framework.main.network.Params;

/**
 * @author Like
 */
public class Refresh {

    public static Params put(@NonNull String key, @NonNull Object value) {
        return get().put(key, value);
    }

    public static Params get() {
        return new Params("refresh", true).put("offset", 0).put("pageNum", 1);
    }

    public static boolean isRefresh(@NonNull Params params) {
        return params.boo("refresh");
    }

}
