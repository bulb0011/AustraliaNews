package com.ruanyun.australianews.ui.life

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.PageInfoBase
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.LifeFoodShopInfo
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.model.params.LifeShopParams
import com.ruanyun.australianews.ui.adapter.LifeReleaseCommonAdapter
import com.ruanyun.australianews.ui.life.release.ReleaseShopActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel
import com.ruanyun.australianews.widget.filterpopwindow.FilterListPopupWindow
import com.ruanyun.australianews.widget.filterpopwindow.OnFilterClickListener
import com.ruanyun.imagepicker.permissions.PermissionsManager
import com.ruanyun.imagepicker.permissions.PermissionsResultAction
import kotlinx.android.synthetic.main.activity_life_shop_list.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @description 餐饮美食列表
 * @author hdl
 * @date 2019/11/12
 */
class ShopListActivity : BaseActivity(), OnFilterClickListener {
    class LifePageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<PageInfoBase<LifeFoodShopInfo>>, LifeShopParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<PageInfoBase<LifeFoodShopInfo>>>?): Observable<ResultBase<PageInfoBase<LifeFoodShopInfo>>> {
            params.longitude = App.getInstance().longitude
            params.latitude = App.getInstance().latitude
            return apiService.getLifeShopList(params).compose(RxUtil.normalSchedulers())
        }
    }

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    @Inject
    lateinit var dataSource: LifePageDataSource
    private lateinit var delegate: IDataDelegate
    lateinit var adapter: LifeReleaseCommonAdapter<Any?>
    lateinit var headerAdapter: RvHeaderFootViewAdapter<LifeFoodShopInfo>
    val params = LifeShopParams()
    private lateinit var cityFilter: FilterListPopupWindow
    private lateinit var industryFilter: FilterListPopupWindow
    private lateinit var typeFilter: FilterListPopupWindow
    var locationHelper: LocationHelper? = null

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, ShopListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_life_shop_list)
        iRefreshViewHolder.init(this)
        registerBus()
        initView()
        initFilter()
        Handler().postDelayed({
            requestJurisdiction()
        }, 200)
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterBus()
        locationHelper?.stop()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)

        adapter = LifeReleaseCommonAdapter(mContext, arrayListOf())
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(0, 0, 0, CommonUtil.dp2px(0.66f))
            }
        })

        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setRefreshViewEnable(true)
                .setLoadMoreEnable(true)
                .setEmptyView(emptyview)
                .setDataAdapter(headerAdapter)
                .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()
    }

    private fun initFilter() {
        cityFilter = FilterListPopupWindow(mContext, 1, 7.5f)
        cityFilter.setOnFilterListener(this, 0)
        cityFilter.setData(CacheHelper.getInstance().getAllCityInfoListAndUnlimited("配送区域"), 0)

        val rentalMethodList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.COOK_STYLE, "菜系", "不限")
        industryFilter = FilterListPopupWindow(mContext, 1, getListFloat(rentalMethodList))
        industryFilter.setOnFilterListener(this, 1)
        industryFilter.setData(rentalMethodList, 0)

        val typeList = ArrayList<ParentCodeInfo>()
        typeList.add(ParentCodeInfo("筛选", "默认", ""))
        typeList.add(ParentCodeInfo("时间排序", "1"))
        typeList.add(ParentCodeInfo("离我最近", "2"))
        typeList.add(ParentCodeInfo("人均低到高", "3"))
        typeFilter = FilterListPopupWindow(mContext, 1, getListFloat(typeList))
        typeFilter.setOnFilterListener(this, 2)
        typeFilter.setData(typeList, 0)

        fl_area.click {
            if (cityFilter.data?.isEmpty() != false) {
                cityFilter.data = CacheHelper.getInstance().getAllCityInfoListAndUnlimited("区域")
            }
            cityFilter.show(pop_view_line)
            tv_area.isSelected = true
        }
        fl_cuisine.click {
            industryFilter.show(pop_view_line)
            tv_cuisine.isSelected = true
        }
        fl_type.click {
            typeFilter.show(pop_view_line)
            tv_type.isSelected = true
        }
    }

    override fun onPopItemSelected(filterInfo: FilterInfoUiModel?, requestCode: Int) {
        when (requestCode) {
            0 -> {
                tv_area.text = filterInfo?.filterName
                if (filterInfo?.filterShowName == CacheHelper.CITY_UNLIMITED_STR) {
                    params.deliveryArea = ""
                } else {
                    params.deliveryArea = filterInfo?.filterName
                }
            }
            1 -> {
                tv_cuisine.text = filterInfo?.filterName
                params.cookStyle = filterInfo?.filterCode
            }
            2 -> {
                tv_type.text = filterInfo?.filterName
                params.type = filterInfo?.filterCode
            }
        }
        delegate.refreshWithLoading()
    }

    override fun onPopWindowDismissed(requestCode: Int) {
        when (requestCode) {
            0 -> {
                tv_area.isSelected = false
            }
            1 -> {
                tv_cuisine.isSelected = false
            }
            2 -> {
                tv_type.isSelected = false
            }
        }
    }

    override fun onTopBarRightImgClick() {
        if (isLoginToActivityByIsRelease) {
            ReleaseShopActivity.start(mContext)
        }
    }

    private fun requestJurisdiction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, LocationHelper.PERMISSIONS,
                    object : PermissionsResultAction() {

                        override fun onGranted() {
                            initLocation()
                        }

                        override fun onDenied(permission: String) {
                            showToast("获取定位权限被拒绝")
                        }
                    })
        } else {
            initLocation()
        }
    }

    var isFirstLocation = true
    private fun initLocation() {
        locationHelper = LocationHelper(mContext, 3000)
        locationHelper?.setLocationListener(object : BDAbstractLocationListener(){
            override fun onReceiveLocation(it: BDLocation?) {
                it?.apply {
                    if(CommonUtil.isNotEmpty(city)) {
                        app.longitude = longitude.toString()
                        app.latitude = latitude.toString()
                        if(isFirstLocation){
                            isFirstLocation = false
                            delegate.refresh()
                        }
                    }
                }
            }
        })
        locationHelper?.start()
    }

    /**
     * 刷新列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateLifeReleaseList(event: Event<String>) {
        if (C.EventKey.UPDATE_LIFE_RELEASE_LIST == event.key) {
            delegate.refreshWithLoading()
        }
    }

}