package com.wanandroid.model.entity;

import java.util.List;

/**
 * WanAndroid用户信息
 */
public class WanAndroidUser {

    /**
     * id : 880
     * username : test~11111
     * password : 1234567890
     * icon : null
     * type : 0
     * collectIds : []
     */
    private int id;
    private String username;
    private String password;
    private String icon;
    private int type;
    private List<Integer> collectIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }
}
