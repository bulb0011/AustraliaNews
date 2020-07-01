package com.ruanyun.australianews.ui.adapter

import android.content.Context
import com.ruanyun.australianews.model.*
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.*


/**
 * @description 浏览收藏生活发布及黄页生活公共列表
 * @author hdl
 * @date 2019/5/30
 */
open class BrowseCollectionLifeCommonAdapter(context: Context, datas: List<LifeBrowseCollectionResultInfo>) :
        LifeReleaseCommonAdapter<LifeBrowseCollectionResultInfo>(context, datas) {



    override fun refresh(datas: MutableList<LifeReleaseCommonUiModel>) {
        mDatas.clear()
        mDatas.addAll(processDataList(datas))
        notifyDataSetChanged()
        if(mDatas.size==0){
            blockEmpty.invoke()
        }
    }

    override fun loadMore(datas: MutableList<LifeReleaseCommonUiModel>) {
        mDatas.addAll(processDataList(datas))
        notifyDataSetChanged()
    }

    private fun processDataList(datas: MutableList<LifeReleaseCommonUiModel>): MutableList<LifeReleaseCommonUiModel>{
        val infoList = mutableListOf<LifeReleaseCommonUiModel>()
        datas.forEach {
            val lifeGeneralInfo = it.getLifeCommonGeneralInfo()
            lifeGeneralInfo?:return@forEach
            var info : LifeReleaseCommonUiModel?=null

            when(lifeGeneralInfo.type){
                2 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeHouseRentInfo::class.java)
                }
                3 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeRecruitmentInfo::class.java)
                }
                4 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeCarSaleInfo::class.java)
                }
                5 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifePetTransactionInfo::class.java)
                }
                6 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeTransactionMarketInfo::class.java)
                }
                7 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeHouseDemandInfo::class.java)
                }
                8 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeBusinessTransferInfo::class.java)
                }
                9 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeTextbookInfo::class.java)
                }
                10 -> {
                    info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeFoodShopInfo::class.java)
                }
                else ->{
                    if(lifeGeneralInfo.type>=100){
                        info = GsonUtil.parseJson(lifeGeneralInfo.content, LifeYellowPageInfo::class.java)
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

    var blockEmpty = fun(){}

}
