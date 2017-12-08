package com.wanandroid.business.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 将Presenter的一些公共操作抽取到这里
 * <p>
 * 负责RxJava的解绑以及View的公共操作
 * </p>
 */
public class BasePresenterImpl<V> implements BasePresenter<V> {

    private static final String TAG = "BasePresenterImpl";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Reference<V> mReference;

    @Override
    public void attachView(V view) {
        mReference = new WeakReference<V>(view);
    }

    @Override
    public void detachView() {
        dispose();
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }

    @Override
    public V getView() {
        return mReference == null ? null : mReference.get();
    }

    @Override
    public boolean isViewAttached() {
        return getView() != null;
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            synchronized (this) {
                if (mCompositeDisposable == null) {
                    mCompositeDisposable = new CompositeDisposable();
                }
            }
        }
        mCompositeDisposable.add(disposable);
    }

    protected void dispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }
}
