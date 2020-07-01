package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-教科书信息
 * @date 2019/5/20
 */
class LifeTextbookInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//
    var photo: String? = null//
    var price: String? = null//
    var updateTime: String? = null//
    var school: String? = null//
    var belongTo: String? = null//
    var course: String? = null//
    var bookeType: String? = null//
    var shape: String? = null//
    var newOldStandard: String? = null//
    var isNotes: String? = null//
    var transactionArea: String? = null//
    var remark: String? = null//

    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO

    override val commonOid: String?
        get() = oid

    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(photo)

    override val commonTitle: String?
        get() = title

    override val commonTime: String?
        get() = updateTime

    override fun getMoneyStr(): String {
        return price?:""
    }


}
