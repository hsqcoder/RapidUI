package pers.like.framework.main.ui.component;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import java.io.Serializable;
import java.util.Map;

import pers.like.framework.main.BaseApplication;
import pers.like.framework.main.di.BaseApplicationComponent;
import pers.like.framework.main.util.Toasty;

/**
 * @author Like
 */
@SuppressWarnings("unused")
public abstract class BaseFragment extends Fragment {

    @NonNull
    public BaseApplicationComponent applicationComponent() {
        return ((BaseApplication) requireActivity().getApplication()).getBaseApplicationComponent();
    }


    public void navigate2(String route) {
        navigate2(route, false);
    }

    public void navigate2(String route, boolean finish) {
        navigate2(route, null, 0, finish, null);
    }

    public void navigate2(String route, int requestCode) {
        navigate2(route, null, requestCode, false, null);
    }

    public void navigate2(String route, Map<String, Object> params) {
        navigate2(route, params, 0, false, null);
    }

    public void navigate2(String route, Map<String, Object> params, int requestCode) {
        navigate2(route, params, requestCode, false, null);
    }

    public void navigate2(String route, Map<String, Object> params, boolean finish) {
        navigate2(route, params, 0, finish, null);
    }

    public void navigate2(String route, Map<String, Object> params, int requestCode, boolean finish, ActivityOptionsCompat compat) {
        Postcard postcard = ARouter.getInstance().build(route);
        if (compat == null) {
            postcard.withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(requireActivity(), android.R.anim.fade_in, android.R.anim.fade_out));
        } else {
            postcard.withOptionsCompat(compat);
        }
        if (params != null) {
            for (String key : params.keySet()) {
                Object obj = params.get(key);
                if (obj instanceof Short) {
                    postcard.withShort(key, (Short) obj);
                } else if (obj instanceof Integer) {
                    postcard.withInt(key, (Integer) obj);
                } else if (obj instanceof Long) {
                    postcard.withLong(key, (Long) obj);
                } else if (obj instanceof Character) {
                    postcard.withChar(key, (Character) obj);
                } else if (obj instanceof Double) {
                    postcard.withDouble(key, (Double) obj);
                } else if (obj instanceof Byte) {
                    postcard.withByte(key, (Byte) obj);
                } else if (obj instanceof String) {
                    postcard.withString(key, (String) obj);
                } else if (obj instanceof Boolean) {
                    postcard.withBoolean(key, (Boolean) obj);
                } else if (obj instanceof Serializable) {
                    postcard.withSerializable(key, (Serializable) obj);
                } else if (obj instanceof Parcelable) {
                    postcard.withParcelable(key, (Parcelable) obj);
                } else {
                    postcard.withObject(key, obj);
                }
            }
        }
        postcard.navigation(requireActivity(), requestCode, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {
            }

            @Override
            public void onLost(Postcard postcard) {
            }

            @Override
            public void onArrival(Postcard postcard) {
                if (finish) {
                    requireActivity().finish();
                }
            }

            @Override
            public void onInterrupt(Postcard postcard) {
            }
        });
    }


    protected void info(String message) {
        Toasty.info(requireContext(), message).show();
    }

    protected void error(String message) {
        Toasty.error(requireContext(), message).show();
    }

    protected void warning(String message) {
        Toasty.warning(requireContext(), message).show();
    }

    protected void success(String message) {
        Toasty.success(requireContext(), message).show();
    }
}
