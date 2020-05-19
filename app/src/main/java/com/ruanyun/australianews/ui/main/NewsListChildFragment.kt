package com.ruanyun.australianews.ui.main

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseFragment
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.DefaultModel2ViewControler
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.base.refreshview.view.IRefreshListLoadViewFactory
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.model.ChannelInfo
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.NewsInfo
import com.ruanyun.australianews.model.params.NewsListParams
import com.ruanyun.australianews.ui.adapter.NewsListAdapter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CommonUtil.dp2px
import com.ruanyun.australianews.util.LogX
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.RYEmptyView
import kotlinx.android.synthetic.main.fragment_news_list_child.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @author hdl
 * @description 新闻列表
 * @date 2019/5/7
 */
class NewsListChildFragment : BaseFragment(), IRefreshListLoadViewFactory.IEmptyView {
    override fun bind(view: View?) {
    }

    override fun unbind() {
    }

    override fun showLoading(msg: String?) {
        adapter.refresh(arrayListOf())
        headerAdapter.notifyDataSetChanged()
        newEmptyView.showLoading()
        newEmptyView.layoutParams.height = newEmptyViewHeight
    }

    override fun showEmpty() {
        newEmptyView.showEmpty()
        newEmptyView.layoutParams.height = newEmptyViewHeight
    }

    private var isFirstLoadFail: Boolean = true
    override fun showLoadFail(erroMsg: String?) {
        newEmptyView.showLoadFail(erroMsg)
        newEmptyView.layoutParams.height = newEmptyViewHeight
//        if(isFirstLoadFail){
//            Handler().postDelayed({
//                delegate?.refresh()
//            }, 5000)
//        }
    }

    override fun loadFinish() {
        isFirstLoadFail = false
        newEmptyView.loadSuccess()
        newEmptyView.layoutParams.height = 1
    }

    override fun setReloadClickListener(clickListener: View.OnClickListener?) {
        newEmptyView.setOnReloadListener(clickListener)
    }

    class NewsListChildPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, NewsListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            params.userOid = App.getInstance().userOid
            params.city = App.getInstance().cityName
            return apiService.getNewsPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_news_list_child, container, false)
        iRefreshViewHolder.init(mContentView)
        registerBus()
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    private var channelInfo: ChannelInfo? = null
    val adapter by lazy { NewsListAdapter(mContext, arrayListOf()) }
    @Inject
    lateinit var dataSource: NewsListChildPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    var delegate: IDataDelegate?=null
    private val params = NewsListParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<NewsInfo>

    private lateinit var newEmptyView: RYEmptyView
    private val newEmptyViewHeight = dp2px(420f)
    private var isRefreshViewEnable: Boolean = false

    var isShow = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        channelInfo = bundle?.getParcelable(C.IntentKey.CHANNEL_INFO)
        isRefreshViewEnable = bundle?.getBoolean(C.IntentKey.IS_REFRESH_VIEW_ENABLE, false)?:false
    }

    private fun initView() {
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        newEmptyView = layoutInflater.inflate(R.layout.layout_header_empty_view, null) as RYEmptyView
        newEmptyView.layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, dp2px(200f))
        headerAdapter.addHeaderView(newEmptyView)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        channelInfo?.apply {
            params.channelInfoOid = oid
            params.type = type
        }
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setLoadMoreEnable(true)
                .setRefreshViewEnable(isRefreshViewEnable)
                .setDataAdapter(headerAdapter)
                .setEmptyView(this)
                .createDataDelegate()
        delegate?.setDataSource(dataSource)
        delegate?.refreshWithLoading()
//        inLoad()
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        isShow = isVisibleToUser
//        inLoad()
//    }
//
//    private fun inLoad() {
//        if(isShow) {
//            delegate?.refreshWithLoading()
//        }
//    }

    fun getTitle():String{
        return channelInfo?.title?:""
    }

    /**
     * 刷新列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateNewsList(event: Event<String>) {
        if (C.EventKey.UPDATE_NEWS_LIST == event.key) {
            if(event.value == channelInfo?.title){
                delegate?.refresh()
            }
        }
    }

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateRefreshViewEnable(event: Event<Boolean>) {
        if (TitleFragmentAdapter.REFRESH_VIEW_ENABLE == event.key) {
            (delegate as DefaultModel2ViewControler<*>).apply {
                refreshViewHolder.setRefreshViewEnable(event.value)
            }
        }
    }

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scrollToTop(event: Event<Boolean>) {
        if (C.EventKey.SCROLL_TO_TOP == event.key) {
            if(list!=null) {
                list.scrollToPosition(0)
            }
        }
    }

}
