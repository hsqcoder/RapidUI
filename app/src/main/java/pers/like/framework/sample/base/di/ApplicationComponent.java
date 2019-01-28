package pers.like.framework.sample.base.di;


import javax.inject.Singleton;

import dagger.Component;
import pers.like.framework.main.di.BaseApplicationComponent;
import pers.like.framework.main.network.stomp.SocketClient;
import pers.like.framework.sample.base.HsqRefreshCenter;
import pers.like.framework.sample.base.HsqUserSystem;
import pers.like.framework.sample.model.network.DoubanApi;

/**
 * @author like
 */
@Singleton
@Component(modules = {ApiModule.class, ApplicationModule.class}, dependencies = BaseApplicationComponent.class)
public interface ApplicationComponent extends BaseApplicationComponent {

    /**
     * 提供全局单例用户中心
     *
     * @return 用户中心
     */
    HsqUserSystem userSystem();

    /**
     * 提供全局单例webSocket客户端
     *
     * @return 客户端
     */
    SocketClient webSocket();

    /**
     * 提供全局单例刷新管理器
     *
     * @return 管理器
     */
    HsqRefreshCenter hsqRefreshCenter();

    /**
     * 提供全局单例doubanApi
     *
     * @return doubanApi
     */
    DoubanApi doubanApi();

}
