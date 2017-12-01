package com.wanandroid.model;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class BaseResponseData {

    /**
     * errorCode : 0
     * errorMsg : null
     * data : null
     */
    public int errorCode = -1;
    public String errorMsg;

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
}
