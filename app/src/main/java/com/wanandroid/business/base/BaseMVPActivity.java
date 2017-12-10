package com.wanandroid.business.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * MVP公共类
 * <p>
 * 负责View以及Presenter的自动绑定与解绑
 * </p>
 */
public abstract class BaseMVPActivity<V, P extends BasePresenterImpl<V>> extends AppCompatActivity {

    private P mPresenter;

    private V mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = bindPresenter();
        mView = bindView();
        if (mPresenter != null) {
            mPresenter.attachView(mView);
        }
    }

    public abstract P bindPresenter();

    public abstract V bindView();

    protected P getBindPresenter() {
        return mPresenter;
    }

    protected V getBindView() {
        return mView;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
