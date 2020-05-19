package com.ruanyun.australianews.presenter

import android.text.TextUtils
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.RxPresenter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.mvpview.ForgetPasswordMvpView
import com.ruanyun.australianews.util.MD5
import com.ruanyun.australianews.util.RxUtil
import javax.inject.Inject

/**
 * @description 忘记密码
 * @author hdl
 * @date 2019/3/4
 */
class ForgetPasswordPresenter @Inject
constructor(apiService: ApiService) : RxPresenter<ForgetPasswordMvpView>(apiService){

    /**
     * 获取短信验证码
     */
    fun getSmsCode(areaCode: String, loginName: String) {
        if (TextUtils.isEmpty(loginName)) {
            mvpView.showToast("请输入手机号码")
            return
        }

        mvpView.showLoadingView(R.string.in_load)
        val subscription = apiService.getSmsCode(areaCode, loginName, "2")
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<String>>() {
                override fun onSuccess(result: ResultBase<String>) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(result.data)
                    mvpView.getVCodeSuccess()
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(msg)
                }
            })
        addSubscrebe(subscription)
    }

    /**
     * 忘记密码
     */
    fun forgetPassword(areaCode: String, loginName: String, validate: String, loginPass: String) {
        if (TextUtils.isEmpty(loginName)) {
            mvpView.showToast("请输入手机号")
            return
        }
        if (TextUtils.isEmpty(loginPass)) {
            mvpView.showToast("请输入密码")
            return
        }
        if (loginPass.length<6) {
            mvpView.showToast("密码最少6位")
            return
        }
        if (TextUtils.isEmpty(validate)) {
            mvpView.showToast("请输入短信验证码")
            return
        }
        mvpView.showLoadingView(R.string.in_submit)
        val subscription = apiService.forgetPassword(areaCode, loginName, MD5.md5(loginPass), validate)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                override fun onSuccess(result: ResultBase<*>) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(result.msg)
                    mvpView.forgetPasswordSuccess()
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(msg)
                }
            })
        addSubscrebe(subscription)
    }


}
