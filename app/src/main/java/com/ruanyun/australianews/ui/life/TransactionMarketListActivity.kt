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
import com.ruanyun.australianews.model.LifeTransactionMarketInfo
import com.ruanyun.australianews.model.params.LifeTransactionMarketParams
import com.ruanyun.australianews.ui.adapter.LifeReleaseCommonAdapter
import com.ruanyun.australianews.ui.life.release.ReleaseTransactionMarketActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.LifeTransactionMarketFilterPopWindow
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel
import com.ruanyun.australianews.widget.filterpopwindow.FilterListPopupWindow
import com.ruanyun.australianews.widget.filterpopwindow.OnFilterClickListener
import kotlinx.android.synthetic.main.activity_life_transaction_market_list.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @description 交易市场列表
 * @author hdl
 * @date 2019/5/21
 */
class TransactionMarketListActivity : BaseActivity(), OnFilterClickListener {
    class LifePageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<PageInfoBase<LifeTransactionMarketInfo>>, LifeTransactionMarketParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<PageInfoBase<LifeTransactionMarketInfo>>>?): Observable<ResultBase<PageInfoBase<LifeTransactionMarketInfo>>> {
            return apiService.getLifeTransactionMarketList(params).compose(RxUtil.normalSchedulers())
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
    lateinit var headerAdapter: RvHeaderFootViewAdapter<LifeTransactionMarketInfo>
    val params = LifeTransactionMarketParams()
    private lateinit var productFilter: FilterListPopupWindow
    private lateinit var mannerFilter: FilterListPopupWindow
    private lateinit var typeFilter: FilterListPopupWindow
    private val lifeTransactionMarketFilterPopWindow by lazy { LifeTransactionMarketFilterPopWindow(mContext) }


    companion object {
        fun start(context: Context) {
            val starter = Intent(context, TransactionMarketListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_life_transaction_market_list)
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
        val productList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.PRODUCT_CATEGORIES, "产品分类", "不限")
        productFilter = FilterListPopupWindow(mContext, 1, getListFloat(productList))
        productFilter.setOnFilterListener(this, 0)
        productFilter.setData(productList, 0)

        val rentalMethodList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.TRANSACTION_NATURE, "交易方式", "不限")
        mannerFilter = FilterListPopupWindow(mContext, 1, getListFloat(rentalMethodList))
        mannerFilter.setOnFilterListener(this, 1)
        mannerFilter.setData(rentalMethodList, 0)

        val rentList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.MEANS_TRANSACTION, "交易类型", "不限")
        typeFilter = FilterListPopupWindow(mContext, 1, getListFloat(rentList))
        typeFilter.setOnFilterListener(this, 2)
        typeFilter.setData(rentList, 0)

        lifeTransactionMarketFilterPopWindow.block = {oid1,oid2,oid3 ->
            params.newOldStandard = oid1
            params.warranty = oid2
            params.transactionArea = oid3
            delegate.refreshWithLoading()
        }


        fl_product.click {
            productFilter.show(pop_view_line)
            tv_product.isSelected = true
        }
        fl_manner.click {
            mannerFilter.show(pop_view_line)
            tv_manner.isSelected = true
        }
        fl_type.click {
            typeFilter.show(pop_view_line)
            tv_type.isSelected = true
        }
        fl_filter.click {
            lifeTransactionMarketFilterPopWindow.show(pop_view_line)
        }
    }

    override fun onPopItemSelected(filterInfo: FilterInfoUiModel?, requestCode: Int) {
        when(requestCode){
            0 -> {
                tv_product.text = filterInfo?.filterName
                params.type = filterInfo?.filterCode
            }
            1 -> {
                tv_manner.text = filterInfo?.filterName
                params.transactionNature = filterInfo?.filterCode
            }
            2 -> {
                tv_type.text = filterInfo?.filterName
                params.transactionType = filterInfo?.filterCode
            }
        }
        delegate.refreshWithLoading()
    }

    override fun onPopWindowDismissed(requestCode: Int) {
        when(requestCode){
            0 -> {
                tv_product.isSelected = false
            }
            1 -> {
                tv_manner.isSelected = false
            }
            2 -> {
                tv_type.isSelected = false
            }
        }
    }

    override fun onTopBarRightImgClick() {
        if(isLoginToActivityByIsRelease) {
            ReleaseTransactionMarketActivity.start(mContext)
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