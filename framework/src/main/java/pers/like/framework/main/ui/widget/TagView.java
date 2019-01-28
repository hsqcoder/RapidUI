package pers.like.framework.main.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pers.like.framework.main.R;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class TagView extends View {

    private float density;

    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private ColorStateList mTextColor;
    private Drawable mTagBackground;
    private int mMaxLines;
    private int mTextSize;
    private List<Tag> mTagList = new ArrayList<>();

    private Rect mPadding = new Rect();
    private Paint mPaint;

    private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private float mTouchX;
    private float mTouchY;
    private int mBreakItem;

    private boolean isSingleMode;

    private OnTagChangeListener mOnTagChangeListener;

    public void setOnTagClickListener(OnTagChangeListener onTagClickListener) {
        this.mOnTagChangeListener = onTagClickListener;
    }

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        mHorizontalSpacing = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_horizontal_spacing, getPx(4));
        mVerticalSpacing = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_vertical_spacing, getPx(4));
        mMaxLines = typedArray.getInt(R.styleable.TagView_tv_max_lines, Integer.MAX_VALUE);

        int padding = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_padding, getPx(4));
        mPadding = new Rect(padding, padding, padding, padding);
        int paddingStart = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_padding_start, -1);
        mPadding.left = paddingStart == -1 ? mPadding.left : paddingStart;
        int paddingEnd = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_padding_end, -1);
        mPadding.right = paddingEnd == -1 ? mPadding.right : paddingEnd;
        int paddingTop = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_padding_top, -1);
        mPadding.top = paddingTop == -1 ? mPadding.top : paddingTop;
        int paddingBottom = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_padding_bottom, -1);
        mPadding.bottom = paddingBottom == -1 ? mPadding.bottom : paddingBottom;

        mTextColor = typedArray.getColorStateList(R.styleable.TagView_tv_text_color);
        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(Color.GRAY);
        }

        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_text_size, getPx(12));

        mTagBackground = typedArray.getDrawable(R.styleable.TagView_tv_tag_background);
        if (mTagBackground == null) {
            mTagBackground = new ColorDrawable(Color.TRANSPARENT);
        }
        isSingleMode = typedArray.getBoolean(R.styleable.TagView_tv_single_select_mode, false);
        String tagSource = typedArray.getString(R.styleable.TagView_tv_tags);

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        addAll(tagSource);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int totalWidth = 0;
        int totalHeight = getPaddingTop();

        int lineWidth = getPaddingLeft();
        int lineHeight = 0;

        int lines = 0;
        for (int i = 0, childNum = mTagList.size(); i < childNum; i++) {
            Tag tag = mTagList.get(i);
            Rect padding = tag.padding();
            if (padding == null) {
                padding = mPadding;
            }
            if (tag.textSize() == 0) {
                mPaint.setTextSize(mTextSize);
            } else {
                mPaint.setTextSize(tag.textSize());
            }
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            int textWidth = (int) mPaint.measureText(tag.text());
            int tagWidth = Math.min(mHorizontalSpacing + padding.left + padding.right + textWidth, widthSize - getPaddingRight() - getPaddingLeft());

            Rect rect = tag.getRect();

            if ((lineWidth + tagWidth) <= widthSize - getPaddingRight()) {
                rect.top = totalHeight;
                if (lineWidth == getPaddingLeft()) {
                    rect.left = lineWidth;
                    lineWidth += tagWidth - mHorizontalSpacing;
                } else {
                    rect.left = lineWidth + mHorizontalSpacing;
                    lineWidth += tagWidth;
                }
                lineHeight = Math.max(lineHeight, textHeight + padding.top + padding.bottom);
            } else {
                lines++;
                if (lines >= mMaxLines) {
                    break;
                }
                totalHeight += lineHeight + mVerticalSpacing;
                rect.top = totalHeight;
                rect.left = getPaddingLeft();
                totalWidth = Math.max(lineWidth, totalWidth);
                lineWidth = getPaddingLeft() + tagWidth - mHorizontalSpacing;
                lineHeight = textHeight + padding.top + padding.bottom;
            }
            mBreakItem = i;
            rect.bottom = rect.top + textHeight + padding.top + padding.bottom;
            rect.right = Math.min(rect.left + padding.left + padding.right + textWidth, widthSize - getPaddingRight());
        }
        totalWidth = Math.max(lineWidth, totalWidth);
        totalHeight += lineHeight;

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

    private int[] selected = new int[]{android.R.attr.state_selected};
    private int[] normal = new int[]{};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0, childNum = mTagList.size(); i < childNum; i++) {
            if (i > mBreakItem) {
                break;
            }
            Tag tag = mTagList.get(i);
            Rect rect = tag.getRect();
            Rect padding = tag.padding();
            if (padding == null) {
                padding = mPadding;
            }
            Drawable drawable = tag.background();
            if (drawable == null) {
                drawable = mTagBackground;
            }
            drawable.setBounds(rect);
            if (drawable.isStateful()) {
                drawable.setState(tag.isSelected() ? selected : normal);
            }
            drawable.draw(canvas);
            ColorStateList colorStateList = tag.textColor();
            if (colorStateList == null) {
                colorStateList = mTextColor;
            }
            mPaint.setColor(colorStateList.getColorForState(tag.isSelected() ? selected : normal, colorStateList.getDefaultColor()));
            if (tag.textSize() == 0) {
                mPaint.setTextSize(mTextSize);
            } else {
                mPaint.setTextSize(tag.textSize());
            }
            int textWidth = (int) mPaint.measureText(tag.text());
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            int baseline = (rect.bottom - padding.bottom + rect.top + padding.top - fontMetrics.bottom - fontMetrics.top) / 2;
            canvas.drawText(tag.text(), rect.left + padding.left,
                    baseline, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                return true;

            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                int position = getTagPosition((int) x, (int) y);
                if (Math.abs(x - mTouchX) < mTouchSlop && Math.abs(y - mTouchY) < mTouchSlop && position != -1) {
                    pressTag(position);
                    invalidate();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void pressTag(int position) {
        if (position < 0) {
            return;
        }
        Tag touchTag = mTagList.get(position);
        if (touchTag == null) {
            return;
        }
        pressTag(touchTag);
    }

    public void pressTag(Tag touchTag) {
        int position = mTagList.indexOf(touchTag);
        if (position < 0) {
            return;
        }
        selectTag(touchTag);
        if (mOnTagChangeListener != null) {
            mOnTagChangeListener.onClick(this, position, touchTag);
        }
    }

    public void selectTag(int position) {
        if (position < 0) {
            return;
        }
        Tag touchTag = mTagList.get(position);
        if (touchTag == null) {
            return;
        }
        selectTag(touchTag);
    }

    public void selectTag(Tag selectedTag) {
        int position = mTagList.indexOf(selectedTag);
        if (position < 0) {
            return;
        }
        if (isSingleMode) {
            for (Tag tag : mTagList) {
                if (tag.isSelected()) {
                    tag.toggleSelected();
                }
            }
        }
        selectedTag.toggleSelected();
    }

    private int getTagPosition(int x, int y) {
        for (int i = 0; i < mTagList.size(); i++) {
            if (mTagList.get(i).getRect().contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    private int getPx(int dp) {
        return (int) (dp * density + 0.5f);
    }

    public void add(List<? extends Tag> tagList) {
        if (tagList == null || tagList.size() <= 0) {
            return;
        }
        if (isSingleMode) {
            for (Tag filted : tagList) {
                filted.setSelected(false);
            }
            if (mTagList.size() <= 0) {
                tagList.get(0).setSelected(true);
            }
        }
        mTagList.addAll(tagList);
        onTagChange();
        requestLayout();
        invalidate();
    }

    public void add(Tag tag) {
        add(Arrays.asList(tag));
    }

    public void add(String tagString) {
        Tag tag = buildTag(tagString);
        add(tag);
    }

    public void addAll(String tagSource) {
        if (!TextUtils.isEmpty(tagSource)) {
            String[] tags = tagSource.split(",");
            List<Tag> tagList = new ArrayList<>();
            for (String tag : tags) {
                tagList.add(buildTag(tag));
            }
            add(tagList);
        }
    }

    public void replace(List<Tag> tagList) {
        mTagList.clear();
        add(tagList);
    }

    public void replace(String tagSource) {
        mTagList.clear();
        addAll(tagSource);
    }

    private Tag buildTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return null;
        }
        return new TagBuilder(tag, mTextColor, mTextSize, mPadding, mTagBackground).build();
    }

    public void removeAll() {
        mTagList.clear();
        onTagChange();
        requestLayout();
    }

    public String selectedTagText() {
        if (mTagList == null || mTagList.size() == 0) {
            return "";
        }
        for (Tag tag : mTagList) {
            if (tag.isSelected()) {
                return tag.text();
            }
        }
        return "";
    }

    public Tag selectedTag() {
        if (mTagList == null || mTagList.size() == 0) {
            return null;
        }
        for (Tag tag : mTagList) {
            if (tag.isSelected()) {
                return tag;
            }
        }
        return null;
    }

    public String selectedTagTexts() {
        if (mTagList == null || mTagList.size() == 0) {
            return "";
        }
        List<String> selectedTags = new ArrayList<>();
        for (Tag tag : mTagList) {
            if (tag.isSelected()) {
                selectedTags.add(tag.text());
            }
        }
        return String.join(",", selectedTags);
    }

    public List<Tag> selectedTags() {
        List<Tag> tagList = new ArrayList<>();
        if (mTagList == null || mTagList.size() == 0) {
            return tagList;
        }
        for (Tag tag : mTagList) {
            if (tag.isSelected()) {
                tagList.add(tag);
            }
        }
        return tagList;
    }

    private void onTagChange() {
        if (mOnTagChangeListener != null) {
            mOnTagChangeListener.onTagChanged(this);
        }
    }

    public static class TagBuilder {
        private String text;
        private ColorStateList textColor;
        private int textSize;
        private Rect padding;
        private Drawable background;
        private boolean selected = false;

        TagBuilder() {
        }

        TagBuilder(String text, ColorStateList textColor, int textSize, Rect padding, Drawable background) {
            this.text = text;
            this.textColor = textColor;
            this.textSize = textSize;
            this.padding = padding;
            this.background = background;
        }

        public InnerTag build() {
            return new InnerTag(text, textColor, textSize, padding, background, selected);
        }


        public TagBuilder text(String text) {
            this.text = text;
            return this;
        }

        public TagBuilder textColor(int textColor) {
            this.textColor = ColorStateList.valueOf(textColor);
            return this;
        }

        public TagBuilder textColor(ColorStateList textColor) {
            this.textColor = textColor;
            return this;
        }

        public TagBuilder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public TagBuilder padding(Rect padding) {
            this.padding = padding;
            return this;
        }

        public TagBuilder padding(int top, int bottom, int start, int end) {
            if (this.padding == null) {
                this.padding = new Rect();
            }
            this.padding.top = top;
            this.padding.bottom = bottom;
            this.padding.left = start;
            this.padding.right = end;
            return this;
        }


        public TagBuilder background(Drawable background) {
            this.background = background;
            return this;
        }

        public TagBuilder selected(boolean selected) {
            this.selected = selected;
            return this;
        }

    }


    public static abstract class Tag {

        private Rect mRect = new Rect();
        private boolean selected = false;

        /**
         * tag内容
         *
         * @return tag内容
         */
        public abstract String text();

        /**
         * 文字大小
         *
         * @return 文字大小
         */
        public abstract int textSize();

        /**
         * Tag内边距
         *
         * @return Tag内边距
         */
        public abstract Rect padding();

        /**
         * tag背景
         *
         * @return 背景
         */
        public abstract Drawable background();

        /**
         * 文字颜色
         *
         * @return 文字颜色
         */
        public abstract ColorStateList textColor();

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void toggleSelected() {
            this.selected = !this.selected;
        }

        public final Rect getRect() {
            return mRect;
        }

        public final void setRect(Rect rect) {
            mRect = rect;
        }

    }

    public static class InnerTag extends Tag {

        private String text;
        private ColorStateList textColor;
        private int textSize;
        private Rect padding;
        private Drawable background;

        public InnerTag() {
        }

        public InnerTag(String text, ColorStateList textColor, int textSize, Rect padding, Drawable background, boolean selected) {
            this.text = text;
            this.textColor = textColor;
            this.textSize = textSize;
            this.padding = padding;
            this.background = background;
            this.setSelected(selected);
        }


        @Override
        public String text() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public ColorStateList textColor() {
            return textColor;
        }

        @Override
        public int textSize() {
            return textSize;
        }

        @Override
        public Rect padding() {
            return padding;
        }

        public void setPadding(int top, int bottom, int start, int end) {
            if (this.padding == null) {
                this.padding = new Rect();
            }
            this.padding.top = top;
            this.padding.bottom = bottom;
            this.padding.left = start;
            this.padding.right = end;
        }

        @Override
        public Drawable background() {
            return background;
        }

    }

    public interface OnTagChangeListener {
        /**
         * 标签点击事件
         *
         * @param position 坐标
         * @param tag      标签
         */
        void onClick(TagView view, int position, Tag tag);

        /**
         * 标签变化事件
         *
         * @param view 视图
         */
        void onTagChanged(TagView view);
    }
}
