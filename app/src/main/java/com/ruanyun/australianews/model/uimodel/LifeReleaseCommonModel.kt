package com.ruanyun.australianews.model.uimodel

import com.ruanyun.australianews.model.AdvertInfoBase
import com.ruanyun.australianews.model.AttachInfo

/**
 * @author hdl
 * @description
 * @date 2019/5/20
 */
interface LifeReleaseCommonModel : AdvertInfoBaseUiModel{

    /** 生活服务及黄页收藏浏览Oid */
    fun getLifeOrYellowPageCollectionBrowseOid(): String?{
        return ""
    }
    /** 生活服务及黄页收藏浏览Oid */
    fun setLifeOrYellowPageCollectionBrowseOid(oid: String?){
    }

}
