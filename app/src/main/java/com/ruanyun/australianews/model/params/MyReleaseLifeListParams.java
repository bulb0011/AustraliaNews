package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2019/5/15
 */
public class MyReleaseLifeListParams extends PageParamsBase {
    private String loginUserNum;

    public String getLoginUserNum() {
        return loginUserNum;
    }

    public void setLoginUserNum(String loginUserNum) {
        this.loginUserNum = loginUserNum;
    }
}
