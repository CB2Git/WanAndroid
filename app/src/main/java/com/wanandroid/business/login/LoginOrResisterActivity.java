package com.wanandroid.business.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wanandroid.R;
import com.wanandroid.business.base.BaseMVPActivity;
import com.wanandroid.model.entity.WanAndroidUser;
import com.wanandroid.widget.CusProgressDialog;

/**
 * 负责用户登录注册的Activity
 */
public class LoginOrResisterActivity extends BaseMVPActivity<LoginOrRegisterContract.View, LoginOrRegisterPresenter> implements LoginOrRegisterContract.View {

    private static final String AUTO_FINISH = "AUTO_FINISH";

    public static final int REQUEST_CODE = 10086;

    public static final int RESULT_CODE = 10010;

    public static final String LOGIN_OR_REGISTER = "login_or_register_result";

    private android.support.v7.widget.Toolbar mToolbar;

    private TextInputLayout mUserName;

    private TextInputLayout mPwd;

    private TextInputLayout mRePwd;

    private Button mLoginOrRegisterBtn;

    private TextView mLoginOrRegisterTv;

    private AlertDialog mProgressDialog = null;

    private boolean mIsInRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_resister);
        initView();
    }


    public static Intent newInstance(Context context) {
        return newInstance(context, false);
    }

    /**
     * 获取登录界面实例
     *
     * @param context
     * @param loginSuccessAuthFinish 是否在登录或注册成功以后自动结束自己
     *                               <p>
     *                               对于结果可以使用{@link android.app.Activity#onActivityResult(int, int, Intent)}中获取
     *                               不过你得使用startActivityForResult()启动本界面
     *                               REQUEST_CODE是{@link LoginOrResisterActivity#REQUEST_CODE}
     *                               RESULT_CODE是{@link LoginOrResisterActivity#RESULT_CODE}
     *                               </p>
     */
    public static Intent newInstance(Context context, boolean loginSuccessAuthFinish) {
        Intent intent = new Intent();
        intent.setClass(context, LoginOrResisterActivity.class);
        intent.putExtra(AUTO_FINISH, loginSuccessAuthFinish);
        return intent;
    }


    @Override
    public LoginOrRegisterPresenter bindPresenter() {
        return new LoginOrRegisterPresenter();
    }

    @Override
    public LoginOrRegisterContract.View bindView() {
        return this;
    }

    private void initView() {
        mUserName = findViewById(R.id.login_use_name);
        mPwd = findViewById(R.id.login_user_psw);
        mRePwd = findViewById(R.id.login_user_pwd_confirm);
        mLoginOrRegisterBtn = findViewById(R.id.login_or_register_button);
        mLoginOrRegisterTv = findViewById(R.id.register_tv);

        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mLoginOrRegisterBtn.setOnClickListener(getOnClickListener());
        mLoginOrRegisterTv.setOnClickListener(getOnClickListener());


        WanAndroidUser user = getBindPresenter().loadUserData();
        //自动记住密码~~~
        if (user != null) {
            mUserName.getEditText().setText(user.getUsername());
            mPwd.getEditText().setText(user.getPassword());

            mUserName.getEditText().setSelection(mUserName.getEditText().getText().length());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.login_or_register_button) {
                    loginOrRegister();
                }
                if (v.getId() == R.id.register_tv) {
                    changeViewStatue();
                }
            }
        };
    }

    /**
     * Login or Register
     */
    private void loginOrRegister() {
        if (!checkInputValidity()) {
            return;
        }
        String userName = mUserName.getEditText().getText().toString();
        String pwd = mPwd.getEditText().getText().toString();
        if (mIsInRegister) {
            getBindPresenter().register(userName, pwd);
        } else {
            getBindPresenter().login(userName, pwd);
        }
    }

    /**
     * 检查输入有效性
     */
    private boolean checkInputValidity() {
        String userName = mUserName.getEditText().getText().toString();
        String pwd = mPwd.getEditText().getText().toString();
        String rePwd = mRePwd.getEditText().getText().toString();
        //检查空数据
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.user_name_empty));
            mUserName.requestFocus();
            return false;
        } else {
            mUserName.setError(null);
        }
        if (TextUtils.isEmpty(pwd)) {
            mPwd.setError(getString(R.string.psw_empty));
            mPwd.requestFocus();
            return false;
        } else {
            mPwd.setError(null);
        }
        //检测密码是否一致
        if (mIsInRegister && !pwd.equals(rePwd)) {
            mRePwd.setError(getString(R.string.pwd_unequals));
            mRePwd.requestFocus();
            return false;
        } else {
            mRePwd.setError(null);
        }
        //检查用户名长度
        if (userName.length() < 6) {
            mUserName.setError(getString(R.string.user_name_too_short));
            mUserName.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 切换登录/注册状态
     */
    private void changeViewStatue() {
        mIsInRegister = !mIsInRegister;
        if (!mIsInRegister) {
            mRePwd.getEditText().setText("");
            mRePwd.setError(null);
        }
        mRePwd.setVisibility(mIsInRegister ? View.VISIBLE : View.GONE);
        mLoginOrRegisterBtn.setText(mIsInRegister ? R.string.register : R.string.login);
        mLoginOrRegisterTv.setText(mIsInRegister ? R.string.login_tip : R.string.register_tip);
    }

    @Override
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = CusProgressDialog.show(this);
        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void loginSuccess(WanAndroidUser user) {
        getBindPresenter().saveUserData(user);
        doSomeThingAfterLoginOrRegister();
    }

    @Override
    public void registerSuccess(WanAndroidUser user) {
        getBindPresenter().saveUserData(user);
        doSomeThingAfterLoginOrRegister();
    }

    private void doSomeThingAfterLoginOrRegister() {
        if (getIntent().getBooleanExtra(AUTO_FINISH, false)) {
            setResult(RESULT_CODE, getIntent().putExtra(LOGIN_OR_REGISTER, true));
            finish();
        }
    }


    @Override
    public void loginFail() {
        getBindPresenter().clearUserData();
        Snackbar.make(mLoginOrRegisterTv, R.string.login_fail, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void registerFail() {
        getBindPresenter().clearUserData();
        Snackbar.make(mLoginOrRegisterTv, R.string.register_fail, Snackbar.LENGTH_SHORT).show();
    }
}
