package com.ruanyun.australianews.data;

import com.ruanyun.australianews.base.PageInfoBase;
import com.ruanyun.australianews.base.ResultBase;
import com.ruanyun.australianews.model.*;
import com.ruanyun.australianews.model.params.*;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.*;
import rx.Observable;

/**
 * Description:
 * author: zhangsan on 2017/12/4 下午5:51.
 */

public interface ApiService {

    /**
     * 获取字典表信息
     **/
    @GET("app/sysDict/getAppSysDict")
    Observable<ResultBase<HashMap<String, List<ParentCodeInfo>>>> getParentCodeList();

    /**
     * 获取七牛上传token
     **/
    @POST("app/user/qx_token")
    Observable<ResultBase<String>> getQiNiuUpToken();

    /**
     * 上传图片
     **/
    @Multipart
    @POST("app/user/saveAppMainPhoto")
    Observable<ResultBase<String>> uploadImage(@PartMap HashMap<String, RequestBody> map);

    /**
     * 第三方是否存在
     **/
    @FormUrlEncoded
    @POST("app/login/thirdRegister")
    Observable<ResultBase<UserInfo>> thirdRegister(@Field("thirdNumber") String thirdNumber, @Field("thirdType") String thirdType);

    /**
     * 绑定第三方
     **/
    @POST("app/login/thirdBinding")
    Observable<ResultBase<UserInfo>> thirdBinding(@Body ThirdParams params);

    /**
     * 登录
     **/
    @POST("app/login/appLogin")
    Observable<ResultBase<UserInfo>> login(@Body LoginParams params);

    /**
     * 注册
     **/
    @POST("app/login/appRegistered")
    Observable<ResultBase<UserInfo>> registered(@Body RegisteredParams params);

    /**
     * 获取用户信息
     **/
    @FormUrlEncoded
    @POST("app/user/appUserPersonInfo")
    Observable<ResultBase<UserInfo>> getUserInfo(@Field("userOid") String userOid);

    /**
     * 获取验证码
     * type 类型  1 注册会员 2 找回密码
     **/
    @FormUrlEncoded
    @POST("app/login/sendMsg")
    Observable<ResultBase<String>> getSmsCode(@Field("phoneAreaCode") String phoneAreaCode, @Field("loginName") String loginName, @Field("type") String type);

    /**
     * 绑定手机号或邮箱
     */
    @POST("/app/login/appBindPhoneOrMailbox")
    Observable<ResultBase<UserInfo>> bindingLinkTel(@Body BindLinkTelParams params);

    /**
     * 找回密码
     **/
    @FormUrlEncoded
    @POST("app/login/appRetrievePwd")
    Observable<ResultBase> forgetPassword(@Field("phoneAreaCode") String phoneAreaCode, @Field("mobile") String mobile, @Field("loginPass") String loginPass, @Field("validate") String validate);

    /**
     * 修改密码
     **/
    @FormUrlEncoded
    @POST("app/external/{loginName}/user/forget_pass")
    Observable<ResultBase> modifyPassword(@Path("loginName") String loginName, @Field("oldPassWord") String oldPassWord, @Field("password") String password);

    /**
     * 修改用户信息
     **/
    @POST("app/user/appUpdateUser")
    Observable<ResultBase<UserInfo>> updateUserInfo(@Body UpdateUserInfoParams params);

    /**
     * 广告列表
     * type 1.首页轮播 2.首页中间插入图 3.坦克联盟轮播 4.坦克联盟中间图 5生活模块头部图 6 联盟商家图标  7生活模块中间图  8生活模块底部图  9个人中心底部图
     **/
    @FormUrlEncoded
    @POST("app/city/adver/typeList")
    Observable<ResultBase<HashMap<String, List<AdvertInfoBase>>>> getAdvertList(@Field("type") String type);

    /**
     * 开屏广告
     **/
    @GET("app/adverinfo/getAdverInfoAdvertisement")
    Observable<ResultBase<AdvertInfoBase>> getOpenScreenAdvert();


    /**
     * 全部城市列表
     **/
    @POST("app/cityInfo/getAppCityInfoList")
    Observable<ResultBase<List<CityInfo>>> getAllCityList();

    /**
     * 热门城市列表
     **/
    @POST("app/city/hotCityList")
    Observable<ResultBase<List<CityInfo>>> getPopularCityList();

