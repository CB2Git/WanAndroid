package com.wanandroid.model.entity;

/**
 * 收藏的文章信息
 */
public class CollectedArticle {

    /**
     * id : 783
     * originId : 1494
     * title : 我的 Android 面试故事 | 13家面试记录
     * chapterId : 73
     * chapterName : 面试相关
     * envelopePic : null
     * link : https://mp.weixin.qq.com/s/N_3jUA1C-W1B-X6DQSXVTQ
     * author : 剑西
     * origin : null
     * publishTime : 1512020515000
     * zan : 0
     * desc : null
     * visible : 0
     * niceDate : 18分钟前
     * courseId : 13
     * userId : 883
     */

    private int id;
    private int originId;
    private String title;
    private int chapterId;
    private String chapterName;
    private String envelopePic;
    private String link;
    private String author;
    private Object origin;
    private long publishTime;
    private int zan;
    private Object desc;
    private int visible;
    private String niceDate;
    private int courseId;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public Object getDesc() {
        return desc;
    }

    public void setDesc(Object desc) {
        this.desc = desc;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
