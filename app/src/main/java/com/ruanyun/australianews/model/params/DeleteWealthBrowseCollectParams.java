package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/16
 */
public class DeleteWealthBrowseCollectParams {
    private int type;//是否清空 1清空  2不清空
    private String userOid;//当前登录人
    private String historyInfoOids;//浏览记录Oid，删除多个浏览记录，Oid以英文逗号分隔
    private String favoriteInfoOids;//收藏记录Oid  可以是一个或多个，多个以逗号分隔

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

    public String getHistoryInfoOids() {
        return historyInfoOids;
    }

    public void setHistoryInfoOids(String historyInfoOids) {
        this.historyInfoOids = historyInfoOids;
    }

    public String getFavoriteInfoOids() {
        return favoriteInfoOids;
    }

    public void setFavoriteInfoOids(String favoriteInfoOids) {
        this.favoriteInfoOids = favoriteInfoOids;
    }
}
