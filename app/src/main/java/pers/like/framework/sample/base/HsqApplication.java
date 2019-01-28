package pers.like.framework.sample.base;

import com.alibaba.android.arouter.launcher.ARouter;

import pers.like.framework.main.BaseApplication;
import pers.like.framework.main.component.zxing.activity.ZXingLibrary;
import pers.like.framework.main.di.BaseApplicationComponent;
import pers.like.framework.sample.base.di.ApplicationComponent;
import pers.like.framework.sample.base.di.ApplicationModule;
import pers.like.framework.sample.base.di.DaggerApplicationComponent;

/**
 * @author like
 */
public class HsqApplication extends BaseApplication {

    protected ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().baseApplicationComponent(baseApplicationComponent).applicationModule(new ApplicationModule(this)).build();
        //if (BuildConfig.DEBUG) {
        ARouter.openDebug();
        ARouter.openLog();
        //}
        ARouter.init(this);
        ZXingLibrary.initDisplayOpinion(this);
    }

    @Override
    public BaseApplicationComponent get() {
        return applicationComponent;
    }

}
