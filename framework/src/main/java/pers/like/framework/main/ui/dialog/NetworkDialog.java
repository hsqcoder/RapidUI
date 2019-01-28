package pers.like.framework.main.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import pers.like.framework.main.R;
import pers.like.framework.main.ui.widget.CircleImage;

/**
 * @author like
 */
@SuppressWarnings("unused")
public class NetworkDialog extends Dialog {

    private TextView mTitle;
    private CircleImage mLogo;
    private View mBorder;

    public NetworkDialog(Context context) {
        super(context, R.style.BaseDialog_Trans);
        setCancelable(false);
        setContentView(R.layout.dialog_network);
        mTitle = findViewById(R.id.text_title);
        mTitle.setTextColor(Color.WHITE);
        mLogo = findViewById(R.id.image_logo);
        mBorder = findViewById(R.id.view_border);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER_VERTICAL);
        }

    }

    @Override
    public void show() {
        super.show();
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        mBorder.startAnimation(operatingAnim);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBorder.clearAnimation();
    }

    public NetworkDialog title(String text) {
        mTitle.setText(text);
        return this;
    }

    public NetworkDialog logo(int res) {
        mLogo.setImageResource(res);
        return this;
    }

}
