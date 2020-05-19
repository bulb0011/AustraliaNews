package com.ruanyun.australianews.model.params;

import android.text.TextUtils;

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布交易市场
 * @date 2019/5/25
 */
public class ReleaseLifeTransactionMarketParams {
    private String oid;//修改
    private String userOid;//当前登录人
    private String mainPhoto;//图片
    private String title;//标题
    private String dignity;//身份  HOUSING_SOURCE
    private String transactionType;//交易类型  MEANS_TRANSACTION
    private String type;//产品分类  PRODUCT_CATEGORIES
    private String newOldStandard;//新旧程度  NEWOID_STANDARD
    private String warranty;//保修期内  WARRANTY
    private String transactionNature;//交易方式  TRANSACTION_NATURE
    private String transactionArea;//交易区域  TRANSACTION_AREA
    private String city;//区域
    private String price;//价格
    private String deliveryPrice;//送货费用
    private String description;//产品描述
    private String descriptionTag;//产品描述

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
        if(TextUtils.isEmpty(dignity)){
            CommonUtil.showToast("请选择身份");
            return false;
        }
        if(TextUtils.isEmpty(transactionType)){
            CommonUtil.showToast("请选择交易类型");
            return false;
        }
        if(TextUtils.isEmpty(type)){
            CommonUtil.showToast("请选择产品分类");
            return false;
        }
        if(TextUtils.isEmpty(warranty)){
            CommonUtil.showToast("请选择新旧程度");
            return false;
        }
        if(TextUtils.isEmpty(newOldStandard)){
            CommonUtil.showToast("请选择保修期内");
            return false;
        }
        if(TextUtils.isEmpty(transactionNature)){
            CommonUtil.showToast("请选择交易方式");
            return false;
        }
        if(TextUtils.isEmpty(transactionArea)){
            CommonUtil.showToast("请选择交易区域");
            return false;
        }
        if(TextUtils.isEmpty(city)){
            CommonUtil.showToast("请选择Suburb");
            return false;
        }
        if(TextUtils.isEmpty(description)){
            CommonUtil.showToast("请填写产品描述");
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

    public String getDignity() {
        return dignity;
    }

    public void setDignity(String dignity) {
        this.dignity = dignity;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewOldStandard() {
        return newOldStandard;
    }

    public void setNewOldStandard(String newOldStandard) {
        this.newOldStandard = newOldStandard;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getTransactionNature() {
        return transactionNature;
    }

    public void setTransactionNature(String transactionNature) {
        this.transactionNature = transactionNature;
    }

    public String getTransactionArea() {
        return transactionArea;
    }

    public void setTransactionArea(String transactionArea) {
        this.transactionArea = transactionArea;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDescriptionTag() {
        return descriptionTag;
    }

    public void setDescriptionTag(String descriptionTag) {
        this.descriptionTag = descriptionTag;
    }
}
