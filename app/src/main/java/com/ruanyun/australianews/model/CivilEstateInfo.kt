package com.ruanyun.australianews.model

import android.content.Context
import android.content.Intent
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel

/**
 * @author hdl
 * @description 地产
 * @date 2020/03/26
 */
class CivilEstateInfo : WealthBaseInfo(), WealthCommonUiModel{
    companion object {
        const val ESTATEINFO_TYPE1 = "ESTATEINFO_TYPE1"//民用地产
        const val ESTATEINFO_TYPE2 = "ESTATEINFO_TYPE2"//商业地产
        const val ESTATEINFO_TYPE3 = "ESTATEINFO_TYPE3"//农场
    }
    override val itemType: Int
        get() {
            when(type){
                ESTATEINFO_TYPE1 -> {
                    return LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE
                }
                ESTATEINFO_TYPE2 -> {
                    return LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE
                }
                ESTATEINFO_TYPE3 -> {
                    return LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE
                }
            }
           return LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE
        }

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
        get() = propertyImage.toImgUrl()

    override val commonTime: String?
        get() = createTime

    override fun getCommonAddress(): String {
        return address
    }

    var area: String = ""// "",
    var huxin: String = ""// "ESTATEINFO_HUXING1",   //户型
    var propertyImage: String = ""// "",   //物业图片
    var address: String = ""// "义井路",   //详细地址
    var city: String = ""// "阿德莱德",  //市
    var endDate: String = ""// null,
    var isDelete: String = ""// 1,
    var latitude: String = ""// "",   //纬度
    var oid: String = ""// "4028811e70f7137b0170f715ce730001",
    var type: String = ""// "ESTATEINFO_TYPE2",   //地产类型(1民用 2商业 3农场)
    var auctionDate: String = ""// "2020-03-20 00:00:00",   //拍卖日期
    var beginDate: String = ""// null,
    var propertyIntroduction: String = ""// //物业介绍
    var propertyStatus: String = ""// "PROPETY_STATUS4",   //物业状态(1在售，2已售出，3开放参观，4拍卖)
    var province: String = ""// "",
    var salesCall: String = ""// "156789654123",   //销售电话
    var salesName: String = ""// "小明",  //销售名字
    var createTime: String = ""// "2020-03-20 16:36:06",   //创建时间
    var price: String = ""// "12.00",   //价格
    var longitude: String = ""// "",  //经度
    var salesMailbox: String = ""// "123@125ee.com",  //销售邮箱
    var title: String = ""// null //标题
}
