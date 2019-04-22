package pers.like.framework.sample.view.system;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import pers.like.framework.main.network.Params;
import pers.like.framework.main.network.resource.Resource;
import pers.like.framework.sample.base.HsqViewModel;
import pers.like.framework.sample.base.di.DaggerRepositoryComponent;
import pers.like.framework.sample.model.pojo.douban.DoubanListWrapper;
import pers.like.framework.sample.model.pojo.douban.Movie;
import pers.like.framework.sample.model.repository.DoubanRepository;

/**
 * @author Like
 */
public class DoubanViewModel extends HsqViewModel {

    @Inject
    public DoubanRepository repository;

    private final MutableLiveData<Params> PARAMS_DOUBAN_SEARCH = new MutableLiveData<>();

    private final LiveData<Resource<DoubanListWrapper<Movie>>> DOUBAN_SEARCH = Transformations.switchMap(PARAMS_DOUBAN_SEARCH, input -> repository.search(input));

    public final LiveData<Resource<List<Movie>>> MOVIE_LIST = Transformations.map(DOUBAN_SEARCH, input ->
            new Resource<>(input.call, input.params, input.headers, input.code, input.data == null ? null : input.data.getSubjects(), input.status, input.message));

    public DoubanViewModel(@NonNull Application application) {
        super(application);
        DaggerRepositoryComponent.builder().applicationComponent(component()).build().inject(this);
    }

    public void search(@NonNull Params params) {
        PARAMS_DOUBAN_SEARCH.setValue(params);
    }

}
