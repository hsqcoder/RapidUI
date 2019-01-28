package pers.like.framework.main.ui.component;

import android.support.annotation.NonNull;

import java.util.Collection;

public interface DataSource<T> {

    void replace(@NonNull Collection<? extends T> data);

    void add(@NonNull Collection<? extends T> data);

    void clear();

    int size();

}
