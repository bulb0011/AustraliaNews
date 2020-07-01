package com.ruanyun.australianews.model.params;

import android.text.TextUtils;

import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布地产
 * @date 2020/3/26
 */
public class ReleaseWealthCivilEstateParams {
    private String oid;//修改
    private String title;//标题
    private String userId;//当前登录人
    private String propertyImage;//图片
    private String city;//市
    private String address;//详细地址
    private String longitude;//经度
    private String latitude;//纬度
    private String price;//价格
    private String huxin;//户型    ESTATEINFO_HUXING
    private String propertyIntroduction;//物业介绍
    private String propertyStatus;//物业状态( PROPETY_STATUS1 在售， PROPETY_STATUS2 已售出，PROPETY_STATUS3 开放参观，PROPETY_STATUS4 拍卖)     PROPETY_STATUS
    private String auctionDate;//拍卖日期 （物业状态(为 ‘PROPETY_STATUS4’拍卖  时必传）
    private String salesName;//销售名字
    private String salesCall;//销售电话
    private String salesMailbox;//销售邮箱

    private String type;//地产类型( ESTATEINFO_TYPE1民用 ESTATEINFO_TYPE2商业  ESTATEINFO_TYPE3农场)   （新增时必传）   ESTATEINFO_TYPE

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(title)){
            CommonUtil.showToast("请填写标题");
            return false;
        }
        if(TextUtils.isEmpty(huxin)){
            CommonUtil.showToast("请选择户型");
            return false;
        }
        if(TextUtils.isEmpty(price)){
            CommonUtil.showToast("请填写价格");
            return false;
        }
        if(TextUtils.isEmpty(city)){
            CommonUtil.showToast("请选择城市");
            return false;
        }
        if(TextUtils.isEmpty(address)){
            CommonUtil.showToast("请填写详细地址");
            return false;
        }
        if(TextUtils.isEmpty(propertyStatus)){
            CommonUtil.showToast("请选择物业状态");
            return false;
        }
        if(TextUtils.isEmpty(auctionDate) && propertyStatus.equals("PROPETY_STATUS4")){
            CommonUtil.showToast("请选择拍卖日期");
            return false;
        }
        if(TextUtils.isEmpty(propertyIntroduction)){
            CommonUtil.showToast("请填写详物业介绍");
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

    public String getPropertyImage() {
        return propertyImage;
    }

    public void setPropertyImage(String propertyImage) {
        this.propertyImage = propertyImage;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHuxin() {
        return huxin;
    }

    public void setHuxin(String huxin) {
        this.huxin = huxin;
    }

    public String getPropertyIntroduction() {
        return propertyIntroduction;
    }

    public void setPropertyIntroduction(String propertyIntroduction) {
        this.propertyIntroduction = propertyIntroduction;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public String getAuctionDate() {
        return auctionDate;
    }

    public void setAuctionDate(String auctionDate) {
        this.auctionDate = auctionDate;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSalesCall() {
        return salesCall;
    }

    public void setSalesCall(String salesCall) {
        this.salesCall = salesCall;
    }

    public String getSalesMailbox() {
        return salesMailbox;
    }

    public void setSalesMailbox(String salesMailbox) {
        this.salesMailbox = salesMailbox;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
