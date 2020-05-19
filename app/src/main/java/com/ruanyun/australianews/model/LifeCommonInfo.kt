package com.ruanyun.australianews.model

import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonModel

/**
 * @description
 * @author hdl
 * @date 2019/5/30
 */
open class LifeCommonInfo : AdvertInfoBase(), LifeReleaseCommonModel {
    var collectionBrowseOid: String? = null
    var linkMan: String? = null
    var linkTel: String? = null
    var email: String? = null
    var qq: String? = null
    var weixin: String? = null

    override fun getLifeOrYellowPageCollectionBrowseOid(): String? {
        return collectionBrowseOid
    }

    override fun setLifeOrYellowPageCollectionBrowseOid(oid: String?) {
        collectionBrowseOid = oid
    }
}