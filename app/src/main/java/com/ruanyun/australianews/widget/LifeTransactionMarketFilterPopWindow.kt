package com.ruanyun.australianews.widget

import android.content.Context
import android.graphics.Rect
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
import com.ruanyun.australianews.util.CommonUtil.dp2px
import com.ruanyun.australianews.util.DbHelper
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @description 交易市场筛选
 * @author hdl
 * @date 2019/5/27
 */
class LifeTransactionMarketFilterPopWindow(context: Context) : BaseLifeFilterPopWindow(context), PopupWindow.OnDismissListener {

    override fun onDetermine() {
        val oid1 = adapter.datas.filter {
            it.parentCode == C.ParentCode.NEWOID_STANDARD && it.isSelect
        }[0].code

        val oid2 = adapter.datas.filter {
            it.parentCode == C.ParentCode.WARRANTY && it.isSelect
        }[0].code

        val oid3 = adapter.datas.filter {
            it.parentCode == C.ParentCode.TRANSACTION_AREA && it.isSelect
        }[0].code
        block.invoke(oid1, oid2, oid3)
    }

    override fun getDataList(): MutableList<ParentCodeInfo> {
        val listInfo = mutableListOf<ParentCodeInfo>()
        listInfo.add(ParentCodeInfo("新旧程度", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.NEWOID_STANDARD, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.NEWOID_STANDARD))

        listInfo.add(ParentCodeInfo("保修期内", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.WARRANTY, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.WARRANTY))

        listInfo.add(ParentCodeInfo("交易区域", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.TRANSACTION_AREA, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.TRANSACTION_AREA))
        return listInfo
    }

    var block = fun (_: String?, _: String?, _: String?){}

}
