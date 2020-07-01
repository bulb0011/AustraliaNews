package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel

/**
 * @author hdl
 * @description
 * @date 2019/5/15
 */
class PushRecordNewsInfo : AdvertInfoBase(), LifeReleaseCommonUiModel {
//    var type: Int = 0//类型  2房屋出租  3招聘信息  4汽车买卖  5宠物交易  6交易市场  7房屋求租  8生意转让  9教科书 10美食店铺  大于等于100黄页
//    var newsInfo: NewsInfo? = null
    var createTimeStr: String? = null
    var content: String? = null//内容
    var type: Int = 0
    var isTimeType = false

    override val itemType: Int
        get() = type

    override fun getLifeOrYellowPageCollectionBrowseOid(): String? {
        return oid
    }

    override val commonOid: String?
        get() = ""

    override val commonTitle: String?
        get() = ""

    override val commonTime: String?
        get() = ""

    override val commonMainPhoto: String?
        get() = ""


    override fun getNewsCommonInfo(): PushRecordNewsInfo? {
        return this
    }
}
