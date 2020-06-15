package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.model.HelpInfo
import com.ruanyun.australianews.ui.WebViewActivity
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.util.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_about_us_list.*

/**
 * @author hdl
 * @description 关于我们
 * @date 2019/10/30
 */
class AboutUsListActivity : BaseActivity() {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_about_us_list)
        initView()
    }

//    /info/goumai.html
//    var httpip="http://afn.resolr.com"

    private fun initView() {
        topbar.setTopBarClickListener(this)

        val yuyan = SharedPreferencesUtils.getParam(
            App.context,
            SharedPreferencesUtils.KEY_SYSTEM_LANGUAGE,
            ""
        ) as String

        tv_gywm.clickWithTrigger {
            //    关于我们 /info/guanyu.html------------------英文版 /info/guanyu_en.html
            if ("en" == yuyan) {
                WebViewActivity.startHtml(mContext, "About us", "http://afn.resolr.com/info/guanyu_en.html")
            }else{
                WebViewActivity.startHtml(mContext, "关于我们", "http://afn.resolr.com/info/guanyu.html")
            }
//            requestData("402881fb6a526fa1016a527229e70000")
        }
        tv_swhz.clickWithTrigger {
            //    商务合作 /info/shangwu.html------------------英文版 /info/shangwu_en.html
            if ("en" == yuyan) {
                WebViewActivity.startHtml(mContext, "Business Cooperation", "http://afn.resolr.com/info/shangwu_en.html")
            }else{
                WebViewActivity.startHtml(mContext, "商务合作", "http://afn.resolr.com/info/shangwu.html")
            }
//            requestData("402881fb6a6315f3016a631c7bc60000")
        }
        tv_tbmx.clickWithTrigger {
            //    用户协议 /info/xieyi.html------------------英文版 /info/xieyi_en.html
            if ("en" == yuyan) {
                WebViewActivity.startHtml(mContext, "特别鸣谢", "http://afn.resolr.com/info/xieyi_en.html")
            }else{
                WebViewActivity.startHtml(mContext, "特别鸣谢", "http://afn.resolr.com/info/xieyi.html")
            }
//            requestData("402881fb6a62b93a016a62ee13130000")
        }
        tv_lxwm.clickWithTrigger {
            //    联系我们 /info/lianxi.html------------------英文版 /info/lianxi_en.html
            if ("en" == yuyan) {
                WebViewActivity.startHtml(mContext, "Contact Us", "http://afn.resolr.com/info/lianxi_en.html")
            }else{
                WebViewActivity.startHtml(mContext, "联系我们", "http://afn.resolr.com/info/lianxi.html")
            }
//            requestData("402881fb6a62b93a016a62ee6f640001")
        }
        tv_jrwm.clickWithTrigger {
            //    购买须知 /info/goumai.html
            if ("en" == yuyan) {
                WebViewActivity.startHtml(mContext, "加入我们", "http://afn.resolr.com/info/goumai.html")
            }else{
                WebViewActivity.startHtml(mContext, "加入我们", "http://afn.resolr.com/info/goumai.html")
            }
//            requestData("402881fb6a62b93a016a62ef25bc0002")
        }
        tv_mzsm.clickWithTrigger {
            //    免责说明 /info/mianze.html------------------英文版 /info/mianze_en.html
            if ("en" == yuyan) {
                WebViewActivity.startHtml(mContext, "Disclaimer", "http://afn.resolr.com/info/mianze_en.html")
            }else{
                WebViewActivity.startHtml(mContext, "免责说明", "http://afn.resolr.com/info/mianze.html")
            }
//            requestData("402881fb6a62b93a016a62ef86570003")
        }
    }

    /**
     * 获取协议信息
     */
    private fun requestData(oid:String) {
        showLoading()
        val subscribe = ApiManger.getApiService().getAppCommonProblem(oid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<HelpInfo>>() {
                override fun onSuccess(result: ResultBase<HelpInfo>) {
                    disMissLoadingView()
                    WebViewActivity.startHtml(mContext, result.data.title, result.data.content)
                }

                override fun onError(errorCode: Int, errorMsg: String) {
                    disMissLoading()
                    showToast(errorMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    disMissLoading()
                    showToast(msg)
                }
            })
        addSubscrebe(subscribe)
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, AboutUsListActivity::class.java)
            context.startActivity(starter)
        }
    }

}
