package com.ruanyun.australianews.mvpview

import com.ruanyun.australianews.base.TipCommonMvpView
import com.ruanyun.australianews.model.NewsCommentCountInfo

/**
 * @description
 * @author hdl
 * @date 2018/11/27
 */
interface WebViewActivityMvpView : TipCommonMvpView {

    fun aliPaySuccess()
    fun aliPayCancel()
    fun payFail(msg: String)
    fun updateNewsInfo(info: NewsCommentCountInfo)
}