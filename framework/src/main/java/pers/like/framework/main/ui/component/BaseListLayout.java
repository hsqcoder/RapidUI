package pers.like.framework.main.ui.component;

import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import pers.like.framework.main.Callback;
import pers.like.framework.main.network.Params;

public interface BaseListLayout<T> extends BaseLayout<List<T>> {

    SmartRefreshLayout refreshLayout();

    RecyclerView recyclerView();

    int size();

    Callback<Params> loadCallback();

}
