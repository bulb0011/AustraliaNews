package com.ruanyun.australianews.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.*
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.setDatePickerDividerColor
import com.ruanyun.australianews.util.DateUtil
import com.ruanyun.australianews.util.StringUtil
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @description 选择时间弹框
 * @author hdl
 * @date 2019/2/26
 */
class SelectDateDialog(context: Context) {

    val tvCancel: TextView by lazy { view!!.findViewById<TextView>(R.id.tv_cancel) }
    val datePicker: DatePicker by lazy { view!!.findViewById<DatePicker>(R.id.datepicker) }
    val tvDetermine: TextView by lazy { view!!.findViewById<TextView>(R.id.tv_determine) }

    var tag: String? = null
    var view: View? = null

    val builder = AlertDialog.Builder(context)
    var dialog: AlertDialog? = null
    var separator = "-"

    init {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_select_time, null)
        builder.setView(view)
        tvCancel.clickWithTrigger { dismiss() }
        tvDetermine.clickWithTrigger {
            var month = (datePicker.month+1).toString()
            if(month.length==1) month = "0$month"
            var day = datePicker.dayOfMonth.toString()
            if(day.length==1) day = "0$day"
            block.invoke(tag!!, "${datePicker.year}$separator$month$separator$day")
            dismiss()
        }
        datePicker.setDatePickerDividerColor("#00ffffff")
    }

    fun show(tag: String, time: String?) {
        this.tag = tag
        if(tag == "1"){//最大今天
            datePicker.maxDate = System.currentTimeMillis()
        }else {//最小今天
            datePicker.minDate = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(DateUtil.getCurrentTimeYMD()).time
        }
        if(!TextUtils.isEmpty(time)){
            val date = try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(time)
            } catch (e: Exception) {
                e.printStackTrace()
                Date()
            }
            val dateCalendar = Calendar.getInstance()
            dateCalendar.time = date
            datePicker.updateDate(dateCalendar.get(Calendar.YEAR),dateCalendar.get(Calendar.MONTH),dateCalendar.get(Calendar.DATE))
        }
        dialog = dialog ?: builder.create()
        dialog?.show()

        val window = dialog?.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
            val params = window.attributes
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.BOTTOM
            window.attributes = params
        }
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    var block = fun (_: String, _: String){}


    /**
     * true:选择时间>=当前时间,false:选择时间<当前时间
     */
    fun isNotLessThanToday(time: String): Boolean {
        val sfd = SimpleDateFormat("yyyy${separator}MM${separator}dd", Locale.getDefault())
        val currentTime = sfd.format(Date())

        return currentTime <= time
    }

}