package com.ruanyun.australianews.model

import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.model.uimodel.WealthCommonUiModel

/**
 * @author hdl
 * @description 活动
 * @date 2020/03/26
 */
class ActivitysInfo : WealthBaseInfo(), WealthCommonUiModel{
    override val itemType: Int
        get() = LifeReleaseCommonUiModel.WEALTH_ACTIVITYS
    override var isSelect: Boolean = false

    override var commonBrowseCollectionOid: String?
        get() = browseCollectionOid
        set(value) {
            browseCollectionOid = value
        }

    override val commonOid: String?
        get() = oid

    override val commonTitle: String?
        get() = title

    override val commonMainPhoto: String?
        get() = image.toImgUrl()

    override val commonTime: String?
        get() = createTime

    override fun getCommonAddress(): String {
        return address
    }

    var image: String = ""// "20191226102718162.jpg",   //主图
    var address: String = ""// "合肥市蜀山区",   //地址
    var endDate: String = ""// null,
    var eventOrganizer: String = ""// "沈晨",   //活动组织者
    var isDelete: String = ""// 1,
    var oid: String = ""// "4028811e711128a60171112eafaf0000",
    var title: String = ""// "发生的范德萨·",   //标题
    var type: String = ""// "ACTIVITY_TYPE1",   //类型
    var userName: String = ""// "",
    var userId: String = ""// "402881f56aa602bc016aa60bed420004",
    var userHeadImageAttaOid: String = ""// "",
    var beginDate: String = ""// null,
    var createTime: String = ""// "2020-03-25 18:13:24",
    var activityDescription: String = ""// "少年不识愁滋味，爱上层楼。爱上层楼，为赋新词强说愁。 而今识尽愁滋味，欲说还休。欲说还休，却道天凉好个秋！",  //活动描述
    var mark: String = ""// false,
    var eventDate: String = ""// null  //活动时间
}
