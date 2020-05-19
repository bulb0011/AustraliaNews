package com.ruanyun.australianews.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.PlatformDb
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.isNotEmpty
import com.ruanyun.australianews.model.PhoneAreaInfo
import com.ruanyun.australianews.mvpview.LoginMvpView
import com.ruanyun.australianews.presenter.LoginPresenter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CacheHelper
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.LogX
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


/**
 * @description 登录
 * @author hdl
 * @date 2019/2/14
 */
class LoginActivity : BaseActivity(), LoginMvpView {
    override fun loginSuccess() {
        finish()
    }

    override fun bind() {
        RegisteredBindActivity.start(this, REQUEST_CODE_REGISTRATION_SUCCESS, platformDb)
    }

    companion object {
        const val REQUEST_CODE_AREA = 1001
        const val REQUEST_CODE_REGISTRATION_SUCCESS = 1006
        const val TYPE_PHONE_LOGIN = 1
        const val TYPE_MAILBOX_LOGIN = 2
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var presenter: LoginPresenter
    var loginType = TYPE_PHONE_LOGIN

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_login)
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
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_AREA -> {
                    val info = data?.getParcelableExtra<PhoneAreaInfo>(C.IntentKey.PHONE_AREA_INFO)
                    info?.apply {
                        tv_phone_area_name.text = countryName
                        tv_area_code.text = areaCode
                        iv_area.setImageResource(drawableId)
                    }
                }
                REQUEST_CODE_REGISTRATION_SUCCESS -> {
                    finish()
                }
            }
        }
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        CacheHelper.getInstance().lastLoginName?.apply {
            loginType = if(CacheHelper.getInstance().lastLoginType){
                et_login_phone.setText(this)
                et_login_phone.setSelection(et_login_phone.length())
                TYPE_PHONE_LOGIN
            }else {
                et_login_mailbox.setText(this)
                et_login_mailbox.setSelection(et_login_mailbox.length())
                TYPE_MAILBOX_LOGIN
            }
            updateLoginMode()
        }

        tv_phone_area_name.clickWithTrigger {
            CountryAreaCodeSelectActivity.start(this, REQUEST_CODE_AREA)
        }
        tv_password_show.clickWithTrigger {
            tv_password_show.isSelected = !tv_password_show.isSelected
            CommonUtil.showHidePassword(et_password, tv_password_show.isSelected)
        }
        tv_forget_password.clickWithTrigger { ForgetPasswordActivity.start(mContext) }
        tv_switch_login.click {
            loginType = if(loginType == TYPE_PHONE_LOGIN){
                TYPE_MAILBOX_LOGIN
            }else {
                TYPE_PHONE_LOGIN
            }
            updateLoginMode()
        }
        tv_go_registered.clickWithTrigger { RegisteredActivity.start(this, REQUEST_CODE_REGISTRATION_SUCCESS) }
        tv_login.clickWithTrigger { presenter.login(tv_area_code.getStr(), et_login_phone.getStr(), et_login_mailbox.getStr(), et_password.getStr(), loginType == TYPE_PHONE_LOGIN) }

        et_login_phone.addTextChangedListener(textWatcher)
        et_login_mailbox.addTextChangedListener(textWatcher)
        et_password.addTextChangedListener(textWatcher)



        qq.clickWithTrigger { thireLogin(QQ.NAME) }
        weixin.clickWithTrigger { thireLogin(Wechat.NAME) }
        weibo.clickWithTrigger { }
    }

    fun thireLogin(name: String) {
        val plat: Platform = ShareSDK.getPlatform(name)
        ShareSDK.setActivity(this) //抖音登录适配安卓9.0
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        plat.setPlatformActionListener(platformListener)
        //执行登录，登录后在回调里面获取用户资料
        plat.showUser(null)
        //获取账号为“3189087725”的资料
        //weibo.showUser(“3189087725”);
    }

    var platformDb: PlatformDb? = null

    val platformListener = object : PlatformActionListener {
        override fun onError(arg0: Platform?, arg1: Int, arg2: Throwable) {
            arg2.printStackTrace()
        }

        override fun onComplete(arg0: Platform, arg1: Int, arg2: HashMap<String?, Any?>?) {
            //输出所有授权信息
            platformDb = arg0.getDb()
            val data = platformDb?.exportData()
            LogX.d(TAG, "data = $data")

            var type = "0"
            if (arg0.name.equals(QQ.NAME)) {
                type = "2"
            } else if (arg0.name.equals(Wechat.NAME)) {
                type = "1"
            }

            runOnUiThread {
                presenter.thridLogin(platformDb?.userId?:"", type)
            }
        }

        override fun onCancel(arg0: Platform?, arg1: Int) {
        }
    }




    private fun updateLoginMode() {
        if (loginType == TYPE_PHONE_LOGIN) {
            rl_phone_area.visibility = View.VISIBLE
            rl_phone.visibility = View.VISIBLE
            rl_mailbox.visibility = View.GONE
            tv_switch_login.text = "邮箱登录"
            topbar.setTitleText("手机号登录")
        } else {
            rl_phone_area.visibility = View.GONE
            rl_phone.visibility = View.GONE
            rl_mailbox.visibility = View.VISIBLE
            tv_switch_login.text = "手机号登录"
            topbar.setTitleText("邮箱登录")
        }
        updateLoginBtnEnabled()
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateLoginBtnEnabled()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

    private fun updateLoginBtnEnabled() {
        val enabled = if (loginType == TYPE_PHONE_LOGIN) {
            et_login_phone.isNotEmpty() && et_password.isNotEmpty()
        }else {
            et_login_mailbox.isNotEmpty() && et_password.isNotEmpty()
        }
        tv_login.isEnabled = enabled
    }
}