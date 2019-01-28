package pers.like.framework.sample.view;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import pers.like.framework.sample.base.HsqViewModel;

/**
 * @author Like
 */
public class PagerViewModel extends HsqViewModel {

    public final MutableLiveData<Integer> PAGE_INDEX = new MutableLiveData<>();

    public PagerViewModel(@NonNull Application application) {
        super(application);
        PAGE_INDEX.setValue(0);
    }

}
