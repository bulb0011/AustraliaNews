package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2019/5/7
 */
public class NewsListParams extends PageParamsBase {
    private String userOid;
    private String channelInfoOid;
    private String city;
    private int type;

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public String getChannelInfoOid() {
        return channelInfoOid;
    }

    public void setChannelInfoOid(String channelInfoOid) {
        this.channelInfoOid = channelInfoOid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
