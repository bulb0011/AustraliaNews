package com.ruanyun.australianews.ext

import android.content.Context
import android.util.TypedValue


/**
 * Description:
author: wangwu on 2018/10/10 10:22.
 */


fun Context.px2Dp( pxValue:Float):Int{
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}


fun Context.dp2Px(dpValue:Float):Int{
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(dpValue: Float):Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,resources.displayMetrics).toInt()
}


fun Context.dp2px(spValue: Float):Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,resources.displayMetrics).toInt()
}