package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.loadImage
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.CollectionBrowseNewsInfo
import com.ruanyun.australianews.model.NewsInfo
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 浏览收藏新闻列表
 * @author hdl
 * @date 2019/5/5
 */
class CollectionBrowseNewsListAdapter(context: Context, datas: List<CollectionBrowseNewsInfo>) : RvMuiltItemAdapter<CollectionBrowseNewsInfo>(context, datas) {

    var isEditMode = false

    init {
        /**
         * 单图片
         */
        addItemViewDelegate(object : ItemViewDelegate<CollectionBrowseNewsInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_single_picture
            }

            override fun isForViewType(item: CollectionBrowseNewsInfo, position: Int): Boolean {
                return item.newsInfo?.pushShowType == NewsInfo.TYPE_SINGLE_PICTURE
            }

            override fun convert(holder: ViewHolder, t: CollectionBrowseNewsInfo, position: Int) {
                val item = t.newsInfo?:return
                holder.getView<ImageView>(R.id.iv_photo).loadImage(item.mainPhoto.toImgUrl())
                holder.setText(R.id.tv_title, item.title)
                holder.setText(R.id.tv_source, NewsInfo.getSourceStrtimeStamp2Date(item))
                holder.getView<TextView>(R.id.tv_hot).visibility = if(item.isHot==1)View.VISIBLE else View.GONE
                updateStatusChange(holder, t)

            }
        })

        /**
         * 多图片
         */
        addItemViewDelegate(object : ItemViewDelegate<CollectionBrowseNewsInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_many_picture
            }

            override fun isForViewType(item: CollectionBrowseNewsInfo, position: Int): Boolean {
                return item.newsInfo?.pushShowType == NewsInfo.TYPE_MANY_PICTURE
            }

            override fun convert(holder: ViewHolder, t: CollectionBrowseNewsInfo, position: Int) {
                val item = t.newsInfo?:return
                holder.setText(R.id.tv_title, item.title)
                holder.setText(R.id.tv_source, NewsInfo.getSourceStrtimeStamp2Date(item))
                holder.getView<TextView>(R.id.tv_hot).visibility = if(item.isHot==1)View.VISIBLE else View.GONE
                updatePhotoList(holder, item)
                updateStatusChange(holder, t)
            }
        })

        /**
         * 视频
         */
        addItemViewDelegate(object : ItemViewDelegate<CollectionBrowseNewsInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_video
            }

            override fun isForViewType(item: CollectionBrowseNewsInfo, position: Int): Boolean {
                return item.newsInfo?.pushShowType == NewsInfo.TYPE_VIDEO
            }

            override fun convert(holder: ViewHolder, t: CollectionBrowseNewsInfo, position: Int) {
                val item = t.newsInfo?:return
                holder.getView<ImageView>(R.id.iv_video_main_photo).loadImage(item.mainPhoto.toImgUrl())
                holder.setText(R.id.tv_title, item.title)
                holder.setText(R.id.tv_source, NewsInfo.getSourceStrtimeStamp2Date(item))
                holder.getView<TextView>(R.id.tv_hot).visibility = if(item.isHot==1)View.VISIBLE else View.GONE
                updateStatusChange(holder, t)
//                holder.convertView.clickWithTrigger {
//                    VideoNewsDetailsActivity.start(mContext, item)
//                }
            }
        })
    }

    override fun refresh(datas: MutableList<CollectionBrowseNewsInfo>) {
        dataProcess(datas)
        mDatas = datas
        notifyDataSetChanged()
        if(mDatas.size==0){
            blockEmpty.invoke()
        }
    }

    override fun loadMore(datas: MutableList<CollectionBrowseNewsInfo>) {
        dataProcess(datas)
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

    private fun dataProcess(datas: MutableList<CollectionBrowseNewsInfo>): List<CollectionBrowseNewsInfo>{
        val listInfo = datas.filter { it.newsInfo!=null }
        listInfo.forEach {
            if (it.newsInfo!!.type == 1) {//图文新闻
                if (TextUtils.isEmpty(it.newsInfo!!.mainPhoto)) {
                    it.newsInfo!!.pushShowType = NewsInfo.TYPE_MANY_PICTURE
                } else {
                    val str = it.newsInfo!!.mainPhoto?.split(",")?: arrayListOf()
                    if (str.size > 1) {
                        it.newsInfo!!.pushShowType = NewsInfo.TYPE_MANY_PICTURE
                        it.newsInfo!!.mainPhotoList = str
                    } else {
                        it.newsInfo!!.pushShowType = NewsInfo.TYPE_SINGLE_PICTURE
                    }
                }
            } else if (it.newsInfo!!.type == 2) {//视频新闻
                it.newsInfo!!.pushShowType = NewsInfo.TYPE_VIDEO
            } else {
                it.newsInfo!!.pushShowType = NewsInfo.TYPE_SINGLE_PICTURE
            }
        }
        return listInfo
    }

    private fun updateStatusChange(holder: ViewHolder?, t: CollectionBrowseNewsInfo) {
        val ivSelect = holder?.getView<ImageView>(R.id.iv_select)
        ivSelect?.visibility = if(isEditMode) View.VISIBLE else View.GONE
        ivSelect?.isSelected = t.isSelect

        holder?.convertView?.click {
            if(isEditMode){
                t.isSelect = !t.isSelect
                ivSelect?.isSelected = t.isSelect
            }else {
                WebViewUrlUtil.showNewsDetailsWeb(mContext, t.newsInfo!!)
            }
        }
    }

    private fun updatePhotoList(holder: ViewHolder, item: NewsInfo) {
        val ivPic1 = holder.getView<ImageView>(R.id.iv_pic_1)
        val ivPic2 = holder.getView<ImageView>(R.id.iv_pic_2)
        val ivPic3 = holder.getView<ImageView>(R.id.iv_pic_3)

        if (item.mainPhotoList!=null && item.mainPhotoList!!.isNotEmpty()) {
            ivPic1.visibility = View.VISIBLE
            ivPic1.loadImage(item.mainPhotoList!![0].toImgUrl())
            if (item.mainPhotoList!!.size > 1) {
                ivPic2.visibility = View.VISIBLE
                ivPic2.loadImage(item.mainPhotoList!![1].toImgUrl())
            } else {
                ivPic2.visibility = View.INVISIBLE
            }
            if (item.mainPhotoList!!.size > 2) {
                ivPic3.visibility = View.VISIBLE
                ivPic3.loadImage(item.mainPhotoList!![2].toImgUrl())
            } else {
                ivPic3.visibility = View.INVISIBLE
            }
        } else {
            ivPic1.visibility = View.INVISIBLE
            ivPic2.visibility = View.INVISIBLE
            ivPic3.visibility = View.INVISIBLE
        }
    }

    var blockEmpty = fun(){}
}
