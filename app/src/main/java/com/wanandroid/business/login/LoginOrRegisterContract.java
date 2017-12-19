package com.wanandroid.business.login;

import com.wanandroid.model.entity.WanAndroidUser;

/**
 * Created by ${jay} on ${2016/8/17
 */

public class LoginOrRegisterContract {

    interface View {

        void showProgress();

        void hideProgress();

        void loginSuccess(WanAndroidUser user);

        void registerSuccess(WanAndroidUser user);

        void loginFail();

        void registerFail();
    }

    interface Presenter {

        void login(String userName, String pwd);

        void register(String userName, String pwd);

        void saveUserData(WanAndroidUser user);

        void clearUserData();

        WanAndroidUser loadUserData();
    }
}
