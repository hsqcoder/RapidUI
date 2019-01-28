package pers.like.framework.main.ui.component;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Like
 */
@SuppressWarnings("unused")
public class BaseAdapter<T, DB extends ViewDataBinding> extends BaseQuickAdapter<T, BaseViewHolder<DB>> implements DataSource<T> {

    private int variable;
    private int resId;
    private OnItemBoundListener<DB, T> onItemBoundListener;

    public BaseAdapter(int variable, int resId) {
        super(resId, null);
        this.variable = variable;
        this.resId = resId;
    }

    @Override
    protected void convert(BaseViewHolder<DB> helper, T item) {
        helper.getBinding().setVariable(variable, item);
        if (onItemBoundListener != null) {
            onItemBoundListener.onItemBound(helper.getBinding(), item, helper.getAdapterPosition());
        }
        helper.getBinding().executePendingBindings();
    }

    @Override
    protected BaseViewHolder<DB> onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), resId, parent, false));
    }

    public void setOnItemBoundListener(OnItemBoundListener<DB, T> onItemBoundListener) {
        this.onItemBoundListener = onItemBoundListener;
    }

    @Override
    public void replace(@NonNull Collection<? extends T> data) {
        this.replaceData(data);
    }

    @Override
    public void add(@NonNull Collection<? extends T> data) {
        this.addData(data);
    }

    @Override
    public void clear() {
        this.replaceData(new ArrayList<>());
    }

    @Override
    public int size() {
        return this.getItemCount();
    }

    public interface OnItemBoundListener<DB, T> {
        /**
         * Item绑定到视图是触发
         *
         * @param binding itemBinding
         * @param item    itemData
         */
        void onItemBound(DB binding, T item, int position);
    }
}