    /**
     * 新闻列表
     **/
    @POST("app/newsinfo/getNewsInfoChannelInfoOid")
    Observable<ResultBase<PageInfoBase<NewsInfo>>> getNewsPageList(@Body NewsListParams params);

    /**
     * 新闻搜索列表
     **/
    @POST("app/newsinfo/getSearchNewsInfo")
    Observable<ResultBase<PageInfoBase<NewsInfo>>> getNewsSearchPageList(@Body NewsSearchListParams params);

    /**
     * 生活服务搜索列表
     **/
    @POST("app/generalInfo/getSearchGeneralInfo")
    Observable<ResultBase<PageInfoBase<LifeSearchResultInfo>>> getLifeSearchPageList(@Body LifeSearchListParams params);

    /**
     * 我的收藏新闻列表
     **/
    @POST("app/collectionInfo/getAppCollectionInfoPage")
    Observable<ResultBase<PageInfoBase<CollectionBrowseNewsInfo>>> getCollectionNewsPageList(@Body CollectionBrowseNewsLifeListParams params);

    /**
     * 我的收藏生活服务列表
     **/
    @POST("app/collectionInfo/getAppCollectionInfoPage")
    Observable<ResultBase<PageInfoBase<LifeBrowseCollectionResultInfo>>> getCollectionLifePageList(@Body CollectionBrowseNewsLifeListParams params);

    /**
     * 我的浏览历史新闻列表
     **/
    @POST("app/browseInfo/getAppBrowseInfoPage")
    Observable<ResultBase<PageInfoBase<CollectionBrowseNewsInfo>>> getBrowseRecordNewsPageList(@Body CollectionBrowseNewsLifeListParams params);

    /**
     * 我的浏览历史生活服务列表
     **/
    @POST("app/browseInfo/getAppBrowseInfoPage")
    Observable<ResultBase<PageInfoBase<LifeBrowseCollectionResultInfo>>> getBrowseRecordLifePageList(@Body CollectionBrowseNewsLifeListParams params);

    /**
     * 我的发布生活服务列表
     **/
    @POST("app/myReleaseController/getMyReleasePage")
    Observable<ResultBase<PageInfoBase<LifeGeneralInfo>>> getReleaseLifePageList(@Body MyReleaseLifeListParams params);

    /**
     * 黄页数据列表
     **/
    @POST("app/yellowPage/getAppYellowPagePage")
    Observable<ResultBase<PageInfoBase<LifeYellowPageInfo>>> getYellowPageList(@Body YellowPageListParams params);

    /**
     * 意见反馈
     **/
    @POST("app/feedback/addFeedBackInfo")
    Observable<ResultBase> addFeedback(@Body FeedbackParams params);

    /**
     * 消息通知列表
     **/
    @POST("app/noticeInfo/getAppNoticeInfo")
    Observable<ResultBase<PageInfoBase<NotificationInfo>>> getNotificationList(@Body NotificationListParams params);

    /**
     * 首页信息
     **/
    @FormUrlEncoded
    @POST("app/newsinfo/getFirstPageInfo")
    Observable<ResultBase<HomeResultBase>> getHomeResultBase(@Field("userOid") String userOid, @Field("city") String city);

    /**
     * 频道信息
     **/
    @FormUrlEncoded
    @POST("app/channelRelation/getChannelRelation")
    Observable<ResultBase<ChannelResult>> getChannelList(@Field("userOid") String userOid);

    /**
     * 修改我的频道信息
     **/
    @FormUrlEncoded
    @POST("app/channelRelation/saveAppChannelRelation")
    Observable<ResultBase> updateChannelList(@Field("userOid") String userOid, @Field("strJson") String strJson);

    /**
     * 添加自定义频道
     **/
    @FormUrlEncoded
    @POST("app/customChannel/saveAppCustomChannel")
    Observable<ResultBase<ChannelInfo>> addChannelInfo(@Field("userOid") String userOid, @Field("title") String title);

    /**
     * 我的评价列表
     **/
    @POST("app/commentInfo/getAppCommentInfoUserPage")
    Observable<ResultBase<PageInfoBase<Evaluation>>> getEvaluationPageList(@Body EvaluationListParams params);

    /**
     * 添加收藏
     * type 类型  1新闻  2房屋出租  3招聘信息  4汽车买卖  5宠物交易  6交易市场  7房屋求租  8生意转让  9教科书
     **/
    @FormUrlEncoded
    @POST("app/collectionInfo/saveAppCollectionInfo")
    Observable<ResultBase<CollectionBrowseNewsInfo>> addFavorites(@Field("userOid") String userOid, @Field("commonOid") String commonOid, @Field("type") String type);

