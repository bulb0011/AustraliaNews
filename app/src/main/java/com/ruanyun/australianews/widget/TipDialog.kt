package com.ruanyun.australianews.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.util.CommonUtil.dp2px

/**
 * @description 标题、内容、取消、确定的弹框
 * @author hdl
 * @date 2019/2/13
 */
class TipDialog(context: Context) {

    val ivDelete: ImageView by lazy { view!!.findViewById<ImageView>(R.id.iv_delete) }
    val tvTitle: TextView by lazy { view!!.findViewById<TextView>(R.id.tv_title) }
    val tvContent: TextView by lazy { view!!.findViewById<TextView>(R.id.tv_content) }
    val tvDetermine: TextView by lazy { view!!.findViewById<TextView>(R.id.tv_determine) }


    var view: View? = null

    val builder = AlertDialog.Builder(context)
    var dialog: AlertDialog? = null
    var tag: String? = null

    init {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_tip_common, null)
        builder.setView(view)
        ivDelete.setOnClickListener { dismiss() }
    }

    fun setOkClick(onclick: (view: View) -> Unit) {
        tvDetermine.setOnClickListener(onclick)
    }

    fun show(title: String, content: CharSequence, okStr: String) {
        show("", title, content, okStr)
    }

    fun show(tag: String, title: String, content: CharSequence, okStr: String) {
        this.tag = tag
        dialog = dialog ?: builder.create()
        dialog?.show()
        tvTitle.text = title
        tvContent.text = content
        tvDetermine.text = okStr

        val window = dialog?.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
            val params = window.getAttributes()
//            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
            params.width = dp2px(300f)
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.CENTER
            window.setAttributes(params)
        }
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}