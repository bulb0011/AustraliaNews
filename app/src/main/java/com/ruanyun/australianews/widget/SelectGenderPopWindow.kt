package com.ruanyun.australianews.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView

import com.ruanyun.australianews.R


/**
 * @description 选择性别
 * @author hdl
 * @date 2019/5/10
 */
class SelectGenderPopWindow(private val mContext: Context) : PopupWindow(), PopupWindow.OnDismissListener {

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

        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_select_gender, null)
        val tvMale = view.findViewById<View>(R.id.tv_male) as TextView
        val tvWoman = view.findViewById<View>(R.id.tv_woman) as TextView
        val tvCancel = view.findViewById<View>(R.id.tv_cancel) as TextView
        tvMale.setOnClickListener {
            block.invoke(mContext.resources.getString(R.string.nan))
            dismiss()
        }
        tvWoman.setOnClickListener {
            block.invoke(mContext.resources.getString(R.string.nv))
            dismiss()
        }
        //取消按钮事件
        tvCancel.setOnClickListener { dismiss() }
        contentView = view
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        setOnDismissListener(this)
    }

    /**
     * 居中弹出popu窗口
     * @param parent
     */
    fun showPopupWindow(parent: View) {
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
    fun backgroundAlpha(bgAlpha: Float) {
        val lp = (mContext as Activity).window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        mContext.window.attributes = lp
    }

    override fun onDismiss() {
        backgroundAlpha(1f)
    }

    var block = fun(_: String) {}

}
