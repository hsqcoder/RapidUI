package pers.like.framework.main.ui.component;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Like
 */
public class BaseHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;

    public BaseHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    public BaseHolder text(int id, String text) {
        TextView textView = getView(id);
        textView.setText(text);
        return this;
    }

    public BaseHolder text(int id, @StringRes int strId) {
        TextView textView = getView(id);
        textView.setText(strId);
        return this;
    }

    public BaseHolder onClick(int id, View.OnClickListener callback) {
        View view = getView(id);
        view.setOnClickListener(callback);
        return this;
    }

    public BaseHolder image(int id, int resId) {
        ImageView imageView = getView(id);
        imageView.setImageResource(resId);
        return this;
    }

    public BaseHolder visible(int id, boolean visible) {
        View view = getView(id);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseHolder clickable(int id, boolean clickable) {
        View view = getView(id);
        view.setClickable(clickable);
        return this;
    }

    public BaseHolder alpha(int id, float alpha) {
        View view = getView(id);
        view.animate().alpha(alpha).setDuration(1).start();
        return this;
    }

    public BaseHolder translationX(int id, int x) {
        View view = getView(id);
        view.setTranslationX(x);
        view.animate().translationX(x).setDuration(1).start();
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
