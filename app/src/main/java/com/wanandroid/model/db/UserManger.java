package com.wanandroid.model.db;

import android.support.annotation.Nullable;

import com.wanandroid.WanApplication;
import com.wanandroid.model.entity.WanAndroidUser;

import java.util.ArrayList;

/**
 * 用于管理当前用户信息的类
 */
public class UserManger {

    public static int clearUserInfo() {
        return WanApplication.liteOrm.deleteAll(WanAndroidUser.class);
    }

    /**
     * 获取当前登陆的用户信息
     *
     * @return 如果没有登录，返回null
     */
    @Nullable
    public static WanAndroidUser getUserInfo() {
        ArrayList<WanAndroidUser> users = WanApplication.liteOrm.query(WanAndroidUser.class);
        return users == null || users.size() == 0 ? null : users.get(0);
    }

    /**
     * 更新用户信息，如果对应用户不存在，则自动保存
     *
     * @param newUserInfo 新用户信息，如果不存在，则直接保存
     */
    public static void updateUserInfo(WanAndroidUser newUserInfo) {

        WanAndroidUser androidUser = WanApplication.liteOrm.queryById(newUserInfo.getId(), WanAndroidUser.class);
        //如果不存在用户，则直接保存
        if (androidUser == null) {
            WanApplication.liteOrm.deleteAll(WanAndroidUser.class);
            WanApplication.liteOrm.save(newUserInfo);
        } else {
            WanApplication.liteOrm.update(newUserInfo);
        }
    }
}
