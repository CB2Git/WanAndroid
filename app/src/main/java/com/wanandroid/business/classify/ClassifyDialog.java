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
import com.wanandroid.business.callback.OnClassifyClickListener;
import com.wanandroid.model.entity.Tree;
import com.wanandroid.utils.ScreenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * 显示"知识体系"的分类对话框
 */

/*
mOneCidIndicator.setVisibility(View.INVISIBLE);
        mOneCidIndicator.hide();
        mCidOne.setVisibility(View.VISIBLE);*/
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

    //"知识体系"数据
    private List<Tree> mTreeData;

    //是否第一次加载，如果是第一次，则需要将第二级也加载一次
    private boolean isFirstLoadCid = true;

    //外界回调
    private OnClassifyClickListener mOnClassifyClickListener;

    private AVLoadingIndicatorView mOneCidIndicator;

    private AVLoadingIndicatorView mTwoCidIndicator;

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

        mOneCidIndicator = (AVLoadingIndicatorView) findViewById(R.id.classify_one_cid_avi_indicator);
        mTwoCidIndicator = (AVLoadingIndicatorView) findViewById(R.id.classify_two_cid_avi_indicator);

        mCidOne = (ListView) findViewById(R.id.classify_one_cid);
        mCidTwo = (ListView) findViewById(R.id.classify_two_cid);

        mCidOneAdapter = new CidListAdapter(getContext());
        mCidTwoAdapter = new CidListAdapter(getContext());

        mCidOne.setAdapter(mCidOneAdapter);
        mCidTwo.setAdapter(mCidTwoAdapter);

        mCidOne.setOnItemClickListener(getOnItemClickListener());
        mCidTwo.setOnItemClickListener(getOnItemClickListener());
    }

    private void displayRootTree(List<Tree> root) {
        mCidOneAdapter.setCids(root);
    }

    private void displayChildrenTree(List<Tree> childRoot) {
        mCidTwoAdapter.setCids(childRoot);
    }

    @NonNull
    private AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tree cidItem = (Tree) parent.getAdapter().getItem(position);
                if (parent.getId() == R.id.classify_one_cid) {
                    mCidOneAdapter.setCurrSelect(position);
                    displayChildrenTree(cidItem.getChildren());
                }
                if (parent.getId() == R.id.classify_two_cid) {
                    mCidTwoAdapter.setCurrSelect(position);
                    if (mOnClassifyClickListener != null) {
                        mOnClassifyClickListener.onClassifyClickListener(cidItem);
                    }
                    dismiss();
                }
            }
        };
    }

    public void setOnClassifyClickListener(OnClassifyClickListener listener) {
        mOnClassifyClickListener = listener;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //如果没有加载过，则加载"知识体系"
        if (mTreeData == null) {
            getBindPresenter().loadTree();
        }
    }

    @Override
    public void displayTreeData(List<Tree> treeData) {
        mOneCidIndicator.hide();
        mTwoCidIndicator.hide();
        mTreeData = treeData;
        mCidOneAdapter.setCurrSelect(0);
        mCidTwoAdapter.setCurrSelect(0);
        displayRootTree(mTreeData);
        displayChildrenTree(mTreeData.get(0).getChildren());
    }
}
