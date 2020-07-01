package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/20
 */
public class LifeCarSaleParams extends LifeListBaseParams {
    private String brand;//品牌
    private String jiaoyiMethod;//交易方式
    private String transactionNature;//来源

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getJiaoyiMethod() {
        return jiaoyiMethod;
    }

    public void setJiaoyiMethod(String jiaoyiMethod) {
        this.jiaoyiMethod = jiaoyiMethod;
    }

    public String getTransactionNature() {
        return transactionNature;
    }

    public void setTransactionNature(String transactionNature) {
        this.transactionNature = transactionNature;
    }
}
