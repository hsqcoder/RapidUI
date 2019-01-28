package pers.like.framework.sample.view.system;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pers.like.framework.main.ui.component.BaseAdapter;
import pers.like.framework.main.ui.component.ListLayout;
import pers.like.framework.main.util.StatusBarUtil;
import pers.like.framework.sample.BR;
import pers.like.framework.sample.R;
import pers.like.framework.sample.base.HsqFragment;
import pers.like.framework.sample.databinding.DoubanRoot;
import pers.like.framework.sample.databinding.ItemMovie;
import pers.like.framework.sample.model.pojo.douban.Movie;

/**
 * @author Like
 */
public class DoubanFragment extends HsqFragment {

    private DoubanViewModel mViewModel;
    private DoubanRoot mRoot;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = ViewModelProviders.of(this).get(DoubanViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            mRoot = DataBindingUtil.inflate(inflater, R.layout.fragment_douban, container, false);
            StatusBarUtil.setPaddingSmart(requireActivity(), mRoot.toolbar);
            BaseAdapter<Movie, ItemMovie> adapter = new BaseAdapter<>(BR.movie, R.layout.item_movie);
            ListLayout<Movie> listLayout = new ListLayout<Movie>().adapter(adapter).pageSize(10).bind(mRoot.getRoot())
                    .loadMore(true).loadCallback(params -> mViewModel.search(params.put("q", "哈利波特")))
                    .autoLoad();
            mViewModel.MOVIE_LIST.observe(this, listLayout.observer());
        }
        return mRoot.getRoot();
    }

}
