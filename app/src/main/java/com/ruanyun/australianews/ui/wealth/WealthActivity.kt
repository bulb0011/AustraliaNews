package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.PageInfoBase
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.loadImageNoDefault
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.*
import com.ruanyun.australianews.model.params.UserOidPageParams
import com.ruanyun.australianews.ui.WebViewActivity
import com.ruanyun.australianews.ui.life.BusinessTransferListActivity
import com.ruanyun.australianews.ui.main.SearchActivity
import com.ruanyun.australianews.ui.wealth.adapter.WealthBrowseCollectionLifeCommonAdapter
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel
import com.ruanyun.australianews.widget.filterpopwindow.FilterListPopupWindow
import com.ruanyun.australianews.widget.filterpopwindow.OnFilterClickListener
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_wealth.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @description 财富
 * @author hdl
 * @date 2020/01/20
 */
class WealthActivity : BaseActivity(), OnFilterClickListener {
    override fun onPopItemSelected(filterInfo: FilterInfoUiModel?, requestCode: Int) {
        when(filterInfo?.filterCode){
            "1" -> {
                WealthFundListActivity.start(mContext)
            }
            "2" -> {
                WebViewActivity.startHtml(mContext, "股票", "file:///android_asset/gupiao.html")
            }
            "3" -> {
                WebViewActivity.startHtml(mContext, "汇率", "file:///android_asset/waihui.html")
            }
        }
    }

    override fun onPopWindowDismissed(requestCode: Int) {

    }

    class LifePageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<PageInfoBase<WealthBrowseCollectionResultInfo>>, UserOidPageParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<PageInfoBase<WealthBrowseCollectionResultInfo>>>?): Observable<ResultBase<PageInfoBase<WealthBrowseCollectionResultInfo>>> {
            return apiService.getWealthNewestPageList(params).compose(RxUtil.normalSchedulers())
        }
    }

    private val iconList = mutableListOf<IconInfo>()
    private val lifeHomeIconAdapter by lazy { WealthIconAdapter(mContext, iconList) }


    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    @Inject
    lateinit var dataSource: LifePageDataSource
    private lateinit var delegate: IDataDelegate
    lateinit var adapter: WealthBrowseCollectionLifeCommonAdapter
    lateinit var headerAdapter: RvHeaderFootViewAdapter<WealthBrowseCollectionResultInfo>
    val params = UserOidPageParams()
    private lateinit var typeFilter: FilterListPopupWindow
    val typeList = ArrayList<ParentCodeInfo>()
    lateinit var recyclerView : RecyclerView

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_wealth)
        if(Build.VERSION.SDK_INT >= 23){
            immerse()
            PixelSizeUtil.setStatusBarHeightPaddingTop(topbar)
            PixelSizeUtil.setStatusBarFontColor(this, false)
        }
        iRefreshViewHolder.init(this)
        registerBus()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterBus()
    }

    private fun initView() {

        //股票
        tv_gupiao.clickWithTrigger { WebViewActivity.startHtml(mContext, "股票", "file:///android_asset/gupiao.html"); }

        //基金
        tv_jijin.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                WealthFundListActivity.start(mContext)
            }
        })

        val yuyan = SharedPreferencesUtils.getParam(
            App.context,
            SharedPreferencesUtils.KEY_SYSTEM_LANGUAGE,
            ""
        ) as String

        //汇率
        tv_hunlv.clickWithTrigger {
                WebViewActivity.startHtml(mContext, resources.getString(R.string.huilv), "file:///android_asset/waihui.html")

        }

        //跳转搜索
        tv_search2.clickWithTrigger { SearchActivity.start(mContext, SearchActivity.HOME_SEARCH) }

        image_que.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish();
            }
        })


        //不动产
        tv_bgudong.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

//                showToast("dainji")
//                val starter = Intent(context, CivilEstateListActivityTo::class.java)
//                context.startActivity(starter)

                CivilEstateListActivityTo.start(mContext, CivilEstateInfo.ESTATEINFO_TYPE4)
