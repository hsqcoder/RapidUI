package pers.like.framework.main.ui.component.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Pager> pagers;

    public PagerAdapter(FragmentManager fm, List<Pager> pagers) {
        super(fm);
        this.pagers = pagers;
    }

    @Override
    public Fragment getItem(int position) {
        return pagers.get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagers.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

}
