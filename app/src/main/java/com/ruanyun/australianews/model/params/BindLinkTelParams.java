package com.ruanyun.australianews.model.params;

/**
 * description:
 * <p/>
 * Created by ycw on 2019/09/02.
 */
public class BindLinkTelParams {
    private String userOid;//待绑定用户userOid
    private String phoneAreaCode;//手机区号
    private String loginName;//手机号或邮箱
    private String validate;//短信验证码
    private String type;//类型 1手机号注册  2邮箱注册

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getPhoneAreaCode() {
        return phoneAreaCode;
    }

    public void setPhoneAreaCode(String phoneAreaCode) {
        this.phoneAreaCode = phoneAreaCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
