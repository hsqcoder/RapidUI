package pers.like.framework.sample.base.di;


import dagger.Module;
import dagger.Provides;
import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.util.Cache;
import pers.like.framework.sample.base.HsqUserSystem;
import pers.like.framework.sample.model.network.DoubanApi;
import pers.like.framework.sample.model.network.UserApi;
import pers.like.framework.sample.model.repository.DoubanRepository;
import pers.like.framework.sample.model.repository.UserRepository;

/**
 * @author like
 */
@Module
public class RepositoryModule {

    @Provides
    @PerActivity
    public UserRepository provideUserRepository(HsqUserSystem userSystem, UserApi userApi, Cache cache, BaseExecutor executor) {
        return new UserRepository(userSystem, userApi, cache, executor);
    }

    @Provides
    @PerActivity
    public DoubanRepository provideOrderRepository(DoubanApi doubanApi, Cache cache, BaseExecutor executor) {
        return new DoubanRepository(doubanApi, cache, executor);
    }

}
