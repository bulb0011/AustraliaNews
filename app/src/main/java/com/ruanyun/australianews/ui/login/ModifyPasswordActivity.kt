package com.ruanyun.australianews.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.ruanyun.australianews.App
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.isEmpty
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.util.MD5
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_modify_password.*
import javax.inject.Inject

/**
 * @description 修改密码
 * @author hdl
 * @date 2019/2/15
 */
class ModifyPasswordActivity : BaseActivity() {

    @Inject
    lateinit var apiService: ApiService

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, ModifyPasswordActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_modify_password)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        tv_forget_password.clickWithTrigger { ForgetPasswordActivity.start(mContext) }
    }

    override fun onTopBarRightTextClick() {
        if(et_old_password.isEmpty()){
            showToast("请输入旧密码")
            return
        }
        if(et_new_password.isEmpty()){
            showToast("请输入新密码")
            return
        }
        if(et_confirm_password.isEmpty()){
            showToast("请再输入一次新密码")
            return
        }
        modifyPassword(et_old_password.getStr(),et_new_password.getStr(),et_confirm_password.getStr())
    }


    /**
     * 修改密码
     */
    private fun modifyPassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        if (TextUtils.isEmpty(oldPassword)) {
            showToast("请输入旧密码")
            return
        }
        if (TextUtils.isEmpty(newPassword)) {
            showToast("请输入新密码")
            return
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            showToast("请再输入一次新密码")
            return
        }
        if(newPassword != confirmPassword){
            showToast("两次密码不一致")
            return
        }
        if (newPassword.length<6) {
            showToast("密码最少6位")
            return
        }
        showLoadingView(R.string.in_submit)
        val subscription = apiService.modifyPassword(app.userOid, MD5.md5(oldPassword), MD5.md5(newPassword))
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                    override fun onSuccess(result: ResultBase<*>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        val user = App.getInstance().user
                        user.loginPass = MD5.md5(newPassword)
                        App.getInstance().user = user
                        finish()
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        disMissLoadingView()
                        showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        disMissLoadingView()
                        showToast(msg)
                    }
                })
        addSubscrebe(subscription)
    }

}