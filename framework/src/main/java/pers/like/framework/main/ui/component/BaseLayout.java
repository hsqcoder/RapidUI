package pers.like.framework.main.ui.component;

import android.view.View;

public interface BaseLayout<T> {

    BaseLayout<T> bind(View view);

    void loading(T data);

    void data(T data);

    default void replace(T data) {
    }

    void error(int code, String message, View.OnClickListener retry);

    void empty();

    boolean isEmpty();

}
