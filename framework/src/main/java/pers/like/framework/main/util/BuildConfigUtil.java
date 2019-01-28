package pers.like.framework.main.util;

import android.content.Context;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * @author Like
 */
public class BuildConfigUtil {

    private static final String BUILD_TYPE = "BUILD_TYPE";
    private static final String DEBUG = "debug";

    private static WeakReference<Context> reference;

    public static void init(Context context) {
        reference = new WeakReference<>(context);
    }

    public static boolean isDebug() {
        String buildType = getBuildConfigValue(BUILD_TYPE);
        return !TextUtils.isEmpty(buildType) && buildType.equals(DEBUG);
    }

    @SuppressWarnings("ALL")
    private static <T> T getBuildConfigValue(String fieldName) {
        if (reference.get() == null) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(reference.get().getPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return (T) field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
