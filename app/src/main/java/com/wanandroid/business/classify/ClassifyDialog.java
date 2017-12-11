package com.wanandroid.business.classify;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wanandroid.R;
import com.wanandroid.business.adapter.CidListAdapter;
import com.wanandroid.business.base.BaseMVPDialog;
import com.wanandroid.model.entity.Cid;
import com.wanandroid.utils.ScreenUtil;

import java.util.List;

public class ClassifyDialog extends BaseMVPDialog<ClassifyContract.View, ClassifyPresenter> implements ClassifyContract.View {

    private View mAnchorView;

    //一级分类
    private ListView mCidOne;

    //适配器
    private CidListAdapter mCidOneAdapter;

    //二级分类
    private ListView mCidTwo;

    //适配器
    private CidListAdapter mCidTwoAdapter;

    //是否第一次加载，如果是第一次，则需要将第二级也加载一次
    private boolean isFirstLoadCid = true;

    public ClassifyDialog(@NonNull Context context, View anchorView) {
        super(context, R.style.ClassifyDialogStyle);
        mAnchorView = anchorView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_classify);
        initDialog();
    }

    @Override
    public ClassifyPresenter bindPresenter() {
        return new ClassifyPresenter();
    }

    @Override
    public ClassifyContract.View bindView() {
        return this;
    }


    /**
     * 将Dialog的位置定位到anchorView的正下方，并初始化控件
     */
    private void initDialog() {
        //初始化窗体属性
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ScreenUtil.getScrWidth(getContext());
        attributes.y = (int) (mAnchorView.getY() + mAnchorView.getHeight());
        window.setGravity(Gravity.TOP);
        window.setAttributes(attributes);

        mCidOne = (ListView) findViewById(R.id.classify_one_cid);
        mCidTwo = (ListView) findViewById(R.id.classify_two_cid);

        mCidOneAdapter = new CidListAdapter(getContext());
        mCidTwoAdapter = new CidListAdapter(getContext());

        mCidOne.setAdapter(mCidOneAdapter);
        mCidTwo.setAdapter(mCidTwoAdapter);

        mCidOne.setOnItemClickListener(getOnItemClickListener());
        mCidTwo.setOnItemClickListener(getOnItemClickListener());
    }

    @NonNull
    private AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.classify_one_cid) {
                    mCidOneAdapter.setCurrSelect(position);
                    Cid oneID = (Cid) mCidOneAdapter.getItem(position);
                    getBindPresenter().loadTwoCid(oneID.getCidId());
                }
                if (parent.getId() == R.id.classify_two_cid) {
                    mCidTwoAdapter.setCurrSelect(position);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBindPresenter().loadOneCid();
    }

    @Override
    public void displayOneCid(List<Cid> oneCids) {
        mCidOneAdapter.setCids(oneCids);
        if (isFirstLoadCid) {
            mCidOneAdapter.setCurrSelect(0);
            Cid oneID = (Cid) mCidOneAdapter.getItem(0);
            getBindPresenter().loadTwoCid(oneID.getCidId());
        }
    }

    @Override
    public void displayOneLoading() {

    }

    @Override
    public void displayOneLoadError() {

    }

    @Override
    public void displayTwoCid(List<Cid> twoCids) {
        mCidTwoAdapter.setCids(twoCids);
    }

    @Override
    public void displayTwoLoading() {

    }

    @Override
    public void displayTwoLoadError() {

    }
}
