package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.loadCircleImage
import com.ruanyun.australianews.model.UserInfo
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.util.CommonUtil.dp2px
import jiguang.chat.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_user_home_page.*
import kotlinx.android.synthetic.main.layout_user_home_page_header.*

/**
 * @description 个人主页
 * @author hdl
 * @date 2019/12/30
 */
class UserHomePageActivity : BaseActivity() {
    companion object {
        fun start(context: Context, userOid: String?) {
            val starter = Intent(context, UserHomePageActivity::class.java)
            starter.putExtra(C.IntentKey.USER_OID, userOid)
            context.startActivity(starter)
        }
    }

    var userOid: String? = null
    lateinit var adapter: ViewPagerAdapter
    var fragments = arrayListOf<Fragment>()
    val drawable by lazy { ColorDrawable(ContextCompat.getColor(mContext, R.color.theme_color)) }
    // 滚动偏移距离
    var height: Int = 0

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_user_home_page)
        immerse()
        initView()
        getUserInfo()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        userOid = intent.getStringExtra(C.IntentKey.USER_OID)
        drawable.alpha = 0
        topbar.background = drawable
        val minHeight = PixelSizeUtil.unDisplayViewSize(topbar)[1]
        collapsing_toolbar_layout.minimumHeight = minHeight
        view_height_bg.layoutParams.height = minHeight + dp2px(27f)
        fragments.add(HomePageReleaseWealthListFragment.newFragment(userOid))
        fragments.add(HomePageEvaluationListFragment.newHomePageEvaluationListFragment(userOid))
        adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        viewpager.adapter = adapter
        if (Build.VERSION.SDK_INT >= 21) {
            viewpager.isNestedScrollingEnabled = true
        }
        tab.setViewPager(viewpager, arrayOf("我的发布", "我的评价"))

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (height == Math.abs(verticalOffset)) {
                return@OnOffsetChangedListener
            }
            height = Math.abs(verticalOffset)

            if (height <= 0) {
                drawable.alpha = 0
                (fragments[0] as HomePageReleaseWealthListFragment).setRefreshEnable(true)
                (fragments[1] as HomePageEvaluationListFragment).setRefreshEnable(true)
            } else {
                val distance = (height / 1.8).toInt()
                drawable.alpha = if (distance > 255) 255 else distance
                (fragments[0] as HomePageReleaseWealthListFragment).setRefreshEnable(false)
                (fragments[1] as HomePageEvaluationListFragment).setRefreshEnable(false)
            }
        })
    }


    private fun getUserInfo() {
        //用户信息
        showLoading()
        val subscription =
            ApiManger.getApiService().getUserInfo(userOid).compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<UserInfo>>() {
                    override fun onSuccess(result: ResultBase<UserInfo>) {
                        disMissLoading()
                        initUserData(result.data)
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        disMissLoading()
                        showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        disMissLoading()
                        showToast(msg)
                    }
                })
        addSubscrebe(subscription)
    }

    private fun initUserData(user: UserInfo) {
        iv_user_avatar.loadCircleImage(user.userPhoto, R.drawable.my_img_headempty)
        tv_user_name.text = user.name
        if (CommonUtil.isNotEmpty(user.birthdate)) {
            tv_birthday.text =
                "(${StringUtil.getTimeStrFromFormatStr("yyyy-MM-dd", user.birthdate)})"
        }
        if (CommonUtil.isNotEmpty(user.sex)) {
            iv_sex.visibility = View.VISIBLE
            iv_sex.isSelected = user.isUserSexMan
        }

        if (CommonUtil.isNotEmpty(user.mobile) && CommonUtil.isNotEmpty(user.email)) {
            tv_phone_separate.visibility = View.VISIBLE
            tv_phone.visibility = View.VISIBLE
            tv_mailbox.visibility = View.VISIBLE
            tv_phone.text = StringUtil.getPwdPhone(user.mobile)
            tv_mailbox.text = StringUtil.getPwdPhone(user.email)
        } else if (CommonUtil.isNotEmpty(user.mobile)) {
            tv_phone.visibility = View.VISIBLE
            tv_phone.text = StringUtil.getPwdPhone(user.mobile)
        } else if (CommonUtil.isNotEmpty(user.email)) {
            tv_mailbox.visibility = View.VISIBLE
            tv_mailbox.text = StringUtil.getPwdPhone(user.email)
        }
        tv_signature.text = if (TextUtils.isEmpty(user.signature)) "未填写签名" else user.signature
    }


}