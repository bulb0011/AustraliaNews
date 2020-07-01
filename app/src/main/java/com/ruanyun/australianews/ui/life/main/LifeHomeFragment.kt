package com.ruanyun.australianews.ui.life.main

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseFragment
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.IconInfo
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.ui.adapter.LifeHomeIconAdapter
import com.ruanyun.australianews.ui.life.*
import com.ruanyun.australianews.ui.main.SearchActivity
import com.ruanyun.australianews.ui.my.MyMessageListActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.C.IconType.*
import com.ruanyun.australianews.util.CommonUtil.dp2px
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.fragment_life_home.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


/**
 * @description 生活首页
 * @author hdl
 * @date 2019/5/5
 */
class LifeHomeFragment : BaseFragment() {

    @Inject
    lateinit var apiService: ApiService

    val adapter by lazy { LifeHomeIconAdapter(mContext, arrayListOf()) }
    lateinit var headAdapter: RvHeaderFootViewAdapter<IconInfo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_life_home, container, false)
        registerBus()
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unRegisterBus()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        getCommonYellowIconList()
    }

    private fun initView() {
        iv_back.clickWithTrigger { activity?.finish() }
        tv_city.text = app.cityName
        tv_city.clickWithTrigger {
            CityListActivity.start(mContext)
        }

        val headView = layoutInflater.inflate(R.layout.layout_life_headview, null)
        headView.findViewById<TextView>(R.id.et_search).clickWithTrigger { SearchActivity.start(mContext, SearchActivity.LIFE_SEARCH) }
        headView.findViewById<RelativeLayout>(R.id.rl_release).clickWithTrigger { SelectReleaseTypeActivity.start(mContext) }
        headView.findViewById<RelativeLayout>(R.id.rl_message).clickWithTrigger { MyMessageListActivity.start(mContext) }
        headAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        headAdapter.addHeaderView(headView)
        recyclerView.adapter = headAdapter
        val gm = GridLayoutManager(mContext, 5)
        gm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position > 0) {
                    val info = adapter.datas[position - 1]
                    if (info.itemType == IconInfo.TYPE_ICON) {
                        return 1
                    }
                }
                return 5
            }
        }
        recyclerView.layoutManager = gm
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val index = parent.getChildAdapterPosition(view)
                val left = when (index) {
                    2, 7, 14, 19 -> dp2px(5f)
                    else -> 0
                }
                val right = when (index) {
                    6, 11, 18, 24 -> dp2px(5f)
                    else -> 0
                }
                outRect.set(left, 0, right, 0)
            }
        })

        val list = mutableListOf<IconInfo>()
        list.add(IconInfo(IconInfo.TYPE_TITLE, "生活服务"))
        list.add(IconInfo(IconInfo.TYPE_ICON, 房屋出租, R.drawable.life_icon_house))
        list.add(IconInfo(IconInfo.TYPE_ICON, 招聘信息, R.drawable.life_icon_recruit))
        list.add(IconInfo(IconInfo.TYPE_ICON, 机票查询, R.drawable.life_icon_planeticket))
        list.add(IconInfo(IconInfo.TYPE_ICON, 汽车买卖, R.drawable.life_icon_sellcars))
        list.add(IconInfo(IconInfo.TYPE_ICON, 宠物交易, R.drawable.life_icon_pets))
        list.add(IconInfo(IconInfo.TYPE_ICON, 交易市场, R.drawable.life_icon_transaction))
        list.add(IconInfo(IconInfo.TYPE_ICON, 房屋求租, R.drawable.life_icon_rent))
        list.add(IconInfo(IconInfo.TYPE_ICON, 生意转让, R.drawable.life_icon_turn))
        list.add(IconInfo(IconInfo.TYPE_ICON, 教科书, R.drawable.life_icon_book))
        list.add(IconInfo(IconInfo.TYPE_ICON, 餐饮美食, R.drawable.life_icon_foods))


        val thirdList = mutableListOf<IconInfo>()
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 汇款中国, R.drawable.life_icon_exchange_rate))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 房贷计算, R.drawable.life_icon_housingloan))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 违规查询, R.drawable.life_icon_violation))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 实时交通, R.drawable.life_icon_traffic))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 油价跟踪, R.drawable.life_icon_oilprice))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 实时汇率, R.drawable.life_icon_exchangerate))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 及时天气, R.drawable.life_icon_weather))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 犯罪调查, R.drawable.life_icon_crime))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 异域美食, R.drawable.life_icon_delicious))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 个人算税, R.drawable.life_icon_tax))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 交通时表, R.drawable.life_icon_form))
        thirdList.add(IconInfo(IconInfo.TYPE_ICON, 公众假日, R.drawable.life_icon_calendar))
        list.add(IconInfo(IconInfo.TYPE_VIEW_PAGE, thirdList))


        list.add(IconInfo(IconInfo.TYPE_TITLE, "常见黄页"))
        adapter.refresh(list)
        headAdapter.notifyDataSetChanged()


        adapter.block = {
            when (it.title) {
                房屋出租 -> {
                    HouseRentListActivity.start(mContext)
                }
                招聘信息 -> {
                    RecruitmentListActivity.start(mContext)
                }
                机票查询 -> {
                    showToast("机票查询")
                }
                汽车买卖 -> {
                    CarSaleListActivity.start(mContext)
                }
                宠物交易 -> {
                    PetTransactionListActivity.start(mContext)
                }
                交易市场 -> {
                    TransactionMarketListActivity.start(mContext)
                }
                房屋求租 -> {
                    HouseDemandListActivity.start(mContext)
                }
                生意转让 -> {
                    BusinessTransferListActivity.start(mContext)
                }
                教科书 -> {
                    TextbookListActivity.start(mContext)
                }
                餐饮美食 -> {
                    ShopListActivity.start(mContext)
                }

                汇款中国 -> {
                    showToast("汇款中国")
                }
                房贷计算 -> {
                    showToast("房贷计算")
                }
                违规查询 -> {
                    showToast("违规查询")
                }
                实时交通 -> {
                    showToast("实时交通")
                }
                油价跟踪 -> {
                    showToast("油价跟踪")
                }
                实时汇率 -> {
                    showToast("实时汇率")
                }
                及时天气 -> {
                    showToast("及时天气")
                }
                犯罪调查 -> {
                    showToast("犯罪调查")
                }
                异域美食 -> {
                    showToast("异域美食")
                }
                个人算税 -> {
                    showToast("个人算税")
                }
                交通时表 -> {
                    showToast("交通时表")
                }
                公众假日 -> {
                    showToast("公众假日")
                }

                else -> {
                    YellowPageListActivity.start(mContext, it)
                }
            }
        }
    }


    /**
     * 获取常见黄页
     */
    private fun getCommonYellowIconList() {
        showLoading()
        val subscription = apiService.commonYellowPageIconList
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<List<IconInfo>>>() {
                    override fun onSuccess(result: ResultBase<List<IconInfo>>) {
                        disMissLoading()
                        val list = mutableListOf<IconInfo>()
                        result.data.forEach {
                            it.itemType = IconInfo.TYPE_ICON
                        }
                        list.addAll(result.data)
                        list.add(IconInfo(IconInfo.TYPE_FILLING))
                        adapter.loadMore(list)
                        headAdapter.notifyDataSetChanged()
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        disMissLoading()
                        showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        disMissLoading()
                        showToast(msg)
                    }
                })
        addSubscrebe(subscription)
    }

    /**
     * 城市切换
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun switchCitySuccess(event: Event<String>) {
        if (C.EventKey.SWITCH_CITY_SUCCESS == event.key) {
            tv_city.text = app.cityName
        }
    }
}
