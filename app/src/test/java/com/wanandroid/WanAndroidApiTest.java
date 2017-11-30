package com.wanandroid;

import com.wanandroid.model.ArticleData;
import com.wanandroid.model.api.WanAndroidApi;
import com.wanandroid.model.api.WanAndroidRetrofitClient;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * www.wanandroid.com接口测试用例
 */
public class WanAndroidApiTest {

    /**
     * 测试获取首页数据
     */
    @Test
    public void testArticleDate() throws Exception {
        WanAndroidApi wanAndroidApi = WanAndroidRetrofitClient.getApiService();
        Observable<ArticleData> articleData = wanAndroidApi.getArticleData(0);
        articleData
                .subscribe(new Consumer<ArticleData>() {
                    @Override
                    public void accept(ArticleData articleData) throws Exception {
                        assertEquals(articleData.getErrorCode(), 0);
                        assertNull((String) articleData.getErrorMsg(), articleData.getErrorMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });
    }

    @Test
    public void testRegister() throws Exception {

    }
}