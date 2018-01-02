package com.wanandroid.model.entity;

import java.util.List;

/**
 * "知识体系"
 */
public class Tree {
    /**
     * id : 150
     * name : 开发环境
     * courseId : 13
     * parentChapterId : 0
     * order : 1
     * visible : 1
     * children : [{"id":60,"name":"Android Studio相关","courseId":13,"parentChapterId":150,"order":1000,"visible":1,"children":[]},{"id":169,"name":"gradle","courseId":13,"parentChapterId":150,"order":1001,"visible":1,"children":[]}]
     */

    private int id;
    private String name;
    private int courseId;
    private int parentChapterId;
    private int order;
    private int visible;
    private List<Tree> children;

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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

}
