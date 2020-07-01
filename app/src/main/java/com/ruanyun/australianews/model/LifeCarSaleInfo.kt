package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-汽车买卖信息
 * @date 2019/5/20
 */
class LifeCarSaleInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//
    var price: String? = null//
    var updateTime: String? = null//
    var carDetail: String? = null//
    var city: String? = null//
    var jiaoyiMethod: String? = null//
    var kilometer: String? = null//
    var transmission: String? = null//
    var year: String? = null//
    var transactionNature: String? = null//
    var brand: String? = null//

    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO

    override val commonOid: String?
        get() = oid

    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)

    override val commonTitle: String?
        get() = title

    override val commonTime: String?
        get() = updateTime

    override fun getMoneyStr(): String {
        return price?:""
    }

}
