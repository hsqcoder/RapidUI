package pers.like.framework.main.ui.component;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import pers.like.framework.main.Callback;
import pers.like.framework.main.R;
import pers.like.framework.main.network.Params;
import pers.like.framework.main.util.ObjectUtils;
import pers.like.widget.loadview.Action;
import pers.like.widget.loadview.LoadView;
import pers.like.widget.loadview.Options;

/**
 * @author Like
 */
@SuppressWarnings("unused")
public class ListLayout<T> extends BaseObserver<List<T>> implements BaseListLayout<T> {

    private LoadView mLoadView;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshView;
    private boolean enableLoadMore = false, enableRefresh = true;
    private Callback<Params> loadCallback;
    private int pageSize = 10;
    private BaseListObserver observer;
    private RecyclerView.Adapter mAdapter;
    private DataSource<T> mDataSource;
    private RecyclerView.LayoutManager mLayoutManager;
    private Options emptyOptions, errorOptions;
    private Interceptor<T> interceptor;
    private SparseArray<Options> optionsMap = new SparseArray<>();

    public ListLayout() {
        this.observer = new BaseListObserver(this);
    }

    @Override
    public ListLayout<T> bind(View root) {
        mLoadView = root.findViewById(R.id.loadView);
        if (emptyOptions != null) {
            mLoadView.emptyOptions(emptyOptions);
        }
        if (errorOptions != null) {
            mLoadView.errorOptions(errorOptions);
        }
        mRefreshView = root.findViewById(R.id.refreshView);
        if (mRefreshView != null) {
            mRefreshView.setOnRefreshListener(refreshLayout -> loadCallback.call(Refresh.put("offset", 0).put("limit", pageSize)
                    .put("start", 0).put("count", pageSize).put("pageNum", 1).put("pageSize", pageSize)));
            mRefreshView.setOnLoadMoreListener(refreshLayout -> loadCallback.call(LoadMore.put("offset", mDataSource.size())
                    .put("limit", pageSize).put("pageSize", pageSize).put("pageNum", mDataSource.size() / pageSize + 1)
                    .put("start", mDataSource.size() / pageSize + 1).put("count", pageSize)));
            mRefreshView.setEnableLoadMore(enableLoadMore);
            mRefreshView.setEnableRefresh(enableRefresh);
        }
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager != null ? mLayoutManager : new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        return this;
    }

    @Override
    public void loading(List<T> data) {
        if (data != null && !data.isEmpty()) {
            mDataSource.replace(data);
            mLoadView.content();
        } else {
            if (isEmpty()) {
                mLoadView.loading();
            } else {
                mLoadView.content();
            }
        }
    }

    @Override
    public void data(List<T> data) {
        mDataSource.add(data);
        if (isEmpty()) {
            mLoadView.empty();
        } else {
            mLoadView.content();
        }
    }

    @Override
    public void replace(List<T> data) {
        mDataSource.replace(data);
        if (isEmpty()) {
            mLoadView.empty();
        } else {
            mLoadView.content();
        }
    }