    /**
     * 删除我的收藏
     **/
    @POST("app/collectionInfo/deleteAppCollectionInfo")
    Observable<ResultBase> deleteCollectionInfo(@Body DeleteCollectionParams params);

    /**
     * 删除我的浏览
     **/
    @POST("app/browseInfo/deleteAppBrowseInfoOid")
    Observable<ResultBase> deleteBrowseInfo(@Body DeleteBrowseParams params);

    /**
     * 删除我的发布
     **/
    @FormUrlEncoded
    @POST("app/myReleaseController/deleteMyRelease")
    Observable<ResultBase> deleteMyRelease(@Field("releaseJson") String releaseJson);

    /**
     * 删除我的评论
     * oid 单个或多个评论删除，中间用逗号
     * userOid 当前登录人 （当类型为清空时，必传）
     * type 是否清空 1清空  2不清空
     **/
    @FormUrlEncoded
    @POST("app/commentInfo/deleteCommentInfo")
    Observable<ResultBase> deleteCommentInfo(@Field("oid") String oid,@Field("userOid") String userOid,@Field("type") Integer type);

    /**
     * 新闻浏览记录
     **/
    @POST("app/newsInfoRecord/saveNewsInfoRecord")
    Observable<ResultBase> addNewsBrowseTimeStatistical(@Body AddNewsBrowseTimeStatisticalParams params);

    /**
     * 我的新闻推送记录列表
     **/
    @POST("app/pushInfo/getAppPushInfo")
    Observable<ResultBase<PageInfoBase<PushRecordNewsInfo>>> getPushRecordNewsPageList(@Body NewsPushListParams params);


    /**
     * 发布黄页
     **/
    @POST("app/yellowPage/saveAppYellowPage")
    Observable<ResultBase<LifeYellowPageInfo>> releaseYellowPage(@Body ReleaseYellowPageParams params);

    /**
     * 获取黄页信息
     **/
    @FormUrlEncoded
    @POST("app/yellowPage/getAppYellowPage")
    Observable<ResultBase<LifeYellowPageInfo>> getYellowPageInfo(@Field("userOid") String userOid, @Field("yellowPageOid") String yellowPageOid);

    /**
     * 常见黄页类别
     **/
    @POST("app/yellowPageType/getAppYellowPageTypeIsCommon")
    Observable<ResultBase<List<IconInfo>>> getCommonYellowPageIconList();

    /**
     * 热门服务黄页类别
     **/
    @POST("app/yellowPageType/getAppYellowPageTypeIsHot")
    Observable<ResultBase<List<IconInfo>>> getPopularServiceYellowPageIconList();

    /**
     * 获取所有黄页类别
     **/
    @POST("app/yellowPageType/getAppYellowPageTypeAll")
    Observable<ResultBase<List<HashMap<String, Object>>>> getAllYellowPageIconList();


    /**
     * 生活-房屋出租列表
     **/
    @POST("app/infoHouse/getAppInfoHousePage")
    Observable<ResultBase<PageInfoBase<LifeHouseRentInfo>>> getLifeHouseRentList(@Body LifeHouseRentParams params);

    /**
     * 生活-招聘信息列表
     **/
    @POST("app/infoRecruit/getAppInfoRecruitPage")
    Observable<ResultBase<PageInfoBase<LifeRecruitmentInfo>>> getLifeRecruitmentList(@Body LifeRecruitmentParams params);

    /**
     * 生活-汽车买卖列表
     **/
    @POST("app/infoCar/getAppInfoCarPage")
    Observable<ResultBase<PageInfoBase<LifeCarSaleInfo>>> getLifeCarSaleList(@Body LifeCarSaleParams params);

    /**
     * 生活-宠物交易列表
     **/
    @POST("app/infoPet/getAppInfoPetPage")
    Observable<ResultBase<PageInfoBase<LifePetTransactionInfo>>> getLifePetTransactionList(@Body LifePetTransactionParams params);

    /**
     * 生活-交易市场列表
     **/
    @POST("app/infoTradingMarket/getAppInfoTradingMarketPage")
    Observable<ResultBase<PageInfoBase<LifeTransactionMarketInfo>>> getLifeTransactionMarketList(@Body LifeTransactionMarketParams params);

