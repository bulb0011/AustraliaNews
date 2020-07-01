package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2019/6/12
 */
public class NewsPushListParams extends PageParamsBase {
    private String userOid;

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }
}
