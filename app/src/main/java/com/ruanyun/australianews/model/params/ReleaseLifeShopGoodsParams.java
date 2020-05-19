package com.ruanyun.australianews.model.params;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布餐饮美食商品
 * @date 2019/11/12
 */
public class ReleaseLifeShopGoodsParams {
    private String oid;//商品Oid（修改商品时传）
    private String userOid;//当前登录人
    private String mainPhoto;//图片
    private String title;//商品名称
    private String price;//价格
    private String description;//商品描述
    private String infoRestaurantOid;//所属店铺Oid

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(title)){
            CommonUtil.showToast("请填写商品名称");
            return false;
        }
        if(TextUtils.isEmpty(price)){
            CommonUtil.showToast("请填写价格");
            return false;
        }
        if(TextUtils.isEmpty(description)){
            CommonUtil.showToast("请填写商品描述");
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfoRestaurantOid() {
        return infoRestaurantOid;
    }

    public void setInfoRestaurantOid(String infoRestaurantOid) {
        this.infoRestaurantOid = infoRestaurantOid;
    }
}
