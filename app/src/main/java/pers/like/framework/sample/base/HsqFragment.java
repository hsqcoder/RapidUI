package pers.like.framework.sample.base;

import android.support.annotation.NonNull;


import pers.like.framework.main.network.stomp.SocketClient;
import pers.like.framework.main.ui.component.BaseFragment;
import pers.like.framework.main.util.Cache;
import pers.like.framework.main.util.ViewUtil;
import pers.like.framework.sample.base.di.ApplicationComponent;


/**
 * @author Like
 */
@SuppressWarnings("All")
public class HsqFragment extends BaseFragment {

    @NonNull
    public <T extends ApplicationComponent> T component() {
        return (T) ((HsqApplication) requireActivity().getApplication()).get();
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
    public Cache cache() {
        return component().cache();
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
