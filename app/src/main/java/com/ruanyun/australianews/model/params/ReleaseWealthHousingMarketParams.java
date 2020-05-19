package com.ruanyun.australianews.model.params;

import android.text.TextUtils;

import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布活动
 * @date 2020/3/26
 */
public class ReleaseWealthHousingMarketParams {
    private String oid;//修改
    private String title;//标题
    private String userId;//当前登录人
    private String image;//图片
    private String type;//类型   HOUSING_MARKET
    private String acreage;//面积
    private String introduction;//介绍
    private String agentName;//中介名字
    private String company;//公司
    private String contactDetails;//联系方式
    private String mailbox;//邮箱
    private String longitude;//经度
    private String latitude;//纬度
    private String address;//详细地址

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(title)){
            CommonUtil.showToast("请填写标题");
            return false;
        }
        if(TextUtils.isEmpty(type)){
            CommonUtil.showToast("请选择类型");
            return false;
        }
        if(TextUtils.isEmpty(acreage)){
            CommonUtil.showToast("请填写面积");
            return false;
        }
        if(TextUtils.isEmpty(contactDetails)){
            CommonUtil.showToast("请填写联系方式");
            return false;
        }
        if(TextUtils.isEmpty(address)){
            CommonUtil.showToast("请选择地址");
            return false;
        }
        return true;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
