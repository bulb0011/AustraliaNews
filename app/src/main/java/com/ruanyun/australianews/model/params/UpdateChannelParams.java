package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/13
 */
public class UpdateChannelParams {
    public int sortNum;//排序
    public int choose;//是否选择
    public String oid;//频道oid
    public int type;//类型 1系统频道 2个人频道

    public UpdateChannelParams(int sortNum, int choose, String oid, int type) {
        this.sortNum = sortNum;
        this.choose = choose;
        this.oid = oid;
        this.type = type;
    }
}
