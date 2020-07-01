package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.widget.ImageView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvMuiltItemAdapter
import com.ruanyun.australianews.ext.*
import com.ruanyun.australianews.model.uimodel.AdvertInfoBaseUiModel
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder


/**
 * @description 首页新闻列表
 * @author hdl
 * @date 2019/5/5
 */
open class AdvertInfoBaseAdapter<T: AdvertInfoBaseUiModel>(context: Context, datas: List<T>) : RvMuiltItemAdapter<T>(context, datas) {


    init {
        /**
         * 广告信息
         */
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return R.layout.item_list_advert_base
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return item.getAdvertMark()
            }

            override fun convert(holder: ViewHolder, item: T, position: Int) {
                holder.getView<ImageView>(R.id.iv_photo).loadImage(item.getAdvertMainPhoto())
                holder.setText(R.id.tv_title, item.getAdvertTitle())
                holder.convertView.clickWithTrigger {
                    WebViewUrlUtil.showAdvertDetailsWeb(mContext, item)
                }
            }
        })

    }

}
