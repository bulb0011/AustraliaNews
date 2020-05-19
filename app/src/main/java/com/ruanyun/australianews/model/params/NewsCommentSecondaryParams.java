package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * description: 新闻评论列表的参数
 * <p/>
 * Created by ycw on 2019/12/23.
 */
public class NewsCommentSecondaryParams extends PageParamsBase {

    private String commentInfoOid;//评论oid
    private String fromUserOid;//被回复人用户oid
    public boolean isWealth = false;

    public String getCommentInfoOid() {
        return commentInfoOid;
    }

    public void setCommentInfoOid(String commentInfoOid) {
        this.commentInfoOid = commentInfoOid;
    }

    public String getFromUserOid() {
        return fromUserOid;
    }

    public void setFromUserOid(String fromUserOid) {
        this.fromUserOid = fromUserOid;
    }
}
