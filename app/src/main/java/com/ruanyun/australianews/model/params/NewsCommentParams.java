package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * description: 新闻评论列表的参数
 * <p/>
 * Created by ycw on 2019/12/23.
 */
public class NewsCommentParams extends PageParamsBase {
    public static final int NEWS = 1;

    private String commonOid;
    /**
     * 类型 1新闻表
     */
    private Integer type = NEWS; //  类型 1新闻表 2-100生活    财富类型(1活动 2基金 3民用 4商业 5农场 6房市 7生意转让)

    public boolean isWealth = false;//是否是财富类型


    public String getCommonOid() {
        return commonOid;
    }

    public void setCommonOid(String commonOid) {
        this.commonOid = commonOid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
