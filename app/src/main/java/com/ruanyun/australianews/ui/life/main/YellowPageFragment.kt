package com.ruanyun.australianews.ui.life.main

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
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
import com.ruanyun.australianews.model.IconInfo
import com.ruanyun.australianews.ui.adapter.LifeHomeIconAdapter
import com.ruanyun.australianews.ui.life.YellowPageListActivity
import com.ruanyun.australianews.ui.main.SearchActivity
import com.ruanyun.australianews.ui.my.MyMessageListActivity
import com.ruanyun.australianews.util.GsonUtil
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.fragment_life_yellow_page.*
import java.util.HashMap
import javax.inject.Inject


/**
 * @description 生活黄页
 * @author hdl
 * @date 2019/5/5
 */
class YellowPageFragment : BaseFragment() {

    @Inject
    lateinit var apiService: ApiService

    val adapter by lazy { LifeHomeIconAdapter(mContext, arrayListOf()) }
    lateinit var headAdapter: RvHeaderFootViewAdapter<IconInfo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContentView = inflater.inflate(R.layout.fragment_life_yellow_page, container, false)
        return mContentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        requestDataList()
    }

    private fun initView() {
        iv_back.clickWithTrigger { activity?.finish() }
        tv_city.text = app.cityName

        val headView = layoutInflater.inflate(R.layout.layout_life_headview, null)
        headView.findViewById<TextView>(R.id.et_search).clickWithTrigger { SearchActivity.start(mContext, SearchActivity.LIFE_SEARCH) }
        headView.findViewById<RelativeLayout>(R.id.rl_release).clickWithTrigger { SelectReleaseTypeActivity.start(mContext) }
        headView.findViewById<RelativeLayout>(R.id.rl_message).clickWithTrigger { MyMessageListActivity.start(mContext) }
        headAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        headAdapter.addHeaderView(headView)
        recyclerView.adapter = headAdapter
        val gm = GridLayoutManager(mContext, 4)
        gm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position > 0) {
                    val info = adapter.datas[position - 1]
                    if (info.itemType == IconInfo.TYPE_CLASSIFICATION) {
                        return 1
                    }
                }
                return 4
            }
        }
        recyclerView.layoutManager = gm
        adapter.block = {
            YellowPageListActivity.start(mContext, it)
        }

        val list = mutableListOf<IconInfo>()
        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "热门服务"))
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

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "汽车服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "汽车买卖"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "搬家物流"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "接机服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "汽车维修"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "驾校教练"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "专业人士"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "移民留学"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "房产物业"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "律师"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "会计税务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "翻译"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "金融贷款"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "医生"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "理财保险"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "电子通讯"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "手机维修"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "电脑网络"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "收音机"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "建材"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "地板地毯"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "门窗玻璃"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "窗帘布艺"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "油漆"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "灯具"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "装修维护"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "室内设计"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "Gyprock间隔"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "装修建筑"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "修理安装"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "厨卫家具"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "电子监控"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "水管木工"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "室外服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "凉棚车库"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "空调冷气"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "除虫白蚁"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "花园园艺"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "屋顶补漏"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "泳池维护"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "教育培训"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "语言"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "培训证书"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "运动/舞蹈"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "音乐美术"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "宠物服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "宠物服务"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "清洁"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "清洁服务"))
        list.add(IconInfo(IconInfo.TYPE_CLASSIFICATION, "除虫白蚁"))
        list.add(IconInfo(IconInfo.TYPE_FILLING))

        list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "其他类别"))
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
    }


    private fun requestDataList() {
        showLoading()
        val subscription = apiService.popularServiceYellowPageIconList
                .flatMap { t ->
                    t.data.forEach {
                        it.itemType = IconInfo.TYPE_CLASSIFICATION
                    }
                    val list = mutableListOf<IconInfo>()
                    list.add(IconInfo(IconInfo.TYPE_TITLE_YELLOW_PAGE, "热门黄页"))
                    list.addAll(t.data)
                    list.add(IconInfo(IconInfo.TYPE_FILLING))
                    adapter.refresh(list)
                    headAdapter.notifyDataSetChanged()
                    apiService.allYellowPageIconList
                }
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
