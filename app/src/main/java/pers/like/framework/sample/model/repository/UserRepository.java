package pers.like.framework.sample.model.repository;

import pers.like.framework.main.BaseExecutor;
import pers.like.framework.main.util.Cache;
import pers.like.framework.sample.base.HsqUserSystem;
import pers.like.framework.sample.model.network.UserApi;

/**
 * @author Like
 */
public class UserRepository {

    private HsqUserSystem userSystem;
    private UserApi userApi;
    private Cache cache;
    private BaseExecutor executor;

    public UserRepository(HsqUserSystem userSystem, UserApi userApi, Cache cache, BaseExecutor executor) {
        this.userSystem = userSystem;
        this.userApi = userApi;
        this.cache = cache;
        this.executor = executor;
    }

}
