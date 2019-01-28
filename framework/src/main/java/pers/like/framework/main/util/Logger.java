package pers.like.framework.main.util;

import android.util.Log;

public class Logger {

    private static String TAG = "Logger";

    public static void e(String log) {
        e(TAG, log);
    }

    public static void e(Object log) {
        e(TAG, JsonUtils.toJson(log));
    }

    public static void e(String tag, String log) {
        Log.e(tag, log);
    }

}
