package pers.like.framework.main.di;


import android.app.Application;

import dagger.Component;
import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.util.Cache;
import pers.like.framework.main.util.ViewUtil;

@Component(modules = {BaseApplicationModule.class})
public interface BaseApplicationComponent {

    Application application();

    BaseExecutor executor();

    Cache cache();

    ViewUtil viewUtil();

}
