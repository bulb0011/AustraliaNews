package com.ruanyun.australianews.ui.main

import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseFragment
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.setStatusBarHeightPaddingTop
import com.ruanyun.australianews.model.AdvertInfoBase
import com.ruanyun.australianews.model.ChannelInfo
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.HomeResultBase
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.ui.WebViewActivity
import com.ruanyun.australianews.ui.adapter.AdverViewHolder
import com.ruanyun.australianews.ui.login.LoginActivity
import com.ruanyun.australianews.ui.news.ChannelManagerActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.util.CommonUtil.dp2px
import com.ruanyun.australianews.widget.MyConvenientBanner
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.layout_news_header.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author hdl
 * @description 新闻
 * @date 2019/5/5
 */
class NewsFragment : BaseFragment() {
    companion object {
        const val REQUEST_CODE_CHANNEL = 1001
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_news, container, false)
        registerBus()
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    lateinit var adapter: TitleFragmentAdapter
    lateinit var convenientBanner: MyConvenientBanner<AdvertInfoBase>
    // 滚动偏移距离
    private var height: Int = 0
    private var minimumHeight: Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        refresh_layout.autoRefresh()
    }

    override fun onResume() {
        super.onResume()
        convenientBanner.startTurning(4000)
    }

    override fun onPause() {
        super.onPause()
        convenientBanner.stopTurning()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_CHANNEL -> {
                    val databaseSubscribedList = DbHelper.getInstance().getSubscribedList(app.isLogin)
                    updateChannelTab(databaseSubscribedList)
                    val position = data?.getIntExtra(C.IntentKey.CLICK_POSITION, -1)
                    if (position != null && position>=0) {
                        viewpager.currentItem = position
                    }
                }
            }
        }
    }

    private fun initView() {
        if(Build.VERSION.SDK_INT >= 23){
            rl_topbar.setStatusBarHeightPaddingTop()
            PixelSizeUtil.setStatusBarHeightView(view_status_bar_height)
        }
        if(Build.VERSION.SDK_INT >= 21){
            viewpager.isNestedScrollingEnabled = true
        }
        rl_tip_login.visibility = if(app.user!=null) View.GONE else View.VISIBLE
        updateMinimumHeight()
        convenientBanner = getView(R.id.banner)
        convenientBanner.setPageIndicator(MyConvenientBanner.indicators)
        convenientBanner.setPageIndicatorAlign(MyConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        convenientBanner.setPageIndicatorMargins(0,0,dp2px(10f),dp2px(25f))

        adapter = TitleFragmentAdapter(childFragmentManager, DbHelper.getInstance().getSubscribedList(app.isLogin) )
        adapter.notifyDataSetChanged()
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 1
        tab.setViewPager(viewpager)

        tab_to.setViewPager(viewpager)//头部的表单
        tab_layout.visibility = View.GONE

//        tab_to.setOnClickListener {  }

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (height == Math.abs(verticalOffset)) {
                return@OnOffsetChangedListener
            }
            val appBarLayoutMaxSlidingDistance = appBarLayout.measuredHeight //减去1个偏移量

            height = Math.abs(verticalOffset)

            if (verticalOffset < -(appBarLayoutMaxSlidingDistance/2)) {
                if (rl_topbar.visibility != View.VISIBLE) {
                    rl_topbar.visibility = View.VISIBLE
                    tab_layout.visibility = View.VISIBLE
//                    adapter.setRefreshViewEnable(false)
                }
            } else {
                if (rl_topbar.visibility != View.GONE) {
                    rl_topbar.visibility = View.GONE
                    tab_layout.visibility = View.GONE
//                    adapter.setRefreshViewEnable(false)
                }
            }
//            if (verticalOffset < -dp2px(150f)) {
//                if (view_status_bar_height.visibility != View.VISIBLE) {
//                    view_status_bar_height.visibility = View.VISIBLE
//                }
//            } else {
//                if (view_status_bar_height.visibility != View.GONE) {
//                    view_status_bar_height.visibility = View.GONE
//                }
//            }
        })

        refresh_layout.setPtrHandler(object : PtrHandler {
            override fun onRefreshBegin(frame: PtrFrameLayout?) {
                getHomeResultBase()
                if(tab.tabCount>0) {
                    EventNotifier.getInstance().updateNewsList(tab.getTitleView(tab.currentTab)?.getStr())
                }
            }

            override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
                return height <= 0
            }
        })
        refresh_layout.disableWhenHorizontalMove(true)


        tv_search1.clickWithTrigger { SearchActivity.start(mContext, SearchActivity.HOME_SEARCH) }
        tv_search2.clickWithTrigger { SearchActivity.start(mContext, SearchActivity.HOME_SEARCH) }
        tv_login.clickWithTrigger {
            LoginActivity.start(mContext)
        }
        iv_tip_login_close.clickWithTrigger {
            rl_tip_login.visibility = View.GONE
            if (rl_topbar.visibility == View.VISIBLE) {
                app_bar_layout.setExpanded(false, false)
            }
            updateMinimumHeight()
        }

        iv_subscribe.clickWithTrigger {
            if (isLoginToActivity) {
                ChannelManagerActivity.start(this, REQUEST_CODE_CHANNEL)
            }
        }
        iv_expansion.clickWithTrigger {
            EventBus.getDefault().post(Event(C.EventKey.SCROLL_TO_TOP, ""))
            app_bar_layout.setExpanded(true, true)
        }


