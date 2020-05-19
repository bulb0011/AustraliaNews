package com.ruanyun.australianews.widget

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.util.CommonUtil


/**
 * Created by  on 2017/1/17.
 */

class LeaveMessageDialogFragment : DialogFragment(), View.OnClickListener {

    lateinit var contentView: View
    lateinit var mContext: Context
    private var tvSend: TextView? = null
    private var etLeaveMessage: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView = inflater.inflate(R.layout.dialog_leave_message, container, false)
        mContext = contentView.context
        initView()
        return contentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.attributes = params
        }

    }

    private fun initView() {
        tvSend = contentView.findViewById(R.id.tv_send)
        etLeaveMessage = contentView.findViewById(R.id.et_leave_message)
        etLeaveMessage!!.isFocusable = true
        etLeaveMessage!!.isFocusableInTouchMode = true
        etLeaveMessage!!.requestFocus()
        tvSend!!.setOnClickListener(this)
        etLeaveMessage!!.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()){
                    tvSend!!.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
                }else {
                    tvSend!!.setTextColor(ContextCompat.getColor(mContext, R.color.theme_color))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tv_send) {
            inputComplete()
        }
    }

    fun show(manager: FragmentManager?, context: Context) {
        super.show(manager, "LeaveMessageDialogFragment")
        if (etLeaveMessage != null) {
            etLeaveMessage!!.isFocusable = true
            etLeaveMessage!!.isFocusableInTouchMode = true
            etLeaveMessage!!.requestFocus()
        }
        showInputMethod(context)
    }

    fun addSuccess(){
        etLeaveMessage!!.setText("")
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        hideSoftInput()
    }

    private fun inputComplete() {
        val str = etLeaveMessage!!.getStr()
        if (CommonUtil.isNotEmpty(str)) {
            block.invoke(str)
        }
    }

    var block = fun(s:String) {}

    private fun showInputMethod(context: Context) {
        //自动弹出键盘
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Handler().postDelayed({ inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS) }, 100)
    }

    fun hideSoftInput() {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 隐藏软键盘
        if (imm != null && imm.isActive && mContext is Activity) {
            imm.hideSoftInputFromWindow((mContext as Activity).getWindow().getDecorView().getWindowToken(), 0)
        }
    }

    companion object {

        fun newInstance(): LeaveMessageDialogFragment {
            val fragment = LeaveMessageDialogFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}