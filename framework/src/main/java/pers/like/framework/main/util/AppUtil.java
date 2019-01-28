package pers.like.framework.main.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.PackageInfoCompat;

/**
 * @author like
 */
public class AppUtil {

    public static long versionCode(Context context) {
        try {
            PackageInfo pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pkg.getLongVersionCode();
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return 0;
    }

    public static String versionName(Context context) {
        try {
            PackageInfo pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pkg.versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
