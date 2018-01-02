package com.wanandroid.business.classify;

import com.wanandroid.model.entity.Tree;

import java.util.List;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class ClassifyContract {

    interface View {

        void displayTreeData(List<Tree> treeData);
    }

    interface Presenter {

        /**
         * 获取"知识体系"分类
         */
        void loadTree();
    }
}