    /**
     * 生活-房屋求租列表
     **/
    @POST("app/infoHouseRentSeeking/getAppInfoHouseRentSeekingPage")
    Observable<ResultBase<PageInfoBase<LifeHouseDemandInfo>>> getLifeHouseDemandList(@Body LifeHouseDemandParams params);

    /**
     * 生活-生意转让列表
     **/
    @POST("app/infoBusinessTransfer/getAppInfoHouseRentSeekingPage")
    Observable<ResultBase<PageInfoBase<LifeBusinessTransferInfo>>> getLifeBusinessTransferList(@Body LifeBusinessTransferParams params);

    /**
     * 生活-教科书列表
     **/
    @POST("app/infoTextbook/getAppInfoTextbookPage")
    Observable<ResultBase<PageInfoBase<LifeTextbookInfo>>> getLifeTextbookList(@Body LifeTextbookParams params);

    /**
     * 生活-餐饮美食列表
     **/
    @POST("app/infoRestaurant/getAppInfoRestaurantPage")
    Observable<ResultBase<PageInfoBase<LifeFoodShopInfo>>> getLifeShopList(@Body LifeShopParams params);


    /**
     * 发布房屋出租
     **/
    @POST("app/infoHouse/saveInfoHouse")
    Observable<ResultBase<LifeHouseRentInfo>> releaseHouseRent(@Body ReleaseLifeHouseRentParams params);

    /**
     * 发布招聘信息
     **/
    @POST("app/infoRecruit/saveAppInfoRecruit")
    Observable<ResultBase<LifeRecruitmentInfo>> releaseRecruitment(@Body ReleaseLifeRecruitmentParams params);

    /**
     * 发布汽车买卖
     **/
    @POST("app/infoCar/saveAppInfoCar")
    Observable<ResultBase<LifeCarSaleInfo>> releaseCarSale(@Body ReleaseLifeCarSaleParams params);

    /**
     * 发布宠物交易
     **/
    @POST("app/infoPet/saveAppInfoPet")
    Observable<ResultBase<LifePetTransactionInfo>> releasePetTransaction(@Body ReleaseLifePetTransactionParams params);

    /**
     * 发布交易市场
     **/
    @POST("app/infoTradingMarket/saveAppInfoTradingMarket")
    Observable<ResultBase<LifeCarSaleInfo>> releaseTransactionMarket(@Body ReleaseLifeTransactionMarketParams params);

    /**
     * 发布房屋求租
     **/
    @POST("app/infoHouseRentSeeking/saveAppInfoHouseRentSeeking")
    Observable<ResultBase<LifeHouseDemandInfo>> releaseTransactionMarket(@Body ReleaseLifeHouseDemandParams params);

    /**
     * 发布生意转让
     **/
    @POST("app/infoBusinessTransfer/saveAppInfoBusinessTransfer")
    Observable<ResultBase<LifeBusinessTransferInfo>> releaseBusinessTransfer(@Body ReleaseLifeBusinessTransferParams params);

    /**
     * 发布教科书
     **/
    @POST("app/infoTextbook/saveInfoTextbook")
    Observable<ResultBase<LifeTextbookInfo>> releaseTextbook(@Body ReleaseLifeTextbookParams params);

    /**
     * 发布餐饮美食
     **/
    @POST("app/infoRestaurant/saveAppInfoRestaurant")
    Observable<ResultBase<LifeFoodShopInfo>> releaseShop(@Body ReleaseLifeShopParams params);

    /**
     * 房屋出租详情
     **/
    @FormUrlEncoded
    @POST("app/infoHouse/getInfoHouse")
    Observable<ResultBase<LifeHouseRentInfo>> getLifeHouseRentInfo(@Field("userOid") String userOid, @Field("infoHouseOid") String infoHouseOid);

    /**
     * 房屋求租详情
     **/
    @FormUrlEncoded
    @POST("app/infoHouseRentSeeking/getAppInfoHouseRentSeeking")
    Observable<ResultBase<LifeHouseDemandInfo>> getLifeHouseDemandInfo(@Field("userOid") String userOid, @Field("infoHouseRentSeekingOid") String infoHouseRentSeekingOid);

