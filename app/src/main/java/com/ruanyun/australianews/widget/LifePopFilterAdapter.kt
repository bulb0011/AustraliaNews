package com.ruanyun.australianews.widget

import android.content.Context
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.model.ParentCodeInfo
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @description
 * @author hdl
 * @date 2019/5/27
 */
class LifePopFilterAdapter(context: Context, datas: List<ParentCodeInfo>) : RvMuiltItemAdapter<ParentCodeInfo>(context, datas) {

    companion object {
        const val FILTER_TITLE = "FILTER_TITLE"
    }

    init {
        /**
         * 标题
         */
        addItemViewDelegate(object : ItemViewDelegate<ParentCodeInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_houses_filter_title
            }

            override fun isForViewType(item: ParentCodeInfo, position: Int): Boolean {
                return item.code == FILTER_TITLE
            }

            override fun convert(holder: ViewHolder, item: ParentCodeInfo, position: Int) {
                holder.setText(R.id.tv_title, item.name)
            }
        })

        /**
         * item
         */
        addItemViewDelegate(object : ItemViewDelegate<ParentCodeInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_houses_filter_item
            }

            override fun isForViewType(item: ParentCodeInfo, position: Int): Boolean {
                return item.code != FILTER_TITLE
            }

            override fun convert(holder: ViewHolder, item: ParentCodeInfo, position: Int) {
                val tvItem = holder.getView<TextView>(R.id.tv_item)
                tvItem.text = item.name
                tvItem.isSelected = item.isSelect
                holder.convertView.click {
                    if(!item.isSelect){
                        mDatas.forEach {
                            if(it.parentCode == item.parentCode){
                                it.isSelect = false
                            }
                        }
                        item.isSelect = true
                        notifyDataSetChanged()
                    }
                }
            }
        })

    }

}