//        tv_push_record.clickWithTrigger { if(isLoginToActivity) MyPushRecordNewListActivity.start(mContext) }
//        tv_house_rent.clickWithTrigger { HouseRentListActivity.start(mContext) }
//        tv_recruitment_info.clickWithTrigger { RecruitmentListActivity.start(mContext) }
//        tv_more_services.clickWithTrigger {
//            showActivity(LifeMainActivity::class.java)
//        }
        tv_push_record.clickWithTrigger { WebViewActivity.startHtml(mContext, "行情", "file:///android_asset/quotes.html"); }
        tv_house_rent.clickWithTrigger { WebViewActivity.startHtml(mContext, "7x24", "file:///android_asset/7_24.html"); }
        tv_recruitment_info.clickWithTrigger { ActivitysListActivity.start(mContext) }
        tv_more_services.clickWithTrigger {
            if (isLoginToActivity) {
                ChannelManagerActivity.start(this, REQUEST_CODE_CHANNEL)
            }
        }

        tv_city.text = app.cityName
        tv_city.clickWithTrigger {
            CityListActivity.start(mContext)
        }
    }


    private fun updateMinimumHeight() {
        val tipLoginHeight = if (rl_tip_login.visibility == View.VISIBLE){
            rl_tip_login.layoutParams.height + view_tab_top_line.layoutParams.height
        } else 0

        if (rl_tip_login.visibility == View.VISIBLE){
            minimumHeight = PixelSizeUtil.unDisplayViewSize(rl_topbar)[1] + tab_layout.layoutParams.height + tipLoginHeight - rl_tip_login.layoutParams.height - view_tab_top_line.layoutParams.height
        }else{
            minimumHeight = PixelSizeUtil.unDisplayViewSize(rl_topbar)[1] + tab_layout.layoutParams.height
        }




        collapsing_toolbar_layout.minimumHeight = minimumHeight
    }

    /**
     * 获取首页信息
     */
    private fun getHomeResultBase() {
        val subscription = ApiManger.getApiService().getHomeResultBase(app.userOid, app.cityName)
                .compose(RxUtil.normalSchedulers<ResultBase<HomeResultBase>>())
                .subscribe(object : ApiSuccessAction<ResultBase<HomeResultBase>>() {
                    override fun onSuccess(result: ResultBase<HomeResultBase>) {
                        refresh_layout.refreshComplete()
                        val advertList = result.data.adverInfoList?: arrayListOf()
                        convenientBanner.setPages({ AdverViewHolder() }, advertList).setOnItemClickListener{
                            if(advertList.isNotEmpty()) {
                                WebViewUrlUtil.showAdvertDetailsWeb(mContext, advertList[it])
                            }
                        }
                        if(advertList.isEmpty()){
                            convenientBanner.setBackgroundColor(ContextCompat.getColor(mContext, R.color.theme_color))
                        }else {
                            convenientBanner.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                        }
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        refresh_layout.refreshComplete()
                        showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        refresh_layout.refreshComplete()
                        showToast(msg)
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
            if(app.user!=null && rl_tip_login.visibility != View.GONE){
                rl_tip_login.visibility = View.GONE
                if (rl_topbar.visibility == View.VISIBLE) {
                    app_bar_layout.setExpanded(false, false)
                }
                updateMinimumHeight()
            }else if(app.user==null){
                rl_tip_login.visibility = View.VISIBLE
                updateMinimumHeight()
            }
        }
    }

    /**
     * 频道列表数据变化
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateChannelList(event: Event<String>) {
        if (C.EventKey.UPDATE_CHANNEL_LIST == event.key) {
            updateChannelTab(DbHelper.getInstance().getSubscribedList(app.isLogin))
        }
    }

    /**
     * 刷新频道Tab
     */
    private fun updateChannelTab(databaseSubscribedList: List<ChannelInfo>) {
        if(databaseSubscribedList.isNotEmpty()) {
            adapter.channels = databaseSubscribedList
            adapter.notifyDataSetChanged()
            tab.notifyDataSetChanged()
        }else {
//            CacheHelper.getInstance().requestChannelList()
        }
    }

    /**
     * 城市切换
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun switchCitySuccess(event: Event<String>) {
        if (C.EventKey.SWITCH_CITY_SUCCESS == event.key) {
            tv_city.text = app.cityName
            getHomeResultBase()
            if(tab.tabCount>0) {
                EventNotifier.getInstance().updateNewsList(tab.getTitleView(tab.currentTab)?.getStr())
            }
        }
    }

}
