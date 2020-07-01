package com.ruanyun.australianews.model.params;

import java.io.File;

/**
 * @author hdl
 * @description
 * @date 2019/3/4
 */
public class UpdateUserInfoParams {
    public File userPhotoFile;

    private String oid;//当前登录人Oid
    private String headImageAttaOid;//头像
    private String name;//昵称
    private String sex;//性别  Y男  X女
    private String birthdate;//出生年月日 例：2019-04-25
    private String email;//邮箱
    private String mobile;//用户手机号
    private String signature;//签名

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getHeadImageAttaOid() {
        return headImageAttaOid;
    }

    public void setHeadImageAttaOid(String headImageAttaOid) {
        this.headImageAttaOid = headImageAttaOid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
