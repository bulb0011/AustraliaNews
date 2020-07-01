package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description 生意转让
 * @date 2019/5/21
 */
public class LifeBusinessTransferParams extends LifeListBaseParams {
    private String city;//区域
    private String businessTransferType;//行业
    private String serviceType;//类型
    private String filterUpdateTime;//筛选最近时间   1.最近3天   2.最近7天   3.最近1个月   4.最近3个月

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBusinessTransferType() {
        return businessTransferType;
    }

    public void setBusinessTransferType(String businessTransferType) {
        this.businessTransferType = businessTransferType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getFilterUpdateTime() {
        return filterUpdateTime;
    }

    public void setFilterUpdateTime(String filterUpdateTime) {
        this.filterUpdateTime = filterUpdateTime;
    }
}
