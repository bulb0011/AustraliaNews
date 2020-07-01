package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2019/3/13
 */
public class UserOidPageParams extends PageParamsBase {
    private String userOid;
    private String loginUserNum;

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getLoginUserNum() {
        return loginUserNum;
    }

    public void setLoginUserNum(String loginUserNum) {
        this.loginUserNum = loginUserNum;
    }
}
