package com.ruanyun.australianews.model;

import java.util.List;

/**
 * @author hdl
 * @description
 * @date 2019/5/11
 */
public class ChannelResult {
    public List<ChannelInfo> channelRelationListChoose;//我的频道
    public List<ChannelInfo> channelRelationListNoChoose;//未订阅频道
    public List<ChannelInfo> customChannelListNoChoose;//自定义频道
}
