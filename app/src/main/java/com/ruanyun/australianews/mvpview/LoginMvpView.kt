package com.ruanyun.australianews.mvpview

import com.ruanyun.australianews.base.TipCommonMvpView

/**
 * @description
 * @author hdl
 * @date 2019/3/4
 */
interface LoginMvpView : TipCommonMvpView {

    fun loginSuccess()
    fun bind()
}