package com.ruanyun.australianews.ui.main

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jpush.im.android.api.JMessageClient
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseFragment
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.loadCircleImage
import com.ruanyun.australianews.ext.setStatusBarHeightPaddingTop
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.ui.login.LoginActivity
import com.ruanyun.australianews.ui.my.*
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.TipDialog
import kotlinx.android.synthetic.main.fragment_my.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


/**
 * @description 个人中心
 * @author hdl
 * @date 2019/5/5
 */
class MyFragment : BaseFragment() {

    @Inject
    lateinit var apiService: ApiService
    private val tipDialog by lazy { TipDialog(mContext) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_my, container, false)
        registerBus()

        return mContentView
    }

    override fun onResume() {
        super.onResume()
        tv_cache.text = ImageUtil.getCacheSize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()

        val yuyan = SharedPreferencesUtils.getParam(
            App.context,
            SharedPreferencesUtils.KEY_SYSTEM_LANGUAGE,
            ""
        ) as String

        if(yuyan == "en"){
            tv_notice_switch_tip.visibility=View.INVISIBLE
        }
        updateMsgCount()
        getUserInfo()
    }

    private fun initView() {
        if(Build.VERSION.SDK_INT >= 23){
            rl_topbar.setStatusBarHeightPaddingTop()
        }

        ptr_classic.disableWhenHorizontalMove(true)
        ptr_classic.setPtrHandler(object : PtrDefaultHandler() {
            override fun onRefreshBegin(frame: PtrFrameLayout) {
                tv_cache.text = ImageUtil.getCacheSize()
                getUserInfo()
            }

            override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
                return false
            }
        })
        rl_user.clickWithTrigger { if(isLoginToActivity) PersonalInformationActivity.start(mContext) }

        tv_user_name.clickWithTrigger { isLoginToActivity}

        tv_message.clickWithTrigger { MyMessageListActivity.start(mContext) }
        tv_comment.clickWithTrigger { if(isLoginToActivity) MyEvaluationListActivity.start(mContext) }
        tv_collection.clickWithTrigger { if(isLoginToActivity) MyCollectionActivity.start(mContext) }
        tv_push_record.clickWithTrigger { if(isLoginToActivity) MyPushRecordNewListActivity.start(mContext) }
        tv_browsing_history.clickWithTrigger { if(isLoginToActivity) MyBrowseRecordActivity.start(mContext) }
        iv_notice_switch.click {
            CacheHelper.getInstance().isTurnOnPush = !iv_notice_switch.isSelected
            initNoticeSwitch()
        }
        initNoticeSwitch()
        tv_feedback.clickWithTrigger { if(isLoginToActivity) FeedbackActivity.start(mContext) }
        tv_clear_cache.clickWithTrigger { tipDialog.show(resources.getString(R.string.wenxin), resources.getString(R.string.shifouqureren),resources.getString(R.string.queren) ) }
        tv_rate_me.clickWithTrigger {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://app.banglebang.net/cx4f")))
        }
        tv_about_us.clickWithTrigger { AboutUsListActivity.start(mContext) }

        tipDialog.setOkClick {
            tipDialog.dismiss()
            ImageUtil.clearImageAllCache()
        }

        tv_language.clickWithTrigger{if(isLoginToActivity) SetLanguageActivity.start(mContext)}

    }



    private fun initNoticeSwitch() {
        iv_notice_switch.isSelected = CacheHelper.getInstance().isTurnOnPush
        if (iv_notice_switch.isSelected) {
            tv_notice_switch_tip.text =getResources().getString(R.string.receivenews_open)
        } else {
            tv_notice_switch_tip.text = getResources().getString(R.string.receivenews_close)
        }
    }

    private fun initUserData() {
        val user = app.user
        if(user!=null) {
            iv_user_avatar.loadCircleImage(user.userPhoto, R.drawable.my_img_headempty)
            tv_user_name.text = user.name
//            tv_phone.text = if(TextUtils.isEmpty(user.mobile)) StringUtil.getPwdPhone(user.email) else StringUtil.getPwdPhone(user.mobile)
            tv_phone.text = if(TextUtils.isEmpty(user.signature)) "未填写签名" else user.signature
            tv_phone.visibility = View.VISIBLE
        }else {
            iv_user_avatar.setImageResource(R.drawable.my_img_headempty)
            tv_user_name.text = getResources().getString(R.string.dianjidenglu)
            tv_phone.visibility = View.GONE
        }
    }


    /**
     * 获取用户信息
     */
    private fun getUserInfo() {
        if(TextUtils.isEmpty(app.userOid)){
            initUserData()
            ptr_classic.refreshComplete()
            return
        }
        //用户信息
        val subscription = apiService.getUserInfo(app.userOid).compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                override fun onSuccess(result: ResultBase<UserInfo>) {
                    ptr_classic.refreshComplete()
                    app.user = result.data
                    initUserData()
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    ptr_classic.refreshComplete()
                    if(erroCode == -1){
                        JMessageClient.logout()
                        App.getInstance().user = null
                        EventNotifier.getInstance().updateUserInfo()
                        CacheHelper.getInstance().requestChannelList()
                        LoginActivity.start(mContext)
                    }else {
                        showToast(erroMsg)
                        initUserData()
                    }
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    ptr_classic.refreshComplete()
                    showToast(msg)
                    initUserData()
                }
            })
        addSubscrebe(subscription)
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


    /**
     * 清除缓存
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun clearImageCache(event: Event<String>) {
        if (C.EventKey.CLEAR_IMAGE_CACHE == event.key) {
            tv_cache.text = ImageUtil.getCacheSize()
            showToast("清除成功")
        }
    }


    /**
     * 有新消息，刷新数量
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUnreadCount(event: Event<String>) {
        if (C.EventKey.UPDATE_UNREAD_COUNT == event.key) {
            updateMsgCount()
        }
    }

    private fun updateMsgCount() {
        val unreadMsgCount = JMessageClient.getAllUnReadMsgCount()
        tv_unread_count_my.text = String.format("%s", unreadMsgCount)
        tv_unread_count_my.visibility = if (unreadMsgCount > 0) View.VISIBLE else View.GONE
    }

}
