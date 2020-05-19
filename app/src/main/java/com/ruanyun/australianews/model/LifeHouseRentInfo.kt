package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-房屋出租信息
 * @date 2019/5/20
 */
class LifeHouseRentInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//标题
    var rent: String? = null//租金
    var huxing: String? = null//"户型
    var updateTime: String? = null//更新时间
    var description: String? = null//
    var city: String? = null//
    var address: String? = null//
    var longitude: String? = null//
    var latitude: String? = null//
    var rentalMethod: String? = null//
    var nearbyFacilities: String? = null//
    var identity: String? = null//
    var houseCharacteristics: String? = null//
    var isPet: String? = null//
    var minimumTenancy: String? = null//
    var checkInTime: String? = null//



    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO

    override val commonTime: String?
        get() = updateTime

    override val commonTitle: String?
        get() = title

    override val commonOid: String?
        get() = oid

    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)

    override fun getCommonType(): String {
        return DbHelper.getInstance().getItemNameByParentCode(C.ParentCode.HOUSE_HUXING, huxing)
    }

    override fun getMoneyStr(): String {
        return rent?:""
    }

    override fun getDistanceStr(): String {
        return ""
    }

}
