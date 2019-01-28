package pers.like.framework.main.ui.component.common;

import android.support.v4.app.Fragment;

/**
 * @author Like
 */
public class Pager {

    private int icon;
    private String title;
    private Fragment fragment;

    public Pager() {
    }

    public Pager(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public Pager(int icon, String title, Fragment fragment) {
        this.icon = icon;
        this.title = title;
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

}
