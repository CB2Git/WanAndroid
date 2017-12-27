package com.wanandroid.business.login;

import android.support.annotation.Nullable;
import android.util.Log;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.AuthData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.db.UserManger;
import com.wanandroid.model.entity.WanAndroidUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class LoginOrRegisterPresenter extends BasePresenterImpl<LoginOrRegisterContract.View> implements LoginOrRegisterContract.Presenter {

    private static final String TAG = "LoginOrRegisterPresente";

    @Override
    public void login(String userName, String pwd) {
        WanAndroidRetrofitClient
                .getApiService()
                .login(userName, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().showProgress();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().hideProgress();
                    }
                })
                .map(new Function<AuthData, WanAndroidUser>() {
                    @Override
                    public WanAndroidUser apply(@NonNull AuthData authData) throws Exception {
                        return authData.getData();
                    }
                })
                .subscribe(new Consumer<WanAndroidUser>() {
                    @Override
                    public void accept(WanAndroidUser authData) throws Exception {
                        getView().loginSuccess(authData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: " + throwable.getMessage());
                        getView().loginFail();
                    }
                });
    }

    @Override
    public void register(String userName, String pwd) {
        WanAndroidRetrofitClient
                .getApiService()
                .register(userName, pwd, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<AuthData, WanAndroidUser>() {
                    @Override
                    public WanAndroidUser apply(@NonNull AuthData authData) throws Exception {
                        return authData.getData();
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().showProgress();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().hideProgress();
                    }
                })
                .subscribe(new Consumer<WanAndroidUser>() {
                    @Override
                    public void accept(WanAndroidUser authData) throws Exception {
                        getView().registerSuccess(authData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: " + throwable.getMessage());
                        getView().registerFail();
                    }
                });
    }

    @Override
    public void saveUserData(WanAndroidUser user) {
        UserManger.updateUserInfo(user);
    }

    @Override
    public void clearUserData() {
        UserManger.clearUserInfo();
    }

    @Override
    @Nullable
    public WanAndroidUser loadUserData() {
        return UserManger.getUserInfo();
    }
}
