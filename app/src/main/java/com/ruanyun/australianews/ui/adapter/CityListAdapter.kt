package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.util.SparseIntArray
import android.widget.SectionIndexer
import com.ruanyun.australianews.R
import com.ruanyun.australianews.model.CityInfo
import com.zhy.adapter.abslistview.MultiItemTypeAdapter
import com.zhy.adapter.abslistview.ViewHolder
import com.zhy.adapter.abslistview.base.ItemViewDelegate

import java.util.ArrayList


/**
 * 切换城市adapter
 * Created by hdl on 2017/4/10.
 */
class CityListAdapter(context: Context, datas: List<CityInfo>) : MultiItemTypeAdapter<CityInfo>(context, datas),
    SectionIndexer {
    internal var list: MutableList<String> = ArrayList()
    private val positionOfSection = SparseIntArray()
    private val sectionOfPosition = SparseIntArray()

    init {
        /**
         * 分类
         */
        addItemViewDelegate(object : ItemViewDelegate<CityInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_city_classify
            }

            override fun isForViewType(item: CityInfo, position: Int): Boolean {
                return item.itemType == CityInfo.CLASSIFY
            }

            override fun convert(holder: ViewHolder, cityInfo: CityInfo, position: Int) {
                holder.setText(R.id.tv_city_classify, cityInfo.cityName)
            }
        })
        /**
         * 内容
         */
        addItemViewDelegate(object : ItemViewDelegate<CityInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_city_content
            }

            override fun isForViewType(item: CityInfo, position: Int): Boolean {
                return item.itemType != CityInfo.CLASSIFY
            }

            override fun convert(holder: ViewHolder, cityInfo: CityInfo, position: Int) {
                holder.setText(R.id.tv_city_content, cityInfo.cityName)
            }
        })
    }

    fun setData(data: List<CityInfo>) {
        this.mDatas = data
        notifyDataSetChanged()
    }

    override fun getSections(): Array<String> {
        positionOfSection.clear()
        sectionOfPosition.clear()
        val count = count
        list.clear()
        list.add("")
        for (i in 1 until count) {
            val letter = getItem(i).classify
            var section = list.size - 1
            if (list[section] != letter) {
                list.add(letter)
                section++
                positionOfSection.put(section, i)
            }
            sectionOfPosition.put(i, section)
        }
        return list.toTypedArray()
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return positionOfSection.get(sectionIndex)
    }

    override fun getSectionForPosition(position: Int): Int {
        return sectionOfPosition.get(position)
    }

}
