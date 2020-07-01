package com.ruanyun.australianews.model.params;

import android.text.TextUtils;

import com.ruanyun.australianews.util.CommonUtil;

/**
 * @author hdl
 * @description 发布活动
 * @date 2020/3/26
 */
public class ReleaseWealthActivitysParams {
    private String oid;//修改
    private String title;//标题
    private String userId;//当前登录人
    private String image;//图片
    private String type;//类型    ACTIVITY_TYPE
    private String eventOrganizer;//活动组织者
    private String address;//地址
    private String activityDescription;//活动描述
    private String eventDate;//活动时间

    public boolean isNotEmpty(){
        if(TextUtils.isEmpty(title)){
            CommonUtil.showToast("请填写标题");
            return false;
        }
        if(TextUtils.isEmpty(type)){
            CommonUtil.showToast("请选择类型");
            return false;
        }
        if(TextUtils.isEmpty(eventOrganizer)){
            CommonUtil.showToast("请填写活动组织者");
            return false;
        }
        if(TextUtils.isEmpty(address)){
            CommonUtil.showToast("请填写活动地址");
            return false;
        }
        if(TextUtils.isEmpty(eventDate)){
            CommonUtil.showToast("请选择活动时间");
            return false;
        }
        if(TextUtils.isEmpty(activityDescription)){
            CommonUtil.showToast("请填写描述");
            return false;
        }
        return true;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