    @Override
    public void error(int code, String message, View.OnClickListener retry) {
        if (isEmpty()) {
            if (optionsMap.get(code) != null) {
                mLoadView.error(optionsMap.get(code));
            } else {
                mLoadView.error();
            }
        } else {
            mLoadView.content();
            Toast.makeText(mLoadView.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void empty() {
        if (isEmpty()) {
            mLoadView.empty();
        } else {
            mLoadView.content();
        }
    }

    @Override
    public boolean isEmpty() {
        return mDataSource.size() <= 0;
    }

    public ListLayout<T> loadView(LoadView loadingLayout) {
        this.mLoadView = loadingLayout;
        mLoadView.emptyOptions(emptyOptions);
        return this;
    }

    @Nullable
    @Override
    public SmartRefreshLayout refreshLayout() {
        return mRefreshView;
    }

    public ListLayout<T> refreshLayout(SmartRefreshLayout refreshLayout) {
        mRefreshView = refreshLayout;
        if (mRefreshView != null) {
            mRefreshView.setOnRefreshListener(layout -> loadCallback.call(Refresh.put("offset", 0).put("start", 0).put("count", pageSize).put("limit", pageSize).put("pageNum", 1).put("pageSize", pageSize)));
            mRefreshView.setOnLoadMoreListener(layout -> loadCallback.call(LoadMore.put("offset", mDataSource.size()).put("start", mDataSource.size()).put("count", pageSize)
                    .put("limit", pageSize).put("pageSize", pageSize).put("pageNum", pageSize == 0 ? 0 : mDataSource.size() / pageSize + 1)));
            mRefreshView.setEnableLoadMore(enableLoadMore);
            mRefreshView.setEnableRefresh(enableRefresh);
        }
        return this;
    }

    @Override
    public RecyclerView recyclerView() {
        return mRecyclerView;
    }

    public ListLayout<T> recyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setLayoutManager(mLayoutManager != null ? mLayoutManager : new LinearLayoutManager(recyclerView.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        return this;
    }

    @Override
    public int size() {
        return pageSize;
    }

    @Override
    public Callback<Params> loadCallback() {
        return loadCallback;
    }

    public BaseListObserver observer() {
        return this.observer;
    }

    public class BaseListObserver extends BaseObserver<List<T>> {

        private final BaseListLayout<T> page;
        private Interceptor<T> interceptor;

        BaseListObserver(@NonNull BaseListLayout<T> page) {
            this.page = page;
        }

        public void setInterceptor(Interceptor<T> interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        protected void emptyData(@NonNull Params params) {
            super.emptyData(params);
            page.empty();
            updateRefreshView(Refresh.isRefresh(params), false, false);
        }

        @Override
        public void success(@NonNull Params params, @NonNull List<T> data) {
            super.success(params, data);
            if (interceptor != null) {
                data = interceptor.doChain(data);
            }
            if (data != null) {
                boolean noMore = data.size() < page.size();
                if (Refresh.isRefresh(params)) {
                    page.replace(data);
                    updateRefreshView(true, true, noMore);
                } else {
                    page.data(data);
                    updateRefreshView(false, true, noMore);
                }
            } else {
                page.empty();
                updateRefreshView(Refresh.isRefresh(params), false, false);
            }
        }

        private void updateRefreshView(boolean isRefresh, boolean success, boolean noMore) {
            if (page.refreshLayout() != null) {
                if (isRefresh) {
                    page.refreshLayout().finishRefresh(success);
                    page.refreshLayout().setNoMoreData(noMore);
                } else {
                    page.refreshLayout().finishLoadMore(0, success, noMore);
                }
            }
        }

        @Override
        public void failed(@NonNull Params params, int code, String message) {
            super.failed(params, code, message);
            if (page.refreshLayout() != null) {
                if (Refresh.isRefresh(params)) {
                    page.refreshLayout().finishRefresh(false);
                }
                if (LoadMore.isLoadMore(params)) {
                    page.refreshLayout().finishLoadMore(false);
                }
            }
            page.error(code, message, v -> page.loadCallback().call(params));
        }

        @Override
        public void loading(@NonNull Params params, @Nullable List<T> data) {
            super.loading(params, data);
            page.loading(data);
        }
    }

    public ListLayout<T> interceptor(Interceptor<T> interceptor) {
        observer.setInterceptor(interceptor);
        return this;
    }

    public ListLayout<T> errorIntercept(int code, Options options) {
        this.optionsMap.put(code, options);
        return this;
    }

    @SuppressWarnings("all")
    public ListLayout<T> adapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof DataSource) {
            mDataSource = (DataSource<T>) adapter;
            this.mAdapter = adapter;
        } else {
            throw new IllegalArgumentException("Your adapter must implements 'DataSource'");
        }
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
        }
        return this;
    }

    public ListLayout<T> layoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(layoutManager);
        }
        return this;
    }

    public ListLayout<T> loadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
        if (mRefreshView != null) {
            mRefreshView.setEnableLoadMore(enableLoadMore);
        }
        return this;
    }

    public ListLayout<T> refresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
        if (mRefreshView != null) {
            mRefreshView.setEnableRefresh(enableRefresh);
        }
        return this;
    }

    public ListLayout<T> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ListLayout<T> error(Options options) {
        this.errorOptions = options;
        if (mLoadView != null) {
            mLoadView.errorOptions(options);
        }
        return this;
    }

    public ListLayout<T> empty(Options options) {
        this.emptyOptions = options;
        if (mLoadView != null) {
            mLoadView.emptyOptions(options);
        }
        return this;
    }


    public ListLayout<T> loadCallback(Callback<Params> loadCallback) {
        this.loadCallback = loadCallback;
        return this;
    }

    public ListLayout<T> autoLoad() {
        this.loadCallback.call(Refresh.get().put("limit", pageSize).put("pageSize", pageSize).put("start", 0).put("count", pageSize));
        return this;
    }

    public ListLayout<T> autoLoad(@NonNull Params params) {
        this.loadCallback.call(Refresh.get().put(params).put("limit", pageSize).put("pageSize", pageSize).put("start", 0).put("count", pageSize));
        return this;
    }


    public interface Interceptor<T> {
        /**
         * 数据拦截
         *
         * @param data 数据
         * @return 处理后的数据
         */
        List<T> doChain(List<T> data);
    }

}
