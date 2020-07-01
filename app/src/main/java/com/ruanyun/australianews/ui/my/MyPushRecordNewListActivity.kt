package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.PushRecordNewsInfo
import com.ruanyun.australianews.model.params.NewsPushListParams
import com.ruanyun.australianews.model.params.DeleteBrowseParams
import com.ruanyun.australianews.ui.adapter.CollectionBrowseNewsListAdapter
import com.ruanyun.australianews.ui.adapter.PushRecordNewsListAdapter
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_my_browse_record.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import rx.Observable
import javax.inject.Inject

/**
 * @author hdl
 * @description 我的推送新闻列表
 * @date 2019/6/12
 */
class MyPushRecordNewListActivity : BaseActivity(){

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MyPushRecordNewListActivity::class.java)
            context.startActivity(starter)
        }
    }

    class NewsListPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, NewsPushListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            return apiService.getPushRecordNewsPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    val adapter by lazy { PushRecordNewsListAdapter(mContext, arrayListOf()) }

    @Inject
    lateinit var dataSource: NewsListPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = NewsPushListParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<PushRecordNewsInfo>

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_push_record_news_list)
        iRefreshViewHolder.init(this)
        initView()
    }


    private fun initView() {
        topbar.setTopBarClickListener(this)
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
//        params.userOid = App.getInstance().userOid
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setLoadMoreEnable(true)
                .setRefreshViewEnable(true)
                .setDataAdapter(headerAdapter)
                .setEmptyView(emptyview)
                .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()

    }

    override fun onTopBarRightTextClick() {
//        clearPush()
//        val word = "你猜"
//        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://m.baidu.com/ssid=f0113935b6b0c1bac93a/s?word=${word}&ts=1253971&t_kt=0&ie=utf-8&rsv_iqid=2042401263&rsv_t=7f62n33rbfM9%252BuITJyjzbQruBGwFgmcuw%252BjEDW1l2h%252BZRZKQNVFm&sa=ib&rsv_pq=2042401263&rsv_sug4=4541&ss=110000000001&inputT=844&tj=1")))
    }

    /**
     * 清空推送
     */
    private fun clearPush() {
        val params = DeleteBrowseParams()
        params.type = 1
        params.userOid = app.userOid
        params.model = 1
        showLoadingView(R.string.in_submit)
        val subscription = ApiManger.getApiService().deleteBrowseInfo(params)
                .compose(RxUtil.normalSchedulers<ResultBase<*>>())
                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                    override fun onSuccess(result: ResultBase<*>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        delegate.refreshWithLoading()
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



}
