package com.wanandroid.business.classify;

import android.util.Log;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.TreeData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.entity.Tree;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class ClassifyPresenter extends BasePresenterImpl<ClassifyContract.View> implements ClassifyContract.Presenter {

    private static final String TAG = "ClassifyPresenter";
    
    @Override
    public void loadTree() {
        Disposable disposable = WanAndroidRetrofitClient.getApiService()
                .getTreeData()
                .map(new Function<TreeData, List<Tree>>() {
                    @Override
                    public List<Tree> apply(TreeData treeData) throws Exception {
                        return treeData.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Tree>>() {
                    @Override
                    public void accept(List<Tree> trees) throws Exception {
                        getView().displayTreeData(trees);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + throwable);
                    }
                });
        addDisposable(disposable);
    }

}
