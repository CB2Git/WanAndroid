package com.wanandroid.model;

import com.wanandroid.model.entity.HotKey;

import java.util.List;

/**
 * 热搜数据
 */
public class HotKeyData {

    /**
     * errorCode : 0
     * errorMsg : null
     * data : [{"id":6,"name":"面试","link":null,"visible":1,"order":1},{"id":9,"name":"Studio3","link":null,"visible":1,"order":1},{"id":5,"name":"动画","link":null,"visible":1,"order":2},{"id":1,"name":"自定义View","link":null,"visible":1,"order":3},{"id":2,"name":"性能优化 速度","link":null,"visible":1,"order":4},{"id":3,"name":"gradle","link":null,"visible":1,"order":5},{"id":4,"name":"Camera 相机","link":null,"visible":1,"order":6},{"id":7,"name":"代码混淆 安全","link":null,"visible":1,"order":7},{"id":8,"name":"逆向 加固","link":null,"visible":1,"order":8}]
     */

    private int errorCode;
    private String errorMsg;
    private List<HotKey> data;

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

    public List<HotKey> getData() {
        return data;
    }

    public void setData(List<HotKey> data) {
        this.data = data;
    }
}
