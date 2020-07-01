package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import butterknife.ButterKnife
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.model.params.BindLinkTelParams
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.LogX
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_update_bind_mailbox.*

/**
 * @description 修改绑定邮箱    绑定新邮箱
 * @author hdl
 * @date 2019/12/6
 */
class UpdateBindMailboxActivity : BaseActivity() {

    var bindLinkTelParams = BindLinkTelParams()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_bind_mailbox)
        ButterKnife.bind(this)

        topbar!!.setTopBarClickListener(this)
        bindLinkTelParams.setUserOid(app.userOid)
        bindLinkTelParams.type = "2"
        if (!TextUtils.isEmpty(app.user.email)) {
            editEmail.setText(app.user.email)
            editEmail.setSelection(editEmail.length())
        }

        button.clickWithTrigger {
            val email = editEmail!!.text!!.toString()
            if (TextUtils.isEmpty(email)) {
                showToast(editEmail.hint.toString())
                return@clickWithTrigger
            }
            bindLinkTelParams.loginName = email
            bindingLinkTel()
        }
    }

    /**
     * 修改邮箱
     */
    private fun bindingLinkTel() {
        showLoadingView(R.string.in_submit)
        val disposable = ApiManger.getApiService().bindingLinkTel(bindLinkTelParams)
                .compose(RxUtil.normalSchedulers<ResultBase<UserInfo>>())
                .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {

                    override fun onSuccess(result: ResultBase<UserInfo>) {
                        disMissLoading()
                        LogX.d(TAG, "onSuccess() called with: result = [$result]")
                        showToast("绑定成功")
                        hideSoftInput()
                        app.user = result.data
                        EventNotifier.getInstance().updateUserInfo()
                        finish()
                    }

                    override fun onError(errorCode: Int, errorMsg: String) {
                        super.onError(errorCode, errorMsg)
                        disMissLoading()
                        showToast(errorMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        super.onFail(msg)
                        disMissLoading()
                        showToast(msg)
                    }
                })
        addSubscrebe(disposable)
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, UpdateBindMailboxActivity::class.java)
            context.startActivity(starter)
        }
    }
}
