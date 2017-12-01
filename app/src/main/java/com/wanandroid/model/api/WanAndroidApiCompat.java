package com.wanandroid.model.api;

import com.wanandroid.model.CidData;
import com.wanandroid.model.HotSearchData;
import com.wanandroid.model.SearchData;
import com.wanandroid.model.entity.Article;
import com.wanandroid.model.entity.Cid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 由于部分接口WanAndroid没有提供，所以采取解析网页的方式来提供兼容功能
 * TODO：当WanAndroid提供对应接口以后进行接口替换
 */
public class WanAndroidApiCompat {

    /**
     * 搜索文章接口
     * <p>
     * 接口地址:www.wanandroid.com/article/query
     * 请求参数:k=?，多个关键词提交时使用+号连接
     * 请求方式:GET
     * </p>
     * 注意:返回的文章数据部分关键数据有效，其他数据很多为null
     */
    public static Observable<SearchData> searchArticle(final String key) {
        return Observable.fromCallable(new Callable<SearchData>() {
            @Override
            public SearchData call() throws Exception {
                Element body = Jsoup.connect("http://www.wanandroid.com/article/query")
                        .data("k", key.replaceAll(" +", "+"))
                        .get()
                        .body();
                Elements select = body.select("ul.list_article.listArticle > li > div");
                SearchData searchData = new SearchData();
                List<Article> articles = new ArrayList<>();
                for (int i = 0; i < select.size(); i++) {
                    Element element = select.get(i);
                    Elements element1 = element.select("span.collect");
                    int artid = Integer.parseInt(element1.attr("artid"));
                    Elements element2 = element.select("a.alink");
                    String href = element2.attr("href");
                    String text = element2.text();
                    String s = element.select("span.aauthor").text().split("[:|：]")[1];
                    String s2 = element.select("span.aniceDate").text().split("[:|：]")[1];

                    Article article = new Article();
                    article.setId(artid);
                    article.setLink(href);
                    article.setTitle(text);
                    article.setAuthor(s);
                    article.setNiceDate(s2);
                    articles.add(article);
                }
                searchData.setArticles(articles);
                searchData.setErrorCode(0);
                return searchData;
            }
        }).onErrorReturn(new Function<Throwable, SearchData>() {
            @Override
            public SearchData apply(@NonNull Throwable throwable) throws Exception {
                SearchData searchData = new SearchData();
                searchData.setErrorCode(-1);
                searchData.setErrorMsg(throwable == null ? "unKnow Error" : throwable.getMessage());
                return searchData;
            }
        });
    }


    /**
     * 获取"大家都在搜"
     * <p/>
     * 接口地址:www.wanandroid.com/
     * 请求方式:GET
     */
    public static Observable<HotSearchData> getHotSearch() {
        return Observable.fromCallable(new Callable<HotSearchData>() {
            @Override
            public HotSearchData call() throws Exception {
                Element body = Jsoup.connect("http://www.wanandroid.com/")
                        .get()
                        .body();
                Elements select = body.select("ul.link_list.search_list > li > a");

                List<String> hotKeys = new ArrayList<>();
                for (int i = 0; i < select.size(); i++) {
                    Element element = select.get(i);
                    hotKeys.add(element.text());
                }
                HotSearchData hotSearchData = new HotSearchData();
                hotSearchData.setErrorCode(0);
                hotSearchData.setHotKeys(hotKeys);
                return hotSearchData;
            }
        }).onErrorReturn(new Function<Throwable, HotSearchData>() {
            @Override
            public HotSearchData apply(@NonNull Throwable throwable) throws Exception {
                HotSearchData hotSearchData = new HotSearchData();
                hotSearchData.setErrorCode(-1);
                hotSearchData.setErrorMsg(throwable == null ? "unKnow Error" : throwable.getMessage());
                return hotSearchData;
            }
        });
    }


    /**
     * 获取一级分类数据
     */
    public static Observable<CidData> getOneCidData() {
        return Observable.fromCallable(new Callable<CidData>() {
            @Override
            public CidData call() throws Exception {
                Element body = Jsoup.connect("http://www.wanandroid.com/tree")
                        .get()
                        .body();
                Elements select = body.select("div.sort_info:not(.last) > div.list_sort > ul > li > a");
                List<Cid> cids = new ArrayList<>();
                for (int i = 0; i < select.size(); i++) {
                    Element element = select.get(i);
                    String href = element.attr("href");
                    String text = element.text();
                    Cid cid = new Cid();
                    //eg:href=/article/list/0?cid=176
                    cid.setCidId(Integer.parseInt(href.split("cid=")[1]));
                    cid.setCidTitle(text);
                    cids.add(cid);
                }
                CidData cidData = new CidData();
                cidData.setErrorCode(0);
                cidData.setCids(cids);
                return cidData;
            }
        }).onErrorReturn(new Function<Throwable, CidData>() {
            @Override
            public CidData apply(@NonNull Throwable throwable) throws Exception {
                CidData cidData = new CidData();
                cidData.setErrorCode(-1);
                cidData.setErrorMsg(throwable == null ? "unKnow Error" : throwable.getMessage());
                return cidData;
            }
        });
    }

    /**
     * 获取二级分类数据
     */
    public static Observable<CidData> getTwoCidData(final int oneCidId) {
        return Observable.fromCallable(new Callable<CidData>() {
            @Override
            public CidData call() throws Exception {
                Element body = Jsoup.connect("http://www.wanandroid.com/article/list/0")
                        .data("cid", String.valueOf(oneCidId))
                        .get()
                        .body();
                Elements select = body.select("div.sort_info.last > div.list_sort > ul > li > a");
                List<Cid> cids = new ArrayList<>();
                for (int i = 0; i < select.size(); i++) {
                    Element element = select.get(i);
                    String href = element.attr("href");
                    String text = element.text();
                    Cid cid = new Cid();
                    //eg:href=/article/list/0?cid=176
                    cid.setCidId(Integer.parseInt(href.split("cid=")[1]));
                    cid.setCidTitle(text);
                    cids.add(cid);
                }
                CidData cidData = new CidData();
                cidData.setErrorCode(0);
                cidData.setCids(cids);
                return cidData;
            }
        }).onErrorReturn(new Function<Throwable, CidData>() {
            @Override
            public CidData apply(@NonNull Throwable throwable) throws Exception {
                CidData cidData = new CidData();
                cidData.setErrorCode(-1);
                cidData.setErrorMsg(throwable == null ? "unKnow Error" : throwable.getMessage());
                return cidData;
            }
        });
    }
}
