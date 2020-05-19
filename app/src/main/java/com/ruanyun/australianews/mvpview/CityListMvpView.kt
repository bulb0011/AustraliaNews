package com.ruanyun.australianews.mvpview

import com.ruanyun.australianews.base.TipCommonMvpView
import com.ruanyun.australianews.model.CityInfo


/**
 * @author hdl
 * @description
 * @date 2019/3/12
 */
interface CityListMvpView : TipCommonMvpView {
    fun getPopularCitiesOnSuccess(citys: List<CityInfo>)
    fun getAllCityOnSuccess(citys: List<CityInfo>)
}
