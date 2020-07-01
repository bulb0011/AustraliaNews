package com.ruanyun.australianews.widget

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView

import com.ruanyun.australianews.ext.dp2Px
import com.ruanyun.australianews.R


/**
 * Description:
 * author: zhangsan on 2018/1/9 下午3:04.
 */

class LoadingDialog(context: Context) : Dialog(context) {

    lateinit var tvMessage: TextView

    var msg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        val contentView = LayoutInflater.from(context).inflate(R.layout.view_loading_dialog, null)
        setContentView(contentView)
        tvMessage = findViewById<View>(R.id.tv_message) as TextView
        tvMessage.text = msg
        val colorDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.black))
        colorDrawable.alpha = 120
        window?.setBackgroundDrawable(colorDrawable)
        window?.setLayout(context.dp2Px(123f), context.dp2Px(123f))
        window?.attributes?.dimAmount = 0f//activity背景不变暗
        setCanceledOnTouchOutside(true)// 点击Dialog外部消失

    }

    fun setTvMessage(msg: String) {
        this.msg = msg
    }
}
