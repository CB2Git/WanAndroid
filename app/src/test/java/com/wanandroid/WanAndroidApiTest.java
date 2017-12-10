package com.wanandroid;

import com.wanandroid.model.ArticleData;
import com.wanandroid.model.AuthData;
import com.wanandroid.model.BaseResponseData;
import com.wanandroid.model.CidData;
import com.wanandroid.model.CollectedArticleData;
import com.wanandroid.model.HotSearchData;
import com.wanandroid.model.SearchData;
import com.wanandroid.model.api.WanAndroidApiCompat;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.entity.WanAndroidUser;

import org.junit.Test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.functions.Consumer;

import static com.wanandroid.model.api.WanAndroidRetrofitClient.getApiService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        getApiService()
                .getArticleData(0)
                .subscribe(new Consumer<ArticleData>() {
                    @Override
                    public void accept(ArticleData articleData) throws Exception {
                        assertEquals(articleData.getErrorCode(), 0);
                        assertNull(articleData.getErrorMsg(), articleData.getErrorMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });
    }

    /**
     * 测试注册
     */
    @Test
    public void testRegister() throws Exception {

        Random random = new Random();
        final String randomUser = String.format("test_%d", random.nextInt());
        System.out.print(String.format("testRegister:name=%s,pwd=%s", randomUser, randomUser));
        WanAndroidRetrofitClient
                .getApiService()
                .register(randomUser, randomUser, randomUser)
                .subscribe(new Consumer<AuthData>() {
                    @Override
                    public void accept(AuthData authData) throws Exception {
                        assertEquals(authData.getErrorCode(), 0);
                        assertNull(authData.getErrorMsg(), authData.getErrorMsg());
                        WanAndroidUser data = authData.getData();
                        assertNotNull(data);
                        assertEquals(randomUser, data.getUsername());
                        assertEquals(randomUser, data.getPassword());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });
    }

    /**
     * 测试登录
     */
    @Test
    public void testLogin() throws Exception {
        getApiService()
                .login("test~11111", "1234567890")
                .subscribe(new Consumer<AuthData>() {
                    @Override
                    public void accept(AuthData authData) throws Exception {
                        assertEquals(authData.getErrorCode(), 0);
                        assertNull(authData.getErrorMsg(), authData.getErrorMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });

        getApiService().login("test~11111", "error_pwd")
                .subscribe(new Consumer<AuthData>() {
                    @Override
                    public void accept(AuthData authData) throws Exception {
                        assertEquals(authData.getErrorCode(), -1);
                        assertNotNull(authData.getErrorMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });
    }

    /**
     * 测试获取收藏信息
     */
    @Test
    public void testGetCollectedData() throws Exception {
        WanAndroidRetrofitClient
                .getApiService()
                .getCollectData(0)
                .subscribe(new Consumer<CollectedArticleData>() {
                    @Override
                    public void accept(CollectedArticleData collectedArticleData) throws Exception {
                        assertEquals(0, collectedArticleData.getErrorCode());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });
    }

    /**
     * 测试收藏一篇文章
     */
    @Test
    public void testCollectArticle() throws Exception {
        WanAndroidRetrofitClient
                .getApiService()
                .collectArticle(1541)
                .subscribe(new Consumer<BaseResponseData>() {
                    @Override
                    public void accept(BaseResponseData baseResponseData) throws Exception {
                        assertEquals(baseResponseData.getErrorCode(), 0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });

    }

    /**
     * 测试取消收藏一篇文章
     */
    @Test
    public void testUnCollectArticle() throws Exception {
        WanAndroidRetrofitClient
                .getApiService()
                .unCollectArticle(1541)
                .subscribe(new Consumer<BaseResponseData>() {
                    @Override
                    public void accept(BaseResponseData responseData) throws Exception {
                        System.out.print(responseData.getErrorCode());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });
    }

    /**
     * 测试获取“大家都在搜”
     */
    @Test
    public void testHotFindArticle() throws Exception {
        WanAndroidApiCompat.getHotSearch().subscribe(new Consumer<HotSearchData>() {
            @Override
            public void accept(HotSearchData hotSearchData) throws Exception {
                assertEquals(hotSearchData.getErrorCode(), 0);
            }
        });
    }

    /**
     * 测试获取搜索文章
     */
    @Test
    public void testSearchArticle() throws Exception {
        WanAndroidApiCompat.searchArticle("代码混淆 安全").subscribe(new Consumer<SearchData>() {
            @Override
            public void accept(SearchData articleData) throws Exception {
                assertEquals(articleData.getErrorCode(), 0);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                fail(throwable.getMessage());
            }
        });
    }

    /**
     * 测试获取指定分类的文章数据
     */
    @Test
    public void testGetCidData() throws Exception {
        WanAndroidRetrofitClient
                .getApiService()
                .getCidData(0, 182)
                .subscribe(new Consumer<ArticleData>() {
                    @Override
                    public void accept(ArticleData articleData) throws Exception {
                        assertEquals(articleData.getErrorCode(), 0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        fail(throwable.getMessage());
                    }
                });
    }

    /**
     * 获取一级分类
     */
    @Test
    public void testGetOneCidList() throws Exception {
        WanAndroidApiCompat.getOneCidData().subscribe(new Consumer<CidData>() {
            @Override
            public void accept(CidData cidData) throws Exception {
                assertEquals(cidData.getErrorCode(), 0);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                fail(throwable.getMessage());
            }
        });
    }

    /**
     * 获取二级分类
     */
    @Test
    public void testGetTwoCidList() throws Exception {
        WanAndroidApiCompat.getTwoCidData(10).subscribe(new Consumer<CidData>() {
            @Override
            public void accept(CidData cidData) throws Exception {
                assertEquals(cidData.getErrorCode(), 0);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                fail(throwable.getMessage());
            }
        });
    }

    /**
     * 测试标题网页转码
     */
    @Test
    public void testTitleFormat() throws Exception {
        String target = "Android UI<em class='highlight'>三</em>实战<em class='highlight'>三</em>识别绘制<em class='highlight'>三</em>中的性能问题";
        Pattern pattern = Pattern.compile("<em.+?>(.+?)</em>");
        Matcher matcher = pattern.matcher(target);
        target.replaceAll("<em.+?>","<font color=\"#f0717e\">");
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
        System.out.println(target.replaceAll("<em.+?>","<font color=\"#f0717e\">").replaceAll("</em>","</font>"));
    }
}