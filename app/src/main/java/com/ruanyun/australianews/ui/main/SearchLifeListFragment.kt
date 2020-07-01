package com.ruanyun.australianews.ui.main

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
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.LifeSearchResultInfo
import com.ruanyun.australianews.model.params.LifeSearchListParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.adapter.SearchLifeReleaseCommonAdapter
import com.ruanyun.australianews.util.*
import kotlinx.android.synthetic.main.fragment_life_search_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @author hdl
 * @description 生活服务搜索列表
 * @date 2019/5/30
 */
class SearchLifeListFragment : BaseFragment(){

    class NewsListPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, LifeSearchListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            return apiService.getLifeSearchPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_life_search_list, container, false)
        iRefreshViewHolder.init(mContentView)
        registerBus()
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    val adapter by lazy { SearchLifeReleaseCommonAdapter(mContext, arrayListOf()) }
    @Inject
    lateinit var dataSource: NewsListPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = LifeSearchListParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<LifeSearchResultInfo>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    fun setTitle(searchStr: String){
        params.searchTerm = searchStr
    }

    private fun initView() {
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setLoadMoreEnable(true)
                .setRefreshViewEnable(true)
                .setDataAdapter(headerAdapter)
                .setEmptyView(emptyview)
                .createDataDelegate()
        delegate.setDataSource(dataSource)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val index = parent.getChildAdapterPosition(view)
                val bottom = when {
                    index == 0 -> 0
                    adapter.datas[index-1].itemType >= LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO -> 0
                    else -> CommonUtil.dp2px(0.66f)
                }
                outRect.set(0, 0, 0, bottom)
            }
        })
    }


    /**
     * 刷新列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateNewsList(event: Event<String>) {
        if (C.EventKey.UPDATE_NEWS_LIFE_SEARCH_LIST == event.key) {
            params.type = event.value
            delegate.refreshWithLoading()
        }
    }


}
