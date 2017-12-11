package com.wanandroid.business.classify;

import com.wanandroid.model.entity.Cid;

import java.util.List;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class ClassifyContract {

    interface View {

        void displayOneCid(List<Cid> oneCids);

        void displayOneLoading();

        void displayOneLoadError();

        void displayTwoCid(List<Cid> twoCids);

        void displayTwoLoading();

        void displayTwoLoadError();
    }

    interface Presenter {

        void loadOneCid();

        void loadTwoCid(int oneCidId);
    }
}
