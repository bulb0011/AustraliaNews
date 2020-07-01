package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-生意转让信息
 * @date 2019/5/20
 */
class LifeBusinessTransferInfo : LifeCommonInfo(), LifeReleaseCommonUiModel, WealthCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//
    var price: String? = null//
    var updateTime: String? = null//
    var businessTransferDetail: String? = null//
    var city: String? = null//
    var transactionNature: String? = null//
    var serviceType: String? = null//
    var businessTransferType: String? = null//
    var companyName: String? = null//
    var companyAddress: String? = null//

    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO

    override var commonBrowseCollectionOid: String?
        get() = collectionBrowseOid
        set(value) {
            collectionBrowseOid = value
        }

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
