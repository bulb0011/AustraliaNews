package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/21
 */
public class LifeTransactionMarketParams extends LifeListBaseParams {
    private String type;//产品分类
    private String transactionNature;//交易方式
    private String transactionType;//交易类型
    private String newOldStandard;//新旧程度
    private String warranty;//保修期内
    private String transactionArea;//交易区域

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransactionNature() {
        return transactionNature;
    }

    public void setTransactionNature(String transactionNature) {
        this.transactionNature = transactionNature;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getNewOldStandard() {
        return newOldStandard;
    }

    public void setNewOldStandard(String newOldStandard) {
        this.newOldStandard = newOldStandard;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getTransactionArea() {
        return transactionArea;
    }

    public void setTransactionArea(String transactionArea) {
        this.transactionArea = transactionArea;
    }
}
