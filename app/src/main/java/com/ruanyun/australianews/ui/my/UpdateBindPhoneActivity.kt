package com.ruanyun.australianews.ui.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import butterknife.ButterKnife
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.PhoneAreaInfo
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.model.params.BindLinkTelParams
import com.ruanyun.australianews.ui.login.CountryAreaCodeSelectActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.LogX
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_update_bind_phone.*

/**
 * @description 修改绑定手机    绑定新手机
 * @author hdl
 * @date 2019/12/6
 */
class UpdateBindPhoneActivity : BaseActivity() {

    var bindLinkTelParams = BindLinkTelParams()
    /**
     * 第一步
     */
    private var isFirstStep = true
    private var mCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_bind_phone)
        ButterKnife.bind(this)

        topbar!!.setTopBarClickListener(this)
        bindLinkTelParams.setUserOid(app.userOid)
        bindLinkTelParams.type = "1"
        if (TextUtils.isEmpty(app.user.mobile)) {
            showSecondStep()
        } else {
            showFirstStep()
        }

        rl_phone_area.clickWithTrigger {
            if(!isFirstStep) {
                CountryAreaCodeSelectActivity.start(this, REQUEST_CODE_AREA)
            }
        }
        verCode.clickWithTrigger {
            getVerificationCode()
        }
        button.clickWithTrigger {
            val phone = editPhone!!.text!!.toString()
            val code = editCode!!.text!!.toString()
            if (TextUtils.isEmpty(phone)) {
                showToast(resources.getString(R.string.shurushoujihao))
                return@clickWithTrigger
            }
            if (TextUtils.isEmpty(code)) {
                showToast("请输入验证码")
                return@clickWithTrigger
            }
            if (isFirstStep) { // 下一步
                if (code != mCode) {
                    showToast("验证码错误")
                    return@clickWithTrigger
                }
                showSecondStep()
            } else { // 确定
                bindLinkTelParams.phoneAreaCode = tv_area_code.getStr()
                bindLinkTelParams.loginName = phone
                bindLinkTelParams.validate = code
                bindingLinkTel()
            }
        }
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

    override fun onTopBarLeftImgClick() {
        onBackPressed()
    }

    private fun showFirstStep() {
        isFirstStep = true
        topbar!!.setTitleText("修改绑定手机")
        tv_phone_area_name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
        tv_area_code.setText(app.user.phoneAreaCode)
        editPhone!!.setText(app.user.mobile)
        setEditEnable(editPhone, false)
        button!!.text = "下一步"
        PhoneAreaInfo.getCountryAreaList().forEach {
            if(it.areaCode == app.user.phoneAreaCode){
                iv_area.setImageResource(it.drawableId)
                tv_phone_area_name.text = it.countryName
                return
            }
        }
    }

    private fun showSecondStep() {
        isFirstStep = false
        topbar!!.setTitleText("绑定新手机")
        tv_phone_area_name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_arrow_right,0)
        editPhone!!.setText("")
        editPhone!!.hint = "请输入新手机"
        editCode!!.setText("")

        setEditEnable(editPhone, true)
        button!!.text = resources.getString(R.string.queding)
        verCode!!.reset()

    }

    internal fun setEditEnable(editText: EditText, enable: Boolean) {
        if (enable) {
            editText.isEnabled = true
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.requestFocus()
        } else {
            editText.isEnabled = false
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
        }
    }

    /**
     * 修改手机
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

    /**
     * 获取验证码
     */
    private fun getVerificationCode() {
        PhoneAreaInfo.getCountryAreaList()
        val phone = editPhone.getStr()
        if (TextUtils.isEmpty(phone)) {
            showToast(editPhone!!.hint.toString())
            return
        }
        val phoneAreaCode = tv_area_code.getStr()
        var type = "1"
        if (isFirstStep) {
            type = "2"
        }
        showLoading()
        val subscribe = ApiManger.getApiService().getSmsCode(phoneAreaCode, phone, type)
                .compose(RxUtil.normalSchedulers<ResultBase<String>>())
                .subscribe(object : ApiSuccessAction<ResultBase<String>>() {
                    override fun onSuccess(result: ResultBase<String>) {
                        disMissLoadingView()
                        verCode!!.start()
                        showToast(result.data)
                        if (type == "2") {
                            mCode = result.data
                        }
                    }

                    override fun onError(errorCode: Int, errorMsg: String) {
                        disMissLoadingView()
                        showToast(errorMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        disMissLoadingView()
                        showToast(msg)
                    }
                })
        addSubscrebe(subscribe)
    }

    companion object {
        const val REQUEST_CODE_AREA = 1006
        fun start(context: Context) {
            val starter = Intent(context, UpdateBindPhoneActivity::class.java)
            context.startActivity(starter)
        }
    }
}
