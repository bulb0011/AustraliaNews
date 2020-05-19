package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.*

/**
 * @author hdl
 * @description 生活-美食餐饮信息
 * @date 2019/5/30
 */
class LifeFoodShopInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//标题
    var price: String? = null//
    var updateTime: String? = null//更新时间
    var distance: String? = null//距离
    var workStartTime: String? = null//1970-01-01 10:23:00
    var workEndTime: String? = null//1970-01-01 15:23:00
    var cookStyle: String? = null//
    var city: String? = null//
    var address: String? = null//
    var workPeriod: String? = null//
    var startPrice: String? = null//
    var perCapitaSpending: String? = null//
    var deliveryFee: String? = null//
    var deliveryTime: String? = null//
    var deliveryArea: String? = null//
    var remark: String? = null//


    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_REPAST_INFO

    override val commonTime: String?
        get() = "营业时间：${StringUtil.getTimeStrFromFormatStr("HH:mm", workStartTime)}-${StringUtil.getTimeStrFromFormatStr("HH:mm", workEndTime)}"

    override val commonTitle: String?
        get() = title

    override val commonOid: String?
        get() = oid

    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)


    override fun getDistanceStr(): String {
        return distance?:""
    }

    override fun getShopStatus(): Boolean {
        if(CommonUtil.isNotEmpty(workStartTime) && CommonUtil.isNotEmpty(workEndTime)){
            val start = StringUtil.getTimeStrFromFormatStr("HH:mm", workStartTime)
            val end = StringUtil.getTimeStrFromFormatStr("HH:mm", workEndTime)
            val current = DateUtil.getCurrentTime("HH:mm")
            if(start<=end) {
                if (current in start..end) {
                    return true
                }
            }else {
                if(current>start || current<end){
                    return true
                }
            }
        }
        return false
    }

}
