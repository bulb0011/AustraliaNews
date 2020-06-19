package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
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
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.Evaluation
import com.ruanyun.australianews.model.params.EvaluationListParams
import com.ruanyun.australianews.ui.adapter.EvaluationListAdapter
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.TipDialog
import kotlinx.android.synthetic.main.activity_my_evaluation_list.*
import kotlinx.android.synthetic.main.layout_bottom_delete_management.*
import rx.Observable
import javax.inject.Inject

/**
 * @description 我发表的评价
 * @author hdl
 * @date 2019/5/15
 */
class MyEvaluationListActivity : BaseActivity(){

    class MyEvaluationPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, EvaluationListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            return apiService.getEvaluationPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MyEvaluationListActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var dataSource: MyEvaluationPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<Evaluation>
    val adapter by lazy { EvaluationListAdapter(mContext, arrayListOf()) }
    private val params = EvaluationListParams()
    private val tipDialog by lazy { TipDialog(mContext) }
    var deleteType = 2//1清空 2删除选中

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_evaluation_list)
        iRefreshViewHolder.init(this)

        topbar.setTopBarClickListener(this)
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        params.userOid = app.userOid
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setLoadMoreEnable(true)
                .setRefreshViewEnable(true)
                .setDataAdapter(headerAdapter)
                .setEmptyView(emptyview)
                .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()
        list.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(0,0,0,CommonUtil.dp2px(8f))
            }
        })

        tipDialog.setOkClick {
            tipDialog.dismiss()
            deleteCommentInfo()
        }
        tv_clear_empty.clickWithTrigger {
            if(adapter.datas.isNotEmpty()){
                deleteType = 1
//                tipDialog.show("温馨提示", "是否确认清空评论?", "确认")
                deleteCommentInfo()
            }
        }
        tv_delete.clickWithTrigger {
            if(adapter.datas.any { it.isSelect }){
                deleteType = 2
//                tipDialog.show("温馨提示", "是否确认删除选中评论?", "确认")
                deleteCommentInfo()
            }
        }
    }

    override fun onTopBarRightTextClick() {
        if(topbar.topBarRightTitle.getStr() == "管理"){
            if(adapter.datas.size<=0){
                showToast("暂无数据")
                return
            }
            topbar.topBarRightTitle.text = "完成"
            rl_delete_manage.visibility = View.VISIBLE
            adapter.isEditorMode = true
            adapter.notifyDataSetChanged()
            headerAdapter.notifyDataSetChanged()
        }else {
            topbar.topBarRightTitle.text = resources.getString(R.string.guangli)
            rl_delete_manage.visibility = View.GONE
            adapter.isEditorMode = false
            adapter.notifyDataSetChanged()
            headerAdapter.notifyDataSetChanged()
        }
    }


    /**
     * 删除评论
     */
    private fun deleteCommentInfo() {
        var collectionInfoOids = ""
        if(deleteType==2){
            adapter.datas.forEach {
                if(it.isSelect){
                    if(CommonUtil.isNotEmpty(collectionInfoOids)){
                        collectionInfoOids += ","
                    }
                    collectionInfoOids += it.oid
                }
            }
        }
        showLoadingView(R.string.in_submit)
        val subscription = ApiManger.getApiService().deleteCommentInfo(collectionInfoOids, app.userOid, deleteType)
            .compose(RxUtil.normalSchedulers<ResultBase<*>>())
            .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                override fun onSuccess(result: ResultBase<*>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    delegate.refreshWithLoading()
                    onTopBarRightTextClick()
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