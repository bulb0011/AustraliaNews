package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.loadImage
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.NewsInfo
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 首页新闻列表
 * @author hdl
 * @date 2019/5/5
 */
class NewsListAdapter(context: Context, datas: List<NewsInfo>) : AdvertInfoBaseAdapter<NewsInfo>(context, datas) {


    init {
        /**
         * 单图片
         */
        addItemViewDelegate(object : ItemViewDelegate<NewsInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_single_picture
            }

            override fun isForViewType(item: NewsInfo, position: Int): Boolean {
                return item.pushShowType == NewsInfo.TYPE_SINGLE_PICTURE
            }

            override fun convert(holder: ViewHolder, item: NewsInfo, position: Int) {
                val url = item.mainPhoto.toImgUrl()
                if (url.isNullOrEmpty()) {
                    holder.getView<ImageView>(R.id.iv_photo).visibility=View.GONE
                } else {
                    holder.getView<ImageView>(R.id.iv_photo).visibility=View.VISIBLE
                    holder.getView<ImageView>(R.id.iv_photo).loadImage(url)
                }
                holder.setText(R.id.tv_title, item.title)
                holder.setText(R.id.tv_source, NewsInfo.getSourceStr(item))

                holder.getView<TextView>(R.id.tv_hot).visibility = if(item.isHot==1){
                    View.GONE
                }else  View.VISIBLE


                if(item.isHot==1){
                    holder.getView<ImageView>(R.id.img_laba).visibility=View.VISIBLE
                }else {
                    holder.getView<ImageView>(R.id.img_laba).visibility=View.GONE
                }

                holder.convertView.clickWithTrigger { showNewsDetails(item) }
            }
        })

        /**
         * 多图片
         */
        addItemViewDelegate(object : ItemViewDelegate<NewsInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_many_picture
            }

            override fun isForViewType(item: NewsInfo, position: Int): Boolean {
                return item.pushShowType == NewsInfo.TYPE_MANY_PICTURE
            }

            override fun convert(holder: ViewHolder, item: NewsInfo, position: Int) {
                holder.setText(R.id.tv_title, item.title)
                holder.setText(R.id.tv_source, NewsInfo.getSourceStr(item))
                holder.getView<TextView>(R.id.tv_hot).visibility = if(item.isHot==1)
                    View.GONE else View.VISIBLE
                if(item.isHot==1){
                    holder.getView<ImageView>(R.id.img_laba).visibility=View.VISIBLE
                }else {
                    holder.getView<ImageView>(R.id.img_laba).visibility=View.GONE
                }
                updatePhotoList(holder, item)
                holder.convertView.clickWithTrigger { showNewsDetails(item) }
            }
        })

        /**
         * 视频
         */
        addItemViewDelegate(object : ItemViewDelegate<NewsInfo> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_news_video
            }

            override fun isForViewType(item: NewsInfo, position: Int): Boolean {
                return item.pushShowType == NewsInfo.TYPE_VIDEO
            }

            override fun convert(holder: ViewHolder, item: NewsInfo, position: Int) {
                holder.getView<ImageView>(R.id.iv_video_main_photo).loadImage(item.mainPhoto.toImgUrl())
                holder.setText(R.id.tv_title, item.title)
                holder.setText(R.id.tv_source, NewsInfo.getSourceStr(item))
                holder.getView<TextView>(R.id.tv_hot).visibility = if(item.isHot==1)View.VISIBLE else View.GONE
                holder.convertView.clickWithTrigger { showNewsDetails(item) }
            }
        })
    }

    private fun showNewsDetails(item: NewsInfo) {
        WebViewUrlUtil.showNewsDetailsWeb(mContext, item)
    }

    var block = fun(_: NewsInfo) {}

    override fun refresh(datas: MutableList<NewsInfo>) {
        mDatas = dataProcess(datas)
        notifyDataSetChanged()
    }

    override fun loadMore(datas: MutableList<NewsInfo>) {
        mDatas.addAll(dataProcess(datas))
        notifyDataSetChanged()
    }

    fun dataProcess(datas: MutableList<NewsInfo>): MutableList<NewsInfo>{
        datas.forEach {
            if (it.mark){
                return@forEach
            }
            if (it.type == 1) {//图文新闻
                if (TextUtils.isEmpty(it.mainPhoto)) {
                    it.pushShowType = NewsInfo.TYPE_MANY_PICTURE
                } else {
                    val str = it.mainPhoto?.split(",")?: arrayListOf()
                    if (str.size > 1) {
                        it.pushShowType = NewsInfo.TYPE_MANY_PICTURE
                        it.mainPhotoList = str
                    } else {
                        it.pushShowType = NewsInfo.TYPE_SINGLE_PICTURE
                    }
                }
            } else if (it.type == 2) {//视频新闻
                it.pushShowType = NewsInfo.TYPE_VIDEO
            } else {
                it.pushShowType = NewsInfo.TYPE_SINGLE_PICTURE
            }
        }
        return datas
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

}
