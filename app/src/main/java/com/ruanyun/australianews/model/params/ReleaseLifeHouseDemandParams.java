package com.ruanyun.australianews.model.params;

import android.text.TextUtils;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布求租
 * @date 2019/3/20
 */
public class ReleaseLifeHouseDemandParams {
    private String oid;//修改
    private String userNum;//当前登录人
    private String title;//标题
    private String city;//区域
    private String type;//求租方式  RENTAL_METHOD
    private String nearbyFacilities;//期望设施  NEARBY_FACILITIES
    private String identity;//交易性质  HOUSING_SOURCE
    private String huxing;//期望户型   HOUSE_HUXING

    private String remark;//附注信息
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
        if(TextUtils.isEmpty(type)){
            CommonUtil.showToast("请选择求租方式");
            return false;
        }
        if(TextUtils.isEmpty(nearbyFacilities)){
            CommonUtil.showToast("请选择期望设施");
            return false;
        }
        if(TextUtils.isEmpty(identity)){
            CommonUtil.showToast("请选择交易性质");
            return false;
        }
        if(TextUtils.isEmpty(huxing)){
            CommonUtil.showToast("请选择期望户型");
            return false;
        }
        if(TextUtils.isEmpty(remark)){
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

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
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

    public String getNearbyFacilities() {
        return nearbyFacilities;
    }

    public void setNearbyFacilities(String nearbyFacilities) {
        this.nearbyFacilities = nearbyFacilities;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
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
}
