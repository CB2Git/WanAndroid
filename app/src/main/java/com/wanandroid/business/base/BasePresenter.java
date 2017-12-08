package com.wanandroid.business.base;

/**
 * BasePresenter,负责Presenter的公共操作
 * <p>
 * 参考《Android源码设计模式与实战》第二十六章
 * </p>
 */
public interface BasePresenter<V> {

    void attachView(V view);

    void detachView();

    V getView();

    boolean isViewAttached();
}
