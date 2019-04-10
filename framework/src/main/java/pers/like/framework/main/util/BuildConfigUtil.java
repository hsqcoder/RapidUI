package pers.like.framework.main.util;

import android.content.Context;
import android.support.annotation.NonNull;
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
        if (reference.get() == null) {
            return true;
        }
        return isDebug(reference.get().getPackageName());
    }

    public static boolean isDebug(@NonNull String packageName) {
        String buildType = getBuildConfigValue(packageName, BUILD_TYPE);
        return !TextUtils.isEmpty(buildType) && buildType.equals(DEBUG);
    }

    @SuppressWarnings("ALL")
    public static <T> T getBuildConfigValue(String packageName, String fieldName) {
        try {
            Class<?> clazz = Class.forName(packageName + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return (T) field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
