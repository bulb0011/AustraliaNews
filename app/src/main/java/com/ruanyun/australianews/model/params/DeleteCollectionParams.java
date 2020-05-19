package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/16
 */
public class DeleteCollectionParams {
    private int type;//是否清空 1清空  2不清空
    private String userOid;//当前登录人
    private String collectionInfoOids;//收藏记录Oid      多个oid，英文逗号隔开
    private int model;//模块，1.新闻广告模块收藏记录，2.生活服务收藏记录 type=1 必传

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

    public String getCollectionInfoOids() {
        return collectionInfoOids;
    }

    public void setCollectionInfoOids(String collectionInfoOids) {
        this.collectionInfoOids = collectionInfoOids;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
