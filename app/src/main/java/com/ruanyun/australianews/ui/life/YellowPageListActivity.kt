package com.ruanyun.australianews.ui.life

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.IconInfo
import com.ruanyun.australianews.model.LifeYellowPageInfo
import com.ruanyun.australianews.model.NewsInfo
import com.ruanyun.australianews.model.params.YellowPageListParams
import com.ruanyun.australianews.ui.adapter.LifeReleaseCommonAdapter
import com.ruanyun.australianews.ui.life.release.ReleaseYellowPageActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CacheHelper
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel
import com.ruanyun.australianews.widget.filterpopwindow.FilterListPopupWindow
import com.ruanyun.australianews.widget.filterpopwindow.OnFilterClickListener
import kotlinx.android.synthetic.main.activity_yellow_page_list.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @description 黄页列表
 * @author hdl
 * @date 2019/5/9
 */
class YellowPageListActivity : BaseActivity(), OnFilterClickListener {
    class YellowPageListPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, YellowPageListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            params.loginUserNum = App.getInstance().userOid
            return apiService.getYellowPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    companion object {
        fun start(context: Context, iconInfo: IconInfo) {
            val starter = Intent(context, YellowPageListActivity::class.java)
            starter.putExtra(C.IntentKey.ICON_INFO, iconInfo)
            context.startActivity(starter)
        }
    }

    val adapter by lazy { LifeReleaseCommonAdapter<Any?>(mContext, arrayListOf()) }
    @Inject
    lateinit var dataSource: YellowPageListPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = YellowPageListParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<LifeYellowPageInfo>
    private lateinit var cityFilter: FilterListPopupWindow

    lateinit var iconInfo: IconInfo

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_yellow_page_list)
        iRefreshViewHolder.init(this)
        registerBus()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterBus()
    }

    private fun initView() {
        iconInfo = intent.getParcelableExtra(C.IntentKey.ICON_INFO)
        topbar.setTitleText(iconInfo.title).setTopBarClickListener(this)

        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)

        params.typeOid = iconInfo.oid
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setLoadMoreEnable(true)
                .setRefreshViewEnable(true)
                .setDataAdapter(headerAdapter)
                .setEmptyView(emptyview)
                .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()

        cityFilter = FilterListPopupWindow(mContext, 1, 7.5f)
        cityFilter.setOnFilterListener(this, 0)
        cityFilter.setData(CacheHelper.getInstance().getAllCityInfoListAndUnlimited("区域筛选"), 0)

        tv_area.click {
            if (cityFilter.data?.isEmpty() != false) {
                cityFilter.data = CacheHelper.getInstance().getAllCityInfoListAndUnlimited("区域筛选")
            }
            cityFilter.show(pop_view_line)
            tv_area.isSelected = true
        }
    }

    override fun onPopItemSelected(filterInfo: FilterInfoUiModel?, requestCode: Int) {
        tv_area.text = filterInfo?.filterName
        if (filterInfo?.filterShowName == CacheHelper.CITY_UNLIMITED_STR) {
            params.city = ""
        } else {
            params.city = filterInfo?.filterName
        }
        delegate.refreshWithLoading()
    }

    override fun onPopWindowDismissed(requestCode: Int) {
        tv_area.isSelected = false
    }

    override fun onTopBarRightImgClick() {
        if (isLoginToActivityByIsRelease) {
            ReleaseYellowPageActivity.start(mContext, iconInfo.oid)
        }
    }

    /**
     * 刷新列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateLifeReleaseList(event: Event<String>) {
        if (C.EventKey.UPDATE_YELLOW_PAGE_LIST == event.key) {
            delegate.refreshWithLoading()
        }
    }
}