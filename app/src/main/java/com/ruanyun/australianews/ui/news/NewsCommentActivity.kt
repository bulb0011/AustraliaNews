package com.ruanyun.australianews.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.PageInfoBase
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvCommonAdapter
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.loadCircleImage
import com.ruanyun.australianews.model.CommentInfo
import com.ruanyun.australianews.model.params.NewsCommentParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.my.UserHomePageActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.LeaveMessageDialogFragment
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_news_comment.*
import rx.Observable
import java.util.*
import javax.inject.Inject


/**
 * description:  新闻评论列表
 * <p/>
 * Created by ycw on 2019/12/23.
 */
class NewsCommentActivity : BaseActivity() {


    class NewsCommentDataSource @Inject constructor(apiService: ApiService) :
        PageDataSource<ResultBase<PageInfoBase<CommentInfo>>, NewsCommentParams>(apiService) {
        override fun fetchData(
            loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<PageInfoBase<CommentInfo>>>?
        ): Observable<ResultBase<PageInfoBase<CommentInfo>>> {
            if(params.isWealth){
                return apiService.getWealthAppCommentInfoPage(params).compose(RxUtil.normalSchedulers())
            }else {
                return apiService.getAppCommentInfoPage(params).compose(RxUtil.normalSchedulers())
            }
        }
    }

    @Inject lateinit var apiService: ApiService
    @Inject lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    @Inject lateinit var dataSource: NewsCommentDataSource
    private lateinit var delegate: IDataDelegate
    private lateinit var adapter: NewsCommentCommonAdapter
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<CommentInfo>
    val params = NewsCommentParams()
    lateinit var leaveMessageDialogFragment: LeaveMessageDialogFragment

    companion object {
        fun start(context: Context, newsInfoOid: String?, type: Int? = NewsCommentParams.NEWS) {
            val starter = Intent(context, NewsCommentActivity::class.java)
            starter.putExtra(C.IntentKey.NEWS_INFO_OID, newsInfoOid)
            starter.putExtra(C.IntentKey.TYPE, type)
            context.startActivity(starter)
        }
    }

    /**
     * 类型
     */
    private var oldType = NewsCommentParams.NEWS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_comment)
        iRefreshViewHolder.init(this)

        val newsInfoOid = intent.getStringExtra(C.IntentKey.NEWS_INFO_OID)
        oldType = intent.getIntExtra(C.IntentKey.TYPE, NewsCommentParams.NEWS)
        if(oldType==NewsCommentParams.NEWS){
            topbar.setTitleText("所有评论")
            emptyview.setEmptyTipstr("暂无评论")
        }else {
            topbar.setTitleText("所有留言")
            emptyview.setEmptyTipstr("暂无留言")
        }
        topbar.setTopBarClickListener(this)

        leaveMessageDialogFragment = LeaveMessageDialogFragment.newInstance()
        leaveMessageDialogFragment.block = {
            if(params.isWealth) {
                addWealthComment(it)
            }else {
                addComment(it)
            }
        }

        adapter = NewsCommentCommonAdapter(mContext, R.layout.layout_comment, arrayListOf())
        adapter.type = oldType
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)

        params.commonOid = newsInfoOid
        if(oldType>0 && oldType != LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO){
            params.type = oldType
        }else {
            params.isWealth = true
            params.type = when(oldType){
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> 1
                LifeReleaseCommonUiModel.WEALTH_FUND -> 2
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE -> 3
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE -> 4
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> 5
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> 6
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> 7
                else -> 0
            }
        }
        dataSource.setParams(params)
        delegate =
            iRefreshViewHolder.setRefreshViewEnable(true).setLoadMoreEnable(true).setEmptyView(emptyview).setDataAdapter(headerAdapter).createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()

        floatingBtn.clickWithTrigger {
            if(isLoginToActivity) {
                leaveMessageDialogFragment.show(supportFragmentManager, mContext)
            }
        }
    }

    /**
     * 添加评论
     */
    private fun addComment(s: String) {
        showLoadingView(R.string.in_load)
        val subscription = ApiManger.getApiService().addComment(params.commonOid, app.userOid, s, "$oldType")
                .compose(RxUtil.normalSchedulers<ResultBase<*>>())
                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                    override fun onSuccess(result: ResultBase<*>) {
                        disMissLoadingView()
                        if(oldType==NewsCommentParams.NEWS){
                            showToast("评论成功")
                        }else {
                            showToast("留言成功")
                        }
                        leaveMessageDialogFragment.addSuccess()
                        delegate.refresh()
                        EventNotifier.getInstance().addSuccessSuccess()
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
     * 添加财富评论
     */
    private fun addWealthComment(s: String) {
        showLoadingView(R.string.in_load)
        val subscription = ApiManger.getApiService().addWealthComment(params.commonOid, app.userOid, s, params.type)
            .compose(RxUtil.normalSchedulers<ResultBase<*>>())
            .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                override fun onSuccess(result: ResultBase<*>) {
                    disMissLoadingView()
                    if(oldType==NewsCommentParams.NEWS){
                        showToast("评论成功")
                    }else {
                        showToast("留言成功")
                    }
                    leaveMessageDialogFragment.addSuccess()
                    delegate.refresh()
                    EventNotifier.getInstance().addSuccessSuccess()
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
     * description:
     * <p/>
     * Created by ycw on 2019/12/23.
     */
    class NewsCommentCommonAdapter(context: Context, layoutView: Int, dataSource: ArrayList<CommentInfo>) : RvCommonAdapter<CommentInfo>(
        context, layoutView, dataSource
    ) {
        var type = NewsCommentParams.NEWS
        override fun convert(p0: ViewHolder?, p1: CommentInfo?, p2: Int) {
            var name = p1?.name
            var userPhoto = p1?.userPhoto
            if(p1?.sysUser!=null){
                name = p1.sysUser!!.name
                userPhoto = p1.sysUser!!.userPhoto
            }
            p0?.getView<ImageView>(R.id.header)?.loadCircleImage(FileUtil.getImageUrl(userPhoto))
            p0?.getView<TextView>(R.id.tv_user_name)?.setText(name)
            p0?.getView<TextView>(R.id.tvContent)?.setText(p1?.replyMsg)
            p0?.getView<TextView>(R.id.tvtime)?.setText(StringUtil.getNewsTime(p1?.createTime))
            if((p1?.replyCount?:0)<=0) {
                p0?.getView<TextView>(R.id.tvNumberOfReviews)?.setText("回复")
            }else {
                p0?.getView<TextView>(R.id.tvNumberOfReviews)?.setText("${p1?.replyCount}回复")
            }
            p0?.getView<ImageView>(R.id.header)?.clickWithTrigger {
                UserHomePageActivity.start(mContext, p1?.userOid)
            }
            p0?.convertView?.clickWithTrigger {
                SecondaryCommentActivity.start(mContext, p1, type)
            }
        }
    }

}
