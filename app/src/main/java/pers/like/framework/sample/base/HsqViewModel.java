package pers.like.framework.sample.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import pers.like.framework.sample.base.di.ApplicationComponent;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class HsqViewModel extends AndroidViewModel {

    private HsqApplication application;

    public HsqViewModel(@NonNull Application application) {
        super(application);
        this.application = (HsqApplication) application;
    }

    @NonNull
    protected ApplicationComponent component() {
        return (ApplicationComponent) application.get();
    }

    @NonNull
    public HsqUserSystem userSystem() {
        return component().userSystem();
    }

    @NonNull
    public HsqRefreshCenter refreshCenter() {
        return component().hsqRefreshCenter();
    }

}
