package com.wanandroid.business.login;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import com.wanandroid.R;

public class LoginOrResisterActivity extends AppCompatActivity {

    private TextInputLayout mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_resister);
        mi = (TextInputLayout) findViewById(R.id.login_user_psw);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
