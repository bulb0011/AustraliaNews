package com.ruanyun.australianews.model.params;

import android.text.TextUtils;
import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description
 * @date 2019/5/9
 */
public class FeedbackParams {

    private String userOid;
    private String contactInformation;//联系方式
    private String content;
    private String mainPhoto;

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(contactInformation)){
            CommonUtil.showToast("请输入您的联系方式");
            return false;
        }
        if(TextUtils.isEmpty(content)){
            CommonUtil.showToast("请提出您的宝贵意见");
            return false;
        }
        return true;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }
}