    /**
     * 交易市场详情
     **/
    @FormUrlEncoded
    @POST("/app/infoTradingMarket/getAppInfoTradingMarket")
    Observable<ResultBase<LifeTransactionMarketInfo>> getLifeTransactionMarketInfo(@Field("userOid") String userOid, @Field("infoTradingMarketOid") String infoTradingMarketOid);

    /**
     * 招聘信息详情
     **/
    @FormUrlEncoded
    @POST("app/infoRecruit/getAppInfoRecruit")
    Observable<ResultBase<LifeRecruitmentInfo>> getLifeRecruitmentInfo(@Field("userOid") String userOid, @Field("infoRecruitOid") String infoRecruitOid);

    /**
     * 宠物交易详情
     **/
    @FormUrlEncoded
    @POST("app/infoPet/getAppInfoPet")
    Observable<ResultBase<LifePetTransactionInfo>> getLifePetTransactionInfo(@Field("userOid") String userOid, @Field("infoPetOid") String infoPetOid);

    /**
     * 生意转让详情
     **/
    @FormUrlEncoded
    @POST("app/infoBusinessTransfer/getAppInfoBusinessTransfer")
    Observable<ResultBase<LifeBusinessTransferInfo>> getLifeBusinessTransferInfo(@Field("userOid") String userOid, @Field("infoBusinessTransferOid") String infoBusinessTransferOid);

    /**
     * 汽车买卖详情
     **/
    @FormUrlEncoded
    @POST("app/infoCar/getAppInfoCar")
    Observable<ResultBase<LifeCarSaleInfo>> getLifeCarSaleInfo(@Field("userOid") String userOid, @Field("infoCarOid") String infoCarOid);

    /**
     * 教科书详情
     **/
    @FormUrlEncoded
    @POST("app/infoTextbook/getInfoTextbook")
    Observable<ResultBase<LifeTextbookInfo>> getLifeTextbookInfo(@Field("userOid") String userOid, @Field("infoTextbookOid") String infoTextbookOid);

    /**
     * 餐饮美食详情
     **/
    @FormUrlEncoded
    @POST("app/infoRestaurant/getAppInfoRestaurant")
    Observable<ResultBase<LifeFoodShopInfo>> getLifeFoodShopInfo(@Field("userOid") String userOid, @Field("infoRestaurantOid") String infoRestaurantOid);

    /**
     * 获取店铺商品详情
     **/
    @FormUrlEncoded
    @POST("app/infoFood/getAppInfoFood")
    Observable<ResultBase<GoodsInfo>> getLifeShopGoodsInfo(@Field("infoFoodOid") String infoFoodOid);

    /**
     * 添加餐饮美食商品信息
     **/
    @POST("app/infoFood/saveAppInfoFood")
    Observable<ResultBase> releaseShopGoods(@Body ReleaseLifeShopGoodsParams params);

    /**
     * 获取协议信息
     * 关于我们：402881fb6a526fa1016a527229e70000
     * 特别鸣谢：402881fb6a62b93a016a62ee13130000
     * 联系我们：402881fb6a62b93a016a62ee6f640001
     * 加入我们：402881fb6a62b93a016a62ef25bc0002
     * 负责声明：402881fb6a62b93a016a62ef86570003
     * 商务合作：402881fb6a6315f3016a631c7bc60000
     * 注册协议：402881fb6a62b93a016a62ef86570004
     * 隐私协议：402881fb6a6315f3016a631c7bc60005
     **/
    @FormUrlEncoded
    @POST("app/commonProblem/getAppCommonProblem")
    Observable<ResultBase<HelpInfo>> getAppCommonProblem(@Field("commonProblemOid") String commonProblemOid);

    /**
     * 获取新闻详情
     **/
    @FormUrlEncoded
    @POST("app/newsinfo/getNewsInfoOid")
    Observable<ResultBase<NewsInfo>> getNewsDetails(@Field("newsInfoOid") String newsInfoOid);

    /**
     * 获取新闻评论数和是否收藏信息
     **/
    @FormUrlEncoded
    @POST("app/newsinfo/getNewsInfoCommentFavorite")
    Observable<ResultBase<NewsCommentCountInfo>> getNewsCommentCount(@Field("newsInfoOid") String newsInfoOid, @Field("userOid") String userOid, @Field("type") Integer type);

