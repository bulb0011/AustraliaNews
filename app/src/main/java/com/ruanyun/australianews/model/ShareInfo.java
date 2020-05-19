package com.ruanyun.australianews.model;

/**
 * @author hdl
 * @description
 * @date 2018/6/4
 */
public class ShareInfo {
    public int type;//1微信，2朋友圈，3qq，4QQ空间
    public String title;//标题
    public String content;//内容
    public String imgUrl;//图片地址
    public String url;//链接地址

    @Override
    public String toString() {
        return "ShareInfo{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
