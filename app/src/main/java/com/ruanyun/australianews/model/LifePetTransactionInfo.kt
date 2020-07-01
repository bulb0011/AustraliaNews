package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-宠物交易信息
 * @date 2019/5/20
 */
class LifePetTransactionInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//
    var price: String? = null//
    var updateTime: String? = null//
    var type: String? = null//
    var petDetail: String? = null//
    var transactionNature: String? = null//
    var city: String? = null//
    var petType: String? = null//
    var petSex: String? = null//
    var lineage: String? = null//
    var vaccinum: String? = null//
    var isExpellingParasite: String? = null//
    var isSterilize: String? = null//
    var publishType: String? = null//
    var provideService: String? = null//
    var petCount: String? = null//

    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO

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
