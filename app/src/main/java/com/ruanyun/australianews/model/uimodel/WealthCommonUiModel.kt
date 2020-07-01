package com.ruanyun.australianews.model.uimodel

/**
 * @author hdl
 * @description
 * @date 2019/5/20
 */
interface WealthCommonUiModel {

    /** item类型  */
    val itemType: Int
    val commonOid: String?
    var commonBrowseCollectionOid: String?
    val commonMainPhoto: String?
    val commonTitle: String?
    val commonTime: String?

    var isSelect: Boolean

    /** 净值 */
    fun getCommonNetWorth(): String {
        return ""
    }
    /** 增长率 */
    fun getCommonGrowthRate(): String {
        return ""
    }
    /** 地址 */
    fun getCommonAddress(): String {
        return ""
    }
    /** 价格 */
    fun getMoneyStr(): String {
        return ""
    }

}
