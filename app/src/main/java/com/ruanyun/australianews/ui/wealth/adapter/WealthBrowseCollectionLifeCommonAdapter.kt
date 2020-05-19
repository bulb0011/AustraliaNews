package com.ruanyun.australianews.ui.wealth.adapter

import android.content.Context
import com.ruanyun.australianews.model.*
import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel
import com.ruanyun.australianews.util.*


/**
 * @description 浏览收藏财富公共列表
 * @author hdl
 * @date 2019/5/30
 */
open class WealthBrowseCollectionLifeCommonAdapter(context: Context, datas: List<WealthCommonUiModel>) :
    WealthCommonAdapter<WealthCommonUiModel>(context, datas) {



    override fun refresh(datas: MutableList<WealthCommonUiModel>) {
        mDatas.clear()
        mDatas.addAll(processDataList(datas))
        notifyDataSetChanged()
        if(mDatas.size==0){
            blockEmpty.invoke()
        }
    }

    override fun loadMore(datas: MutableList<WealthCommonUiModel>) {
        mDatas.addAll(processDataList(datas))
        notifyDataSetChanged()
    }

    private fun processDataList(datas: MutableList<WealthCommonUiModel>): MutableList<WealthCommonUiModel>{
        val infoList = mutableListOf<WealthCommonUiModel>()
        datas.forEach {
            var info : WealthCommonUiModel?=null
            when(it.itemType){
                1 -> {
                    info = GsonUtil.parseJson(it.commonTitle, ActivitysInfo::class.java)
                }
                2 -> {
                    info = GsonUtil.parseJson(it.commonTitle, FundInfo::class.java)
                }
                3 -> {
                    info = GsonUtil.parseJson(it.commonTitle, CivilEstateInfo::class.java)
                }
                4 -> {
                    info = GsonUtil.parseJson(it.commonTitle, CivilEstateInfo::class.java)
                }
                5 -> {
                    info = GsonUtil.parseJson(it.commonTitle, CivilEstateInfo::class.java)
                }
                6 -> {
                    info = GsonUtil.parseJson(it.commonTitle, HousingMarketInfo::class.java)
                }
                7 -> {
                    info = GsonUtil.parseJson(it.commonTitle, LifeBusinessTransferInfo::class.java)
                }
            }
            if(info!=null) {
                info.commonBrowseCollectionOid = it.commonBrowseCollectionOid
                infoList.add(info)
            }
        }
        return infoList
    }

    var blockEmpty = fun(){}

}
