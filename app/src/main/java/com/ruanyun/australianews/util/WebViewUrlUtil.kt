package com.ruanyun.australianews.util

import android.content.Context
import android.text.TextUtils
import com.ruanyun.australianews.App
import com.ruanyun.australianews.model.CivilEstateInfo
import com.ruanyun.australianews.model.NewsInfo
import com.ruanyun.australianews.model.ShareJsonInfo
import com.ruanyun.australianews.model.params.NewsCommentParams
import com.ruanyun.australianews.model.uimodel.AdvertInfoBaseUiModel
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.WebViewActivity
import com.ruanyun.australianews.ui.life.release.*
import com.ruanyun.australianews.ui.main.ReleaseActivitysActivity
import com.ruanyun.australianews.ui.news.NewsDetailsActivity
import com.ruanyun.australianews.ui.news.NewsDetailsActivityTo
import com.ruanyun.australianews.ui.news.VideoNewsDetailsActivity
import com.ruanyun.australianews.ui.wealth.ReleaseCivilEstateActivity
import com.ruanyun.australianews.ui.wealth.ReleaseHousingMarketActivity

/**
 * @description
 * @author hdl
 * @date 2019/3/15
 */
open class WebViewUrlUtil {

    companion object {

        const val NEWS_DETAILS = "app/newsinfo/getNewsInfoDetails?city=%s&newsInfoOid=%s&userOid=%s"//新闻详情
        const val ADVERTISING_DETAILS = "app/adverinfo/getAdvertisingDetail?adverInfoOid=%s"//广告详情


        /**
         * 跳转广告详情
         * @param context
         */
        fun showAdvertDetailsWeb(context: Context, advertInfo: AdvertInfoBaseUiModel): Boolean {
            return when(advertInfo.getAdvertJumpType()){
                2 -> {//广告详情
                    WebViewActivity.start(context, FileUtil.getWebViewUrl(ADVERTISING_DETAILS, advertInfo.getAdvertOid()))
                    true
                }
                4 -> {//外部链接
                    WebViewActivity.start(context, advertInfo.getAdvertLinkUrl())
                    true
                }
                else ->{
                    false
                }
            }
        }

        /**
         * 跳转新闻详情
         * @param context
         */
        fun showNewsDetailsWeb(context: Context, info: NewsInfo) {
            val shareJsonInfo = ShareJsonInfo()
            shareJsonInfo.share_title = info.title
            shareJsonInfo.share_image = info.commonMainPhoto

            when(info.type){

                1-> {
                    val url = FileUtil.getWebViewUrl(NEWS_DETAILS, App.getInstance().cityName, info.oid, App.getInstance().userOid)
                    shareJsonInfo.share_url = url
                    val json = GsonUtil.toJson(shareJsonInfo)

                    val url_zh=url+"&language=zh"
                    val url_en=url+"&language=en"

                    LogX.e("dengpao","url_zh="+url_zh)
                    LogX.e("dengpao","url_en="+url_en)

                    NewsDetailsActivity.startNewsDetails(context, url_zh, info.oid, NewsCommentParams.NEWS, json,info.commentCount,info.watchCount,info.baseWebsite,info.commonTime,url_en)
                }
                2-> {
                    VideoNewsDetailsActivity.start(context, info)
                }
                3-> {

                    val url = info.outUrl

                    val url_zh=url+"&language=zh"
                    val url_en=url+"&language=en"

                    shareJsonInfo.share_url = url
                    val json = GsonUtil.toJson(shareJsonInfo)
                    NewsDetailsActivity.startNewsDetails(context, url_zh, info.oid, NewsCommentParams.NEWS, json,info.commentCount,info.watchCount,info.baseWebsite,info.commonTime,url_en)
                }
            }
        }

        /**
         * 跳转新闻详情 FLAG_ACTIVITY_NEW_TASK
         * @param context
         */
        @JvmStatic
        fun showNewsDetailsWebNewTask(context: Context, info: NewsInfo) {
            val shareJsonInfo = ShareJsonInfo()
            shareJsonInfo.share_title = info.title
            shareJsonInfo.share_image = info.commonMainPhoto
            when(info.type){
                1-> {
                    val url = FileUtil.getWebViewUrl(NEWS_DETAILS, App.getInstance().cityName, info.oid, App.getInstance().userOid)

                    val url_zh=url+"&language=zh"
                    val url_en=url+"&language=en"

                    shareJsonInfo.share_url = url
                    val json = GsonUtil.toJson(shareJsonInfo)
                    NewsDetailsActivity.startNewsDetailsNewTask(context, url_zh, info.oid, NewsCommentParams.NEWS, json,info.commentCount,info.watchCount,info.baseWebsite,info.commonTime,url_en)
                }
                2-> {
                    VideoNewsDetailsActivity.startNewTask(context, info)
                }
                3-> {
                    val url = info.outUrl
                    shareJsonInfo.share_url = url

                    val url_zh=url+"&language=zh"
                    val url_en=url+"&language=en"

                    val json = GsonUtil.toJson(shareJsonInfo)
                    NewsDetailsActivity.startNewsDetailsNewTask(context, url_zh, info.oid, NewsCommentParams.NEWS, json,info.commentCount,info.watchCount,info.baseWebsite,info.commonTime,url_en)
                }
            }
        }

        /**
         * 跳转生活详情
         * @param context
         */
        fun showLifeDetailsWeb(context: Context, type: Int?, commonOid : String?) {
            if(TextUtils.isEmpty(commonOid)){
                return
            }
            val url = when(type){

                LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO -> "app/page/index#HouseDetails?city=%s&infoHouseOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO -> "app/page/index#recruitDetails?city=%s&infoRecruitOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO -> "app/page/index1#carDetails?city=%s&infoCarOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO -> "app/page/index2#petsDetails?city=%s&infoPetOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO -> "app/page/index2#marketDetails?city=%s&infoTradingMarketOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO -> "app/page/index1#rentDetails?city=%s&infoHouseRentSeekingOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> "app/page/index3#businessDetails?city=%s&infoBusinessTransferOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO -> "app/page/index3#bookDetails?city=%s&infoTextbookOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_REPAST_INFO -> "app/page/index4#RestaurantDetails?city=%s&infoRestaurantOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO -> "app/page/index4#yellowPageDetails?city=%s&yellowPageOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> "app/newPage/index#ActivityInfo?city=%s&activityInfoOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_FUND -> "app/newPage/index1#fundInfo?city=%s&fundInfoOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> "app/newPage/index#housingMarket?city=%s&housingMarketOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE,
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE,
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> "app/newPage/index#estateInfo?city=%s&estateInfoOid=%s&userOid=%s"
                else -> return
            }
//            NewsDetailsActivity.startNewsDetails(context, FileUtil.getWebViewUrl(url, App.getInstance().cityName, commonOid, App.getInstance().userOid), commonOid, type, "")
            NewsDetailsActivityTo.startNewsDetails(context, FileUtil.getWebViewUrl(url, App.getInstance().cityName, commonOid, App.getInstance().userOid), commonOid, type, "")

        }

        /**
         * 跳转生活详情 FLAG_ACTIVITY_NEW_TASK
         * @param context
         */
        fun showLifeDetailsWebNewTask(context: Context, type: Int?, commonOid : String?) {
            if(TextUtils.isEmpty(commonOid)){
                return
            }
            val url = when(type){
                LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO -> "app/page/index#HouseDetails?city=%s&infoHouseOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO -> "app/page/index#recruitDetails?city=%s&infoRecruitOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO -> "app/page/index1#carDetails?city=%s&infoCarOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO -> "app/page/index2#petsDetails?city=%s&infoPetOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO -> "app/page/index2#marketDetails?city=%s&infoTradingMarketOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO -> "app/page/index1#rentDetails?city=%s&infoHouseRentSeekingOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> "app/page/index3#businessDetails?city=%s&infoBusinessTransferOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO -> "app/page/index3#bookDetails?city=%s&infoTextbookOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_REPAST_INFO -> "app/page/index4#RestaurantDetails?city=%s&infoRestaurantOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO -> "app/page/index4#yellowPageDetails?city=%s&yellowPageOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> "app/newPage/index#ActivityInfo?city=%s&activityInfoOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_FUND -> "app/newPage/index1#fundInfo?city=%s&fundInfoOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> "app/newPage/index#housingMarket?city=%s&housingMarketOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE,
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE,
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> "app/newPage/index#estateInfo?city=%s&estateInfoOid=%s&userOid=%s"
                else -> return
            }
//            NewsDetailsActivity.startNewsDetailsNewTask(context,FileUtil.getWebViewUrl(url, App.getInstance().cityName, commonOid, App.getInstance().userOid), commonOid, type, "")
            NewsDetailsActivityTo.startNewsDetailsNewTask(context,FileUtil.getWebViewUrl(url, App.getInstance().cityName, commonOid, App.getInstance().userOid), commonOid, type, "")

        }

        /**
         * 跳转生活修改
         * @param context
         */
        fun showLifeRelease(context: Context, type: Int, commonOid : String?) {
            if(commonOid.isNullOrEmpty()){
                return
            }
            when(type){
                LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO -> {
                    ReleaseHouseRentActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO -> {
                    ReleaseRecruitmentActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO -> {
                    ReleaseCarSaleActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO -> {
                    ReleasePetTransactionActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO -> {
                    ReleaseTransactionMarketActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO -> {
                    ReleaseHouseDemandActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> {
                    ReleaseBusinessTransferActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO -> {
                    ReleaseTextbookActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_REPAST_INFO -> {
                    ReleaseShopActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> {
                    ReleaseActivitysActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> {
                    ReleaseHousingMarketActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE -> {
                    ReleaseCivilEstateActivity.startModify(context, CivilEstateInfo.ESTATEINFO_TYPE1, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE -> {
                    ReleaseCivilEstateActivity.startModify(context, CivilEstateInfo.ESTATEINFO_TYPE2, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> {
                    ReleaseCivilEstateActivity.startModify(context, CivilEstateInfo.ESTATEINFO_TYPE3, commonOid)
                }
                else -> {
                    if(type>=LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO){
                        ReleaseYellowPageActivity.startModify(context, commonOid)
                    }
                }
            }
        }


//=====================================================================================================

        /**
         * 跳转财富详情
         * @param context
         */
        fun showWealthDetailsWeb(context: Context, type: Int?, commonOid: String?, commonTitle: String?, commonMainPhoto: String?) {

            val shareJsonInfo = ShareJsonInfo()
            shareJsonInfo.share_title = commonTitle
            shareJsonInfo.share_image = commonMainPhoto

            if(TextUtils.isEmpty(commonOid)){
                return
            }
            val url = when(type){
                LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO -> "app/page/index#HouseDetails?city=%s&infoHouseOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO -> "app/page/index#recruitDetails?city=%s&infoRecruitOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO -> "app/page/index1#carDetails?city=%s&infoCarOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO -> "app/page/index2#petsDetails?city=%s&infoPetOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO -> "app/page/index2#marketDetails?city=%s&infoTradingMarketOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO -> "app/page/index1#rentDetails?city=%s&infoHouseRentSeekingOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> "app/page/index3#businessDetails?city=%s&infoBusinessTransferOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO -> "app/page/index3#bookDetails?city=%s&infoTextbookOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_REPAST_INFO -> "app/page/index4#RestaurantDetails?city=%s&infoRestaurantOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO -> "app/page/index4#yellowPageDetails?city=%s&yellowPageOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> "app/newPage/index#ActivityInfo?city=%s&activityInfoOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_FUND -> "app/newPage/index1#fundInfo?city=%s&fundInfoOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> "app/newPage/index#housingMarket?city=%s&housingMarketOid=%s&userOid=%s"
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE,
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE,
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> "app/newPage/index#estateInfo?city=%s&estateInfoOid=%s&userOid=%s"
                else -> return
            }

            val finalUrl = FileUtil.getWebViewUrl(url, App.getInstance().cityName, commonOid, App.getInstance().userOid)
            shareJsonInfo.share_url = finalUrl
            val json = GsonUtil.toJson(shareJsonInfo)


//            NewsDetailsActivity.startNewsDetails(context, finalUrl, commonOid, type, json)

            NewsDetailsActivityTo.startNewsDetails(context, finalUrl, commonOid, type, json)
        }


        /**
         * 跳转财富修改
         * @param context
         */
        fun showWealthRelease(context: Context, type: Int, commonOid : String?) {
            if(commonOid.isNullOrEmpty()){
                return
            }
            when(type){
                LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO -> {
                    ReleaseHouseRentActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO -> {
                    ReleaseRecruitmentActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO -> {
                    ReleaseCarSaleActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO -> {
                    ReleasePetTransactionActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO -> {
                    ReleaseTransactionMarketActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO -> {
                    ReleaseHouseDemandActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> {
                    ReleaseBusinessTransferActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO -> {
                    ReleaseTextbookActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.LIFE_REPAST_INFO -> {
                    ReleaseShopActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> {
                    ReleaseActivitysActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> {
                    ReleaseHousingMarketActivity.startModify(context, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE -> {
                    ReleaseCivilEstateActivity.startModify(context, CivilEstateInfo.ESTATEINFO_TYPE1, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE -> {
                    ReleaseCivilEstateActivity.startModify(context, CivilEstateInfo.ESTATEINFO_TYPE2, commonOid)
                }
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> {
                    ReleaseCivilEstateActivity.startModify(context, CivilEstateInfo.ESTATEINFO_TYPE3, commonOid)
                }
                else -> {
                    if(type>=LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO){
                        ReleaseYellowPageActivity.startModify(context, commonOid)
                    }
                }
            }
        }

    }

}