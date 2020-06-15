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
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.WealthBrowseCollectionResultInfo
import com.ruanyun.australianews.model.params.DeleteMyReleaseParams
import com.ruanyun.australianews.model.params.UserOidPageParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.wealth.adapter.WealthBrowseCollectionLifeCommonAdapter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.GsonUtil
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.TipDialog
import kotlinx.android.synthetic.main.fragment_manage_page_list.*
import kotlinx.android.synthetic.main.layout_bottom_delete_management.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @author hdl
 * @description 财富-我的发布
 * @date 2020/3/28
 */
class WealthMyReleaseListFragment : BaseFragment() {

    class NewsListPageDataSource @Inject constructor(apiService: ApiService) :
        PageDataSource<ResultBase<*>, UserOidPageParams>(apiService) {
        override fun fetchData(
            loadType: Int,
            handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?
        ): Observable<ResultBase<*>> {
            return apiService.getWealthReleasePageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContentView = inflater.inflate(R.layout.fragment_manage_page_list, container, false)
        iRefreshViewHolder.init(mContentView)
        registerBus()
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    val adapter by lazy {
        WealthBrowseCollectionLifeCommonAdapter(
            mContext,
            arrayListOf()
        )
    }
    @Inject
    lateinit var dataSource: NewsListPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = UserOidPageParams()
    private lateinit var headerAdapter: RvHeaderFootViewAdapter<WealthBrowseCollectionResultInfo>
    private val tipDialog by lazy { TipDialog(mContext) }

    fun setEditMode(isEditMode: Boolean) {
        rl_delete_manage.visibility = if (isEditMode) View.VISIBLE else View.GONE
        if (!isEditMode) {
            adapter.datas.forEach { it.isSelect = false }
        }
        adapter.isEditMode = isEditMode
        adapter.notifyDataSetChanged()
        headerAdapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        tv_clear_empty.visibility = View.GONE
        adapter.isMyRelease = true
        adapter.isShowLabel = true
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
        params.pageSize = 15
        params.loginUserNum = app.userOid
        dataSource.setParams(params)

        delegate = iRefreshViewHolder
            .setLoadMoreEnable(true)
            .setRefreshViewEnable(true)
            .setDataAdapter(headerAdapter)
            .setEmptyView(emptyview)
            .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()

        tipDialog.setOkClick {
            tipDialog.dismiss()
            deleteReleaseInfo()
        }

        adapter.blockEmpty = {
            emptyview.showEmpty()
        }
        tv_delete.clickWithTrigger {
            if (adapter.datas.any { it.isSelect }) {
                tipDialog.show(resources.getString(R.string.wenxin), "是否确认删除选中发布?", resources.getString(R.string.queren))
            }
        }

    }


    /**
     * 删除发布
     */
    private fun deleteReleaseInfo() {
        val list = arrayListOf<DeleteMyReleaseParams>()
        adapter.datas.forEach {
            if (it.isSelect) {
                list.add(DeleteMyReleaseParams(it.commonBrowseCollectionOid, when(it.itemType){
                    LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> 1
                    LifeReleaseCommonUiModel.WEALTH_FUND -> 2
                    LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE -> 3
                    LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE -> 4
                    LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> 5
                    LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> 6
                    LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> 7
                    else -> 0
                }))
            }
        }
        if(list.isEmpty()){
            showToast("请选择")
            return
        }
        showLoadingView(R.string.in_submit)
        val subscription = ApiManger.getApiService().deleteMyWealthRelease(GsonUtil.toJson(list))
            .compose(RxUtil.normalSchedulers<ResultBase<*>>())
            .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                override fun onSuccess(result: ResultBase<*>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    delegate.refreshWithLoading()
                    (activity as WealthMyReleaseListActivity).onTopBarRightTextClick()
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
    fun updateLifeReleaseList(event: Event<String>) {
        if (C.EventKey.UPDATE_LIFE_RELEASE_LIST == event.key) {
            delegate.refresh()
        }
    }

}
