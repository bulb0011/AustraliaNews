package com.ruanyun.australianews.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseFragment
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.ChannelInfo
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.NewsInfo
import com.ruanyun.australianews.model.params.NewsSearchListParams
import com.ruanyun.australianews.ui.adapter.NewsListAdapter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.fragment_news_search_list.*
import kotlinx.android.synthetic.main.layout_search_news_header.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @author hdl
 * @description 新闻搜索列表
 * @date 2019/5/13
 */
class SearchNewsListFragment : BaseFragment(){

    class NewsListPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, NewsSearchListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            params.userOid = App.getInstance().userOid
            return apiService.getNewsSearchPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_news_search_list, container, false)
        iRefreshViewHolder.init(mContentView)
        registerBus()
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    val adapter by lazy {NewsListAdapter(mContext, arrayListOf())}
    @Inject
    lateinit var dataSource: NewsListPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = NewsSearchListParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<NewsInfo>

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
        delegate.addLoadResultInterceptor(object : IDataDelegate.LoadResultInterceptor<ResultBase<*>> {
            override fun onLoadResult(loadAction: Int, t: ResultBase<*>?) {
                showCustomizeChannel()
            }

            override fun shouldInterceptHanldle(): Boolean {
                return false
            }
        })
        tv_add_channel.clickWithTrigger {
            if(isLoginToActivity) {
                addChannelInfo()
            }
        }
    }

    private fun showCustomizeChannel() {
        if(TextUtils.isEmpty(params.searchTerm)){
            rl_channel.visibility = View.GONE
        }else {
            rl_channel.visibility = View.VISIBLE
        }
        tv_channel_title.text = "#${params.searchTerm}"
        if(app.isLogin){
            val channelInfo = DbHelper.getInstance().getCustomizeChannelInfo(params.searchTerm)
            if(channelInfo==null){
                tv_add_channel.visibility = View.VISIBLE
            }else {
                tv_add_channel.visibility = View.GONE
            }
        }
    }

    /**
     * 添加频道
     */
    private fun addChannelInfo() {
        val title = tv_channel_title.getStr().substring(1, tv_channel_title.length())
        showLoadingView(R.string.in_submit)
        val subscription = ApiManger.getApiService().addChannelInfo(app.userOid, title)
                .compose(RxUtil.normalSchedulers<ResultBase<ChannelInfo>>())
                .subscribe(object : ApiSuccessAction<ResultBase<ChannelInfo>>() {
                    override fun onSuccess(result: ResultBase<ChannelInfo>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        tv_add_channel.visibility = View.GONE
                        val info = ChannelInfo()
                        info.oid = result.data.oid
                        info.isSubscribe = false
                        info.title = result.data.title
                        info.isLogin = true
                        info.type = 2
                        info.sort = DbHelper.getInstance().getChannelList(true).size+10
                        DbHelper.getInstance().addChannelInfo(info)
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        super.onError(erroCode, erroMsg)
                        disMissLoadingView()
                        showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        super.onFail(msg)
                        disMissLoadingView()
                        showToast(msg)
                    }
                })
        addSubscrebe(subscription)
    }

    /**
     * 刷新列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateNewsList(event: Event<String>) {
        if (C.EventKey.UPDATE_NEWS_LIFE_SEARCH_LIST == event.key) {
            showCustomizeChannel()
            delegate.refreshWithLoading()
        }
    }

    /**
     * 频道列表数据变化
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateChannelList(event: Event<String>) {
        if (C.EventKey.UPDATE_CHANNEL_LIST == event.key) {
            showCustomizeChannel()
        }
    }

}
