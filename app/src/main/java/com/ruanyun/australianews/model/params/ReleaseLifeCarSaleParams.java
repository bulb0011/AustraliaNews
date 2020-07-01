package com.ruanyun.australianews.model.params;

import android.text.TextUtils;
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布汽车买卖
 * @date 2019/3/22
 */
public class ReleaseLifeCarSaleParams {
    private String oid;//修改
    private String userOid;//当前登录人
    private String mainPhoto;//图片
    private String title;//标题
    private String year;//年份
    private String city;//区域
    private String brand;//品牌   CAR_BRAND
    private String transmission;//变速箱 TRANSMISSION
    private String kilometer;//公里数
    private String jiaoyiMethod;//交易方式
    private String transactionNature;//车辆来源
    private String price;//价格
    private String carDetail;//详情
    private String linkMan;//联系人
    private String linkTel;//手机号
    private String email;//邮箱
    private String qq;//QQ
    private String weixin;//微信

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(title)){
            CommonUtil.showToast("请填写标题");
            return false;
        }
        if(TextUtils.isEmpty(city)){
            CommonUtil.showToast("请选择Suburb");
            return false;
        }
        if(TextUtils.isEmpty(transactionNature)){
            CommonUtil.showToast("请选择交易方式");
            return false;
        }
        if(TextUtils.isEmpty(kilometer)){
            CommonUtil.showToast("请填写公里数");
            return false;
        }
        if(TextUtils.isEmpty(transmission)){
            CommonUtil.showToast("请选择变速箱");
            return false;
        }
        if(TextUtils.isEmpty(year)){
            CommonUtil.showToast("请填写年份");
            return false;
        }
        if(TextUtils.isEmpty(transactionNature)){
            CommonUtil.showToast("请选择车辆来源");
            return false;
        }
        if(TextUtils.isEmpty(brand)){
            CommonUtil.showToast("请选择品牌");
            return false;
        }
        if(TextUtils.isEmpty(carDetail)){
            CommonUtil.showToast("请填写附注信息");
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
    }

    public String getTransactionNature() {
        return transactionNature;
    }

    public void setTransactionNature(String transactionNature) {
        this.transactionNature = transactionNature;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarDetail() {
        return carDetail;
    }

    public void setCarDetail(String carDetail) {
        this.carDetail = carDetail;
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

    public String getJiaoyiMethod() {
        return jiaoyiMethod;
    }

    public void setJiaoyiMethod(String jiaoyiMethod) {
        this.jiaoyiMethod = jiaoyiMethod;
    }
}
