package com.ruanyun.australianews.model

import android.text.TextUtils
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.FileUtil
import com.ruanyun.australianews.util.StringUtil

/**
 * @author hdl
 * @description
 * @date 2019/5/5
 */
class NewsInfo(
        var type: Int = 0, //1图文，2视频，3外部
        var pushShowType: Int = 0,
        var outUrl: String?,
        var videoUrl: String?,
        var createTime: String?,
        var baseWebsite: String?,
        var baseWebsiteName: String?,
        var isHot: Int = 0,
        var commentCount: Int = 0,
        var watchCount: Int = 0,
        var mainPhotoList: List<String>?
) : AdvertInfoBase() , LifeReleaseCommonUiModel {
    override val commonOid: String?
        get() = oid
    override val commonTitle: String?
        get() = title
    override val commonTime: String?
        get() = createTime
    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)
    override val itemType: Int
        get() = pushShowType

    override fun getINewsInfo(): NewsInfo? {
        return this
    }

    companion object {
        const val TYPE_SINGLE_PICTURE = 1000

        const val TYPE_MANY_PICTURE = 2000

        const val TYPE_VIDEO = 3000

        fun getSourceStr(item: NewsInfo): CharSequence? {
            val baseWebsite = if (TextUtils.isEmpty(item.baseWebsiteName)) {
                if (TextUtils.isEmpty(item.baseWebsite)) {
                    ""
                } else {
                    "${DbHelper.getInstance().getItemName(item.baseWebsite, C.ParentCode.NEWSINFO_WEBSITE)}  "
                }
            } else {
                "${item.baseWebsiteName}  "
            }
//            return "$baseWebsite${StringUtil.getWCount(item.watchCount)}浏览  ${StringUtil.getWCount(item.commentCount)}评论  ${StringUtil.getNewsTime(item.createTime)}"
//            return "$baseWebsite${StringUtil.getWCount(item.watchCount)}浏览  ${StringUtil.getNewsTime(item.createTime)}"

            return "$baseWebsite"
        }

        fun getSourceStrPing(item: NewsInfo): CharSequence? {
            val baseWebsite = if (TextUtils.isEmpty(item.baseWebsiteName)) {
                if (TextUtils.isEmpty(item.baseWebsite)) {
                    ""
                } else {
                    "${DbHelper.getInstance().getItemName(item.baseWebsite, C.ParentCode.NEWSINFO_WEBSITE)}  "
                }
            } else {
                "${item.baseWebsiteName}  "
            }
//            return "$baseWebsite${StringUtil.getWCount(item.watchCount)}浏览  ${StringUtil.getWCount(item.commentCount)}评论  ${StringUtil.getNewsTime(item.createTime)}"
//            return "$baseWebsite${StringUtil.getWCount(item.watchCount)}浏览  ${StringUtil.getNewsTime(item.createTime)}"
            return "$baseWebsite  ${StringUtil.getNewsTime(item.createTime)}"

//            return "$baseWebsite"
        }


    }

}
