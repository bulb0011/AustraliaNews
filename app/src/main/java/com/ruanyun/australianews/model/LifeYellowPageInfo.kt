package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-黄页信息
 * @date 2019/2/23
 */
class LifeYellowPageInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null
//    var title: String? = null
//    var mainPhoto: String? = null
    var companyName: String? = null
    var updateTime: String? = null
    var description: String? = null
    var city: String? = null
    var companyAddress: String? = null
    var companyWebsite: String? = null
    var transactionNature: String? = null

    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO
    override val commonOid: String?
        get() = oid
    override val commonTitle: String?
        get() = title
    override val commonTime: String?
        get() = updateTime
    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)

    override fun getYellowPageCompanyName(): String {
        return companyName?:""
    }

    override fun getLifePhone(): String? {
        return linkTel
    }
}
