package com.wanandroid.model.entity;

/**
 * Created by ${jay} on ${2016/8/17
 */

public class HotKey {
    /**
     * id : 6
     * name : 面试
     * link : null
     * visible : 1
     * order : 1
     */
    private int id;
    private String name;
    private String link;
    private int visible;
    private int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
