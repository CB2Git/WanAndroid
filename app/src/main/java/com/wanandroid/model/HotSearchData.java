package com.wanandroid.model;

import java.util.List;

/**
 * "大家都在搜"的数据
 */
public class HotSearchData extends BaseResponseData {

    private List<String> hotKeys;

    public List<String> getHotKeys() {
        return hotKeys;
    }

    public void setHotKeys(List<String> hotKeys) {
        this.hotKeys = hotKeys;
    }
}
