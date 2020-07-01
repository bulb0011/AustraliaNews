package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2019/5/15
 */
public class CollectionBrowseNewsLifeListParams extends PageParamsBase {
    private int model;//模块，1.新闻广告模块收藏记录，2.生活服务收藏记录
    private String userOid;

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }
}
