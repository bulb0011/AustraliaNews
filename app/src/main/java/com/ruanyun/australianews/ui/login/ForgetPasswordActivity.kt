package com.ruanyun.australianews.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.isNotEmpty
import com.ruanyun.australianews.model.PhoneAreaInfo
import com.ruanyun.australianews.mvpview.ForgetPasswordMvpView
import com.ruanyun.australianews.presenter.ForgetPasswordPresenter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CommonUtil
import kotlinx.android.synthetic.main.activity_forget_password.*
import javax.inject.Inject

/**
 * @description 忘记密码
 * @author hdl
 * @date 2019/2/15
 */
class ForgetPasswordActivity: BaseActivity(), ForgetPasswordMvpView {
    override fun getVCodeSuccess() {
        tv_vcode.start()
    }

    override fun forgetPasswordSuccess() {
        finish()
    }

    @Inject
    lateinit var presenter: ForgetPasswordPresenter

    companion object {
        const val REQUEST_CODE_AREA = 1001
        fun start(context: Context) {
            val starter = Intent(context, ForgetPasswordActivity::class.java)
            context.startActivity(starter)
        }
    }
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_forget_password)
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
        topbar.setTopBarClickListener(this)
        tv_phone_area_name.clickWithTrigger {
            CountryAreaCodeSelectActivity.start(this, LoginActivity.REQUEST_CODE_AREA)
        }

        tv_password_show.clickWithTrigger {
            tv_password_show.isSelected = !tv_password_show.isSelected
            CommonUtil.showHidePassword(et_password, tv_password_show.isSelected)
        }
        tv_vcode.clickWithTrigger { presenter.getSmsCode(tv_area_code.getStr(), et_phone.getStr()) }
        tv_reset.clickWithTrigger { presenter.forgetPassword(tv_area_code.getStr(), et_phone.getStr(), et_vcode.getStr(), et_password.getStr()) }

        et_phone.addTextChangedListener(textWatcher)
        et_password.addTextChangedListener(textWatcher)
        et_vcode.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            val enabled = et_phone.isNotEmpty() && et_vcode.isNotEmpty() && et_password.isNotEmpty()
            tv_reset.isEnabled = enabled
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }
}