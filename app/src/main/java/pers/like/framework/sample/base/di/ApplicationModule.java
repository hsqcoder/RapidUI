package pers.like.framework.sample.base.di;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pers.like.framework.main.BaseApplication;
import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.di.BaseApplicationModule;
import pers.like.framework.main.network.stomp.SocketClient;
import pers.like.framework.main.util.Cache;
import pers.like.framework.sample.base.HsqRefreshCenter;
import pers.like.framework.sample.base.HsqUserSystem;

/**
 * @author Like
 */
@Module
public class ApplicationModule extends BaseApplicationModule {

    public ApplicationModule(BaseApplication application) {
        super(application);
    }

    @Singleton
    @Provides
    HsqUserSystem userSystem(Cache cache) {
        return new HsqUserSystem(cache);
    }

    @Singleton
    @Provides
    HsqRefreshCenter refreshCenter() {
        return new HsqRefreshCenter();
    }

    @Singleton
    @Provides
    SocketClient webSocket(BaseExecutor executor) {
        return new SocketClient(executor);
    }

}
