package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    private fun initView() {
        topbar.setTopBarClickListener(this)
        tv_gywm.clickWithTrigger {
            requestData("402881fb6a526fa1016a527229e70000")
        }
        tv_swhz.clickWithTrigger {
            requestData("402881fb6a6315f3016a631c7bc60000")
        }
        tv_tbmx.clickWithTrigger {
            requestData("402881fb6a62b93a016a62ee13130000")
        }
        tv_lxwm.clickWithTrigger {
            requestData("402881fb6a62b93a016a62ee6f640001")
        }
        tv_jrwm.clickWithTrigger {
            requestData("402881fb6a62b93a016a62ef25bc0002")
        }
        tv_mzsm.clickWithTrigger {
            requestData("402881fb6a62b93a016a62ef86570003")
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
