package com.wanandroid.business.welcome;

import android.content.Context;

/**
 * Created by ${jay} on ${2016/8/17
 */
public interface WelcomeContract {

    interface View {
        void autoLoginFail();

        void autoLoginSuccess();
    }

    interface Presenter {

        /**
         * 根据本地的用户信息自动登录下
         */
        void autoLogin();

        /**
         * 更新本地用户登录状态
         */
        void modifyLocalLoginStatue(Context context, boolean isLogin);

        /**
         * 加载每日一句
         */
        void loadEveryDayNiceWord();
    }
}
