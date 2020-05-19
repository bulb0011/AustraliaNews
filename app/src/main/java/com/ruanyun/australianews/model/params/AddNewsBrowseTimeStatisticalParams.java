package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/24
 */
public class AddNewsBrowseTimeStatisticalParams {
    private String userOid;//当前登录人
    private String newsInfoOid;//新闻oid
    private String startTime;//浏览开始时间
    private String endTime;//浏览结束时间

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getNewsInfoOid() {
        return newsInfoOid;
    }

    public void setNewsInfoOid(String newsInfoOid) {
        this.newsInfoOid = newsInfoOid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
