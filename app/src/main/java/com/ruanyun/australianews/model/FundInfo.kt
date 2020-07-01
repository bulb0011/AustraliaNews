package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel

/**
 * @author hdl
 * @description 基金
 * @date 2020/03/26
 */
class FundInfo : WealthBaseInfo(), WealthCommonUiModel{
    override val commonMainPhoto: String?
        get() = ""
    override val itemType: Int
        get() = LifeReleaseCommonUiModel.WEALTH_FUND

    override var isSelect: Boolean = false

    override var commonBrowseCollectionOid: String?
        get() = browseCollectionOid
        set(value) {
            browseCollectionOid = value
        }

    override val commonOid: String?
        get() = oid

    override val commonTitle: String?
        get() = name

    override fun getCommonNetWorth(): String {
        return netWorth
    }

    override fun getCommonGrowthRate(): String {
        return growthRate
    }

    override val commonTime: String?
        get() = createTime


    var beginDate: String = ""// null,
    var createTime: String = ""// "2020-03-18 14:26:35",
    var endDate: String = ""// null,
    var isDelete: String = ""// 1,
    var growthRate: String = ""// "3.02",   //增长率
    var netWorth: String = ""// "0.7782",   //净值
    var name: String = ""// "南方有色金属联接C",   //名称
    var oid: String = ""// "4028811e70ebc5470170ec527fd2000d",    //基金oid
    var content: String = ""// "<p>如果与人TV该方法GV分隔符GV草草收场吧 父发是从VCD测试下</p>"   //内容
}
