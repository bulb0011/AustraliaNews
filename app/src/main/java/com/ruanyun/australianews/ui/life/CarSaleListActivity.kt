package com.ruanyun.australianews.ui.life

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
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
import com.ruanyun.australianews.model.LifeCarSaleInfo
import com.ruanyun.australianews.model.params.LifeCarSaleParams
import com.ruanyun.australianews.ui.adapter.LifeReleaseCommonAdapter
import com.ruanyun.australianews.ui.life.release.ReleaseCarSaleActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel
import com.ruanyun.australianews.widget.filterpopwindow.FilterListPopupWindow
import com.ruanyun.australianews.widget.filterpopwindow.OnFilterClickListener
import kotlinx.android.synthetic.main.activity_life_car_sale_list.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @description 汽车买卖列表
 * @author hdl
 * @date 2019/5/21
 */
class CarSaleListActivity : BaseActivity(), OnFilterClickListener {
    class LifePageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<PageInfoBase<LifeCarSaleInfo>>, LifeCarSaleParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<PageInfoBase<LifeCarSaleInfo>>>?): Observable<ResultBase<PageInfoBase<LifeCarSaleInfo>>> {
            return apiService.getLifeCarSaleList(params).compose(RxUtil.normalSchedulers())
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
    lateinit var headerAdapter: RvHeaderFootViewAdapter<LifeCarSaleInfo>
    val params = LifeCarSaleParams()
    private lateinit var brandFilter: FilterListPopupWindow
    private lateinit var transactionTypeFilter: FilterListPopupWindow
    private lateinit var sourceFilter: FilterListPopupWindow


    companion object {
        fun start(context: Context) {
            val starter = Intent(context, CarSaleListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_life_car_sale_list)
        iRefreshViewHolder.init(this)
        registerBus()
        initView()
        initFilter()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterBus()
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
        val brandList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.CAR_BRAND, "品牌", "不限")
        brandFilter = FilterListPopupWindow(mContext, 1, getListFloat(brandList))
        brandFilter.setOnFilterListener(this, 0)
        brandFilter.setData(brandList, 0)

        val rentalMethodList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.CAR_JIAOYI_METHOD, "交易方式", "不限")
        transactionTypeFilter = FilterListPopupWindow(mContext, 1, getListFloat(rentalMethodList))
        transactionTypeFilter.setOnFilterListener(this, 1)
        transactionTypeFilter.setData(rentalMethodList, 0)

        val sourceList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.VEHICLE_SOURCE, "来源", "不限")
        sourceFilter = FilterListPopupWindow(mContext, 1, getListFloat(sourceList))
        sourceFilter.setOnFilterListener(this, 2)
        sourceFilter.setData(sourceList, 0)

        fl_brand.click {
            brandFilter.show(pop_view_line)
            tv_brand.isSelected = true
        }
        fl_transaction_type.click {
            transactionTypeFilter.show(pop_view_line)
            tv_transaction_type.isSelected = true
        }
        fl_source.click {
            sourceFilter.show(pop_view_line)
            tv_source.isSelected = true
        }
    }

    override fun onPopItemSelected(filterInfo: FilterInfoUiModel?, requestCode: Int) {
        when(requestCode){
            0 -> {
                tv_brand.text = filterInfo?.filterName
                params.brand = filterInfo?.filterCode
            }
            1 -> {
                tv_transaction_type.text = filterInfo?.filterName
                params.jiaoyiMethod = filterInfo?.filterCode
            }
            2 -> {
                tv_source.text = filterInfo?.filterName
                params.transactionNature = filterInfo?.filterCode
            }
        }
        delegate.refreshWithLoading()
    }

    override fun onPopWindowDismissed(requestCode: Int) {
        when(requestCode){
            0 -> {
                tv_brand.isSelected = false
            }
            1 -> {
                tv_transaction_type.isSelected = false
            }
            2 -> {
                tv_source.isSelected = false
            }
        }
    }

    override fun onTopBarRightImgClick() {
        if(isLoginToActivityByIsRelease) {
            ReleaseCarSaleActivity.start(mContext)
        }
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