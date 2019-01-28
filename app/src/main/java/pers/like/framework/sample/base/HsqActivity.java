package pers.like.framework.sample.base;

import android.support.annotation.NonNull;

import pers.like.framework.main.network.stomp.SocketClient;
import pers.like.framework.main.ui.component.BaseActivity;
import pers.like.framework.main.util.ViewUtil;
import pers.like.framework.sample.base.di.ApplicationComponent;

/**
 * @author Like
 */
@SuppressWarnings("ALL")
public class HsqActivity extends BaseActivity {

    @NonNull
    public <T extends ApplicationComponent> T component() {
        return (T) ((HsqApplication) getApplication()).get();
    }

    @NonNull
    public HsqUserSystem userSystem() {
        return component().userSystem();
    }

    @NonNull
    public ViewUtil viewUtil() {
        return component().viewUtil();
    }

    @NonNull
    public SocketClient webSocket() {
        return component().webSocket();
    }

    @NonNull
    public HsqRefreshCenter refreshCenter() {
        return component().hsqRefreshCenter();
    }

}
