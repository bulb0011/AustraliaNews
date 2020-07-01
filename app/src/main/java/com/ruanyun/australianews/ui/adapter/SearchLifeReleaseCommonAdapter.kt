package com.ruanyun.australianews.ui.adapter

import android.content.Context
import com.ruanyun.australianews.model.*
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.*


/**
 * @description 搜索生活发布及黄页生活公共列表
 * @author hdl
 * @date 2019/5/30
 */
open class SearchLifeReleaseCommonAdapter(context: Context, datas: List<LifeSearchResultInfo>) :
        LifeReleaseCommonAdapter<LifeSearchResultInfo>(context, datas) {



    override fun refresh(datas: MutableList<LifeReleaseCommonUiModel>) {
        mDatas.clear()
        mDatas.addAll(processDataList(datas))
        notifyDataSetChanged()
    }

    override fun loadMore(datas: MutableList<LifeReleaseCommonUiModel>) {
        mDatas.addAll(processDataList(datas))
        notifyDataSetChanged()
    }

    private fun processDataList(datas: MutableList<LifeReleaseCommonUiModel>): MutableList<LifeReleaseCommonUiModel>{
        val infoList = mutableListOf<LifeReleaseCommonUiModel>()
        datas.forEach {
            var info : LifeReleaseCommonUiModel?=null
            when(it.itemType){
                2 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeHouseRentInfo::class.java)
                }
                3 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeRecruitmentInfo::class.java)
                }
                4 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeCarSaleInfo::class.java)
                }
                5 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifePetTransactionInfo::class.java)
                }
                6 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeTransactionMarketInfo::class.java)
                }
                7 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeHouseDemandInfo::class.java)
                }
                8 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeBusinessTransferInfo::class.java)
                }
                9 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeTextbookInfo::class.java)
                }
                10 -> {
                    info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeFoodShopInfo::class.java)
                }
                else ->{
                    if(it.itemType>=100){
                        info = GsonUtil.parseJson(it.getLifeOrYellowPageInfo(), LifeYellowPageInfo::class.java)
                    }
                }
            }
            if(info!=null) {
                info.setLifeOrYellowPageCollectionBrowseOid(it.getLifeOrYellowPageCollectionBrowseOid())
                infoList.add(info)
            }
        }
        return infoList
    }

}
