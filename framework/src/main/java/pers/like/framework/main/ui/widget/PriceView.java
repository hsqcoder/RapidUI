package pers.like.framework.main.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;

import pers.like.framework.main.R;


/**
 * @author Like
 */
public class PriceView extends View {

    private static final String DEFAULT_UNIT = "￥";
    private static final String DEFAULT_FORMAT = "%.2f";

    private String unit;
    private int unitTextSize;
    private float price;
    private String priceText;
    private int priceTextSize;

    private int color;
    private String format;
    private boolean hasDeleteLine;

    private Paint mPaint;

    public PriceView(Context context) {
        this(context, null);
    }

    public PriceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PriceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float scale = context.getResources().getDisplayMetrics().densityDpi / 160f;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PriceView);
        price = ta.getFloat(R.styleable.PriceView_pv_price, 0);
        priceTextSize = ta.getDimensionPixelSize(R.styleable.PriceView_pv_price_size, (int) (14 * scale + 0.5f));
        unit = ta.getString(R.styleable.PriceView_pv_unit);
        unitTextSize = ta.getDimensionPixelSize(R.styleable.PriceView_pv_unit_size, (int) (10 * scale + 0.5f));
        hasDeleteLine = ta.getBoolean(R.styleable.PriceView_pv_delete_line, false);
        color = ta.getColor(R.styleable.PriceView_pv_color, Color.RED);
        if (unit == null) {
            unit = DEFAULT_UNIT;
        }
        format = ta.getString(R.styleable.PriceView_pv_format);
        if (format == null) {
            format = DEFAULT_FORMAT;
        }
        priceText = String.format(format, price);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int totalWidth = getPaddingStart();
        mPaint.setTextSize(unitTextSize);
        totalWidth += mPaint.measureText(unit);
        int unitHeight = (int) (mPaint.descent() - mPaint.ascent());
        mPaint.setTextSize(priceTextSize);
        totalWidth += mPaint.measureText(priceText);
        int priceHeight = (int) (mPaint.descent() - mPaint.ascent());
        int totalHeight = getPaddingTop() + Math.max(unitHeight, priceHeight);

        if (widthMode == MeasureSpec.EXACTLY) {
            totalWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            totalWidth = Math.min(totalWidth + getPaddingRight(), widthSize);
        } else {
            totalWidth = totalWidth + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            totalHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            totalHeight = Math.min(totalHeight + getPaddingBottom(), heightSize);
        } else {
            totalHeight = totalHeight + getPaddingBottom();
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(color);
        mPaint.setTextSize(unitTextSize);
        int totalWidth = 0;
        if (!"--".equals(priceText)) {
            totalWidth += (int) mPaint.measureText(unit);
            canvas.drawText(unit, getPaddingStart(), getMeasuredHeight() / 2f + (mPaint.descent() - mPaint.ascent()) / 2 - mPaint.descent(), mPaint);
        }
        mPaint.setTextSize(priceTextSize);
        canvas.drawText(priceText, getPaddingStart() + totalWidth, getMeasuredHeight() / 2f + (mPaint.descent() - mPaint.ascent()) / 2 - mPaint.descent(), mPaint);

        if (hasDeleteLine) {
            canvas.drawLine(getPaddingStart(), getMeasuredHeight() / 2, getMeasuredWidth() - getPaddingEnd(), getMeasuredHeight() / 2, mPaint);
        }
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
        invalidate();
    }

    public int getUnitTextSize() {
        return unitTextSize;
    }

    public void setUnitTextSize(int unitTextSize) {
        this.unitTextSize = unitTextSize;
        invalidate();
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if (price == -1) {
            this.priceText = "--";
        } else {
            this.priceText = String.format(format, price);
        }
        requestLayout();
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            this.priceText = "--";
        } else {
            this.priceText = price.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        requestLayout();
    }

    public int getPriceTextSize() {
        return priceTextSize;
    }

    public void setPriceTextSize(int priceTextSize) {
        this.priceTextSize = priceTextSize;
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        invalidate();
    }

    public boolean isHasDeleteLine() {
        return hasDeleteLine;
    }

    public void setHasDeleteLine(boolean hasDeleteLine) {
        this.hasDeleteLine = hasDeleteLine;
        invalidate();
    }
}
