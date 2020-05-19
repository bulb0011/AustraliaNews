package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/16
 */
public class DeleteMyReleaseParams {
    private String commonOid;
    private int type;

    public String getCommonOid() {
        return commonOid;
    }

    public void setCommonOid(String commonOid) {
        this.commonOid = commonOid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DeleteMyReleaseParams(String commonOid, int type) {
        this.commonOid = commonOid;
        this.type = type;
    }
}
