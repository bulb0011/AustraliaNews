package com.ruanyun.australianews.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.ruanyun.imagepicker.bean.SelectListImpl
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.refreshview.impl.RvCommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_multiple_selection.*
import java.util.*

/**
 * @description 多选列表
 * @author hdl
 * @date 2019/3/19
 */
class MultipleSelectionActivity : BaseActivity() {

    companion object {
        const val TITLE = "title"
        /** 选中返回的数据  */
        const val MULTIPLE_SELECTION_LIST = "multiple_selection_list"
        /** 筛选的集合  */
        const val SELECT_LIST = "select_list"

        fun start(activity: Activity, title: String, selectInfo: List<SelectListImpl>, requestCode: Int) {
            val starter = Intent(activity, MultipleSelectionActivity::class.java)
            starter.putExtra(TITLE, title)
            starter.putParcelableArrayListExtra(SELECT_LIST, selectInfo as ArrayList<out Parcelable>)
            activity.startActivityForResult(starter, requestCode)
        }
    }

    val adapter by lazy { MultipleSelectionAdapter(mContext, R.layout.item_list_multiple_selection, arrayListOf()) }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_multiple_selection)
        initView()
    }

    private fun initView() {
        topbar.setTitleText(intent.getStringExtra(TITLE)).setTopBarClickListener(this)
        adapter.setData(intent.getParcelableArrayListExtra(SELECT_LIST))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    override fun onTopBarRightTextClick() {
        val selectList = adapter.datas.filter { it.isSelect }
        if(selectList.isNotEmpty()){
            setResult(Activity.RESULT_OK, Intent().putParcelableArrayListExtra(MULTIPLE_SELECTION_LIST, selectList as ArrayList<out Parcelable>))
            finish()
        }else {
            showToast("至少选择一项")
        }
    }


    /**
     * @description 多选
     * @author hdl
     * @date 2019/3/19
     */
    class MultipleSelectionAdapter(context: Context, layoutId: Int, datas: List<SelectListImpl>) : RvCommonAdapter<SelectListImpl>(context, layoutId, datas){

        override fun convert(holder: ViewHolder?, t: SelectListImpl, position: Int) {
            val tvName =holder?.getView<TextView>(R.id.tv_name)
            tvName?.text = t.showName
            tvName?.isSelected = t.isSelect
            holder?.convertView?.setOnClickListener {
                t.isSelect = !t.isSelect
                notifyItemChanged(position)
            }
        }

    }

}