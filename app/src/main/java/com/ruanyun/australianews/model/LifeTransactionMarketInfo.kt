package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-交易市场信息
 * @date 2019/5/20
 */
class LifeTransactionMarketInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//标题
    var updateTime: String? = null//"2019-02-28 17:33:21",
    var price: String? = null//
    var dignity: String? = null//
    var transactionType: String? = null//
    var type: String? = null//
    var newOldStandard: String? = null//
    var warranty: String? = null//
    var transactionNature: String? = null//
    var transactionArea: String? = null//
    var city: String? = null//
    var deliveryPrice: String? = null//
    var description: String? = null//
    var descriptionTag: String? = null//


    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO

    override val commonTitle: String?
        get() = title

    override val commonTime: String?
        get() = updateTime

    override val commonOid: String?
        get() = oid

    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)

    override fun getMoneyStr(): String {
        return price?:""
    }

}
