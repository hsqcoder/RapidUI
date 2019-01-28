package pers.like.framework.main.ui.component;

import android.databinding.ViewDataBinding;

/**
 * @author Like
 */
@SuppressWarnings("WeakerAccess")
public class BaseViewHolder<T extends ViewDataBinding> extends com.chad.library.adapter.base.BaseViewHolder {

    private final T binding;

    public final T getBinding() {
        return this.binding;
    }

    public BaseViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

}
