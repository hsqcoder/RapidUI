package pers.like.framework.sample.base.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pers.like.framework.main.network.Network;
import pers.like.framework.sample.model.network.DoubanApi;

/**
 * @author Like
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    DoubanApi provideUserApi() {
        return createApi(DoubanApi.class);
    }

//    @Provides
//    @Singleton
//    OrderApi provideOrderApi() {
//        return createApi(OrderApi.class);
//    }


    private <T> T createApi(Class<T> clazz) {
        return Network.instance().create(clazz);
    }

}
