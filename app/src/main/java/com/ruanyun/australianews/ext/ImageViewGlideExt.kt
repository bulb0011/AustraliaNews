package com.ruanyun.australianews.ext

import android.graphics.Bitmap
import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ruanyun.australianews.R

/**
 * Description:
author: wangwu on 2018/8/27 15:11.
 */



fun ImageView.loadCircleImage(url: String) {
    Glide.with(this).load(url).apply(RequestOptions.circleCropTransform().placeholder(R.drawable.default_user).error(R.drawable.default_user)).into(this)
}


fun ImageView.loadCircleImage(url: String, @DrawableRes errorInt: Int) {
    Glide.with(this).load(url).apply(RequestOptions.circleCropTransform().placeholder(errorInt).error(errorInt)).into(this)
}


fun ImageView.loadRoundedCornerImage(url: String, roundingRadius: Int) {
    Glide.with(this).load(url)
        .apply(RequestOptions.bitmapTransform(MultiTransformation<Bitmap>(CenterCrop(), RoundedCorners(roundingRadius))).error(
            R.drawable.default_user))
        .into(this)
}

fun ImageView.loadImage(url: Any?) {
    if(url==null)return
    Glide.with(this).load(url).apply(RequestOptions().placeholder(R.drawable.default_img).error(R.drawable.default_img))
        .into(this)
}
fun ImageView.loadImageNoDefault(url: Any?) {
    if(url==null)return
    Glide.with(this).load(url).into(this)
}
fun ImageView.loadImage(uri: Uri) {
    Glide.with(this).load(uri).apply(RequestOptions().placeholder(R.drawable.default_img).error(R.drawable.default_img))
        .into(this)
}
fun ImageView.loadCircleImage(any: Any) {
    Glide.with(this).load(any).apply(RequestOptions.circleCropTransform().placeholder(R.drawable.default_user).error(R.drawable.default_user)).into(this)
}
fun ImageView.loadCircleImageNotCache(any: Any) {
    Glide.with(this).load(any).apply(RequestOptions.circleCropTransform().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.default_user).error(R.drawable.default_user)).into(this)
}