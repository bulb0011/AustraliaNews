package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2019/5/30
 */
public class LifeSearchListParams extends PageParamsBase {
    private String searchTerm;
    private String type;//2房屋出租  3招聘信息  4汽车买卖  5宠物交易  6交易市场  7房屋求租  8生意转让  9教科书  10美食店铺  100黄页

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
