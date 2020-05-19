package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel

/**
 * @author hdl
 * @description
 * @date 2019/5/30
 */
class LifeSearchResultInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {
    var type: Int = 0//类型  2房屋出租  3招聘信息  4汽车买卖  5宠物交易  6交易市场  7房屋求租  8生意转让  9教科书 10美食店铺  大于等于100黄页
    var content: String? = null//内容 对象json

    override val itemType: Int
        get() = type

    override fun getLifeOrYellowPageInfo(): String? {
        return content
    }

    override val commonOid: String?
        get() = null

    override val commonTitle: String?
        get() = null

    override val commonTime: String?
        get() = null

    override val commonMainPhoto: String?
        get() = null

}
