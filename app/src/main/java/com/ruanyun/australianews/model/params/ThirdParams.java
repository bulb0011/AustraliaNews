package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 *
 * @description
 * @date 2019/5/10
 */
public class ThirdParams {
    private String phoneAreaCode;//区号 （手机注册时传）
    private String linkTel;//电话（或邮箱）
    private String loginPass;//密码

    private String avatar;//头像
    private String nickName;//昵称
    private String thirdNumber; //第三方账户
    private String thirdType;//类型 1.微信 2.QQ 3.苹果

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public String getPhoneAreaCode() {
        return phoneAreaCode;
    }

    public void setPhoneAreaCode(String phoneAreaCode) {
        this.phoneAreaCode = phoneAreaCode;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getThirdNumber() {
        return thirdNumber;
    }

    public void setThirdNumber(String thirdNumber) {
        this.thirdNumber = thirdNumber;
    }

    public String getThirdType() {
        return thirdType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }
}
