package com.ruanyun.australianews.model.params;

import android.text.TextUtils;
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布招聘信息
 * @date 2019/3/21
 */
public class ReleaseLifeRecruitmentParams {
    private String oid;//修改
    private String userOid;//当前登录人
    private String mainPhoto;//图片
    private String title;//标题
    private String recruiyDetail;//工作要求
    private String linkMan;//联系人
    private String linkTel;//手机号
    private String email;//邮箱
    private String qq;//QQ
    private String weixin;//微信
    private String city;//区域

    private String address;//工作地点
    private String companyDescription;//公司介绍
    private String companyName;//公司名称
    private String salary;//工资水平
    private String jobFunctions;//工作性质  JOB_FUNCTIONS
    private String industryRequirements;//行业要求
    private String experienceRequirements;//经验要求
    private String sexRequirements;//性别要求
    private String educationalRequirements;//学历要求
    private String visaRequirements;//签证要求
    private String recruiyCount;//招聘人数

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(title)){
            CommonUtil.showToast("请填写标题");
            return false;
        }
        if(TextUtils.isEmpty(companyDescription)){
            CommonUtil.showToast("请填写公司介绍");
            return false;
        }
        if(TextUtils.isEmpty(companyName)){
            CommonUtil.showToast("请填写公司名称");
            return false;
        }
        if(TextUtils.isEmpty(address)){
            CommonUtil.showToast("请填写工作地点");
            return false;
        }
        if(TextUtils.isEmpty(jobFunctions)){
            CommonUtil.showToast("请选择工作性质");
            return false;
        }
        if(TextUtils.isEmpty(industryRequirements)){
            CommonUtil.showToast("请选择行业要求");
            return false;
        }
        if(TextUtils.isEmpty(experienceRequirements)){
            CommonUtil.showToast("请选择经验要求");
            return false;
        }
        if(TextUtils.isEmpty(city)){
            CommonUtil.showToast("请选择Suburb");
            return false;
        }
        if(TextUtils.isEmpty(sexRequirements)){
            CommonUtil.showToast("请选择性别要求");
            return false;
        }
        if(TextUtils.isEmpty(educationalRequirements)){
            CommonUtil.showToast("请选择学历要求");
            return false;
        }
        if(TextUtils.isEmpty(recruiyCount)){
            CommonUtil.showToast("请填写招聘人数");
            return false;
        }
        if(TextUtils.isEmpty(visaRequirements)){
            CommonUtil.showToast("请选择签证要求");
            return false;
        }
        if(TextUtils.isEmpty(recruiyDetail)){
            CommonUtil.showToast("请填写工作要求");
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

    public String getRecruiyDetail() {
        return recruiyDetail;
    }

    public void setRecruiyDetail(String recruiyDetail) {
        this.recruiyDetail = recruiyDetail;
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

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobFunctions() {
        return jobFunctions;
    }

    public void setJobFunctions(String jobFunctions) {
        this.jobFunctions = jobFunctions;
    }

    public String getIndustryRequirements() {
        return industryRequirements;
    }

    public void setIndustryRequirements(String industryRequirements) {
        this.industryRequirements = industryRequirements;
    }

    public String getExperienceRequirements() {
        return experienceRequirements;
    }

    public void setExperienceRequirements(String experienceRequirements) {
        this.experienceRequirements = experienceRequirements;
    }

    public String getSexRequirements() {
        return sexRequirements;
    }

    public void setSexRequirements(String sexRequirements) {
        this.sexRequirements = sexRequirements;
    }

    public String getEducationalRequirements() {
        return educationalRequirements;
    }

    public void setEducationalRequirements(String educationalRequirements) {
        this.educationalRequirements = educationalRequirements;
    }

    public String getRecruiyCount() {
        return recruiyCount;
    }

    public void setRecruiyCount(String recruiyCount) {
        this.recruiyCount = recruiyCount;
    }

    public String getVisaRequirements() {
        return visaRequirements;
    }

    public void setVisaRequirements(String visaRequirements) {
        this.visaRequirements = visaRequirements;
    }
}
