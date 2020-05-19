package com.ruanyun.australianews.mvpview

import com.ruanyun.australianews.base.TipCommonMvpView

/**
 * @description
 * @author hdl
 * @date 2019/3/4
 */
interface RegisteredMvpView : TipCommonMvpView {

    fun getVCodeSuccess()
    fun registeredSuccess()
}