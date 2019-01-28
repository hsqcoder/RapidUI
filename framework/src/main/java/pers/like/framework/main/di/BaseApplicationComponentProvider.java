package pers.like.framework.main.di;

public interface BaseApplicationComponentProvider<T extends BaseApplicationComponent> {

    T get();

}
