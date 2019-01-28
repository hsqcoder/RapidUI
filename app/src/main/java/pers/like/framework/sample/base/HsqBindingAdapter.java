package pers.like.framework.sample.base;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import pers.like.framework.sample.R;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class HsqBindingAdapter {

    private final static String HTTP_PREFIX = "http";

    @BindingAdapter("picture")
    public static void picture(ImageView view, String url) {
        if (TextUtils.isEmpty(url)) {
            Glide.with(view.getContext()).load(R.mipmap.placeholder).into(view);
        } else {
            Glide.with(view.getContext()).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).error(R.mipmap.placeholder)).into(view);
        }
    }

    @BindingAdapter({"picture", "placeHolder"})
    public static void picture(ImageView view, String url, int placeHolder) {
        if (placeHolder == 0) {
            placeHolder = R.mipmap.placeholder;
        }
        if (TextUtils.isEmpty(url)) {
            Glide.with(view.getContext()).load(placeHolder).into(view);
        } else {
            Glide.with(view.getContext()).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).error(placeHolder)).into(view);
        }
    }

    @BindingAdapter("visible")
    public static void visible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("imageSrc")
    public static void imageSrc(ImageView view, int src) {
        view.setImageResource(src);
    }

}
