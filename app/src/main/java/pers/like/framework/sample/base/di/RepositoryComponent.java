package pers.like.framework.sample.base.di;

import dagger.Component;
import pers.like.framework.sample.view.system.DoubanViewModel;

/**
 * @author like
 */
@PerActivity
@Component(modules = {RepositoryModule.class}, dependencies = ApplicationComponent.class)
public interface RepositoryComponent extends ApplicationComponent {

    /**
     * 注入doubanViewModel
     *
     * @param doubanViewModel doubanViewModel
     */
    void inject(DoubanViewModel doubanViewModel);

}
