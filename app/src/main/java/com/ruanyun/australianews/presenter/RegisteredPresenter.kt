package com.ruanyun.australianews.presenter

import android.text.TextUtils
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.RxPresenter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.model.params.RegisteredParams
import com.ruanyun.australianews.model.params.ThirdParams
import com.ruanyun.australianews.mvpview.RegisteredMvpView
import com.ruanyun.australianews.util.CacheHelper
import com.ruanyun.australianews.util.MD5
import com.ruanyun.australianews.util.RxUtil
import javax.inject.Inject

/**
 * @description 注册
 * @author hdl
 * @date 2019/3/4
 */
class RegisteredPresenter @Inject
constructor(apiService: ApiService) : RxPresenter<RegisteredMvpView>(apiService){

    /**
     * 获取短信验证码
     */
    fun getSmsCode(areaCode: String, loginName: String) {
        if (TextUtils.isEmpty(loginName)) {
            mvpView.showToast("请输入手机号码")
            return
        }

        mvpView.showLoadingView(R.string.in_load)
        val subscription = apiService.getSmsCode(areaCode, loginName, "1")
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
     * 注册
     */
    fun register(areaCode: String, loginName: String, loginMailbox: String, validate: String, loginPass: String, loginConfirmPass: String, isPhoneRegister: Boolean) {
        if (isPhoneRegister && TextUtils.isEmpty(loginName)) {
            mvpView.showToast("请输入手机号")
            return
        }
        if (!isPhoneRegister && TextUtils.isEmpty(loginMailbox)) {
            mvpView.showToast("请输入邮箱地址")
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
        if (loginPass != loginConfirmPass) {
            mvpView.showToast("两次密码不一致")
            return
        }
        if (isPhoneRegister && TextUtils.isEmpty(validate)) {
            mvpView.showToast("请输入短信验证码")
            return
        }

        val params = RegisteredParams()
        params.loginPass = MD5.md5(loginPass)
        if(isPhoneRegister){
            params.phoneAreaCode = areaCode
            params.loginName = loginName
            params.type = 1
            params.validate = validate
        }else {
            params.loginName = loginMailbox
            params.type = 2
        }

        mvpView.showLoadingView(R.string.in_submit)
        val subscription = apiService.registered(params)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                override fun onSuccess(result: ResultBase<UserInfo>) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(result.msg)
                    CacheHelper.getInstance().loginRegisteredSuccess(result.data, isPhoneRegister,  params.loginName)
                    mvpView.registeredSuccess()
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
     * 绑定
     */
    fun thirdBinding(areaCode: String, loginName: String, loginMailbox: String, validate: String, loginPass: String, loginConfirmPass: String, isPhoneRegister: Boolean) {
        if (isPhoneRegister && TextUtils.isEmpty(loginName)) {
            mvpView.showToast("请输入手机号")
            return
        }
        if (!isPhoneRegister && TextUtils.isEmpty(loginMailbox)) {
            mvpView.showToast("请输入邮箱地址")
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
        if (loginPass != loginConfirmPass) {
            mvpView.showToast("两次密码不一致")
            return
        }
        if (isPhoneRegister && TextUtils.isEmpty(validate)) {
            mvpView.showToast("请输入短信验证码")
            return
        }

        val params = ThirdParams()
        params.avatar= avatar
        params.thirdNumber = this.thirdNumber
        params.thirdType = this.thirdType
        params.nickName = this.nickName

        if(isPhoneRegister){
            params.phoneAreaCode = areaCode
            params.linkTel = loginName
        }else {
            params.linkTel = loginMailbox
        }
        params.loginPass = MD5.md5(loginPass)
        mvpView.showLoadingView(R.string.in_submit)
        val subscription = apiService.thirdBinding(params)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                override fun onSuccess(result: ResultBase<UserInfo>) {
                    mvpView.disMissLoadingView()
                    mvpView.showToast(result.msg)
                    CacheHelper.getInstance().loginRegisteredSuccess(result.data, isPhoneRegister,  loginName)
                    mvpView.registeredSuccess()
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

    var thirdNumber:String ?=null
    var avatar:String ?=null
    var nickName:String ?=null
    var thirdType:String ?=null

    fun init(id: String?, avatar: String?, nickName: String?, thirdType: String?) {
        this.thirdNumber=id
        this.avatar=avatar
        this.nickName=nickName
        this.thirdType=thirdType
    }


}
