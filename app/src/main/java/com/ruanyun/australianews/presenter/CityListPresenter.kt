package com.ruanyun.australianews.presenter

import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.RxPresenter
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.CityInfo
import com.ruanyun.australianews.mvpview.CityListMvpView
import com.ruanyun.australianews.util.LogX
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.util.StringUtil
import java.util.*
import javax.inject.Inject

/**
 * @description 切换城市视图
 * @author hdl
 * @date 2018/11/26
 */
class CityListPresenter @Inject
constructor(apiService: ApiService) : RxPresenter<CityListMvpView>(apiService) {

    /**
     * 根据昵称拼音首字母排序
     */
    fun sortUserList(cityInfos: List<CityInfo>): List<CityInfo> {
        val i = System.currentTimeMillis()
        for (cityInfo in cityInfos) {
            val classify = cityInfo.cityName.substring(0,1)
            if (classify >= "A" && classify <= "z"){
                cityInfo.classify = classify.toUpperCase()
            }else {
                cityInfo.classify = StringUtil.getPinYinFirstLetter(cityInfo.cityName)
            }
        }
        Collections.sort<CityInfo>(cityInfos) { lhs, rhs -> lhs.classify.compareTo(rhs.classify) }
        LogX.e("retrofit", "sortUserList()=" + (System.currentTimeMillis()-i))
        return cityInfos
    }


    /**
     * 通过前后比较拼音首字母是否相同,给排序后的列表添加首字母分组
     */
    fun getLetterList(contactList: List<CityInfo>): List<CityInfo> {
        val tempList = ArrayList<CityInfo>()
        if (!contactList.isEmpty()) {
            contactList.forEachIndexed { i, city ->
                if(i==0){
                    tempList.add(CityInfo(city.classify, CityInfo.CLASSIFY))
                    tempList.add(contactList[i])
                } else {
                    if(city.classify != contactList[i - 1].classify){
                        tempList.add(CityInfo(city.classify, CityInfo.CLASSIFY))
                    }
                    tempList.add(city)
                }
            }
        }
        return tempList
    }


    /**
     * 获取全部城市
     */
    fun getAllCityList() {
        val subscription = apiService.allCityList
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<List<CityInfo>>>() {
                    override fun onSuccess(result: ResultBase<List<CityInfo>>) {
                        mvpView.getAllCityOnSuccess(result.data)
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        mvpView.showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        mvpView.showToast(msg)
                    }
                })
        addSubscrebe(subscription)
    }

    /**
     * 获取热门城市
     */
    fun getHotCityList() {
        val subscription = apiService.popularCityList
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<List<CityInfo>>>() {
                    override fun onSuccess(result: ResultBase<List<CityInfo>>) {
                        mvpView.getPopularCitiesOnSuccess(result.data)
                    }

                    override fun onError(erroCode: Int, erroMsg: String) {
                        mvpView.showToast(erroMsg)
                    }
                }, object : ApiFailAction() {
                    override fun onFail(msg: String) {
                        mvpView.showToast(msg)
                    }
                })
        addSubscrebe(subscription)
    }

}
