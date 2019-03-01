package pers.like.framework.main.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pers.like.framework.main.R;

/**
 * @author Like
 */

@SuppressWarnings("unused")
public class MenuView extends LinearLayout {
    private TextView mTitleView;
    private ImageView mIconView, mArrowView;

    private Drawable icon;
    private ColorStateList iconTint, arrowTint;
    private Drawable arrow;
    private int iconSize;
    private int arrowSize;

    private String title;
    private ColorStateList titleColor;

    private int titleSize;

    private int spacingHorizontal;
    private int spacingVertical;

    private boolean isInit = false;

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MenuView);

        float scale = context.getResources().getDisplayMetrics().densityDpi / 160f;

        icon = ta.getDrawable(R.styleable.MenuView_mv_icon);
        iconTint = ta.getColorStateList(R.styleable.MenuView_mv_icon_tint);
        iconSize = ta.getDimensionPixelSize(R.styleable.MenuView_mv_icon_size, context.getResources().getDimensionPixelOffset(R.dimen.dp_15));

        mIconView = new AppCompatImageView(context);
        mIconView.setScaleType(ImageView.ScaleType.FIT_XY);
        mIconView.setImageDrawable(icon);
        mIconView.setImageTintList(iconTint);
        LayoutParams lp1 = new LayoutParams(iconSize + 10, iconSize + 10);
        mIconView.setLayoutParams(lp1);
        addView(mIconView);

        title = ta.getString(R.styleable.MenuView_mv_title);
        if (title == null) {
            title = "";
        }
        titleColor = ta.getColorStateList(R.styleable.MenuView_mv_title_color);
        titleSize = ta.getDimensionPixelSize(R.styleable.MenuView_mv_title_size, context.getResources().getDimensionPixelOffset(R.dimen.dp_15));

        mTitleView = new AppCompatTextView(context);
        mTitleView.setGravity(Gravity.CENTER | Gravity.START);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        mTitleView.setMaxLines(1);
        mTitleView.setText(title);
        if (titleColor != null) {
            mTitleView.setTextColor(titleColor);
        }
        mTitleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        addView(mTitleView);

        arrow = ta.getDrawable(R.styleable.MenuView_mv_arrow);
        arrowTint = ta.getColorStateList(R.styleable.MenuView_mv_arrow_tint);
        arrowSize = ta.getDimensionPixelSize(R.styleable.MenuView_mv_arrow_size, context.getResources().getDimensionPixelOffset(R.dimen.dp_15));
        if (arrow != null) {
            mArrowView = new AppCompatImageView(getContext());
            mArrowView.setImageDrawable(arrow);
            mArrowView.setImageTintList(arrowTint);
            mArrowView.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(mArrowView);
            LayoutParams lp = new LayoutParams(arrowSize + 10, arrowSize + 10);
            lp.leftMargin = context.getResources().getDimensionPixelOffset(R.dimen.dp_4);
            mArrowView.setLayoutParams(lp);
        }

        spacingHorizontal = ta.getDimensionPixelOffset(R.styleable.MenuView_mv_spacing_horizontal, context.getResources().getDimensionPixelOffset(R.dimen.dp_16));
        spacingVertical = ta.getDimensionPixelOffset(R.styleable.MenuView_mv_spacing_vertical, context.getResources().getDimensionPixelOffset(R.dimen.dp_3));
        ta.recycle();

        isInit = true;
        onOrientationChanged();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        if (isInit) {
            onOrientationChanged();
        }
    }

    private void onOrientationChanged() {
        if (getOrientation() == HORIZONTAL) {
            LayoutParams lp;
            if (arrow != null) {
                lp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = spacingHorizontal;
                lp.weight = 1;
            } else {
                lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = spacingHorizontal;
            }
            mTitleView.setLayoutParams(lp);
            if (mArrowView != null) {
                mArrowView.setVisibility(VISIBLE);
            }
        } else if (getOrientation() == VERTICAL) {
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = spacingVertical;
            mTitleView.setLayoutParams(lp);
            if (mArrowView != null) {
                mArrowView.setVisibility(GONE);
            }
        }
    }

    public void setTitle(String title) {
        this.mTitleView.setText(title);
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public ImageView getIconView() {
        return mIconView;
    }

    public ImageView getArrowView() {
        return mArrowView;
    }
}

