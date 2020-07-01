package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description 教科书
 * @date 2019/11/12
 */
public class LifeShopParams extends LifeListBaseParams {
    private String cookStyle;//菜系   COOK_STYLE
    private String deliveryArea;//配送区域
    private String type;//筛选类型  1时间排序   2离我最近   3人均低到高
    private String longitude;//经度  （筛选类型 为2时传）
    private String latitude;//纬度  （筛选类型 为2时传）

    public String getCookStyle() {
        return cookStyle;
    }

    public void setCookStyle(String cookStyle) {
        this.cookStyle = cookStyle;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
