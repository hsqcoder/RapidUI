package pers.like.framework.sample.view.system;

import android.os.Bundle;
import android.support.annotation.Nullable;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import pers.like.framework.sample.R;
import pers.like.framework.sample.base.HsqActivity;

/**
 * @author like
 */
public class SplashActivity extends HsqActivity {

    private Disposable subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        subscribe = Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> {
                    navigate2("/system/main", true);
                    if (subscribe != null && !subscribe.isDisposed()) {
                        subscribe.dispose();
                    }
                });
    }

}
