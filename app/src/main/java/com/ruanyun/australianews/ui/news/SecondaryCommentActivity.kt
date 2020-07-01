package com.ruanyun.australianews.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
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
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.CommentInfo
import com.ruanyun.australianews.model.params.NewsCommentParams
import com.ruanyun.australianews.model.params.NewsCommentSecondaryParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.my.UserHomePageActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.LeaveMessageDialogFragment
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_secondary_comment.*
import rx.Observable
import java.util.*
import javax.inject.Inject


/**
 * description: 二级回复
 * <p/>
 * Created by ycw on 2019/12/23.
 */

class SecondaryCommentActivity : BaseActivity() {

    class NewsCommentDataSource @Inject constructor(apiService: ApiService) :
        PageDataSource<ResultBase<PageInfoBase<CommentInfo>>, NewsCommentSecondaryParams>(apiService) {
        override fun fetchData(
            loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<PageInfoBase<CommentInfo>>>?
        ): Observable<ResultBase<PageInfoBase<CommentInfo>>> {
            if(params.isWealth){
                return apiService.getWealthSecondaryAppCommentInfoPage(params).compose(RxUtil.normalSchedulers())
            }else {
                return apiService.getSecondaryAppCommentInfoPage(params).compose(RxUtil.normalSchedulers())
            }
        }
    }

    @Inject lateinit var apiService: ApiService
    @Inject lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    @Inject lateinit var dataSource: NewsCommentDataSource
    private lateinit var delegate: IDataDelegate
    private lateinit var adapter: NewsCommentCommonAdapter
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<CommentInfo>
    val params = NewsCommentSecondaryParams()
    lateinit var leaveMessageDialogFragment: LeaveMessageDialogFragment
    var replyAiTeContent: String = ""

    companion object {
        private const val COMMENT_INFO = "comment_info"
        fun start(context: Context, info: CommentInfo?, type: Int?= NewsCommentParams.NEWS) {
            val starter = Intent(context, SecondaryCommentActivity::class.java)
            starter.putExtra(COMMENT_INFO, info)
            starter.putExtra(C.IntentKey.TYPE, type)
            context.startActivity(starter)
        }
    }

    /**
     * 类型
     */
    private var type = NewsCommentParams.NEWS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary_comment)
        iRefreshViewHolder.init(this)

        val info = intent.getParcelableExtra<CommentInfo>(COMMENT_INFO)
        type = intent.getIntExtra(C.IntentKey.TYPE, NewsCommentParams.NEWS)
        params.isWealth = type<0 || type == LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO
        topbar.setTopBarClickListener(this)

        leaveMessageDialogFragment = LeaveMessageDialogFragment.newInstance()
        leaveMessageDialogFragment.block = {
            addComment(it)
        }

        adapter = NewsCommentCommonAdapter(mContext, R.layout.layout_comment, arrayListOf())
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)

        info?.let {
            params.commentInfoOid = it.oid
            params.fromUserOid = it.userOid
            var name = it.name
            var userPhoto = it.userPhoto
            if(it.sysUser!=null){
                name = it.sysUser!!.name
                userPhoto = it.sysUser!!.userPhoto
            }
            header.loadCircleImage(userPhoto.toImgUrl())
            tv_user_name.text = name
            tvContent.text = it.replyMsg
            tvtime.text = StringUtil.getNewsTime(it.createTime)
            header.clickWithTrigger {_->
                UserHomePageActivity.start(mContext, it.userOid)
            }
        }
        dataSource.setParams(params)
        emptyview.setEmptyTipstr("暂无回复")
        delegate =
            iRefreshViewHolder.setRefreshViewEnable(true).setLoadMoreEnable(true).setEmptyView(emptyview).setDataAdapter(headerAdapter).createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()

        floatingBtn.clickWithTrigger {
            if(isLoginToActivity) {
                if(app.userOid == info.userOid){
                    showToast("不能回复自己哦")
                }else {
                    leaveMessageDialogFragment.show(supportFragmentManager, mContext)
                    replyAiTeContent = ""
                }
            }
        }

        adapter.setOnItemClickListener(object :MultiItemTypeAdapter.OnItemClickListener{
            override fun onItemLongClick(p0: View?, p1: RecyclerView.ViewHolder?, p2: Int): Boolean {
                return false
            }

            override fun onItemClick(p0: View?, p1: RecyclerView.ViewHolder?, p2: Int) {
                if(isLoginToActivity) {
                    val t = adapter.datas[p2]
                    if(app.userOid == t.userOid){
                        showToast("不能回复自己哦")
                    }else {
                        leaveMessageDialogFragment.show(supportFragmentManager, mContext)
                        replyAiTeContent = "//@${t.sysUser.name} ${t.replyMsg}"
                    }
                }
            }

        })

    }

    /**
     * 添加评论
     */
    private fun addComment(s: String) {
        showLoadingView(R.string.in_load)
        val observable = if(params.isWealth){
            ApiManger.getApiService().addWealthReply(params.commentInfoOid, params.fromUserOid, app.userOid, s + replyAiTeContent)
        }else {
            ApiManger.getApiService().addReply(params.commentInfoOid, params.fromUserOid, app.userOid, s + replyAiTeContent)
        }
        val subscription = observable
                .compose(RxUtil.normalSchedulers<ResultBase<*>>())
                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                    override fun onSuccess(result: ResultBase<*>) {
                        disMissLoadingView()
                        showToast("评论成功")
                        leaveMessageDialogFragment.addSuccess()
                        delegate.refresh()
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
        override fun convert(p0: ViewHolder?, p1: CommentInfo?, p2: Int) {

            p0?.getView<ImageView>(R.id.header)?.loadCircleImage(FileUtil.getImageUrl(p1?.sysUser?.userPhoto ?: ""))
            p0?.getView<TextView>(R.id.tv_user_name)?.setText(p1?.sysUser?.name)
            p0?.getView<TextView>(R.id.tvContent)?.setText(p1?.replyMsg)
            p0?.getView<TextView>(R.id.tvtime)?.setText(StringUtil.getNewsTime(p1?.createTime))
            p0?.getView<TextView>(R.id.tvNumberOfReviews)?.setText("回复TA")

            p0?.getView<ImageView>(R.id.header)?.clickWithTrigger {
                UserHomePageActivity.start(mContext, p1?.userOid)
            }
        }
    }

}
