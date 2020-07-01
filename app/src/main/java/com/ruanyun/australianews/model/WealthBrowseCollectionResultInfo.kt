package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel

/**
 * @author hdl
 * @description 浏览收藏 生活服务信息
 * @date 2019/5/30
 */
class WealthBrowseCollectionResultInfo : WealthCommonUiModel {

    override var isSelect: Boolean = false
    var oid : String?=null//浏览表oid
    val content : String = ""//json 数据
    val type : Int = 0//类型，1活动 2基金  3民用 4商业 5农场 6房市 7生意

    override var commonBrowseCollectionOid: String?
        get() = oid
        set(value) {
            oid = value
        }

    override val itemType: Int
        get() = type
    override val commonOid: String?
        get() = ""
    override val commonTitle: String?
        get() = content
    override val commonTime: String?
        get() = ""
    override val commonMainPhoto: String?
        get() = ""

}
