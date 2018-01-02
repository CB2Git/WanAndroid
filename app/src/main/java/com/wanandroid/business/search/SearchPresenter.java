package com.wanandroid.business.search;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.ArticleData;
import com.wanandroid.model.HotKeyData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.entity.Article;
import com.wanandroid.model.entity.HotKey;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class SearchPresenter extends BasePresenterImpl<SearchContract.View> implements SearchContract.Presenter {

    private String mCurrentKey;

    private int mCurrentPage = 0;

    @Override
    public void getHotKeys() {
        Disposable disposable = WanAndroidRetrofitClient
                .getApiService()
                .getHotKey()
                .map(new Function<HotKeyData, List<HotKey>>() {

                    @Override
                    public List<HotKey> apply(HotKeyData hotKeyData) throws Exception {
                        return hotKeyData.getData();
                    }
                })
                .filter(new Predicate<List<HotKey>>() {
                    @Override
                    public boolean test(List<HotKey> hotKeys) throws Exception {
                        return hotKeys != null && hotKeys.size() > 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<HotKey>>() {
                    @Override
                    public void accept(List<HotKey> hotKeys) throws Exception {
                        if (getView() != null) {
                            getView().displayHotKeys(hotKeys);
                        }
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void doSearch(String key) {
        mCurrentKey = key;
        mCurrentPage = 0;
        Disposable disposable = WanAndroidRetrofitClient
                .getApiService()
                .searchArticle(mCurrentPage, mCurrentKey)
                .map(new Function<ArticleData, List<Article>>() {
                    @Override
                    public List<Article> apply(@NonNull ArticleData articleData) throws Exception {
                        return articleData.getData().getDatas();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().displaySearching();
                    }
                })
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> articles) throws Exception {
                        getView().displaySearchResult(articles);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displaySearchError();
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void doSearchNextPage() {
        Disposable disposable = WanAndroidRetrofitClient
                .getApiService()
                .searchArticle(mCurrentPage + 1, mCurrentKey)
                .map(new Function<ArticleData, List<Article>>() {
                    @Override
                    public List<Article> apply(@NonNull ArticleData articleData) throws Exception {
                        return articleData.getData().getDatas();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<List<Article>>() {
                    @Override
                    public boolean test(@NonNull List<Article> articles) throws Exception {
                        if (articles.size() == 0) {
                            getView().displayNoMore();
                        }
                        return articles.size() != 0;
                    }
                })
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> articles) throws Exception {
                        //搜索有结果才+1
                        mCurrentPage++;
                        getView().appendSearchResult(articles);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayLoadMoreError();
                    }
                });
        addDisposable(disposable);
    }
}
