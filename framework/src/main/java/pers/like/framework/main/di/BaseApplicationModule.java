package pers.like.framework.main.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.util.Cache;
import pers.like.framework.main.util.ViewUtil;

/**
 * @author like
 */
@Module
public class BaseApplicationModule {

    private Application mApplication;

    public BaseApplicationModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    Application provideContext() {
        return mApplication;
    }

    @Provides
    Cache providesCache() {
        return new Cache(mApplication);
    }

    @Provides
    BaseExecutor providesExecutor() {
        return new BaseExecutor();
    }

    @Provides
    ViewUtil providesViewUtl() {
        return new ViewUtil(mApplication);
    }


}
