package pers.like.framework.main.ui.component;


import android.support.annotation.NonNull;

import pers.like.framework.main.network.Params;

public class LoadMore {

    public static Params put(@NonNull String key, @NonNull Object value) {
        return new Params(key, value).put("loadMore", true);
    }

    public static boolean isLoadMore(@NonNull Params params) {
        return params.boo("loadMore");
    }

}
