package com.ruanyun.australianews.ext

import com.ruanyun.australianews.util.FileUtil
import com.ruanyun.australianews.util.StringUtil


fun String?.toImgUrl(): String {
    return FileUtil.getImageUrl(this)
}

fun String?.toyyyyMMddHHmm(): String {
    return StringUtil.getyyyyMMddHHmmStr("yyyy-MM-dd HH:mm", this)
}

fun String?.toYMD(): String {
    return StringUtil.getTimeStrFromFormatStr("yyyy-MM-dd", this)
}

fun String?.toFormatStr(format:String): String {
    return StringUtil.getTimeStrFromFormatStr(format, this)
}

