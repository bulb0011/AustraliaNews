package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/20
 */
public class LifeHouseRentParams extends LifeListBaseParams {
    private String city;//区域
    private String distanceType;//距离 DISTANCE
    private String rentRange;//租金范围 SCREEN_RENT
    private String identity;//来源 HOUSING_SOURCE
    private String rentalMethod;//出租方式 RENTAL_METHOD
    private String huxing;//房屋户型 HOUSE_HUXING
    private String longitude;//
    private String latitude;//

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(String distanceType) {
        this.distanceType = distanceType;
    }

    public String getRentRange() {
        return rentRange;
    }

    public void setRentRange(String rentRange) {
        this.rentRange = rentRange;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getRentalMethod() {
        return rentalMethod;
    }

    public void setRentalMethod(String rentalMethod) {
        this.rentalMethod = rentalMethod;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
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
