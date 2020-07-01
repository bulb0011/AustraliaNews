package com.ruanyun.australianews.model.params;

import android.text.TextUtils;

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布教科书
 * @date 2019/527
 */
public class ReleaseLifeTextbookParams {
    private String oid;//修改
    private String userOid;//当前登录人
    private String photo;//图片
    private String title;//标题
    private String school;//学校名称  BOOK_SCHOOL_NAME
    private String belongTo;//书籍属于  BOOK_SCHOOL
    private String course;//所属课程
    private String bookeType;//书本类型   BOOK_TYPE
    private String shape;//形式   BOOK_SHAPE
    private String newOldStandard;//新旧程度   BOOK_NEW_OLD
    private String isNotes;//有无笔记	 BOOK_IS_NOTE
    private String price;//价格
    private String transactionArea;//交易地点	 BOOK_TRAN_AREA

    private String remark;//描述
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
        if(TextUtils.isEmpty(school)){
            CommonUtil.showToast("请选择学校名称");
            return false;
        }
        if(TextUtils.isEmpty(belongTo)){
            CommonUtil.showToast("请选择书籍属于");
            return false;
        }
        if(TextUtils.isEmpty(course)){
            CommonUtil.showToast("请填写所属课程");
            return false;
        }
        if(TextUtils.isEmpty(bookeType)){
            CommonUtil.showToast("请选择书本类型");
            return false;
        }
        if(TextUtils.isEmpty(shape)){
            CommonUtil.showToast("请选择形式");
            return false;
        }
        if(TextUtils.isEmpty(newOldStandard)){
            CommonUtil.showToast("请选择新旧程度");
            return false;
        }
        if(TextUtils.isEmpty(isNotes)){
            CommonUtil.showToast("请选择有无笔记");
            return false;
        }
        if(TextUtils.isEmpty(transactionArea)){
            CommonUtil.showToast("请选择交易地点");
            return false;
        }
        if(TextUtils.isEmpty(remark)){
            CommonUtil.showToast("请填写描述");
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

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTransactionArea() {
        return transactionArea;
    }

    public void setTransactionArea(String transactionArea) {
        this.transactionArea = transactionArea;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
