package com.ruanyun.australianews.ext

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.ruanyun.australianews.util.PixelSizeUtil
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.widget.*
import cn.addapp.pickers.picker.WheelPicker
import com.ruanyun.australianews.R
import com.ruanyun.australianews.util.CommonUtil.dp2px


/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}




/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {

    if (clickEnable()) {
        block(it as T)
    }
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 600, block: (T) -> Unit){
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}



private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

fun <T : View> T.setStatusBarHeightPaddingTop() {
    PixelSizeUtil.setStatusBarHeightPaddingTop(this)
}


fun <T : TextView> T.isEmpty(): Boolean {
    return TextUtils.isEmpty(text.toString().trim())
}

fun <T : TextView> T.isNotEmpty(): Boolean {
    return !isEmpty()
}

fun <T : TextView> T.getStr(): String {
    return text.toString().trim()
}


/**
 * 设置时间选择器的分割线颜色
 *
 * @param datePicker
 */
fun <T : DatePicker> T.setDatePickerDividerColor(colorString: String) {
    // Divider changing:

    // 获取 mSpinners
    val llFirst = this.getChildAt(0) as LinearLayout

    // 获取 NumberPicker
    val mSpinners = llFirst.getChildAt(0) as LinearLayout
    for (i in 0 until mSpinners.getChildCount()) {
        val picker = mSpinners.getChildAt(i) as NumberPicker
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dp2px(120f))
        picker.layoutParams = params

        val pickerFields = NumberPicker::class.java.getDeclaredFields()
        for (pf in pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true)
                try {
                    pf.set(picker, ColorDrawable(Color.parseColor(colorString)))//设置分割线颜色
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                break
            }
        }
    }
}



fun <T : WheelPicker> T.setDefaultStyle(mContext: Context) {
    setTopHeight(44)
    setCancelTextSize(16)
    setTextSize(18)
    setTopLineVisible(false)
    setCancelTextColor(ContextCompat.getColor(mContext, R.color.picker_color))
    setSubmitTextColor(ContextCompat.getColor(mContext, R.color.picker_color))
    setPressedTextColor(ContextCompat.getColor(mContext, R.color.picker_color))
    setSelectedTextColor(ContextCompat.getColor(mContext, R.color.picker_color))
    setLineColor(ContextCompat.getColor(mContext, R.color.picker_color))
}
