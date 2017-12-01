package com.wanandroid.model;

import com.wanandroid.model.entity.Cid;

import java.util.List;

/**
 * 分类数据
 */

public class CidData extends BaseResponseData {

    private List<Cid> cids;

    public List<Cid> getCids() {
        return cids;
    }

    public void setCids(List<Cid> cids) {
        this.cids = cids;
    }
}
