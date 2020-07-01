package com.ruanyun.australianews.presenter

import android.text.TextUtils
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.RxPresenter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.model.params.LoginParams
import com.ruanyun.australianews.mvpview.LoginMvpView
import com.ruanyun.australianews.util.CacheHelper
import com.ruanyun.australianews.util.MD5
import com.ruanyun.australianews.util.RxUtil
import javax.inject.Inject

/**
 * @description 登录
 * @author hdl
 * @date 2019/3/4
 */
class LoginPresenter @Inject
constructor(apiService: ApiService) : RxPresenter<LoginMvpView>(apiService){


    /**
     * 登录
     */
    fun login(areaCode: String, loginName: String, loginMailbox: String, loginPass: String, isPhoneLogin: Boolean) {
        if (isPhoneLogin && TextUtils.isEmpty(loginName)) {
            mvpView.showToast("请输入手机号")
            return
        }
        if (!isPhoneLogin && TextUtils.isEmpty(loginMailbox)) {
            mvpView.showToast("请输入邮箱地址")
            return
        }
        if (TextUtils.isEmpty(loginPass)) {
            mvpView.showToast("请输入登录密码")
            return
        }
        val params = LoginParams()
        params.loginPass = MD5.md5(loginPass)
        if(isPhoneLogin){
            params.phoneAreaCode = areaCode
            params.loginName = loginName
        }else {
            params.loginName = loginMailbox
        }
        mvpView.showLoadingView(R.string.in_login)
        val subscription = apiService.login(params)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                override fun onSuccess(result: ResultBase<UserInfo>) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(result.msg)
                    CacheHelper.getInstance().loginRegisteredSuccess(result.data, isPhoneLogin, params.loginName)
                    mvpView.loginSuccess()
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
     * 登录
     */
    fun thridLogin(thirdNumber: String, thirdType: String) {

        mvpView.showLoadingView(R.string.in_login)
        val subscription = apiService.thirdRegister(thirdNumber,thirdType)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                override fun onSuccess(result: ResultBase<UserInfo>) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(result.msg)
                    CacheHelper.getInstance().loginRegisteredSuccess(result.data, false, "")
                    mvpView.loginSuccess()
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(erroMsg)

                    if (erroCode==-2) {
//                        TODO("绑定")
                        mvpView.bind()
                    }

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
