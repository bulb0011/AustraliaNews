package com.ruanyun.australianews.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @description 房屋出租筛选
 * @author hdl
 * @date 2019/5/21
 */
class LifeHousesFilterPopWindow(context: Context) : BaseLifeFilterPopWindow(context), PopupWindow.OnDismissListener {
    override fun onDetermine() {
        val oid1 = adapter.datas.filter {
            it.parentCode == C.ParentCode.HOUSING_SOURCE && it.isSelect
        }[0].code

        val oid2 = adapter.datas.filter {
            it.parentCode == C.ParentCode.RENTAL_METHOD && it.isSelect
        }[0].code

        val oid3 = adapter.datas.filter {
            it.parentCode == C.ParentCode.HOUSE_HUXING && it.isSelect
        }[0].code
        block.invoke(oid1, oid2, oid3)
    }

    override fun getDataList(): MutableList<ParentCodeInfo> {
        val listInfo = mutableListOf<ParentCodeInfo>()
        listInfo.add(ParentCodeInfo("来源", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.HOUSING_SOURCE, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSING_SOURCE))

        listInfo.add(ParentCodeInfo("方式", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.RENTAL_METHOD, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.RENTAL_METHOD))

        listInfo.add(ParentCodeInfo("户型", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.HOUSE_HUXING, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSE_HUXING))
        return listInfo
    }

    var block = fun (_: String?, _: String?, _: String?){}

}

