package com.ruanyun.australianews.ui.life.release

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.AddressInfo
import com.ruanyun.australianews.model.LifeHouseRentInfo
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.model.params.ReleaseLifeHouseRentParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.ui.MultipleSelectionActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SelectDateDialog
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_life_house_rent.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_details_fill_in.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布房屋出租
 * @author hdl
 * @date 2019/5/22
 */
class ReleaseHouseRentActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeHouseRentInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeHouseRentInfo>>() {
                override fun onSuccess(result: ResultBase<LifeHouseRentInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.mainPhoto, t.title)
                    setReleaseDetails(t.description)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_city.text = t.city
                    params.city = t.city
                    et_address.setText(t.address)
                    params.longitude = t.longitude
                    params.latitude = t.latitude
                    tv_rental_method.text = DbHelper.getInstance().getItemNames(t.rentalMethod, C.ParentCode.RENTAL_METHOD)
                    params.rentalMethod = t.rentalMethod
                    tv_nearby_facilities.text = DbHelper.getInstance().getItemNames(t.nearbyFacilities, C.ParentCode.NEARBY_FACILITIES)
                    params.nearbyFacilities = t.nearbyFacilities
                    tv_housing_source.text = DbHelper.getInstance().getItemName(t.identity, C.ParentCode.HOUSING_SOURCE)
                    params.identity = t.identity
                    tv_property_type.text = DbHelper.getInstance().getItemName(t.huxing, C.ParentCode.HOUSE_HUXING)
                    params.huxing = t.huxing
                    if(!isNegotiable(t.rent)) {
                        et_rental_rent.setText(t.rent)
                    }
                    tv_house_configuration.text = DbHelper.getInstance().getItemNames(t.houseCharacteristics, C.ParentCode.HOUSE_CONFIGURATION)
                    params.houseCharacteristics = t.houseCharacteristics
                    tv_pets_allowed.text = DbHelper.getInstance().getItemName(t.isPet, C.ParentCode.PETS_ALLOWED)
                    params.isPet = t.isPet
                    et_shortest_lease.setText(t.minimumTenancy)
                    tv_housing_time.text = t.checkInTime
                    params.checkInTime = t.checkInTime
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    disMissLoadingView()
                    showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    disMissLoadingView()
                    showToast(msg)
                }
            })
    }

    companion object {
        const val HOUSE_CONFIGURATION = 1001
        const val NEARBY_FACILITIES = 1002
        const val SELECT_CITY = 1003
        const val SELECT_ADDRESS = 1006
        const val RENTAL_METHOD = 1007
        fun start(context: Context) {
            val starter = Intent(context, ReleaseHouseRentActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseHouseRentActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    private val dialog by lazy { SelectDateDialog(mContext) }
    val params = ReleaseLifeHouseRentParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_house_rent)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                HOUSE_CONFIGURATION -> {
                    val list = data?.getParcelableArrayListExtra<ParentCodeInfo>(MultipleSelectionActivity.MULTIPLE_SELECTION_LIST)
                    var houseConfigurationName = ""
                    var code = ""
                    list?.forEach {
                        if(CommonUtil.isNotEmpty(houseConfigurationName)){
                            houseConfigurationName += "、"
                        }
                        if(CommonUtil.isNotEmpty(code)){
                            code += ","
                        }
                        houseConfigurationName += it.name
                        code += it.code
                    }
                    tv_house_configuration.text = houseConfigurationName
                    params.houseCharacteristics = code
                }
                NEARBY_FACILITIES -> {
                    val list = data?.getParcelableArrayListExtra<ParentCodeInfo>(MultipleSelectionActivity.MULTIPLE_SELECTION_LIST)
                    var nearbyFacilitiesName = ""
                    var code = ""
                    list?.forEach {
                        if(CommonUtil.isNotEmpty(nearbyFacilitiesName)){
                            nearbyFacilitiesName += "、"
                        }
                        if(CommonUtil.isNotEmpty(code)){
                            code += ","
                        }
                        nearbyFacilitiesName += it.name
                        code += it.code
                    }
                    tv_nearby_facilities.text = nearbyFacilitiesName
                    params.nearbyFacilities = code
                }
                RENTAL_METHOD -> {
                    val list = data?.getParcelableArrayListExtra<ParentCodeInfo>(MultipleSelectionActivity.MULTIPLE_SELECTION_LIST)
                    var rentalMethodName = ""
                    var code = ""
                    list?.forEach {
                        if(CommonUtil.isNotEmpty(rentalMethodName)){
                            rentalMethodName += "、"
                        }
                        if(CommonUtil.isNotEmpty(code)){
                            code += ","
                        }
                        rentalMethodName += it.name
                        code += it.code
                    }
                    tv_rental_method.text = rentalMethodName
                    params.rentalMethod = code
                }
                SELECT_CITY -> {
                    val city = data?.getStringExtra(C.IntentKey.CITY_NAME)
                    tv_city.text = city
                    params.city = city
                    MapPointActivity.start(this, SELECT_ADDRESS)
                }
                SELECT_ADDRESS -> {
                    val info = data?.getParcelableExtra<AddressInfo>(C.IntentKey.ADDRESS_INFO)
                    info?.apply {
                        et_address.setText(addressName)
                        params.longitude = longitude
                        params.latitude = latitude
                    }
                }
            }
        }
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        setInputDetailsName("附注信息")
        params.userNum = app.userOid

        dialog.block = { tag, time ->
            if(dialog.isNotLessThanToday(time)) {
                tv_housing_time.text = time
                params.checkInTime = time
            }else {
                showToast("入住时间不能小于当前时间")
            }
        }


        tv_rental_method.clickWithTrigger {
            MultipleSelectionActivity.start(this, "选择出租方式",
                DbHelper.getInstance().getParentCodeList(C.ParentCode.RENTAL_METHOD), RENTAL_METHOD)
        }
        tv_housing_source.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_housing_source, DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSING_SOURCE))
                block = {
                    tv_housing_source.text = it.showName
                    params.identity = it.showCode
                }
            }
        }
        tv_property_type.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_property_type, DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSE_HUXING))
                block = {
                    tv_property_type.text = it.showName
                    params.huxing = it.showCode
                }
            }
        }
        tv_house_configuration.clickWithTrigger {
            MultipleSelectionActivity.start(this, "选择房屋配置",
                DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSE_CONFIGURATION), HOUSE_CONFIGURATION)
        }
        tv_nearby_facilities.clickWithTrigger {
            MultipleSelectionActivity.start(this, "选择附近设施",
                DbHelper.getInstance().getParentCodeList(C.ParentCode.NEARBY_FACILITIES), NEARBY_FACILITIES)
        }
        tv_housing_time.clickWithTrigger {
            dialog.show("2", tv_housing_time.getStr())
        }
        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_pets_allowed.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_rental_method, DbHelper.getInstance().getParentCodeList(C.ParentCode.PETS_ALLOWED))
                block = {
                    tv_pets_allowed.text = it.showName
                    params.isPet = it.showCode
                }
            }
        }
        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(1)){
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.rent = et_rental_rent.getStr()
            params.address = et_address.getStr()
            params.description = et_details.getStr()
            params.minimumTenancy = et_shortest_lease.getStr()
            params.linkMan = et_contact.getStr()
            params.linkTel = et_phone.getStr()
            params.email = et_mailbox.getStr()
            params.qq = et_qq.getStr()
            params.weixin = et_wechat.getStr()

            if(params.isNotEmpty){
                processPhoto()
            }

        }
    }


    /**
     * 发布房屋出租
     */
    override fun release(photoUrl: String) {
        params.mainPhoto = photoUrl
        params.oid = commonOid
        apiService.releaseHouseRent(params)
            .compose(RxUtil.normalSchedulers<ResultBase<LifeHouseRentInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeHouseRentInfo>>() {
                override fun onSuccess(result: ResultBase<LifeHouseRentInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_HOUSE_RENT_INFO, result.data.oid)
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    disMissLoadingView()
                    showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    disMissLoadingView()
                    showToast(msg)
                }
            })
    }




}