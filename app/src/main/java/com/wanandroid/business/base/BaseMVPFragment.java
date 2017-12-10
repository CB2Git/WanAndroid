package com.wanandroid.business.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by ${jay} on ${2016/8/17
 */

public abstract class BaseMVPFragment<V, P extends BasePresenterImpl<V>> extends Fragment {

    private P mPresenter;

    private V mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = bindPresenter();
        mView = bindView();
        if (mPresenter != null) {
            mPresenter.attachView(mView);
        }
    }

    @Override
    public void onDetach() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDetach();
    }

    public abstract P bindPresenter();

    public abstract V bindView();

    protected P getBindPresenter() {
        return mPresenter;
    }

    protected V getBindView() {
        return mView;
    }
}
