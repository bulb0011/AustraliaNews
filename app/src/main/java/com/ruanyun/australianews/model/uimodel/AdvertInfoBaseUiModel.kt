package com.ruanyun.australianews.model.uimodel

/**
 * @description
 * @author hdl
 * @date 2019/5/21
 */
interface AdvertInfoBaseUiModel {

    var isSelect: Boolean

    fun getAdvertOid(): String?{
        return ""
    }
    fun getAdvertMainPhoto(): String{
        return ""
    }
    fun getAdvertTitle(): String?{
        return ""
    }
    fun getAdvertJumpType(): Int{
        return 0
    }
    fun getAdvertLinkUrl(): String?{
        return ""
    }
    fun getAdvertMark(): Boolean{
        return false
    }
}