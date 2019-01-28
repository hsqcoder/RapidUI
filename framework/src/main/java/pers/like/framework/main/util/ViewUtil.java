package pers.like.framework.main.util;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created By Like on 2016/8/15.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ViewUtil {

    private Context mContext;

    public ViewUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 密度转换像素
     */
    public int dipToPx(float dipValue) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int scale = metrics.densityDpi / 160;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * convert sp to its equivalent px
     * 将sp转换为px
     */
    public int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 像素转换密度
     */
    public int pxToDip(float pxValue) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int scale = metrics.densityDpi / 160;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 当前屏幕的density因子
     */
    public float getDmDensityDpi() {
        return mContext.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取设备的屏幕宽度(Pixels)
     */
    public int getWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public int getWidth(float scale) {
        return (int) (mContext.getResources().getDisplayMetrics().widthPixels * scale);
    }

    /**
     * 获取设备的屏幕高度(Pixels)
     *
     * @return mHeight
     */
    public int getHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    public int getHeight(float scale) {
        return (int) (mContext.getResources().getDisplayMetrics().heightPixels * scale);
    }

    public boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    public String toString(TextView view) {
        return view.getText().toString().trim();
    }

    public String toString(EditText view) {
        return view.getText().toString().trim();
    }

    public boolean isEmpty(TextView view) {
        return TextUtils.isEmpty(view.getText().toString().trim());
    }

    public boolean isEmpty(EditText view) {
        return TextUtils.isEmpty(view.getText().toString().trim());
    }

}
