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
 * @description 教科书筛选
 * @author hdl
 * @date 2019/5/27
 */
class LifeTextbookFilterPopWindow(context: Context) : BaseLifeFilterPopWindow(context), PopupWindow.OnDismissListener {

    override fun onDetermine() {
        val oid1 = adapter.datas.filter {
            it.parentCode == C.ParentCode.BOOK_NEW_OLD && it.isSelect
        }[0].code

        val oid2 = adapter.datas.filter {
            it.parentCode == C.ParentCode.BOOK_IS_NOTE && it.isSelect
        }[0].code

        val oid3 = adapter.datas.filter {
            it.parentCode == C.ParentCode.BOOK_TRAN_AREA && it.isSelect
        }[0].code

        val oid4 = adapter.datas.filter {
            it.parentCode == C.ParentCode.BOOK_SCHOOL && it.isSelect
        }[0].code
        block.invoke(oid1, oid2, oid3, oid4)
    }

    override fun getDataList(): MutableList<ParentCodeInfo> {
        val listInfo = mutableListOf<ParentCodeInfo>()
        listInfo.add(ParentCodeInfo("新旧程度", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.BOOK_NEW_OLD, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_NEW_OLD))

        listInfo.add(ParentCodeInfo("有无笔记", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.BOOK_IS_NOTE, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_IS_NOTE))

        listInfo.add(ParentCodeInfo("交易地点", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.BOOK_TRAN_AREA, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_TRAN_AREA))

        listInfo.add(ParentCodeInfo("书籍属于", LifePopFilterAdapter.FILTER_TITLE))
        listInfo.add(ParentCodeInfo("不限", C.ParentCode.BOOK_SCHOOL, true))
        listInfo.addAll(DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_SCHOOL))
        return listInfo
    }

    var block = fun (_: String?, _: String?, _: String?, _: String?){}


}
