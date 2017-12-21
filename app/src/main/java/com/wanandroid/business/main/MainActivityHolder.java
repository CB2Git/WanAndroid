package com.wanandroid.business.main;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanandroid.R;
import com.wanandroid.model.entity.WanAndroidUser;

/**
 * 由于MainActivity包裹的View过多，所有使用一个Holder进行统一管理
 */
public class MainActivityHolder {

    private static final String TAG = "MainActivityHolder";

    private MainActivity mMainActivity;

    private Toolbar mToolbar;

    private EditText mSearchEdit;

    //侧滑控件
    private DrawerLayout mDrawerLayout;

    //侧滑菜单
    private NavigationView mNavigationView;

    //侧滑菜单顶部布局
    private View mNavigationViewHeader;

    private ImageView mUserImage;

    private TextView mUserName;

    public MainActivityHolder(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        //对视图进行初始化
        mDrawerLayout = mMainActivity.findViewById(R.id.main_drawer);
        mSearchEdit = mMainActivity.findViewById(R.id.main_edit_search);
        mToolbar = mMainActivity.findViewById(R.id.main_toolbar);
        mNavigationView = mMainActivity.findViewById(R.id.main_left_menu);
        mNavigationViewHeader = mNavigationView.getHeaderView(0);
        mUserImage = mNavigationViewHeader.findViewById(R.id.menu_user_icon);
        mUserName = mNavigationViewHeader.findViewById(R.id.menu_user_name);

        //对视图进行设置
        mMainActivity.setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    public void initUserInfo(WanAndroidUser user) {
        if (user == null) {
            Log.w(TAG, "initUserInfo: user info is null");
            return;
        }
        //TODO 加载真实地址
        mUserImage.setImageResource(R.mipmap.ic_launcher);
        mUserName.setText(user.getUsername());
    }

    public void clearUserInfo() {
        mUserImage.setImageResource(R.mipmap.ic_launcher);
        mUserName.setText(R.string.unlogin);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.START);
    }


    public Toolbar getToolbar() {
        return mToolbar;
    }

    public EditText getSearchEdit() {
        return mSearchEdit;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    public View getNavigationViewHeader() {
        return mNavigationViewHeader;
    }

    public ImageView getUserImage() {
        return mUserImage;
    }

    public TextView getUserName() {
        return mUserName;
    }
}
