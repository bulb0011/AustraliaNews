package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/10
 */
public class LoginParams {
    private String phoneAreaCode;//区号 （手机登录时传）
    private String loginName;//登录账号（手机号或邮箱）
    private String loginPass;//登录密码【MD5后】

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

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }
}
