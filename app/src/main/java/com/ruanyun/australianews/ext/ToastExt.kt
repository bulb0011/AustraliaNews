package com.ruanyun.australianews.ext

import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.ruanyun.australianews.R

/**
 * Description:
author: wangwu on 2018/9/7 11:09.
 */

fun Activity.showToast(msg: String) {
    showToast(this, msg)
}

fun Fragment.showToast(msg: String) {
    this.activity?.let { showToast(it, msg) }
}

fun Fragment.showToast(@StringRes tip: Int) {
    this.activity?.let { showToast(it, this.resources.getString(tip)) }
}


fun PopupWindow.showToast(msg: String) {
    showToast(this.contentView.context, msg)
}

fun Activity.showToast(@StringRes tip: Int) {
    showToast(this, this.resources.getString(tip))
}

private fun showToast(context: Context, tip: String) {
    val contentView: View
    if (null == toast) {
        toast = Toast(context.applicationContext)
        contentView = LayoutInflater.from(context.applicationContext).inflate(R.layout.view_custom_toast, null)
        toast?.view = contentView
        toast?.setGravity(Gravity.CENTER, 0, 0)
        toast?.duration = Toast.LENGTH_SHORT
    } else {
        contentView = toast?.view!!
    }
    setToastViewAttr(contentView, tip)
    toast?.show()
}

private fun setToastViewAttr(contentView: View?, tip: String) {
    if (null == contentView) return
    contentView.background.alpha = 150
    val tvTips = contentView.findViewById<View>(R.id.tv_message) as TextView
    tvTips.text = tip
}

var toast: Toast? = null
