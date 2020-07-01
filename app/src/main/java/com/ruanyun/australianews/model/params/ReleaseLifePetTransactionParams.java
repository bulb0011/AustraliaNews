package com.ruanyun.australianews.model.params;

import android.text.TextUtils;
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.ui.life.release.ReleasePetTransactionSelectTypeActivity;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布宠物交易
 * @date 2019/3/21
 */
public class ReleaseLifePetTransactionParams {
    private String oid;//修改
    private String userOid;//当前登录人
    private String mainPhoto;//图片
    private String title;//标题
    private String transactionNature;//身份
    private String city;//区域
    private String type;//宠物大类
    private String petType;//宠物品种
    private String petSex;//性别
    private String lineage;//血统证明
    private String vaccinum;//疫苗情况
    private String isExpellingParasite;//是否驱虫
    private String isSterilize;//是否绝育
    private String petCount;//宠物数量
    private String publishType;//发布类型
    private String provideService;//提供服务
    private String price;//出售价格
    private String petDetail;//描述
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
        if(TextUtils.isEmpty(transactionNature)){
            CommonUtil.showToast("请选择身份");
            return false;
        }
        if(TextUtils.isEmpty(city)){
            CommonUtil.showToast("请选择Suburb");
            return false;
        }
        if(TextUtils.isEmpty(petType)){
            CommonUtil.showToast("请选择品种");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(petSex)){
            CommonUtil.showToast("请选择性别");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(lineage)){
            CommonUtil.showToast("请选择性别");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(petSex)){
            CommonUtil.showToast("请选择血统证明");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(vaccinum)){
            CommonUtil.showToast("请选择疫苗情况");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(isExpellingParasite)){
            CommonUtil.showToast("请选择是否驱虫");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(isSterilize)){
            CommonUtil.showToast("请选择是否绝育");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(petCount)){
            CommonUtil.showToast("请填写宠物数量");
            return false;
        }
        if(TextUtils.isEmpty(publishType)){
            CommonUtil.showToast("请选择发布类型");
            return false;
        }
        if(isPetRequired() && TextUtils.isEmpty(provideService)){
            CommonUtil.showToast("请选择提供服务");
            return false;
        }
        if(TextUtils.isEmpty(petDetail)){
            CommonUtil.showToast("请填写描述");
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

    private boolean isPetRequired(){
        return ReleasePetTransactionSelectTypeActivity.ALL_PET_TYPE_CAT.equals(type)
                || ReleasePetTransactionSelectTypeActivity.ALL_PET_TYPE_DOG.equals(type);
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTransactionNature() {
        return transactionNature;
    }

    public void setTransactionNature(String transactionNature) {
        this.transactionNature = transactionNature;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetSex() {
        return petSex;
    }

    public void setPetSex(String petSex) {
        this.petSex = petSex;
    }

    public String getLineage() {
        return lineage;
    }

    public void setLineage(String lineage) {
        this.lineage = lineage;
    }

    public String getVaccinum() {
        return vaccinum;
    }

    public void setVaccinum(String vaccinum) {
        this.vaccinum = vaccinum;
    }

    public String getIsExpellingParasite() {
        return isExpellingParasite;
    }

    public void setIsExpellingParasite(String isExpellingParasite) {
        this.isExpellingParasite = isExpellingParasite;
    }

    public String getIsSterilize() {
        return isSterilize;
    }

    public void setIsSterilize(String isSterilize) {
        this.isSterilize = isSterilize;
    }

    public String getPetCount() {
        return petCount;
    }

    public void setPetCount(String petCount) {
        this.petCount = petCount;
    }

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public String getProvideService() {
        return provideService;
    }

    public void setProvideService(String provideService) {
        this.provideService = provideService;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPetDetail() {
        return petDetail;
    }

    public void setPetDetail(String petDetail) {
        this.petDetail = petDetail;
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
}
