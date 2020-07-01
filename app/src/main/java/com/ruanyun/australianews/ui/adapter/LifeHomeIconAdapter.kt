package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.ext.*
import com.ruanyun.australianews.model.IconInfo
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.widget.ViewPagerAdapter
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 首页新闻列表
 * @author hdl
 * @date 2019/5/5
 */
class LifeHomeIconAdapter(context: Context, datas: List<IconInfo>) : RvMuiltItemAdapter<IconInfo>(context, datas) {

    init {
        /**
         * 标题
         */
        addItemViewDelegate(object : ItemViewDelegate<IconInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_home_title
            }

            override fun isForViewType(item: IconInfo, position: Int): Boolean {
                return item.itemType == IconInfo.TYPE_TITLE || item.itemType == IconInfo.TYPE_TITLE_YELLOW_PAGE
            }

            override fun convert(holder: ViewHolder, item: IconInfo, position: Int) {
                val viewGrayLine = holder.getView<View>(R.id.view_gray_line)
                viewGrayLine.visibility = if(item.isShowGrayLine) View.VISIBLE else View.GONE
                val tvTitle = holder.getView<TextView>(R.id.tv_title)
                tvTitle.apply {
                    text = item.title
                    setTextColor(ContextCompat.getColor(mContext,
                        if(item.itemType == IconInfo.TYPE_TITLE) R.color.black else R.color.theme_color ))
                }
            }
        })

        /**
         * 图标
         */
        addItemViewDelegate(object : ItemViewDelegate<IconInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_home_icon
            }

            override fun isForViewType(item: IconInfo, position: Int): Boolean {
                return item.itemType == IconInfo.TYPE_ICON
            }

            override fun convert(holder: ViewHolder, item: IconInfo, position: Int) {
                if (item.iconRes == 0) {
                    holder.getView<ImageView>(R.id.iv_icon).loadImageNoDefault(item.mainPhoto.toImgUrl())
                } else {
                    holder.getView<ImageView>(R.id.iv_icon).setImageResource(item.iconRes)
                }
                holder.setText(R.id.tv_name, item.title)
                holder.convertView.clickWithTrigger {
                    block.invoke(item)
                }
            }
        })

        /**
         * 文字类型
         */
        addItemViewDelegate(object : ItemViewDelegate<IconInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_home_classification
            }

            override fun isForViewType(item: IconInfo, position: Int): Boolean {
                return item.itemType == IconInfo.TYPE_CLASSIFICATION
            }

            override fun convert(holder: ViewHolder, item: IconInfo, position: Int) {
                holder.setText(R.id.tv_name, item.title)
                holder.convertView.clickWithTrigger {
                    block.invoke(item)
                }
            }
        })

        /**
         * 填充空白
         */
        addItemViewDelegate(object : ItemViewDelegate<IconInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_home_filling
            }

            override fun isForViewType(item: IconInfo, position: Int): Boolean {
                return item.itemType == IconInfo.TYPE_FILLING
            }

            override fun convert(holder: ViewHolder, item: IconInfo, position: Int) {
            }
        })

        /**
         * 第三方滚动数据
         */
        addItemViewDelegate(object : ItemViewDelegate<IconInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_home_view_page
            }

            override fun isForViewType(item: IconInfo, position: Int): Boolean {
                return item.itemType == IconInfo.TYPE_VIEW_PAGE
            }

            override fun convert(holder: ViewHolder, item: IconInfo, position: Int) {
                val viewPage = holder.getView<ViewPager>(R.id.viewpager)
                val llIndicator = holder.getView<LinearLayout>(R.id.ll_indicator)
                llIndicator.removeAllViews()
                val recyclerViewList = mutableListOf<RecyclerView>()
                for (i in 0..2){
                    val recyclerView = RecyclerView(mContext)
                    var lastIndex = i*5+5
                    if(lastIndex>=item.iconInfoList.size){
                        lastIndex = item.iconInfoList.size
                    }
                    val adapter = LifeHomeIconAdapter(mContext, item.iconInfoList.subList(i*5, lastIndex))
                    adapter.block = {
                        block.invoke(it)
                    }
                    recyclerView.adapter = adapter
                    val gm = GridLayoutManager(mContext, 5)
                    gm.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
                        override fun getSpanSize(position: Int): Int {
                            val info = adapter.datas[position]
                            if (info.itemType == IconInfo.TYPE_ICON) {
                                return 1
                            }
                            return 5
                        }
                    }
                    recyclerView.layoutManager = gm
                    recyclerView.setPadding(CommonUtil.dp2px(5f),CommonUtil.dp2px(10f),CommonUtil.dp2px(5f),0)
                    recyclerViewList.add(recyclerView)
                    val indicatorView = LayoutInflater.from(mContext).inflate(R.layout.indicator_life_icon, null)
                    indicatorView.isSelected = i==0
                    llIndicator.addView(indicatorView)
                }
                val viewPageAdapter = ViewPagerAdapter(recyclerViewList)
                viewPage.adapter = viewPageAdapter
                viewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                    override fun onPageScrollStateChanged(p0: Int) {
                    }

                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                    }

                    override fun onPageSelected(p0: Int) {
                        for (i in 0 until llIndicator.childCount){
                            llIndicator.getChildAt(i).isSelected = i==p0
                        }
                    }

                })

            }
        })

    }

    var block = fun(_: IconInfo) {}

}
