package com.ruanyun.australianews.model.uimodel

import android.text.TextUtils
import com.ruanyun.australianews.model.*

/**
 * @author hdl
 * @description
 * @date 2019/5/20
 */
interface LifeReleaseCommonUiModel : LifeReleaseCommonModel{

    val itemType: Int

    val commonOid: String?
    val commonTitle: String?
    val commonTime: String?
    val commonMainPhoto: String?

    /** 户型/工作性质  */
    fun getCommonType(): String{
        return ""
    }
    /** 距离 */
    fun getDistanceStr(): String{
        return ""
    }
    /** 金额 */
    fun getMoneyStr(): String{
        return ""
    }
    /** 经验要求 */
    fun getExperienceRequirementsStr(): String{
        return ""
    }
    /** 黄页发布公司信息 */
    fun getYellowPageCompanyName(): String{
        return ""
    }
    /** 电话号码 */
    fun getLifePhone(): String?{
        return ""
    }
    /** 餐饮美食营业状态 */
    fun getShopStatus(): Boolean{
        return false
    }
    /** 生活服务及黄页搜索数据 */
    fun getLifeOrYellowPageInfo(): String?{
        return ""
    }
    /** 生活服务公共表数据 */
    fun getLifeCommonGeneralInfo(): LifeGeneralInfo?{
        return null
    }
    /** 新闻推送信息数据 */
    fun getNewsCommonInfo(): PushRecordNewsInfo?
    /** 新闻推送信息数据 */
    fun setNewsCommonInfo(info: PushRecordNewsInfo?)

    /** 新闻信息 */
    fun getINewsInfo(): NewsInfo?{
        return null
    }

    companion object {
        const val NEWS_INFO = 1//新闻信息
        const val LIFE_HOUSE_RENT_INFO = 2//房屋出租
        const val LIFE_RECRUITMENT_INFO = 3//招聘信息
        const val LIFE_CAR_SALE_INFO = 4//汽车买卖
        const val LIFE_PET_TRANSACTION_INFO = 5//宠物交易
        const val LIFE_TRANSACTION_MARKET_INFO = 6//交易市场
        const val LIFE_HOUSE_DEMAND_INFO = 7//房屋求租
        const val LIFE_BUSINESS_TRANSFER_INFO = 8//生意转让
        const val LIFE_TEXTBOOK_INFO = 9//教科书
        const val LIFE_REPAST_INFO = 10//餐饮美食
        const val LIFE_YELLOW_PAGE_INFO = 100//黄页

        const val WEALTH_ACTIVITYS = -1//活动
        const val WEALTH_FUND = -2//基金
        const val WEALTH_CIVIL_ESTATE = -3//民用地产
        const val WEALTH_COMMERCE_ESTATE = -4//商业地产
        const val WEALTH_FARM_ESTATE = -5//农场
        const val WEALTH_HOUSING_MARKET = -6//房市

    }

}
