package com.ruanyun.australianews.ui.wealth

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseFragment
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.model.FundInfo
import com.ruanyun.australianews.model.params.WealthCommonListParams
import com.ruanyun.australianews.ui.wealth.adapter.WealthCommonAdapter
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.fragment_page_list.*
import rx.Observable
import javax.inject.Inject

/**
 * @author hdl
 * @description 基金列表
 * @date 2020/3/26
 */
class WealthFundListFragment : BaseFragment() {

    class NewsListPageDataSource @Inject constructor(apiService: ApiService) :
        PageDataSource<ResultBase<*>, WealthCommonListParams>(apiService) {
        override fun fetchData(
            loadType: Int,
            handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?
        ): Observable<ResultBase<*>> {
            return apiService.getWealthFundPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContentView = inflater.inflate(R.layout.fragment_page_list, container, false)
        iRefreshViewHolder.init(mContentView)
        return mContentView
    }

    val adapter by lazy {
        WealthCommonAdapter<FundInfo>(
            mContext,
            arrayListOf()
        )
    }
    @Inject
    lateinit var dataSource: NewsListPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = WealthCommonListParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<FundInfo>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(0, 0, 0, CommonUtil.dp2px(0.66f))
            }
        })
        params.pageSize = 20
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
            .setLoadMoreEnable(true)
            .setRefreshViewEnable(true)
            .setDataAdapter(headerAdapter)
            .setEmptyView(emptyview)
            .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()

    }


}
