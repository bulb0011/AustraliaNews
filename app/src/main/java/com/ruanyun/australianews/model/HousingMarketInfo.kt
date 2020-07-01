package com.ruanyun.australianews.model

import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel

/**
 * @author hdl
 * @description 房市
 * @date 2020/03/26
 */
class HousingMarketInfo : WealthBaseInfo(), WealthCommonUiModel{
    override val itemType: Int
        get() = LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET
    override var isSelect: Boolean = false

    override var commonBrowseCollectionOid: String?
        get() = browseCollectionOid
        set(value) {
            browseCollectionOid = value
        }

    override val commonOid: String?
        get() = oid

    override val commonTitle: String?
        get() = title

    override val commonMainPhoto: String?
        get() = image.toImgUrl()

    override val commonTime: String?
        get() = createTime

    override fun getCommonAddress(): String {
        return address
    }

    var title: String = ""// null,
    var image: String = ""// "20200319141604940.jpg",   //图片
    var endDate: String = ""// null,
    var isDelete: String = ""// 1,
    var address: String = ""//地址
    var latitude: String = ""// "4569.126",  //纬度
    var longitude: String = ""// "1230.411"   //经度
    var acreage: String = ""// "123",  //面积
    var flag3: String = ""// "",
    var agentName: String = ""// "安居客",  //中介名字
    var flag2: String = ""// "",
    var flag1: String = ""// "",
    var oid: String = ""// "4028811e711128a6017114666d900002",
    var type: String = ""// "HOUSING_MARKET1",   //类型   HOUSING_MARKET
    var contactDetails: String = ""// "18965412397",   //联系方式
    var userId: String = ""// "402881f56aa602bc016aa60bed420004",   //创建人oid
    var beginDate: String = ""// null,
    var mailbox: String = ""// "12867452149@qq.com",   //邮箱
    var createTime: String = ""// "2020-03-26 09:13:09",
    var company: String = ""// "置地栢悦湾",   //公司
    var introduction: String = ""// "介绍" //介绍
}
