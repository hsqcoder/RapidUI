package pers.like.framework.main.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import pers.like.framework.main.R;
import pers.like.framework.main.util.ViewUtil;


/**
 * @author like
 */
@SuppressWarnings("unused")
public class MappingDialog extends Dialog {

    public MappingDialog(@NonNull Context context, String title, List<Mapping> mappingArrayList, @NonNull OnItemSelectedListener listener) {
        super(context, R.style.BaseDialog);
        setContentView(R.layout.dialog_mapping);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        TextView tvTitle = findViewById(R.id.text_title);
        tvTitle.setText(title);
        BaseQuickAdapter<Mapping, BaseViewHolder> mAdapter = new BaseQuickAdapter<Mapping, BaseViewHolder>(R.layout.item_mapping) {
            @Override
            protected void convert(BaseViewHolder helper, Mapping item) {
                helper.setText(R.id.text, item.getName());
                helper.itemView.setOnClickListener(v -> {
                    listener.onItemSelected(item);
                    dismiss();
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.replaceData(mappingArrayList);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = new ViewUtil(context).getWidth(0.8f);
            window.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    public interface OnItemSelectedListener {
        /**
         * item点击事件
         *
         * @param item 选中的item
         */
        void onItemSelected(Mapping item);
    }

}
