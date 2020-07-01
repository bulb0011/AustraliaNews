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
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.DbHelper
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @description 生活服务筛选
 * @author hdl
 * @date 2019/5/21
 */
abstract class BaseLifeFilterPopWindow(context: Context) : BasePopWindow(), PopupWindow.OnDismissListener {

    private lateinit var flRoot: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvReset: TextView
    lateinit var tvDetermine: TextView

    lateinit var adapter: LifePopFilterAdapter

    init {
        mContext = context
        initView()
    }

    final override fun initView() {
        super.initView()
        animationStyle = R.style.popupAnimation_right_left
        height = RelativeLayout.LayoutParams.MATCH_PARENT
        width = RelativeLayout.LayoutParams.MATCH_PARENT
        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_houses_info_filter, null)
        flRoot = contentView.findViewById(R.id.fl_root)
        recyclerView = contentView.findViewById(R.id.recyclerView)
        tvReset = contentView.findViewById(R.id.tv_reset)
        tvDetermine = contentView.findViewById(R.id.tv_determine)


        tvReset.clickWithTrigger {
            adapter.refresh(initDataList())
        }
        tvDetermine.clickWithTrigger {
            dismiss()
            onDetermine()
        }
        flRoot.clickWithTrigger {
            dismiss()
        }

        val listInfo = initDataList()
        listInfo.filter { it.name != "不限" }.forEach { it.isSelect = false }
        adapter = LifePopFilterAdapter(mContext, listInfo)
        recyclerView.adapter = adapter
        val gm = GridLayoutManager(mContext, 3)
        gm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val info = adapter.datas[position]
                if (info.code == LifePopFilterAdapter.FILTER_TITLE) {
                    return 3
                }
                return 1
            }
        }
        recyclerView.layoutManager = gm
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val index = parent.getChildAdapterPosition(view)
                if(index == 0){

                }
                val top = 0
                val bottom = if(index == adapter.datas.size-1){
                    CommonUtil.dp2px(20f)
                }else {
                    0
                }
                outRect.set(0, top, 0, bottom)
            }
        })
    }

    fun show(parent: View) {
        showTop(parent)
    }

    private fun initDataList(): MutableList<ParentCodeInfo>{
        val listInfo = getDataList()
        listInfo.filter { it.name != "不限" }.forEach { it.isSelect = false }
        return listInfo
    }

    abstract fun onDetermine()

    abstract fun getDataList(): MutableList<ParentCodeInfo>

}

