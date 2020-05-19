package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description 教科书
 * @date 2019/5/21
 */
public class LifeTextbookParams extends LifeListBaseParams {
    private String school;//学校
    private String bookeType;//类型
    private String shape;//形式
    private String newOldStandard;//新旧程度
    private String isNotes;//有无笔记
    private String transactionArea;//交易地点
    private String belongTo;//书籍属于

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBookeType() {
        return bookeType;
    }

    public void setBookeType(String bookeType) {
        this.bookeType = bookeType;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getNewOldStandard() {
        return newOldStandard;
    }

    public void setNewOldStandard(String newOldStandard) {
        this.newOldStandard = newOldStandard;
    }

    public String getIsNotes() {
        return isNotes;
    }

    public void setIsNotes(String isNotes) {
        this.isNotes = isNotes;
    }

    public String getTransactionArea() {
        return transactionArea;
    }

    public void setTransactionArea(String transactionArea) {
        this.transactionArea = transactionArea;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }
}
