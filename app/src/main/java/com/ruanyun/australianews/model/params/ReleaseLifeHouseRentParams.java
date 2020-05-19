package com.ruanyun.australianews.model.params;

import android.text.TextUtils;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布出租
 * @date 2019/3/19
 */
public class ReleaseLifeHouseRentParams {
    private String oid;//修改时传
    private String userNum;//当前登录人
    private String mainPhoto;//图片
    private String title;//标题
    private String city;//所在区域
    private String address;//详细地址
    private String longitude;//经度
    private String latitude;//纬度
    private String rentalMethod;//出租方式  RENTAL_METHOD
    private String nearbyFacilities;//附近设施  NEARBY_FACILITIES
    private String identity;//房屋来源  HOUSING_SOURCE
    private String huxing;//房屋户型  HOUSE_HUXING
    private String rent;//出租租金
    private String houseCharacteristics;//房屋配置  HOUSE_CONFIGURATION
    private String isPet;//是否允许宠物（1允许 2不允许）
    private String minimumTenancy;//最短租期
    private String checkInTime;//入住时间
    private String description;//附注信息
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
        if(TextUtils.isEmpty(rentalMethod)){
            CommonUtil.showToast("请选择出租方式");
            return false;
        }
        if(TextUtils.isEmpty(identity)){
            CommonUtil.showToast("请选择房屋来源");
            return false;
        }
        if(TextUtils.isEmpty(huxing)){
            CommonUtil.showToast("请选择房屋类型");
            return false;
        }
        if(TextUtils.isEmpty(houseCharacteristics)){
            CommonUtil.showToast("请选择房屋配置");
            return false;
        }
        if(TextUtils.isEmpty(nearbyFacilities)){
            CommonUtil.showToast("请选择附近设施");
            return false;
        }
//        if(TextUtils.isEmpty(checkInTime)){
//            CommonUtil.showToast("请选择入住时间");
//            return false;
//        }
        if(TextUtils.isEmpty(city)){
            CommonUtil.showToast("请选择Suburb");
            return false;
        }
//        if(TextUtils.isEmpty(address)){
//            CommonUtil.showToast("请填写详细地址");
//            return false;
//        }
        if(TextUtils.isEmpty(isPet)){
            CommonUtil.showToast("请选择是否允许宠物");
            return false;
        }
        if(TextUtils.isEmpty(minimumTenancy)){
            CommonUtil.showToast("请填写最短周期");
            return false;
        }
        if(TextUtils.isEmpty(description)){
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

    public String getRentalMethod() {
        return rentalMethod;
    }

    public String getMinimumTenancy() {
        return minimumTenancy;
    }

    public void setMinimumTenancy(String minimumTenancy) {
        this.minimumTenancy = minimumTenancy;
    }

    public void setRentalMethod(String rentalMethod) {
        this.rentalMethod = rentalMethod;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
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

    public String getHouseCharacteristics() {
        return houseCharacteristics;
    }

    public void setHouseCharacteristics(String houseCharacteristics) {
        this.houseCharacteristics = houseCharacteristics;
    }

    public String getNearbyFacilities() {
        return nearbyFacilities;
    }

    public void setNearbyFacilities(String nearbyFacilities) {
        this.nearbyFacilities = nearbyFacilities;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getIsPet() {
        return isPet;
    }

    public void setIsPet(String isPet) {
        this.isPet = isPet;
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

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }
}
