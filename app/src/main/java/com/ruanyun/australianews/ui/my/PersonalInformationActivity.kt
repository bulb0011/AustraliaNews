package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.AndroidRuntimeException
import cn.jpush.im.android.api.JMessageClient
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.loadCircleImage
import com.ruanyun.australianews.ext.loadCircleImageNotCache
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.model.params.UpdateUserInfoParams
import com.ruanyun.australianews.ui.login.LoginActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SelectDateDialog
import com.ruanyun.australianews.widget.SelectGenderPopWindow
import com.ruanyun.australianews.widget.TipDialog
import kotlinx.android.synthetic.main.activity_personal_information.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * @description 个人资料
 * @author hdl
 * @date 2019/5/9
 */
class PersonalInformationActivity : HeaderPickerActivity() {

    companion object {
        const val REGISTER_TO_FILL_OUT = 1001
        fun start(context: Context) {
            val starter = Intent(context, PersonalInformationActivity::class.java)
            context.startActivity(starter)
        }
        fun startAfterRegister(context: Context) {
            val starter = Intent(context, PersonalInformationActivity::class.java)
            starter.putExtra(C.IntentKey.INFORMATION_TYPE, REGISTER_TO_FILL_OUT)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService: ApiService
    private val selectTimeDialog by lazy { SelectDateDialog(mContext) }
    private val selectGenderPopWindow by lazy { SelectGenderPopWindow(mContext) }
    private val tipDialog by lazy { TipDialog(mContext) }
    val params = UpdateUserInfoParams()
    var informationType: Int = REGISTER_TO_FILL_OUT

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_personal_information)
        initView()
        registerBus()
        initSelectImageHeaderPopView()
        setSelectTypeRound()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterBus()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        informationType = intent.getIntExtra(C.IntentKey.INFORMATION_TYPE, 0)
        if(informationType == REGISTER_TO_FILL_OUT){
            topbar.setRightTextEnable(false)
            tv_confirm.text = resources.getString(R.string.queding)
        }

        rl_avatar.clickWithTrigger { showSelectPopView(rl_avatar) }
        tv_gender.clickWithTrigger { selectGenderPopWindow.showPopupWindow(tv_gender) }
        tv_birthday.clickWithTrigger { selectTimeDialog.show("1", tv_birthday.getStr()) }
        tv_phone.clickWithTrigger { UpdateBindPhoneActivity.start(mContext) }
        tv_mailbox.clickWithTrigger { UpdateBindMailboxActivity.start(mContext) }

        selectGenderPopWindow.block = {
            tv_gender.text = it
            params.sex = if("男"==it) "Y" else "N"
        }

        selectTimeDialog.block = { _, time ->
            if(StringUtil.isNotLessThanToday(time)) {
                showToast("出生日期不能大于今天")
            }else {
                tv_birthday.text = time
                params.birthdate = tv_birthday.getStr()
            }
        }

        tipDialog.setOkClick {
            tipDialog.dismiss()
            JMessageClient.logout()
            App.getInstance().user = null
            EventNotifier.getInstance().updateUserInfo()
            CacheHelper.getInstance().requestChannelList()
            LoginActivity.start(mContext)
            finish()
        }
        tv_confirm.clickWithTrigger {
            if(informationType == REGISTER_TO_FILL_OUT){
                updateUserInfo()
            }else {
                tipDialog.show(resources.getString(R.string.wenxin), resources.getString(R.string.quedingtc), resources.getString(R.string.queding))
            }
        }
        initUserData()
    }

    private fun initUserData() {
        app.user?.apply {
            iv_avatar.loadCircleImage(userPhoto)
            et_nick_name.setText(name)
            tv_gender.text = userSexName
            tv_birthday.text = StringUtil.getTimeStrFromFormatStr("yyyy-MM-dd", birthdate)
            if(CommonUtil.isNotEmpty(mobile)){
                tv_phone.setText(mobile)
            }else {
                tv_phone.setText(resources.getString(R.string.qubangding))
            }
            if(CommonUtil.isNotEmpty(email)){
                tv_mailbox.setText(email)
            }else {
                tv_mailbox.setText(resources.getString(R.string.qubangding))
            }
            et_signature.setText(signature)
        }
    }

    override fun onTopBarRightTextClick() {
        updateUserInfo()
    }

    override fun saveUserHeaderImage(file: File) {
        iv_avatar.loadCircleImageNotCache(file)
        params.userPhotoFile = file
        updateUserInfo()
    }

    /**
     * 保存用户信息
     */
    private fun updateUserInfo() {
        params.oid = app.userOid
        params.name = et_nick_name.getStr()
        params.signature = et_signature.getStr()
        showLoadingView(R.string.in_submit)
        if(params.userPhotoFile != null){
            val map = HashMap<String, RequestBody>()
            map["mainPhoto\";filename=\"" + params.userPhotoFile.name] = FileRequestBody(FileRequestBody.TYPE_IMAGE, params.userPhotoFile)
            val subscription = apiService.uploadImage(map)
                    .flatMap { t ->
                        if(t==null || TextUtils.isEmpty(t.data)){
                            throw AndroidRuntimeException("上传图片失败")
                        }else {
                            params.headImageAttaOid = t.data
                            apiService.updateUserInfo(params)
                        }
                    }
                    .compose(RxUtil.normalSchedulers<ResultBase<UserInfo>>())
                    .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                        override fun onSuccess(result: ResultBase<UserInfo>) {
                            updateSuccess(result)
                            params.userPhotoFile = null
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
        }else {
            val subscription = apiService.updateUserInfo(params)
                    .compose(RxUtil.normalSchedulers<ResultBase<UserInfo>>())
                    .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                        override fun onSuccess(result: ResultBase<UserInfo>) {
                            updateSuccess(result)
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

    private fun updateSuccess(result: ResultBase<UserInfo>) {
        disMissLoadingView()
        showToast(result.msg)
        App.getInstance().user = result.data
        EventNotifier.getInstance().updateUserInfo()
        if (informationType == REGISTER_TO_FILL_OUT) {
            finish()
        }
    }

    /**
     * 用户信息更新、退出登录
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUserInfo(event: Event<String>) {
        if (C.EventKey.UPDATE_USER_INFO == event.key) {
            initUserData()
        }
    }

}