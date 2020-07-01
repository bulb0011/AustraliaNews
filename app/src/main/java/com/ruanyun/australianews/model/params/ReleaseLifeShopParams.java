package com.ruanyun.australianews.model.params;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布餐饮美食
 * @date 2019/11/12
 */
public class ReleaseLifeShopParams {
    private String oid;//修改
    private String userOid;//当前登录人
    private String mainPhoto;//图片
    private String title;//店铺名称
    private String cookStyle;//菜系    COOK_STYLE
    private String workPeriod;//营业周期   WORK_PERIOD
    private String workStartTimes;//营业开始时间
    private String workEndTimes;//营业结束时间
    private String startPrice;//起送价格
    private String perCapitaSpending;//人均消费
    private String deliveryFee;//配送费
    private String deliveryTime;//配送时间   DELIVERY_TIME
    private String deliveryArea;//配送区域
    private String remark;//描述
    private String linkMan;//联系人
    private String linkTel;//手机号
    private String email;//邮箱
    private String qq;//QQ
    private String weixin;//微信
    private String city;//所在区域
    private String longitude;//经度
    private String latitude;//纬度
    private String address;//店铺地址

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(title)){
            CommonUtil.showToast("请填写店铺名称");
            return false;
        }
        if(TextUtils.isEmpty(cookStyle)){
            CommonUtil.showToast("请选择菜系");
            return false;
        }
        if(TextUtils.isEmpty(city)){
            CommonUtil.showToast("请选择Suburb");
            return false;
        }
        if(TextUtils.isEmpty(address)){
            CommonUtil.showToast("请填写店铺地址");
            return false;
        }
        if(TextUtils.isEmpty(workPeriod)){
            CommonUtil.showToast("请选择营业周期");
            return false;
        }
        if(TextUtils.isEmpty(workStartTimes)){
            CommonUtil.showToast("请选择营业开始时间");
            return false;
        }
        if(TextUtils.isEmpty(workEndTimes)){
            CommonUtil.showToast("请选择营业结束时间");
            return false;
        }
        if(TextUtils.isEmpty(startPrice)){
            CommonUtil.showToast("请填写起送价格");
            return false;
        }
        if(TextUtils.isEmpty(perCapitaSpending)){
            CommonUtil.showToast("请填写人均消费");
            return false;
        }
        if(TextUtils.isEmpty(deliveryFee)){
            CommonUtil.showToast("请填写配送费");
            return false;
        }
        if(TextUtils.isEmpty(deliveryTime)){
            CommonUtil.showToast("请选择配送时间");
            return false;
        }
        if(TextUtils.isEmpty(deliveryArea)){
            CommonUtil.showToast("请选择配送区域");
            return false;
        }
        if(TextUtils.isEmpty(linkMan)){
            CommonUtil.showToast("请填写联系人");
            return false;
        }
//        if(TextUtils.isEmpty(linkTel)){
//            CommonUtil.showToast("请填写手机号");
//            return false;
//        }
//        if(TextUtils.isEmpty(email)){
//            CommonUtil.showToast("请填写邮箱");
//            return false;
//        }
//        if(TextUtils.isEmpty(qq)){
//            CommonUtil.showToast("请填写QQ");
//            return false;
//        }
//        if(TextUtils.isEmpty(weixin)){
//            CommonUtil.showToast("请填写微信");
//            return false;
//        }
        if(TextUtils.isEmpty(remark)){
            CommonUtil.showToast("请填写描述");
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

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCookStyle() {
        return cookStyle;
    }

    public void setCookStyle(String cookStyle) {
        this.cookStyle = cookStyle;
    }

    public String getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(String workPeriod) {
        this.workPeriod = workPeriod;
    }

    public String getWorkStartTimes() {
        return workStartTimes;
    }

    public void setWorkStartTimes(String workStartTimes) {
        this.workStartTimes = workStartTimes;
    }

    public String getWorkEndTimes() {
        return workEndTimes;
    }

    public void setWorkEndTimes(String workEndTimes) {
        this.workEndTimes = workEndTimes;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getPerCapitaSpending() {
        return perCapitaSpending;
    }

    public void setPerCapitaSpending(String perCapitaSpending) {
        this.perCapitaSpending = perCapitaSpending;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
