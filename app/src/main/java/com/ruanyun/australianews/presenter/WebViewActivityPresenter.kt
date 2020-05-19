package com.ruanyun.australianews.presenter

import android.app.Activity
import android.text.TextUtils
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.RxPresenter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.NewsCommentCountInfo
import com.ruanyun.australianews.model.WeiXinInfo
import com.ruanyun.australianews.model.params.AddNewsBrowseTimeStatisticalParams
import com.ruanyun.australianews.model.params.NewsCommentParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.mvpview.WebViewActivityMvpView
import com.ruanyun.australianews.util.*
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject

/**
 * @description webView
 * @author hdl
 * @date 2018/11/27
 */
class WebViewActivityPresenter @Inject
constructor(apiService: ApiService) : RxPresenter<WebViewActivityMvpView>(apiService) {

    var startTime: String = DateUtil.getCurrentTime()
    private var thirdPayMgr: ThirdPayMgr? = null
    private var mWeChatApi: IWXAPI? = null

    var newsInfoOid: String? = null
    var newsInfoType: Int? = null

    /**
     * 支付宝支付业务
     */
    fun payV2(activity: Activity, paySign: String) {
        if (thirdPayMgr == null) {
            thirdPayMgr = ThirdPayMgr()
        }
        thirdPayMgr!!.alipay(activity, paySign, object : ThirdPayMgr.payCallback {
            override fun onPaySuccess() {
                mvpView.aliPaySuccess()
            }

            override fun onPayCancel() {
                mvpView.aliPayCancel()
            }

            override fun onPayFail(msg: String) {
                mvpView.payFail("支付失败")
            }
        })
    }

    /**
     * 微信支付业务
     */
    fun payWeChat(weiXinInfo: WeiXinInfo) {
        if (mWeChatApi == null) {
            mWeChatApi = WXAPIFactory.createWXAPI(App.getInstance().applicationContext, null)
        }
        val req = PayReq()
        //应用ID
        req.appId = ""
        //商户号
        req.partnerId = weiXinInfo.partnerId
        //预支付交易会话ID
        req.prepayId = weiXinInfo.prepayId
        //扩展字段
        req.packageValue = weiXinInfo.packageValue
        //随机字符串
        req.nonceStr = weiXinInfo.nonceStr
        //时间戳
        req.timeStamp = weiXinInfo.timeStamp
        //签名
        req.sign = weiXinInfo.sign
        mWeChatApi!!.sendReq(req)
    }

    override fun detachView() {
        super.detachView()
        if(CommonUtil.isNotEmpty(newsInfoOid) && newsInfoType == NewsCommentParams.NEWS){
            addNewsBrowseTimeStatistical()
        }
    }

    /**
     * 添加新闻浏览时间统计
     */
    fun addNewsBrowseTimeStatistical() {
        if (TextUtils.isEmpty(App.getInstance().userOid)) {
            return
        }
        val params = AddNewsBrowseTimeStatisticalParams()
        params.userOid = App.getInstance().userOid
        params.newsInfoOid = newsInfoOid
        params.startTime = startTime
        params.endTime = DateUtil.getCurrentTime()
        apiService.addNewsBrowseTimeStatistical(params)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                override fun onSuccess(result: ResultBase<*>) {
//                    mvpView.showToast(result.msg)
                }

                override fun onError(erroCode: Int, erroMsg: String) {
//                    mvpView.showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
//                    mvpView.showToast(msg)
                }
            })
    }

    /**
     * 获取新闻评论数和是否收藏信息
     */
    fun getNewsCommentCount() {
        if(newsInfoType?:1>0 && newsInfoType != LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO) {
            apiService.getNewsCommentCount(newsInfoOid, App.getInstance().userOid, newsInfoType)
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<NewsCommentCountInfo>>() {
                    override fun onSuccess(result: ResultBase<NewsCommentCountInfo>) {
                        mvpView.updateNewsInfo(result.data)
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        mvpView.showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        mvpView.showToast(msg)
                    }
                })
        }else {
            val type = when(newsInfoType){
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> 1
                LifeReleaseCommonUiModel.WEALTH_FUND -> 2
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE -> 3
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE -> 4
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> 5
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> 6
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> 7
                else -> 0
            }
            apiService.getWealthCommentCount(newsInfoOid, App.getInstance().userOid, type)
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<NewsCommentCountInfo>>() {
                    override fun onSuccess(result: ResultBase<NewsCommentCountInfo>) {
                        mvpView.updateNewsInfo(result.data)
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        mvpView.showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        mvpView.showToast(msg)
                    }
                })
        }
    }

}
