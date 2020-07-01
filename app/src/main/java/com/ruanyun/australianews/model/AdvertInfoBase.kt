package com.ruanyun.australianews.model

import android.os.Parcelable
import com.ruanyun.australianews.model.uimodel.AdvertInfoBaseUiModel
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.FileUtil
import kotlinx.android.parcel.Parcelize

/**
 * @author hdl
 * @description
 * @date 2019/5/13
 */
open class AdvertInfoBase(
        val jumpType: Int,
        val mainPhoto: String?,
        var oid: String,
        val title: String,
        val linkUrl: String,
        var mark: Boolean,
        override var isSelect: Boolean,
        var pushRecordNewsInfo: PushRecordNewsInfo?

) : AdvertInfoBaseUiModel, LifeReleaseCommonUiModel {
    override fun getNewsCommonInfo(): PushRecordNewsInfo? {
        return pushRecordNewsInfo
    }

    override fun setNewsCommonInfo(info: PushRecordNewsInfo?) {
        pushRecordNewsInfo = info
    }

    override val itemType: Int
        get() = 0
    override val commonOid: String?
        get() = oid
    override val commonTitle: String?
        get() = title
    override val commonTime: String?
        get() = ""
    override val commonMainPhoto: String?
        get() = mainPhoto

    constructor() : this(0, "", "", "", "", false, false, null)

    override fun getAdvertOid(): String? {
        return oid
    }

    override fun getAdvertMainPhoto(): String {
        return FileUtil.getImageUrl(mainPhoto)
    }

    override fun getAdvertTitle(): String? {
        return title
    }

    override fun getAdvertJumpType(): Int {
        return jumpType
    }

    override fun getAdvertLinkUrl(): String? {
        return linkUrl
    }

    override fun getAdvertMark(): Boolean {
        return mark
    }

}
