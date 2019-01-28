package pers.like.framework.main.ui.component;

import android.content.Context;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.Map;

import pers.like.framework.main.BaseApplication;
import pers.like.framework.main.di.BaseApplicationComponent;
import pers.like.framework.main.util.Toasty;

/**
 * @author Like
 */
@SuppressWarnings("unused")
public class BaseActivity extends AppCompatActivity {

    @NonNull
    public BaseApplicationComponent applicationComponent() {
        return ((BaseApplication) getApplication()).getBaseApplicationComponent();
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
            postcard.withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out));
        } else {
            postcard.withOptionsCompat(compat);
        }
        if (params != null) {
            for (String key : params.keySet()) {
                Object obj = params.get(key);
                if (obj instanceof Integer) {
                    postcard.withInt(key, (Integer) obj);
                } else if (obj instanceof String) {
                    postcard.withString(key, (String) obj);
                } else if (obj instanceof Boolean) {
                    postcard.withBoolean(key, (Boolean) obj);
                } else {
                    postcard.withObject(key, obj);
                }
            }
        }
        postcard.navigation(this, requestCode, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {
            }

            @Override
            public void onLost(Postcard postcard) {
            }

            @Override
            public void onArrival(Postcard postcard) {
                if (finish) {
                    finish();
                }
            }

            @Override
            public void onInterrupt(Postcard postcard) {
            }
        });
    }

    protected void info(String message) {
        Toasty.info(this, message).show();
    }

    protected void error(String message) {
        Toasty.error(this, message).show();
    }

    protected void warning(String message) {
        Toasty.warning(this, message).show();
    }

    protected void success(String message) {
        Toasty.success(this, message).show();
    }

    protected void bindToolbarWithBack(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            this.hideIME();
            this.finish();
        });
    }

    protected void bindToolbarWithOutBack(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public void hideIME() {
        if (this.getCurrentFocus() != null) {
            this.hideKeyboard(this.getCurrentFocus().getApplicationWindowToken());
        }

    }

    protected void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

            assert im != null;

            im.hideSoftInputFromWindow(token, 2);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
