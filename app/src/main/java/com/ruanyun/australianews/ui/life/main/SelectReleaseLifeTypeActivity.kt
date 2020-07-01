package com.ruanyun.australianews.ui.life.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.IconInfo
import com.ruanyun.australianews.ui.adapter.LifeHomeIconAdapter
import com.ruanyun.australianews.ui.life.release.*
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.GsonUtil
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_select_release_life_type.*


/**
 * @description 选择发布生活服务或黄页的类型
 * @author hdl
 * @date 2019/5/5
 */
class SelectReleaseLifeTypeActivity : BaseActivity() {

    companion object {
        fun start(context: Context, isLife: Boolean) {
            val starter = Intent(context, SelectReleaseLifeTypeActivity::class.java)
            starter.putExtra(C.IntentKey.TYPE, isLife)
            context.startActivity(starter)
        }
    }

    val adapter by lazy { LifeHomeIconAdapter(mContext, arrayListOf()) }
    lateinit var headAdapter: RvHeaderFootViewAdapter<IconInfo>
    var isLife = true

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_select_release_life_type)
        initView()
    }

    private fun initView() {
        isLife = intent.getBooleanExtra(C.IntentKey.TYPE, true)
        headAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        val headView = layoutInflater.inflate(R.layout.layout_select_release_life_type_headview, null)
        val tvTip = headView.findViewById<TextView>(R.id.tv_tip)
        headAdapter.addHeaderView(headView)
        recyclerView.adapter = headAdapter
        val gm = GridLayoutManager(mContext, 4)
        gm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position > 0) {
                    val info = adapter.datas[position - 1]
                    if (info.itemType == IconInfo.TYPE_ICON || info.itemType == IconInfo.TYPE_CLASSIFICATION) {
                        return 1
                    }
                }
                return 4
            }
        }
        recyclerView.layoutManager = gm

        if (isLife) {
            topbar.setTitleText("选择生活服务").setTopBarClickListener(this)
            tvTip.text = "请选择您要发布的生活服务类型"
            val list = mutableListOf<IconInfo>()
            list.add(IconInfo(IconInfo.TYPE_TITLE, "生活服务", false))
            list.add(IconInfo(IconInfo.TYPE_ICON, "房屋出租", R.drawable.life_icon_house))
            list.add(IconInfo(IconInfo.TYPE_ICON, "招聘信息", R.drawable.life_icon_recruit))
            list.add(IconInfo(IconInfo.TYPE_ICON, "汽车买卖", R.drawable.life_icon_sellcars))
            list.add(IconInfo(IconInfo.TYPE_ICON, "宠物交易", R.drawable.life_icon_pets))
            list.add(IconInfo(IconInfo.TYPE_ICON, "交易市场", R.drawable.life_icon_transaction))
            list.add(IconInfo(IconInfo.TYPE_ICON, "房屋求租", R.drawable.life_icon_rent))
            list.add(IconInfo(IconInfo.TYPE_ICON, "生意转让", R.drawable.life_icon_turn))
            list.add(IconInfo(IconInfo.TYPE_ICON, "教科书", R.drawable.life_icon_book))
            list.add(IconInfo(IconInfo.TYPE_ICON, "餐饮美食", R.drawable.life_icon_foods))
            adapter.refresh(list)
            headAdapter.notifyDataSetChanged()
        } else {
            topbar.setTitleText("选择黄页").setTopBarClickListener(this)
            tvTip.text = "请选择您要发布的黄页类型"
            requestDataList()
        }

        adapter.block = {
            if(isLoginToActivity) {
                if (isLife) {
                    when (it.title) {
                        C.IconType.房屋出租 -> {
                            ReleaseHouseRentActivity.start(mContext)
                        }
                        C.IconType.招聘信息 -> {
                            ReleaseRecruitmentActivity.start(mContext)
                        }
                        C.IconType.汽车买卖 -> {
                            ReleaseCarSaleActivity.start(mContext)
                        }
                        C.IconType.宠物交易 -> {
                            ReleasePetTransactionSelectTypeActivity.start(mContext)
                        }
                        C.IconType.交易市场 -> {
                            ReleaseTransactionMarketActivity.start(mContext)
                        }
                        C.IconType.房屋求租 -> {
                            ReleaseHouseDemandActivity.start(mContext)
                        }
                        C.IconType.生意转让 -> {
                            ReleaseBusinessTransferActivity.start(mContext)
                        }
                        C.IconType.教科书 -> {
                            ReleaseTextbookActivity.start(mContext)
                        }
                        C.IconType.餐饮美食 -> {
                            ReleaseShopActivity.start(mContext)
                        }
                    }
                } else {
                    ReleaseYellowPageActivity.start(mContext, it.oid)
                }
            }
        }
    }

    private fun requestYellowPageList() {
        val list = mutableListOf<IconInfo>()
        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "热门服务", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "搬家物流"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "汽车维修"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "清洁服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "移民留学"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "语言培训"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "电工监控"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "接机服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "装修建筑"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "餐饮美食"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "机票旅行"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "房产物业"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "水管木工"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "婚庆摄影"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "其他"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "汽车服务", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "汽车买卖"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "搬家物流"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "接机服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "汽车维修"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "驾校教练"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "专业人士", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "移民留学"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "房产物业"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "律师"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "会计税务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "翻译"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "金融贷款"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "医生"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "理财保险"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "电子通讯", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "手机维修"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "电脑网络"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "收音机"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "建材", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "地板地毯"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "门窗玻璃"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "窗帘布艺"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "油漆"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "灯具"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "装修维护", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "室内设计"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "Gyprock间隔"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "装修建筑"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "修理安装"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "厨卫家具"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "电子监控"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "水管木工"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "室外服务", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "凉棚车库"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "空调冷气"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "除虫白蚁"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "花园园艺"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "屋顶补漏"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "泳池维护"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "教育培训", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "语言"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "培训证书"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "运动/舞蹈"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "音乐美术"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "宠物服务", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "宠物服务"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "清洁", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "清洁服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "除虫白蚁"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "其他类别", false))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "餐饮美食"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "旅行机票"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "广告印刷"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "美容/美发"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "母婴幼教"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "医疗保健"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "婚庆摄影"))
        //底部添加一个大间距
        for (i in 1..4) list.add(IconInfo(IconInfo.TYPE_FILLING))

        adapter.refresh(list)
        headAdapter.notifyDataSetChanged()
    }


    private fun requestDataList() {
        showLoading()
        val subscription = ApiManger.getApiService().allYellowPageIconList
                .compose(RxUtil.normalSchedulers<ResultBase<List<HashMap<String, Any>>>>())
                .subscribe(object : ApiSuccessAction<ResultBase<List<HashMap<String, Any>>>>() {
                    override fun onSuccess(result: ResultBase<List<HashMap<String, Any>>>) {
                        disMissLoadingView()
                        result.data.forEach {
                            val list = mutableListOf<IconInfo>()
                            val childList = if (it["list"] != null) {
                                GsonUtil.parseList(GsonUtil.toJson(it["list"]), IconInfo::class.java)
                            } else {
                                listOf()
                            }
                            childList.forEach { it.itemType = IconInfo.TYPE_CLASSIFICATION }
                            list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, it["title"].toString(), false))
                            list.addAll(childList)
                            list.add(IconInfo(IconInfo.TYPE_FILLING))
                            adapter.loadMore(list)
                        }
                        headAdapter.notifyDataSetChanged()
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        disMissLoadingView()
                        showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        disMissLoadingView()
                        showToast(msg)
                    }
                })
        addSubscrebe(subscription)
    }

}
