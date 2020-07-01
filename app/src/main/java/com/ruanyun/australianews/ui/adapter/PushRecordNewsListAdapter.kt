package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.ext.*
import com.ruanyun.australianews.model.*
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.DateUtil
import com.ruanyun.australianews.util.GsonUtil
import com.ruanyun.australianews.util.StringUtil
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 推送记录新闻列表
 * @author hdl
 * @date 2019/6/12
 */
class PushRecordNewsListAdapter(context: Context, datas: List<LifeReleaseCommonUiModel>) : LifeReleaseCommonAdapter<LifeReleaseCommonUiModel>(context, datas) {

    init {
        /**
         * 时间
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_push_record_time
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.getNewsCommonInfo()?.isTimeType==true
            }

            override fun convert(holder: ViewHolder?, t: LifeReleaseCommonUiModel, position: Int) {
                holder?.getView<View>(R.id.view_top_line)?.visibility = if (position == 0) View.GONE else View.VISIBLE
                holder?.setText(R.id.tv_time, StringUtil.getNewsTime(t.getNewsCommonInfo()?.createTimeStr))
            }
        })

        /**
         * 单图片
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_single_picture
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == NewsInfo.TYPE_SINGLE_PICTURE
            }

            override fun convert(holder: ViewHolder?, t: LifeReleaseCommonUiModel, position: Int) {
                val item = t.getINewsInfo()?: return
                holder?.getView<ImageView>(R.id.iv_photo)?.loadImage(item.mainPhoto.toImgUrl())
                updateStatusChange(holder, t)

            }
        })

        /**
         * 多图片
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_many_picture
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == NewsInfo.TYPE_MANY_PICTURE
            }

            override fun convert(holder: ViewHolder?, t: LifeReleaseCommonUiModel, position: Int) {
                val item = t.getINewsInfo()?: return
                updatePhotoList(holder, item)
                updateStatusChange(holder, t)
            }
        })

        /**
         * 视频
         */
        addItemViewDelegate(object : ItemViewDelegate<LifeReleaseCommonUiModel> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_video
            }

            override fun isForViewType(item: LifeReleaseCommonUiModel, position: Int): Boolean {
                return item.itemType == NewsInfo.TYPE_VIDEO
            }

            override fun convert(holder: ViewHolder?, t: LifeReleaseCommonUiModel, position: Int) {
                val item = t.getINewsInfo()?: return
                holder?.getView<ImageView>(R.id.iv_video_main_photo)?.loadImage(item.mainPhoto.toImgUrl())
                updateStatusChange(holder, t)
            }
        })
    }


    private fun updateStatusChange(holder: ViewHolder?, t: LifeReleaseCommonUiModel?) {
        t?:return
        holder?.setText(R.id.tv_title, t.commonTitle)
        holder?.setText(R.id.tv_source, NewsInfo.getSourceStr(t.getINewsInfo()!!))
//        holder?.getView<TextView>(R.id.tv_hot)?.visibility = if(t.newsInfo!!.isHot==1)View.VISIBLE else View.GONE
        val tvUnread = holder?.getView<TextView>(R.id.tv_unread)
//        tvUnread?.visibility = View.GONE
//        if (true) {
//            tvUnread?.text = "未读"
//            tvUnread?.setBackgroundResource(R.drawable.corners_orange_red_bg_shape)
//        } else {
//            tvUnread?.text = "已读"
//            tvUnread?.setBackgroundResource(R.drawable.corners_hot_bg_shape)
//        }
        holder?.convertView?.click {
            WebViewUrlUtil.showNewsDetailsWeb(mContext, t.getINewsInfo()!!)
        }
    }

    private fun updatePhotoList(holder: ViewHolder?, item: NewsInfo) {
        val ivPic1 = holder?.getView<ImageView>(R.id.iv_pic_1)
        val ivPic2 = holder?.getView<ImageView>(R.id.iv_pic_2)
        val ivPic3 = holder?.getView<ImageView>(R.id.iv_pic_3)

        if (item.mainPhotoList != null && item.mainPhotoList!!.isNotEmpty()) {
            ivPic1?.visibility = View.VISIBLE
            ivPic1?.loadImage(item.mainPhotoList!![0].toImgUrl())
            if (item.mainPhotoList!!.size > 1) {
                ivPic2?.visibility = View.VISIBLE
                ivPic2?.loadImage(item.mainPhotoList!![1].toImgUrl())
            } else {
                ivPic2?.visibility = View.INVISIBLE
            }
            if (item.mainPhotoList!!.size > 2) {
                ivPic3?.visibility = View.VISIBLE
                ivPic3?.loadImage(item.mainPhotoList!![2].toImgUrl())
            } else {
                ivPic3?.visibility = View.INVISIBLE
            }
        } else {
            ivPic1?.visibility = View.INVISIBLE
            ivPic2?.visibility = View.INVISIBLE
            ivPic3?.visibility = View.INVISIBLE
        }
    }

    override fun refresh(datas: MutableList<LifeReleaseCommonUiModel>) {
//        dataProcess(datas)
        mDatas = dataProcess(datas)
        timeItemTypeProcess()
        notifyDataSetChanged()
    }

    override fun loadMore(datas: MutableList<LifeReleaseCommonUiModel>) {
//        dataProcess(datas)
        mDatas.addAll(dataProcess(datas))
        timeItemTypeProcess()
        notifyDataSetChanged()
    }

    private fun dataProcess(datas: MutableList<LifeReleaseCommonUiModel>): List<LifeReleaseCommonUiModel> {
        val infoList = mutableListOf<LifeReleaseCommonUiModel>()
        datas.forEach {
            var info: LifeReleaseCommonUiModel? = null
            when(it.itemType){
                LifeReleaseCommonUiModel.NEWS_INFO -> {
                    val newsInfo = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, NewsInfo::class.java)
                    if (newsInfo.type == 1) {//图文新闻
                        if (TextUtils.isEmpty(newsInfo.mainPhoto)) {
                            newsInfo.pushShowType = NewsInfo.TYPE_MANY_PICTURE
                        } else {
                            val str = newsInfo.mainPhoto?.split(",")?: arrayListOf()
                            if (str.size > 1) {
                                newsInfo.pushShowType = NewsInfo.TYPE_MANY_PICTURE
                                newsInfo.mainPhotoList = str
                            } else {
                                newsInfo.pushShowType = NewsInfo.TYPE_SINGLE_PICTURE
                            }
                        }
                    } else if (newsInfo.type == 2) {//视频新闻
                        newsInfo.pushShowType = NewsInfo.TYPE_VIDEO
                    } else {
                        newsInfo.pushShowType = NewsInfo.TYPE_SINGLE_PICTURE
                    }
                    newsInfo.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(newsInfo)
                }
                0->{
                    val advertInfo = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, AdvertInfoBase::class.java)
                    advertInfo.mark = true
                    advertInfo.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(advertInfo)
                }
                2 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeHouseRentInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                3 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeRecruitmentInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                4 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeCarSaleInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                5 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifePetTransactionInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                6 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeTransactionMarketInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                7 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeHouseDemandInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                8 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeBusinessTransferInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                9 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeTextbookInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                10 -> {
                    info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeFoodShopInfo::class.java)
                    info.setNewsCommonInfo(it as PushRecordNewsInfo)
                    infoList.add(info)
                }
                else -> {
                    if (it.itemType >= 100) {
                        info = GsonUtil.parseJson(it.getNewsCommonInfo()?.content, LifeYellowPageInfo::class.java)
                        info.setNewsCommonInfo(it as PushRecordNewsInfo)
                        infoList.add(info)
                    }
                }
            }

        }
        return infoList
    }

    private fun timeItemTypeProcess() {
        val oldList = mDatas.filter {it.getNewsCommonInfo()?.isTimeType != true }
        if (oldList.isEmpty()) {
            return
        }
        val newList = mutableListOf<LifeReleaseCommonUiModel>()
        var timeInfo = PushRecordNewsInfo()
        timeInfo.isTimeType = true
        var time = oldList[0].getNewsCommonInfo()?.createTimeStr?.toYMD()
        if(time == DateUtil.getCurrentTimeYMD()){
            time = "今天"
        }
        timeInfo.createTimeStr = time
        timeInfo.setNewsCommonInfo(timeInfo)
        newList.add(timeInfo)
        newList.add(oldList[0])
        oldList.forEachIndexed { index, it ->
            if (index == 0) return@forEachIndexed
            if (it.getNewsCommonInfo()?.createTimeStr?.toYMD() == oldList[index - 1].getNewsCommonInfo()?.createTimeStr?.toYMD()) {
                newList.add(it)
            } else {
                timeInfo = PushRecordNewsInfo()
                timeInfo.isTimeType = true
                timeInfo.createTimeStr = it.getNewsCommonInfo()?.createTimeStr?.toYMD()
                timeInfo.setNewsCommonInfo(timeInfo)
                newList.add(timeInfo)
                newList.add(it)
            }
        }
        mDatas.clear()
        mDatas.addAll(newList)
    }

}
