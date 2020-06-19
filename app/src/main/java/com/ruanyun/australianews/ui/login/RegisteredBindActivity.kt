package com.ruanyun.australianews.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.sharesdk.framework.PlatformDb
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.isNotEmpty
import com.ruanyun.australianews.model.HelpInfo
import com.ruanyun.australianews.model.PhoneAreaInfo
import com.ruanyun.australianews.mvpview.RegisteredMvpView
import com.ruanyun.australianews.presenter.RegisteredPresenter
import com.ruanyun.australianews.ui.WebViewActivity
import com.ruanyun.australianews.ui.wealth.YongHuXieYiActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_registered.*
import javax.inject.Inject

/**
 * @description 绑定
 * @author hdl
 * @date 2019/2/15
 */

class RegisteredBindActivity : BaseActivity(), RegisteredMvpView {
    override fun getVCodeSuccess() {
        tv_vcode.start()
    }

    override fun registeredSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    @Inject
    lateinit var presenter: RegisteredPresenter
    var registeredType = TYPE_PHONE_REGISTER

    companion object {
        const val REQUEST_CODE_AREA = 1001
        const val TYPE_PHONE_REGISTER = 1
        const val TYPE_MAILBOX_REGISTER = 2
        fun start(activity: Activity, requestCode: Int, platformDb: PlatformDb?) {
            val starter = Intent(activity, RegisteredBindActivity::class.java)
            starter.putExtra("id", platformDb?.userId)
            starter.putExtra("avatar", platformDb?.userIcon)
            starter.putExtra("nickName", platformDb?.userName)
            var type = "0"
            if (platformDb?.platformNname.equals(QQ.NAME)) {
                type = "2"
            } else if (platformDb?.platformNname.equals(Wechat.NAME)) {
                type = "1"
            }
            starter.putExtra("thirdType", type)
            activity.startActivityForResult(starter, requestCode)
        }

    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_registered_bind)
        setStatusBarWhite()
        presenter.attachView(this)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AREA){
            val info = data?.getParcelableExtra<PhoneAreaInfo>(C.IntentKey.PHONE_AREA_INFO)
            info?.apply {
                tv_phone_area_name.text = countryName
                tv_area_code.text = areaCode
                iv_area.setImageResource(drawableId)
            }
        }
    }

    private fun initView() {
        val id = intent.getStringExtra("id")
        val avatar = intent.getStringExtra("avatar")
        val nickName = intent.getStringExtra("nickName")
        val thirdType = intent.getStringExtra("thirdType")
        presenter.init(id, avatar, nickName, thirdType)

        topbar.setTopBarClickListener(this)
        tv_phone_area_name.clickWithTrigger {
            CountryAreaCodeSelectActivity.start(this, REQUEST_CODE_AREA)
        }
        tv_password_show.clickWithTrigger {
            tv_password_show.isSelected = !tv_password_show.isSelected
            CommonUtil.showHidePassword(et_password, tv_password_show.isSelected)
        }
        tv_confirm_password_show.clickWithTrigger {
            tv_confirm_password_show.isSelected = !tv_confirm_password_show.isSelected
            CommonUtil.showHidePassword(et_confirm_password, tv_confirm_password_show.isSelected)
        }
        tv_vcode.clickWithTrigger {
            presenter.getSmsCode(tv_area_code.getStr(), et_login_phone.getStr())
        }
        tv_switch_register.click {
            registeredType = if(registeredType == TYPE_PHONE_REGISTER){
                TYPE_MAILBOX_REGISTER
            }else {
                TYPE_PHONE_REGISTER
            }
            updateRegisterMode()
        }
        tv_go_login.click {
            finish()
        }
        tv_read_and_agree.click {
            tv_read_and_agree.isSelected = !tv_read_and_agree.isSelected
        }
        tv_protocol.clickWithTrigger {
//            requestData()
            showActivity(YongHuXieYiActivity::class.java)

        }
        tv_registered.clickWithTrigger {
            if (!tv_read_and_agree.isSelected) {
                showToast("请阅读《澳洲财经用户注册协议》")
                return@clickWithTrigger
            }
            presenter.thirdBinding(tv_area_code.getStr(), et_login_phone.getStr(), et_login_mailbox.getStr(), et_vcode.getStr(), et_password.getStr(),
                et_confirm_password.getStr(), registeredType == TYPE_PHONE_REGISTER)
        }

        et_login_phone.addTextChangedListener(textWatcher)
        et_password.addTextChangedListener(textWatcher)
        et_confirm_password.addTextChangedListener(textWatcher)
        et_vcode.addTextChangedListener(textWatcher)

    }

    private fun updateRegisterMode() {
        if (registeredType == TYPE_PHONE_REGISTER) {
            rl_phone_area.visibility = View.VISIBLE
            rl_phone.visibility = View.VISIBLE
            rl_vcode.visibility = View.VISIBLE
            rl_mailbox.visibility = View.GONE
            tv_switch_register.text = "邮箱绑定"
            topbar.setTitleText("手机号绑定")
        } else {
            rl_phone_area.visibility = View.GONE
            rl_phone.visibility = View.GONE
            rl_vcode.visibility = View.GONE
            rl_mailbox.visibility = View.VISIBLE
            tv_switch_register.text = "手机号绑定"
            topbar.setTitleText("邮箱绑定")
        }
        updateRegisterBtnEnabled()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateRegisterBtnEnabled()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

    private fun updateRegisterBtnEnabled() {
        val enabled = if (registeredType == TYPE_PHONE_REGISTER) {
            et_login_phone.isNotEmpty() && et_password.isNotEmpty() && et_confirm_password.isNotEmpty() && et_vcode.isNotEmpty()
        }else {
            et_login_mailbox.isNotEmpty() && et_password.isNotEmpty() && et_confirm_password.isNotEmpty()
        }
        tv_registered.isEnabled = enabled
    }

    /**
     * 获取协议信息
     */
    private fun requestData() {
        showLoading()
        val subscribe = ApiManger.getApiService().getAppCommonProblem("402881fb6a62b93a016a62ef86570004")
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

}