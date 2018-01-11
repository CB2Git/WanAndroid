package com.wanandroid.business.welcome;

import android.content.Context;
import android.util.Log;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.AuthData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.db.UserManger;
import com.wanandroid.model.entity.WanAndroidUser;
import com.wanandroid.utils.SharedPreferencesUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * app启动闪屏界面
 */
public class WelcomePresenter extends BasePresenterImpl<WelcomeContract.View> implements WelcomeContract.Presenter {

    private static final String TAG = "WelcomePresenter";

    @Override
    public void autoLogin() {
        WanAndroidUser userInfo = UserManger.getUserInfo();
        if (userInfo != null) {
            Disposable disposable = WanAndroidRetrofitClient
                    .getApiService()
                    .login(userInfo.getUsername(), userInfo.getPassword())
                    .map(new Function<AuthData, WanAndroidUser>() {
                        @Override
                        public WanAndroidUser apply(AuthData authData) throws Exception {
                            return authData.getData();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WanAndroidUser>() {
                        @Override
                        public void accept(WanAndroidUser wanAndroidUser) throws Exception {
                            Log.i(TAG, "accept: auto login success");
                            getView().autoLoginSuccess();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.w(TAG, "accept: auto autoLogin error");
                            getView().autoLoginFail();
                        }
                    });
            addDisposable(disposable);
        } else {
            Log.w(TAG, "autoLogin: local userinfo is empty");
        }
    }

    @Override
    public void modifyLocalLoginStatue(Context context, boolean isLogin) {
        SharedPreferencesUtil.put(context, "isLogin", isLogin);
    }

    @Override
    public void loadEveryDayNiceWord() {
        // OkHttpClientManger.getOkHttpClient().
    }
}
