package pers.like.framework.sample.base;

import android.text.TextUtils;


import pers.like.framework.main.util.Cache;
import pers.like.framework.main.util.JsonUtils;
import pers.like.framework.main.util.Logger;
import pers.like.framework.sample.model.pojo.User;

/**
 * @author Like
 */
public class HsqUserSystem {

    public static final String TAG = "user-system";

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password_";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_USER = "key_user";
    private static final String KEY_FORM = "key_form";

    private User mUser;
    private Cache mCache;
    private Form mForm;

    public HsqUserSystem(Cache cache) {
        this.mCache = cache;
    }

    public String userName() {
        Form form = form();
        if (form == null) {
            return "";
        }
        return form.username;
    }

    public String password() {
        Form form = form();
        if (form == null) {
            return "";
        }
        return form.password;
    }

    public String token() {
        Form form = form();
        if (form == null) {
            return "";
        }
        Logger.e(TAG, "  get   :" + form.token);
        return form.token;
    }

    public void form(Form form) {
        mForm = form;
        if (form == null) {
            mCache.remove(KEY_FORM);
            Logger.e(TAG, "  clear :form");
        } else {
            mCache.put(KEY_FORM, JsonUtils.toJson(form));
            Logger.e(TAG, "  save  :<" + JsonUtils.toJson(form) + ">");
        }
    }

    public Form form() {
        if (mForm == null) {
            mForm = JsonUtils.fromJson(mCache.string(KEY_FORM), Form.class);
        }
        return mForm;
    }

    public void user(User user) {
        this.mUser = user;
        if (mUser != null) {
            mCache.put(KEY_USER, JsonUtils.toJson(mUser));
        } else {
            mCache.remove(KEY_USER);
        }
    }

    public User user() {
        if (mUser == null) {
            mUser = JsonUtils.fromJson(mCache.string(KEY_USER), User.class);
        }
        return mUser;
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(userName()) && !TextUtils.isEmpty(token());
    }

    public void logOut() {
        form(null);
    }

    public static class Form {
        public String username;
        public String password;
        public String token;

        public Form(String username, String password, String token) {
            this.username = username;
            this.password = password;
            this.token = token;
        }
    }

}
