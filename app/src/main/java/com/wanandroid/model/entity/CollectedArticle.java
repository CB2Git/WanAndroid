package com.wanandroid.model.entity;

/**
 * 收藏的文章信息
 * <p>
 * 由于收藏的文章信息大部分与Article相同<br/>
 * 并且存在一个BaseArticlesFragment，为了重用，所以这里直接继承Article
 * <p/>
 */
public class CollectedArticle extends Article {

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

    private int originId;
    private int userId;

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
