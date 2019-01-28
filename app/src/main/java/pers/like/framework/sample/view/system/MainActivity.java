package pers.like.framework.sample.view.system;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import pers.like.framework.main.ui.component.common.Pager;
import pers.like.framework.main.ui.component.common.PagerAdapter;
import pers.like.framework.main.util.StatusBarUtil;
import pers.like.framework.sample.R;
import pers.like.framework.sample.base.HsqActivity;
import pers.like.framework.sample.databinding.MainRoot;
import pers.like.framework.sample.view.PagerViewModel;

/**
 * @author Like
 */
@Route(path = "/system/main")
public class MainActivity extends HsqActivity {

    private PagerViewModel mPageViewModel;
    private List<Pager> viewList = new ArrayList<>();
    private MainRoot mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);
        mPageViewModel = ViewModelProviders.of(this).get(PagerViewModel.class);
        mRoot = DataBindingUtil.setContentView(this, R.layout.activity_main);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), viewList);

        viewList.add(new Pager(R.drawable.main_tab_dou, "豆瓣", new DoubanFragment()));
        viewList.add(new Pager(R.drawable.main_tab_dou, "豆瓣", new DoubanFragment()));
        mPageViewModel.PAGE_INDEX.observe(this, integer -> {
            if (integer == null) {
                mRoot.viewpagerMainContent.setCurrentItem(0);
            } else {
                mRoot.viewpagerMainContent.setCurrentItem(integer);
            }
        });
        mRoot.viewpagerMainContent.setAdapter(adapter);
        mRoot.viewpagerMainContent.setOffscreenPageLimit(3);
        mRoot.layoutMainTab.setupWithViewPager(mRoot.viewpagerMainContent);
        mRoot.layoutMainTab.setOnApplyWindowInsetsListener((view, windowInsets) -> windowInsets.consumeSystemWindowInsets());
        setupTabs();

    }

    private void setupTabs() {
        if (mRoot.layoutMainTab.getTabCount() <= 0) {
            return;
        }
        for (int i = 0; i < mRoot.layoutMainTab.getTabCount(); i++) {
            Pager pager = viewList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_main_tab, null);
            ImageView imageView = view.findViewById(R.id.image_tab_icon);
            TextView textView = view.findViewById(R.id.text_tab_title);
            imageView.setImageResource(pager.getIcon());
            textView.setText(pager.getTitle());
            TabLayout.Tab tab = mRoot.layoutMainTab.getTabAt(i);
            if (tab == null) {
                continue;
            }
            tab.setCustomView(view);
        }
    }

    private long time = 0;
    public static final int FINISH_INTERVAL = 2000;

    @Override
    public void onBackPressed() {
        long current = System.currentTimeMillis();
        if (current - time < FINISH_INTERVAL) {
            finish();
        } else {
            info("再按一次退出应用");
            time = current;
        }
    }
}
