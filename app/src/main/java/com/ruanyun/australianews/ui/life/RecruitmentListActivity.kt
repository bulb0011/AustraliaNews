package com.ruanyun.australianews.ui.life

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.PageInfoBase
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.LifeRecruitmentInfo
import com.ruanyun.australianews.model.params.LifeRecruitmentParams
import com.ruanyun.australianews.ui.adapter.LifeReleaseCommonAdapter
import com.ruanyun.australianews.ui.life.release.ReleaseRecruitmentActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel
import com.ruanyun.australianews.widget.filterpopwindow.FilterListPopupWindow
import com.ruanyun.australianews.widget.filterpopwindow.OnFilterClickListener
import kotlinx.android.synthetic.main.activity_life_recruitment_list.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @description 招聘信息列表
 * @author hdl
 * @date 2019/5/21
 */
class RecruitmentListActivity : BaseActivity(), OnFilterClickListener {
    class LifePageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<PageInfoBase<LifeRecruitmentInfo>>, LifeRecruitmentParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<PageInfoBase<LifeRecruitmentInfo>>>?): Observable<ResultBase<PageInfoBase<LifeRecruitmentInfo>>> {
            return apiService.getLifeRecruitmentList(params).compose(RxUtil.normalSchedulers())
        }
    }

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    @Inject
    lateinit var dataSource: LifePageDataSource
    private lateinit var delegate: IDataDelegate
    lateinit var adapter: LifeReleaseCommonAdapter<Any?>
    lateinit var headerAdapter: RvHeaderFootViewAdapter<LifeRecruitmentInfo>
    val params = LifeRecruitmentParams()
    private lateinit var cityFilter: FilterListPopupWindow
    private lateinit var industryFilter: FilterListPopupWindow
    private lateinit var jobFunctionsFilter: FilterListPopupWindow
    private lateinit var experienceRequirementFilter: FilterListPopupWindow


    companion object {
        fun start(context: Context) {
            val starter = Intent(context, RecruitmentListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_life_recruitment_list)
        iRefreshViewHolder.init(this)
        registerBus()
        initView()
        initFilter()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterBus()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)

        adapter = LifeReleaseCommonAdapter(mContext, arrayListOf())
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(0, 0, 0, CommonUtil.dp2px(0.66f))
            }
        })

        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setRefreshViewEnable(true)
                .setLoadMoreEnable(true)
                .setEmptyView(emptyview)
                .setDataAdapter(headerAdapter)
                .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()
    }

    private fun initFilter() {
        cityFilter = FilterListPopupWindow(mContext, 1, 7.5f)
        cityFilter.setOnFilterListener(this, 0)
        cityFilter.setData(CacheHelper.getInstance().getAllCityInfoListAndUnlimited("区域"), 0)

        val rentalMethodList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.INDUSTRY_REQUIREMENTS, "行业", "不限")
        industryFilter = FilterListPopupWindow(mContext, 1, getListFloat(rentalMethodList))
        industryFilter.setOnFilterListener(this, 1)
        industryFilter.setData(rentalMethodList, 0)

        val rentList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.JOB_FUNCTIONS, "工作性质", "不限")
        jobFunctionsFilter = FilterListPopupWindow(mContext, 1, getListFloat(rentList))
        jobFunctionsFilter.setOnFilterListener(this, 2)
        jobFunctionsFilter.setData(rentList, 0)

        val experienceRequirementList = DbHelper.getInstance().getParentCodeListAndAll(C.ParentCode.EXPERIENCE_REQUIREMENTS, "经验要求", "不限")
        experienceRequirementFilter = FilterListPopupWindow(mContext, 1, getListFloat(experienceRequirementList))
        experienceRequirementFilter.setOnFilterListener(this, 3)
        experienceRequirementFilter.setData(experienceRequirementList, 0)

        fl_area.click {
            if(cityFilter.data?.isEmpty() != false){
                cityFilter.data = CacheHelper.getInstance().getAllCityInfoListAndUnlimited("区域")
            }
            cityFilter.show(pop_view_line)
            tv_area.isSelected = true
        }
        fl_industry.click {
            industryFilter.show(pop_view_line)
            tv_industry.isSelected = true
        }
        fl_job_functions.click {
            jobFunctionsFilter.show(pop_view_line)
            tv_job_functions.isSelected = true
        }
        fl_experience_requirement.click {
            experienceRequirementFilter.show(pop_view_line)
            tv_experience_requirement.isSelected = true
        }
    }

    override fun onPopItemSelected(filterInfo: FilterInfoUiModel?, requestCode: Int) {
        when(requestCode){
            0 -> {
                tv_area.text = filterInfo?.filterName
                if(filterInfo?.filterShowName == CacheHelper.CITY_UNLIMITED_STR){
                    params.city = ""
                }else {
                    params.city = filterInfo?.filterName
                }
            }
            1 -> {
                tv_industry.text = filterInfo?.filterName
                params.industryRequirements = filterInfo?.filterCode
            }
            2 -> {
                tv_job_functions.text = filterInfo?.filterName
                params.jobFunctions = filterInfo?.filterCode
            }
            3 -> {
                tv_experience_requirement.text = filterInfo?.filterName
                params.experienceRequirements = filterInfo?.filterCode
            }
        }
        delegate.refreshWithLoading()
    }

    override fun onPopWindowDismissed(requestCode: Int) {
        when(requestCode){
            0 -> {
                tv_area.isSelected = false
            }
            1 -> {
                tv_industry.isSelected = false
            }
            2 -> {
                tv_job_functions.isSelected = false
            }
            3 -> {
                tv_experience_requirement.isSelected = false
            }
        }
    }

    override fun onTopBarRightImgClick() {
        if(isLoginToActivityByIsRelease) {
            ReleaseRecruitmentActivity.start(mContext)
        }
    }

    /**
     * 刷新列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateLifeReleaseList(event: Event<String>) {
        if (C.EventKey.UPDATE_LIFE_RELEASE_LIST == event.key) {
            delegate.refreshWithLoading()
        }
    }

}