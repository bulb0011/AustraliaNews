package com.ruanyun.australianews.model

import android.text.TextUtils
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-房屋求租信息
 * @date 2019/5/20
 */
class LifeHouseDemandInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {
    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//标题
    var updateTime: String? = null//更新时间
    var createTime: String? = null//创建时间
    var remark: String? = null//
    var city: String? = null//
    var type: String? = null//
    var nearbyFacilities: String? = null//
    var identity: String? = null//
    var huxing: String? = null//

    override val commonTime: String?
        get() = if(TextUtils.isEmpty(updateTime)) createTime else updateTime

    override val commonOid: String?
        get() = oid

    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)

    override val commonTitle: String?
        get() = title

}