    /**
     * 添加评论
     * type 类型  1新闻  2房屋出租  3招聘信息  4汽车买卖  5宠物交易  6交易市场  7房屋求租  8生意转让  9教科书
     **/
    @FormUrlEncoded
    @POST("app/commentInfo/saveAppCommentInfo")
    Observable<ResultBase> addComment(@Field("commonOid") String commonOid, @Field("userOid") String userOid, @Field("replyMsg") String replyMsg, @Field("type") String type);

    /**
     * 添加回复
     * commentInfoOid 评论对象的oid
     * fromUserOid 被回复人用户oid
     * userOid 回复人用户oid
     * replyMsg 回复内容
     **/
    @FormUrlEncoded
    @POST("app/replyInfo/saveAppReplyInfo")
    Observable<ResultBase> addReply(@Field("commentInfoOid") String commentInfoOid, @Field("fromUserOid") String fromUserOid, @Field("userOid") String userOid, @Field("replyMsg") String replyMsg);

    /**
     * 添加财富回复
     * commentInfoOid 评论对象的oid
     * fromUserOid 被回复人用户oid
     * userOid 回复人用户oid
     * replyMsg 回复内容
     **/
    @FormUrlEncoded
    @POST("app/newReplyInfo/saveAppReplyInfo")
    Observable<ResultBase> addWealthReply(@Field("commentInfoOid") String commentInfoOid, @Field("fromUserOid") String fromUserOid, @Field("userOid") String userOid, @Field("replyMsg") String replyMsg);


    /**
     * 单个新闻评论分页信息
     **/
    @POST("app/commentInfo/getAppCommentInfoPage")
    Observable<ResultBase<PageInfoBase<CommentInfo>>> getAppCommentInfoPage(@Body NewsCommentParams params);


    /**
     * 新闻评论回复二级分页信息
     **/
    @POST("app/replyInfo/getAppReplyInfoLoadMore")
    Observable<ResultBase<PageInfoBase<CommentInfo>>> getSecondaryAppCommentInfoPage(@Body NewsCommentSecondaryParams params);

    /**
     * 财富-基金列表
     **/
    @POST("app/fundInfo/getAppFundInfoPage")
    Observable<ResultBase<PageInfoBase<FundInfo>>> getWealthFundPageList(@Body WealthCommonListParams params);

    /**
     * 活动列表
     **/
    @POST("app/activityInfo/getAppActivityInfoPage")
    Observable<ResultBase<PageInfoBase<ActivitysInfo>>> getActivitysPageList(@Body WealthCommonListParams params);

    /**
     * 发布活动
     **/
    @POST("app/activityInfo/saveAppActivityInfo")
    Observable<ResultBase<ActivitysInfo>> releaseActivitysInfo(@Body ReleaseWealthActivitysParams params);

    /**
     * 活动详情
     **/
    @FormUrlEncoded
    @POST("app/activityInfo/getAppActivityInfo")
    Observable<ResultBase<ActivitysInfo>> getActivitysInfo(@Field("userOid") String userOid, @Field("oid") String oid);

    /**
     * 房市列表
     **/
    @POST("app/housingMarket/getAppHousingMarketPage")
    Observable<ResultBase<PageInfoBase<HousingMarketInfo>>> getHousingMarketPageList(@Body WealthCommonListParams params);

    /**
     * 发布房市
     **/
    @POST("app/housingMarket/saveAppHousingMarket")
    Observable<ResultBase<HousingMarketInfo>> releaseHousingMarketInfo(@Body ReleaseWealthHousingMarketParams params);

    /**
     * 房市详情
     **/
    @FormUrlEncoded
    @POST("app/housingMarket/getAppHousingMarket")
    Observable<ResultBase<HousingMarketInfo>> getHousingMarketInfo(@Field("userOid") String userOid, @Field("oid") String oid);

    /**
     * 民用地产列表
     **/
    @POST("app/estateInfo/getAppEstateInfoPage")
    Observable<ResultBase<PageInfoBase<CivilEstateInfo>>> getCivilEstatePageList(@Body WealthCommonListParams params);

    /**
     * 发布民用地产
     **/
    @POST("app/estateInfo/saveAppEstateInfo")
    Observable<ResultBase<CivilEstateInfo>> releaseCivilEstateInfo(@Body ReleaseWealthCivilEstateParams params);

    /**
     * 民用地产详情
     **/
    @FormUrlEncoded
    @POST("app/estateInfo/getAppEstateInfo")
    Observable<ResultBase<CivilEstateInfo>> getCivilEstateInfo(@Field("userOid") String userOid, @Field("oid") String oid);

