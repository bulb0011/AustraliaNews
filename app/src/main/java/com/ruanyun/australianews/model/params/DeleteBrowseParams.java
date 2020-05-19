package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/16
 */
public class DeleteBrowseParams {
    private int type;//是否清空 1清空  2不清空
    private String userOid;//当前登录人
    private String browseInfoOids;//浏览记录Oid，删除多个浏览记录，Oid以英文逗号分隔
    private int model;//模块，1.新闻广告模块收藏记录，2.生活服务收藏记录

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getBrowseInfoOids() {
        return browseInfoOids;
    }

    public void setBrowseInfoOids(String browseInfoOids) {
        this.browseInfoOids = browseInfoOids;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
