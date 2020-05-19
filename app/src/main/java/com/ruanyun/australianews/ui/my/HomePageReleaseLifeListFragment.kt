package com.ruanyun.australianews.ui.my

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrUIHandler
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
import com.ruanyun.australianews.model.LifeGeneralInfo
import com.ruanyun.australianews.model.params.MyReleaseLifeListParams
import com.ruanyun.australianews.ui.adapter.MyReleaseLifeCommonAdapter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.fragment_home_page_release_life_list.*
import rx.Observable
import javax.inject.Inject

/**
 * @author hdl
 * @description 我的发布生活服务列表(别人查看)
 * @date 2019/6/11
 */
class HomePageReleaseLifeListFragment : BaseFragment() {

    class NewsListPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, MyReleaseLifeListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            return apiService.getReleaseLifePageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    companion object {
        fun newHomePageReleaseLifeListFragment(userOid: String?): HomePageReleaseLifeListFragment {
            val fragment = HomePageReleaseLifeListFragment()
            val bundle = Bundle()
            bundle.putString(C.IntentKey.USER_OID, userOid)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_home_page_release_life_list, container, false)
        iRefreshViewHolder.init(mContentView)
        return mContentView
    }

    val adapter by lazy { MyReleaseLifeCommonAdapter(mContext, arrayListOf()) }
    @Inject
    lateinit var dataSource: NewsListPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = MyReleaseLifeListParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<LifeGeneralInfo>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(0, 0, 0, CommonUtil.dp2px(0.66f))
            }
        })
        params.loginUserNum = arguments?.getString(C.IntentKey.USER_OID)
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setLoadMoreEnable(true)
                .setRefreshViewEnable(true)
                .setDataAdapter(headerAdapter)
                .setEmptyView(emptyview)
                .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()
    }

    fun setRefreshEnable(enable : Boolean){
        iRefreshViewHolder.refreshView.setRefreshEnable(enable)
    }


}
