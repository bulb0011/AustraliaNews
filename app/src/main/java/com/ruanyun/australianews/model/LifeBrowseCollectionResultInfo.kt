package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel

/**
 * @author hdl
 * @description 浏览收藏 生活服务信息
 * @date 2019/5/30
 */
class LifeBrowseCollectionResultInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {
    var generalInfo: LifeGeneralInfo?=null


    override val itemType: Int
        get() = 0
    override val commonOid: String?
        get() = ""
    override val commonTitle: String?
        get() = ""
    override val commonTime: String?
        get() = ""
    override val commonMainPhoto: String?
        get() = ""

    override fun getLifeCommonGeneralInfo(): LifeGeneralInfo? {
        return generalInfo
    }

    override fun getLifeOrYellowPageCollectionBrowseOid(): String? {
        return oid
    }
}
