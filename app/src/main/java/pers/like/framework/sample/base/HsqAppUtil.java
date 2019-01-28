package pers.like.framework.sample.base;

import android.content.Context;


import pers.like.framework.sample.base.di.ApplicationComponent;

/**
 * @author like
 */
public class HsqAppUtil {

    public static ApplicationComponent component(Context context) {
        return (ApplicationComponent) ((HsqApplication) context.getApplicationContext()).get();
    }

}