    /**
     * 添加财富收藏
     * @param userOid 收藏人oid
     * @param commonOid 收藏对象oid
     * @param type 类型(1活动 2基金 3民用 4商业 5农场 6房市 7生意)
     **/
    @FormUrlEncoded
    @POST("app/favoriteInfo/saveAppFavoriteInfo")
    Observable<ResultBase> addWealthFavorites(@Field("userOid") String userOid, @Field("commonOid") String commonOid, @Field("type") Integer type);

    /**
     * 删除财富收藏
     * @param userOid 收藏人oid
     * @param commonOid 收藏对象oid
     * @param type 类型(1活动 2基金 3民用 4商业 5农场 6房市 7生意)
     **/
    @FormUrlEncoded
    @POST("app/favoriteInfo/deleteAppCollectionInfo")
    Observable<ResultBase<CollectionBrowseNewsInfo>> deleteWealthFavorites(@Field("userOid") String userOid, @Field("commonOid") String commonOid, @Field("type") Integer type);

    /**
     * 获取财富评论数和是否收藏信息
     * @param type 类型(1活动 2基金 3民用 4商业 5农场 6房市 7生意)
     **/
    @FormUrlEncoded
    @POST("app/favoriteInfo/getDetailsCommentFavorite")
    Observable<ResultBase<NewsCommentCountInfo>> getWealthCommentCount(@Field("detailsOid") String detailsOid, @Field("userOid") String userOid, @Field("type") Integer type);

    /**
     * 添加财富评论
     * @param type 类型(1活动 2基金 3民用 4商业 5农场 6房市 7生意)
     **/
    @FormUrlEncoded
    @POST("app/newCommentInfo/saveAppNewCommentInfo")
    Observable<ResultBase<NewsCommentCountInfo>> addWealthComment(@Field("commonOid") String commonOid, @Field("userOid") String userOid, @Field("replyMsg") String replyMsg, @Field("type") Integer type);

    /**
     * 单个财富评论分页信息
     **/
    @POST("app/newCommentInfo/getAppNewCommentInfoPage")
    Observable<ResultBase<PageInfoBase<CommentInfo>>> getWealthAppCommentInfoPage(@Body NewsCommentParams params);

    /**
     * 单个财富二级评论分页信息
     **/
    @POST("app/newReplyInfo/getAppReplyInfoLoadMore")
    Observable<ResultBase<PageInfoBase<CommentInfo>>> getWealthSecondaryAppCommentInfoPage(@Body NewsCommentSecondaryParams params);

    /**
     *  财富-我的浏览
     **/
    @POST("app/historyInfo/getAppHistoryInfoPage")
    Observable<ResultBase<PageInfoBase<WealthBrowseCollectionResultInfo>>> getWealthBrowsePageList(@Body UserOidPageParams params);

    /**
     *  财富-我的收藏
     **/
    @POST("app/favoriteInfo/getAppFavoriteInfoPage")
    Observable<ResultBase<PageInfoBase<WealthBrowseCollectionResultInfo>>> getWealthCollectionPageList(@Body UserOidPageParams params);

    /**
     *  财富-最新发布列表
     **/
    @POST("app/intermediateInfo/getSearchIntermediateInfo")
    Observable<ResultBase<PageInfoBase<WealthBrowseCollectionResultInfo>>> getWealthNewestPageList(@Body UserOidPageParams params);

    /**
     *  财富-我的发布
     **/
    @POST("app/myReleaseController/getNewMyReleasePage")
    Observable<ResultBase<PageInfoBase<WealthBrowseCollectionResultInfo>>> getWealthReleasePageList(@Body UserOidPageParams params);

    /**
     * 删除我的财富浏览
     **/
    @POST("app/historyInfo/deleteAppHistoryInfoOid")
    Observable<ResultBase> deleteWealthBrowseInfo(@Body DeleteWealthBrowseCollectParams params);

    /**
     * 删除我的财富收藏
     **/
    @POST("app/favoriteInfo/deleteAppFavoriteInfoOid")
    Observable<ResultBase> deleteWealthCollectInfo(@Body DeleteWealthBrowseCollectParams params);

    /**
     * 删除我的财富发布
     **/
    @FormUrlEncoded
    @POST("app/myReleaseController/deleteNewMyRelease")
    Observable<ResultBase> deleteMyWealthRelease(@Field("releaseJson") String releaseJson);

}
