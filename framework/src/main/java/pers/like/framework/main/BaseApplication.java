package pers.like.framework.main;

import android.app.Application;
import android.support.annotation.CallSuper;

import pers.like.framework.main.di.BaseApplicationComponent;
import pers.like.framework.main.di.BaseApplicationComponentProvider;
import pers.like.framework.main.di.BaseApplicationModule;
import pers.like.framework.main.di.DaggerBaseApplicationComponent;
import pers.like.framework.main.util.BuildConfigUtil;


/**
 * @author Like
 */
public class BaseApplication extends Application implements BaseApplicationComponentProvider {

    protected BaseApplicationComponent baseApplicationComponent;

    @Override
    @CallSuper
    public void onCreate() {
        super.onCreate();
        BuildConfigUtil.init(this);
        baseApplicationComponent = DaggerBaseApplicationComponent.builder().baseApplicationModule(new BaseApplicationModule(this)).build();
    }

    public BaseApplicationComponent getBaseApplicationComponent() {
        return this.baseApplicationComponent;
    }

    @Override
    public BaseApplicationComponent get() {
        return baseApplicationComponent;
    }
}
