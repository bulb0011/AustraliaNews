package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.holder.Holder
import com.ruanyun.australianews.ext.loadImage
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.AdvertInfoBase

/**
 * @author hdl
 * @description
 * @date 2019/3/4
 */
class AdverViewHolder : Holder<AdvertInfoBase> {
    private var imageView: ImageView? = null
    private var mContext: Context? = null

    override fun createView(context: Context): View {
        this.mContext = context
        imageView = ImageView(mContext).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return imageView!!
    }

    override fun UpdateUI(context: Context, position: Int, data: AdvertInfoBase) {
        imageView?.loadImage(data.getAdvertMainPhoto())
    }
}
