package com.ruanyun.australianews.ui.wealth.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.loadImage
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel
import com.ruanyun.australianews.util.StringUtil
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 生活公共列表
 * @author hdl
 * @date 2020/3/26
 */
open class WealthCommonAdapter<T: WealthCommonUiModel>(context: Context, datas: List<T>) :
    RvMuiltItemAdapter<T>(context, datas) {

    var isEditMode = false
    var isMyRelease = false
    var isShowLabel = false

    init {
        /**
         * 基金
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_wealth_fund
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.WEALTH_FUND
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_net_worth, item.getCommonNetWorth())
                val tvGrowthRate = holder.getView<TextView>(R.id.tv_growth_rate)
                tvGrowthRate.text = item.getCommonGrowthRate()
                if(item.getCommonGrowthRate().startsWith("-")){
                    tvGrowthRate.setTextColor(ContextCompat.getColor(mContext, R.color.color_fund_green))
                }else {
                    tvGrowthRate.setTextColor(ContextCompat.getColor(mContext, R.color.color_fund_red))
                }
                updateStatusChange(holder, item)
            }
        })

        /**
         * 活动
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_wealth_activitys
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.WEALTH_ACTIVITYS
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, "更新于 ${StringUtil.getLifeTime(item.commonTime)}")
                holder.setText(R.id.tv_address, item.getCommonAddress())
                updateStatusChange(holder, item)
            }
        })

        /**
         * 房市
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_wealth_housing_market
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, "更新于 ${StringUtil.getLifeTime(item.commonTime)}")
                holder.setText(R.id.tv_address, item.getCommonAddress())
                updateStatusChange(holder, item)
            }
        })

        /**
         * 民用地产
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_wealth_housing_market
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, "更新于 ${StringUtil.getLifeTime(item.commonTime)}")
                holder.setText(R.id.tv_address, item.getCommonAddress())
                updateStatusChange(holder, item)
            }
        })

        /**
         * 商业地产
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_wealth_housing_market
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, "更新于 ${StringUtil.getLifeTime(item.commonTime)}")
                holder.setText(R.id.tv_address, item.getCommonAddress())
                updateStatusChange(holder, item)
            }
        })

        /**
         * 农场
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_wealth_housing_market
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, "更新于 ${StringUtil.getLifeTime(item.commonTime)}")
                holder.setText(R.id.tv_address, item.getCommonAddress())
                updateStatusChange(holder, item)
            }
        })

        /**
         * 生意
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_business_transfer
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, "更新于 ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString("面议")
                    sp.setSpan(RelativeSizeSpan(1.4f), 0, sp.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    tvMoney.text = sp
                }else {
                    val sp = SpannableString("$${item.getMoneyStr()}")
                    sp.setSpan(RelativeSizeSpan(1.4f), 0, sp.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    tvMoney.text = sp
                }
                updateStatusChange(holder, item)
            }
        })

    }

    private fun updateStatusChange(holder: ViewHolder?, item: WealthCommonUiModel) {
        val tvLabel = holder?.getView<TextView>(R.id.tv_label)
        tvLabel?.apply {
            tvLabel.visibility = if(isShowLabel) View.VISIBLE else View.GONE
            tvLabel.text = when(item.itemType){
                LifeReleaseCommonUiModel.WEALTH_ACTIVITYS -> "活动"
                LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET -> "房市"
                LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE -> "民用"
                LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE -> "商业"
                LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE -> "农场"
                LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO -> "生意"
                else -> ""
            }
        }
        val ivSelect = holder?.getView<ImageView>(R.id.iv_select)
        ivSelect?.visibility = if(isEditMode) View.VISIBLE else View.GONE
        ivSelect?.isSelected = item.isSelect

        ivSelect?.click {
            if(isEditMode){
                item.isSelect = !item.isSelect
                ivSelect.isSelected = item.isSelect
            }
        }
        holder?.getView<RelativeLayout>(R.id.rl_show_content)?.clickWithTrigger {
            if(isEditMode){
                if(isMyRelease) {
                    WebViewUrlUtil.showWealthRelease(mContext, item.itemType, item.commonOid)
                }else {
                    item.isSelect = !item.isSelect
                    ivSelect?.isSelected = item.isSelect
                }
            }else {
                WebViewUrlUtil.showWealthDetailsWeb(mContext, item.itemType, item.commonOid, item.commonTitle,item.commonMainPhoto)
            }
        }
    }


    /**
     * 是否是面议
     */
    private fun isNegotiable(money: String?): Boolean {
        return if (TextUtils.isEmpty(money)) {
            true
        } else {
            if (money!!.toDoubleOrNull() == null) {
                return true
            } else {
                money.toDouble() <= 0
            }
        }
    }

}
