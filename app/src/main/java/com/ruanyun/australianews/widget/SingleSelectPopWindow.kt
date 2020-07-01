package com.ruanyun.australianews.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView

import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvCommonAdapter
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.model.NotificationInfo
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.util.StringUtil
import com.ruanyun.imagepicker.bean.SelectListImpl
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 单选
 * @author hdl
 * @date 2019/5/23
 */
class SingleSelectPopWindow(private val mContext: Context) : PopupWindow(), PopupWindow.OnDismissListener {

    lateinit var adapter : SingleSelectListAdapter<SelectListImpl>

    init {
        initView()
    }

    private fun initView() {
        setBackgroundDrawable(ColorDrawable(mContext.resources.getColor(R.color.transparent)))
        isOutsideTouchable = false
        isFocusable = true
        height = WindowManager.LayoutParams.WRAP_CONTENT
        width = WindowManager.LayoutParams.MATCH_PARENT
        animationStyle = R.style.popupwindow_anim_style

        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_select_single, null)
        val recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        val tvCancel = view.findViewById<View>(R.id.tv_cancel) as TextView

        adapter = SingleSelectListAdapter(mContext, R.layout.item_list_pop_single_select_item, arrayListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        adapter.adapterBlock = {
            dismiss()
            block.invoke(it)
        }

        //取消按钮事件
        view.setOnClickListener { dismiss() }
        tvCancel.setOnClickListener { dismiss() }
        contentView = view
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        inputMethodMode = INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        setOnDismissListener(this)
    }

    /**
     * 居中弹出popu窗口
     * @param parent
     */
    fun <T>showPopupWindow(parent: View, datas: MutableList<T>) where T : SelectListImpl{
        adapter.refresh(datas as List<SelectListImpl>?)
        showAtLocation(parent, Gravity.BOTTOM, 0, 0)
        backgroundAlpha(0.6f)
        //   5.1  有效
        (mContext as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private fun backgroundAlpha(bgAlpha: Float) {
        val lp = (mContext as Activity).window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        mContext.window.attributes = lp
    }

    override fun onDismiss() {
        backgroundAlpha(1f)
    }

    var block = fun(_: SelectListImpl) {}

}

class SingleSelectListAdapter<T: SelectListImpl>(context: Context?, layoutId: Int, datas: MutableList<T>?) : RvCommonAdapter<T>(context, layoutId, datas) {

    override fun convert(holder: ViewHolder?, item: T, position: Int) {
        val tvTitle = holder?.getView<TextView>(R.id.tv_title)
        tvTitle?.text = item.showName
        if(position==0){
            tvTitle?.setBackgroundResource(R.drawable.corner_rectangle_white_top_radius_selector)
        }else {
            tvTitle?.setBackgroundResource(R.drawable.corner_rectangle_white_selector)
        }
        holder?.convertView?.clickWithTrigger {
            adapterBlock.invoke(item)
        }
    }

    var adapterBlock = fun(_: SelectListImpl) {}
}