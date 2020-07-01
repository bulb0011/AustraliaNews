package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.loadImage
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.StringUtil
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 生活公共列表
 * @author hdl
 * @date 2019/5/20
 */
open class LifeReleaseCommonAdapter<T>(context: Context, datas: List<LifeReleaseCommonUiModel>) :
    AdvertInfoBaseAdapter<LifeReleaseCommonUiModel>(context, datas) {

    var isEditMode = false
    var isMyRelease = false

    var ctx:Context

    init {

        this.ctx=context

        /**
         * 房屋出租
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_house_rent
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_house_type, item.getCommonType())
                holder.setText(R.id.tv_distance, item.getDistanceStr())
                holder.setVisible(R.id.tv_distance, CommonUtil.isNotEmpty(item.getDistanceStr()))
                holder.setText(R.id.tv_reside_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString(ctx.resources.getString(R.string.mimyi))
                    sp.setSpan(RelativeSizeSpan(1.4f), 0, sp.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    tvMoney.text = sp
                }else {
                    val sp = SpannableString("$${item.getMoneyStr()}/周")
                    sp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_gray)), sp.length - 2, sp.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    sp.setSpan(RelativeSizeSpan(1.4f), 0, sp.length-2, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    tvMoney.text = sp
                }
                updateStatusChange(holder, item)
            }
        })

        /**
         * 房屋求租
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_house_demand
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                updateStatusChange(holder, item)
            }
        })

        /**
         * 交易市场
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_transaction_market
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_TRANSACTION_MARKET_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString(ctx.resources.getString(R.string.mimyi))
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

        /**
         * 招聘信息
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_recruitment
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_type, item.getCommonType())
                holder.setText(R.id.tv_experience_requirements, item.getExperienceRequirementsStr())
                holder.setText(R.id.tv_update_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString(ctx.resources.getString(R.string.mimyi))
                    sp.setSpan(RelativeSizeSpan(1.4f), 0, sp.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    tvMoney.text = sp
                }else {
                    val sp = SpannableString("$${item.getMoneyStr()}/小时")
                    sp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_gray)), sp.length - 3, sp.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    sp.setSpan(RelativeSizeSpan(1.4f), 0, sp.length-3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    tvMoney.text = sp
                }
                updateStatusChange(holder, item)
            }
        })

        /**
         * 宠物交易
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_pet_transaction
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString(ctx.resources.getString(R.string.mimyi))
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

        /**
         * 生意转让
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_business_transfer
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString(ctx.resources.getString(R.string.mimyi))
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

        /**
         * 汽车买卖
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_car_sale
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString(ctx.resources.getString(R.string.mimyi))
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

        /**
         * 教科书
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_textbook
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_update_time, ctx.resources.getString(R.string.gengxinyu)+" ${StringUtil.getLifeTime(item.commonTime)}")
                val tvMoney = holder.getView<TextView>(R.id.tv_money)
                if(isNegotiable(item.getMoneyStr())){
                    val sp = SpannableString(ctx.resources.getString(R.string.mimyi))
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

        /**
         * 餐饮美食
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_life_food_shop
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_REPAST_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_pic).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_business_hours, StringUtil.getLifeTime(item.commonTime))
                val tvOperateStatus = holder.getView<TextView>(R.id.tv_operate_status)
                if(item.getShopStatus()){
                    tvOperateStatus.text = "营业中"
                }else {
                    tvOperateStatus.text = "休息中"
                }
                updateStatusChange(holder, item)
            }
        })

        /**
         * 黄页
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_yellow_page_info
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO
            }

            override fun convert(holder: ViewHolder, item: LifeReleaseCommonUiModel, position: Int) {
                holder.getView<ImageView>(R.id.iv_photo).loadImage(item.commonMainPhoto)
                holder.setText(R.id.tv_title, item.commonTitle)
                holder.setText(R.id.tv_source, item.getYellowPageCompanyName())
                val ivCall = holder.getView<ImageView>(R.id.iv_call)
                if(TextUtils.isEmpty(item.getLifePhone())){
                    ivCall.visibility = View.GONE
                }else {
                    ivCall.visibility = View.VISIBLE
                    ivCall.clickWithTrigger {
                        mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+item.getLifePhone())))
                    }
                }
                updateStatusChange(holder, item)
            }
        })

    }

    private fun updateStatusChange(holder: ViewHolder?, item: LifeReleaseCommonUiModel) {
        val ivSelect = holder?.getView<ImageView>(R.id.iv_select)
        ivSelect?.visibility = if(isEditMode) View.VISIBLE else View.GONE
        ivSelect?.isSelected = item.isSelect

//        holder?.convertView?.clickWithTrigger {
//            if(isEditMode){
//                item.isSelect = !item.isSelect
//                ivSelect?.isSelected = item.isSelect
//            }else {
//                WebViewUrlUtil.showLifeDetailsWeb(mContext, item.itemType, item.commonOid)
//            }
//        }

        ivSelect?.click {
            if(isEditMode){
                item.isSelect = !item.isSelect
                ivSelect.isSelected = item.isSelect
            }
        }
        holder?.getView<RelativeLayout>(R.id.rl_show_content)?.click {
            if(isEditMode){
                if(isMyRelease) {
                    WebViewUrlUtil.showLifeRelease(mContext, item.itemType, item.commonOid)
                }else {
                    item.isSelect = !item.isSelect
                    ivSelect?.isSelected = item.isSelect
                }
            }else {
                WebViewUrlUtil.showLifeDetailsWeb(mContext, item.itemType, item.commonOid)
            }
        }
    }


    /**
     * 是否是面议
     */
    private fun isNegotiable(money: String?): Boolean{
        return if(TextUtils.isEmpty(money)){
            true
        }else {
            if(money!!.toDoubleOrNull() == null){
                return true
            }else {
                money.toDouble() <=0
            }
        }
    }

}
