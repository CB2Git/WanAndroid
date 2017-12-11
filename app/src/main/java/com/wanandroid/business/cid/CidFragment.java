package com.wanandroid.business.cid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanandroid.R;
import com.wanandroid.business.base.BaseArticlesFragment;

/**
 * 显示"知识体系分类"的Fragment
 */

public class CidFragment extends BaseArticlesFragment<CidContract.View, CidPresenter> implements CidContract.View {

    @Override
    public View bindFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cid, container, false);
        return root;
    }

    @Override
    public CidPresenter bindPresenter() {
        return new CidPresenter();
    }

    @Override
    public CidContract.View bindView() {
        return this;
    }
}
