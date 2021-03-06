package com.wanandroid.business.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.wanandroid.R;
import com.wanandroid.business.base.BaseMVPActivity;
import com.wanandroid.business.main.MainActivity;

/**
 * Apk启动的欢迎界面
 * TODO:每日一句？？？
 */
public class WelcomeActivity extends BaseMVPActivity<WelcomeContract.View, WelcomePresenter> implements WelcomeContract.View {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getBindPresenter().modifyLocalLoginStatue(this, false);
        getBindPresenter().autoLogin();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

    @Override
    public void autoLoginFail() {
        Toast.makeText(this, R.string.auto_login_error, Toast.LENGTH_SHORT).show();
        getBindPresenter().modifyLocalLoginStatue(this, false);
    }

    @Override
    public void autoLoginSuccess() {
        getBindPresenter().modifyLocalLoginStatue(this, true);
    }
}
