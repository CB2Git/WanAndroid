package com.wanandroid.business.welcome;

/**
 * Created by ${jay} on ${2016/8/17
 */
public interface WelcomeContract {

    interface View {

    }

    interface Presenter {

        /**
         * 根据本地的用户信息自动登录下
         */
        void autoLogin();
    }
}
