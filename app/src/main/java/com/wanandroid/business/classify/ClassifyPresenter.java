package com.wanandroid.business.classify;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.CidData;
import com.wanandroid.model.api.WanAndroidApiCompat;
import com.wanandroid.model.entity.Cid;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class ClassifyPresenter extends BasePresenterImpl<ClassifyContract.View> implements ClassifyContract.Presenter {

    @Override
    public void loadOneCid() {
        Disposable subscribe = WanAndroidApiCompat.getOneCidData()
                .map(new Function<CidData, List<Cid>>() {
                    @Override
                    public List<Cid> apply(@NonNull CidData cidData) throws Exception {
                        return cidData.getCids();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().displayOneLoading();
                    }
                })
                .subscribe(new Consumer<List<Cid>>() {
                    @Override
                    public void accept(List<Cid> cids) throws Exception {
                        getView().displayOneCid(cids);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayOneLoadError();

                    }
                });
        addDisposable(subscribe);
    }

    @Override
    public void loadTwoCid(int oneCidId) {
        Disposable subscribe = WanAndroidApiCompat.getTwoCidData(oneCidId)
                .map(new Function<CidData, List<Cid>>() {
                    @Override
                    public List<Cid> apply(@NonNull CidData cidData) throws Exception {
                        return cidData.getCids();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().displayTwoLoading();
                    }
                })
                .subscribe(new Consumer<List<Cid>>() {
                    @Override
                    public void accept(List<Cid> cids) throws Exception {
                        getView().displayTwoCid(cids);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayTwoLoadError();

                    }
                });
        addDisposable(subscribe);
    }
}
