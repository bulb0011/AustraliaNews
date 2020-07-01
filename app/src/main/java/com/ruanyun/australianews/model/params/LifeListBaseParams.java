package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.App;
import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2019/5/20
 */
public class LifeListBaseParams extends PageParamsBase {
    private String loginUserNum;//当前登录人

    public String getLoginUserNum() {
        return App.getInstance().getUserOid();
    }

    public void setLoginUserNum(String loginUserNum) {
        this.loginUserNum = loginUserNum;
    }
}
