package com.wanandroid.model;

import com.wanandroid.model.entity.WanAndroidUser;

/**
 * WanAndroid注册/登录返回信息
 * <p>
 * 登录注册均使用的此数据结构
 */
public class AuthData {


    /**
     * errorCode : 0
     * errorMsg : null
     * data : {"id":880,"username":"test~11111","password":"1234567890","icon":null,"type":0,"collectIds":[]}
     */
    private int errorCode;
    private String errorMsg;
    private WanAndroidUser data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public WanAndroidUser getData() {
        return data;
    }

    public void setData(WanAndroidUser data) {
        this.data = data;
    }


}