//
//                CivilEstateListActivity.start(mContext, CivilEstateInfo.ESTATEINFO_TYPE2)

            }
        })


        topbar.setTopBarClickListener(this)
        iconList.add(IconInfo(IconInfo.TYPE_ICON, C.IconType.基金, R.drawable.icon_funding))
        iconList.add(IconInfo(IconInfo.TYPE_ICON, C.IconType.股票, R.drawable.icon_stock))
        iconList.add(IconInfo(IconInfo.TYPE_ICON, C.IconType.汇率, R.drawable.icon_foreign_currency_exchange))
        iconList.add(IconInfo(IconInfo.TYPE_ICON, C.IconType.民用地产, R.drawable.icon_civil_real_estate))
        iconList.add(IconInfo(IconInfo.TYPE_ICON, C.IconType.商业地产, R.drawable.icon_commercial_estate))
//        iconList.add(IconInfo(IconInfo.TYPE_ICON, C.IconType.估值, R.drawable.icon_valuation))
        iconList.add(IconInfo(IconInfo.TYPE_ICON, C.IconType.农场, R.drawable.icon_farm))

        lifeHomeIconAdapter.block = {
            when (it.title) {
                C.IconType.基金 -> {
                    WealthFundListActivity.start(mContext)
                }
                C.IconType.股票 -> {
                    WebViewActivity.startHtml(mContext, "股票", "file:///android_asset/gupiao.html")
                }
                C.IconType.汇率 -> {
                    WebViewActivity.startHtml(mContext, "汇率", "file:///android_asset/waihui.html")
                }
                C.IconType.民用地产 -> {
                    CivilEstateListActivity.start(mContext, CivilEstateInfo.ESTATEINFO_TYPE1)
                }
                C.IconType.商业地产 -> {
                    CivilEstateListActivity.start(mContext, CivilEstateInfo.ESTATEINFO_TYPE2)
                }
//                C.IconType.估值 -> {
//                }
                C.IconType.农场 -> {
                    CivilEstateListActivity.start(mContext, CivilEstateInfo.ESTATEINFO_TYPE3)
                }
            }
        }

//        tv_housing_market.clickWithTrigger {
//                        HousingMarketListActivity.start(mContext)
//        }


        adapter = WealthBrowseCollectionLifeCommonAdapter(mContext, arrayListOf())
        adapter.isShowLabel = true
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)

        val headerView = layoutInflater.inflate(R.layout.layout_wealth_newest_header, list, false)
        recyclerView = headerView.findViewById(R.id.recyclerView)

        recyclerView.isNestedScrollingEnabled = false
//        recyclerView.adapter = lifeHomeIconAdapter
        val gm = GridLayoutManager(mContext, 5)
        recyclerView.layoutManager = gm
        //去掉头部
        headerAdapter.addHeaderView(headerView)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val index = parent.getChildAdapterPosition(view)
                val bottom = if(index==0){
                    0
                }else {
                    CommonUtil.dp2px(0.66f)
                }
                outRect.set(0, 0, 0, bottom)
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


//        tv_financial_management.clickWithTrigger {
//            typeFilter.setData(typeList, -1)
//            typeFilter.show(tv_financial_management)
//        }

//        tv_housing_market.clickWithTrigger {
//            HousingMarketListActivity.start(mContext)
//        }

        tv_business.clickWithTrigger {
            BusinessTransferListActivity.start(mContext)
        }
        tv_my.clickWithTrigger {
            WealthMyActivity.start(mContext)
        }

        typeList.add(ParentCodeInfo("基金", "1"))
        typeList.add(ParentCodeInfo("股票", "2"))
        typeList.add(ParentCodeInfo("汇率", "3"))
        typeFilter = FilterListPopupWindow(mContext, 1, getListFloat(typeList))
        typeFilter.setOnFilterListener(this, 0)
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


class WealthIconAdapter(context: Context, datas: List<IconInfo>) : RvMuiltItemAdapter<IconInfo>(context, datas) {

    init {
        /**
         * 图标
         */
        addItemViewDelegate(object : ItemViewDelegate<IconInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_wealth_home_icon
            }

            override fun isForViewType(item: IconInfo, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, item: IconInfo, position: Int) {
                if (item.iconRes == 0) {
                    holder.getView<ImageView>(R.id.iv_icon).loadImageNoDefault(item.mainPhoto.toImgUrl())
                } else {
                    holder.getView<ImageView>(R.id.iv_icon).setImageResource(item.iconRes)
                }
                holder.setText(R.id.tv_name, item.title)
                holder.convertView.clickWithTrigger {
                    block.invoke(item)
                }
            }
        })

    }

    var block = fun(_: IconInfo) {}

}