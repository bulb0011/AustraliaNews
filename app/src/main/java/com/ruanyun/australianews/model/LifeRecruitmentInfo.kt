package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.FileUtil

/**
 * @author hdl
 * @description 生活-招聘信息信息
 * @date 2019/5/20
 */
class LifeRecruitmentInfo : LifeCommonInfo(), LifeReleaseCommonUiModel {

//    var oid: String? = null//
//    var mainPhoto: String? = null//
//    var title: String? = null//标题
    var updateTime: String? = null//"2019-02-28 17:33:21",
    var jobFunctions: String? = null//工作性质
    var experienceRequirements: String? = null//经验要求
    var salary: String? = null//薪资水平
    var recruiyDetail: String? = null//
    var city: String? = null//
    var companyDescription: String? = null//
    var address: String? = null//
    var companyName: String? = null//
    var sexRequirements: String? = null//
    var recruiyCount: String? = null//
    var educationalRequirements: String? = null//
    var visaRequirements: String? = null//
    var industryRequirements: String? = null//


    override val itemType: Int
        get() = LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO

    override val commonTitle: String?
        get() = title

    override val commonTime: String?
        get() = updateTime

    override val commonOid: String?
        get() = oid
    override val commonMainPhoto: String?
        get() = FileUtil.getImageUrl(mainPhoto)

    override fun getMoneyStr(): String {
        return salary?:""
    }
    override fun getCommonType(): String {
        return DbHelper.getInstance().getItemNameByParentCode(C.ParentCode.JOB_FUNCTIONS, jobFunctions)
    }

    override fun getExperienceRequirementsStr(): String {
        return DbHelper.getInstance().getItemNameByParentCode(C.ParentCode.EXPERIENCE_REQUIREMENTS, experienceRequirements)
    }
}
