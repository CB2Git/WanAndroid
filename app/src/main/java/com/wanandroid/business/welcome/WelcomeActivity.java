package com.wanandroid.business.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wanandroid.R;
import com.wanandroid.business.base.BaseMVPActivity;
import com.wanandroid.business.main.MainActivity;

/**
 * Apk启动的欢迎界面
 */
public class WelcomeActivity extends BaseMVPActivity<WelcomeContract.View, WelcomePresenter> implements WelcomeContract.View {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getBindPresenter().autoLogin();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    public WelcomePresenter bindPresenter() {
        return new WelcomePresenter();
    }

    @Override
    public WelcomeContract.View bindView() {
        return this;
    }
}
