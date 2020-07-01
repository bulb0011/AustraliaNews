package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description 房屋求租
 * @date 2019/5/20
 */
public class LifeHouseDemandParams extends LifeListBaseParams {
    private String city;//区域
    private String identity;//来源 HOUSING_SOURCE

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

}
