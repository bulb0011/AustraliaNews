package com.ruanyun.australianews.ui.life.main

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseFragment
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.loadCircleImage
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.ui.my.MyBrowseRecordActivity
import com.ruanyun.australianews.ui.my.MyCollectionActivity
import com.ruanyun.australianews.ui.my.MyReleaseActivity
import com.ruanyun.australianews.ui.my.PersonalInformationActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.fragment_life_my.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


/**
 * @description 生活我的
 * @author hdl
 * @date 2019/5/5
 */
class LifeMyFragment : BaseFragment() {

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_life_my, container, false)
        registerBus()
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initUserData()
    }

    private fun initView() {
        topbar.topBarLeftImg.clickWithTrigger { activity?.finish() }
        ptr_classic.disableWhenHorizontalMove(true)
        ptr_classic.setPtrHandler(object : PtrDefaultHandler() {
            override fun onRefreshBegin(frame: PtrFrameLayout) {
                getUserInfo()
            }

            override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
                return false
            }
        })
        rl_user.clickWithTrigger { if(isLoginToActivity) PersonalInformationActivity.start(mContext) }
        tv_release.clickWithTrigger { if(isLoginToActivity) MyReleaseActivity.startMode(mContext, MyReleaseActivity.MODE_LIFE) }
        tv_collection.clickWithTrigger { if(isLoginToActivity) MyCollectionActivity.startMode(mContext, MyCollectionActivity.MODE_LIFE) }
        tv_footprint.clickWithTrigger { if(isLoginToActivity) MyBrowseRecordActivity.startMode(mContext, MyBrowseRecordActivity.MODE_LIFE) }
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
                    showToast(erroMsg)
                    initUserData()
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

}
