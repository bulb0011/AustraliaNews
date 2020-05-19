package com.ruanyun.australianews.ui.life.release

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.addapp.pickers.picker.SinglePicker
import com.ruanyun.imagepicker.bean.ImageItem
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.setDefaultStyle
import com.ruanyun.australianews.model.LifeHouseDemandInfo
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.model.params.ReleaseLifeHouseDemandParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.ui.MultipleSelectionActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_life_house_demand.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_details_fill_in.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import java.io.File
import javax.inject.Inject

/**
 * @description 发布房屋求租
 * @author hdl
 * @date 2019/5/22
 */
class ReleaseHouseDemandActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeHouseDemandInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeHouseDemandInfo>>() {
                override fun onSuccess(result: ResultBase<LifeHouseDemandInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(null, t.title)
                    setReleaseDetails(t.remark)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_city.text = t.city
                    params.city = t.city
                    tv_rent_method.text = DbHelper.getInstance().getItemNames(t.type, C.ParentCode.RENTAL_METHOD)
                    params.type = t.type
                    tv_house_configuration.text = DbHelper.getInstance().getItemNames(t.nearbyFacilities, C.ParentCode.HOUSE_CONFIGURATION)
                    params.nearbyFacilities = t.nearbyFacilities
                    tv_nature_transaction.text = DbHelper.getInstance().getItemName(t.identity, C.ParentCode.HOUSING_SOURCE)
                    params.identity = t.identity
                    tv_expected_type.text = DbHelper.getInstance().getItemName(t.huxing, C.ParentCode.HOUSE_HUXING)
                    params.huxing = t.huxing
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
        const val SELECT_CITY = 1003
        const val RENTAL_METHOD = 1005
        fun start(context: Context) {
            val starter = Intent(context, ReleaseHouseDemandActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseHouseDemandActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifeHouseDemandParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_house_demand)
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
                    params.nearbyFacilities = code
                }
                RENTAL_METHOD -> {
                    val list = data?.getParcelableArrayListExtra<ParentCodeInfo>(MultipleSelectionActivity.MULTIPLE_SELECTION_LIST)
                    var rentMethodName = ""
                    var code = ""
                    list?.forEach {
                        if(CommonUtil.isNotEmpty(rentMethodName)){
                            rentMethodName += "、"
                        }
                        if(CommonUtil.isNotEmpty(code)){
                            code += ","
                        }
                        rentMethodName += it.name
                        code += it.code
                    }
                    tv_rent_method.text = rentMethodName
                    params.type = code
                }
                SELECT_CITY -> {
                    val city = data?.getStringExtra(C.IntentKey.CITY_NAME)
                    tv_city.text = city
                    params.city = city
                }
            }
        }
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        setInputDetailsName("附注信息")
        params.userNum = app.userOid

        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_rent_method.clickWithTrigger {
//            singleSelectPopWindow.apply {
//                showPopupWindow(tv_rent_method, DbHelper.getInstance().getParentCodeList(C.ParentCode.RENTAL_METHOD))
//                block = {
//                    tv_rent_method.text = it.showName
//                    params.type = it.showCode
//                }
//            }
            MultipleSelectionActivity.start(this, "选择求租方式",
                DbHelper.getInstance().getParentCodeList(C.ParentCode.RENTAL_METHOD), RENTAL_METHOD)
        }
        tv_house_configuration.clickWithTrigger {
            MultipleSelectionActivity.start(this, "选择期望设施",
                    DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSE_CONFIGURATION), HOUSE_CONFIGURATION)
        }
        tv_nature_transaction.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_nature_transaction, DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSING_SOURCE))
                block = {
                    tv_nature_transaction.text = it.showName
                    params.identity = it.showCode
                }
            }
        }
        tv_expected_type.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_expected_type, DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSE_HUXING))
                block = {
                    tv_expected_type.text = it.showName
                    params.huxing = it.showCode
                }
            }
        }

        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(0)){
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.remark = et_details.getStr()
            params.linkMan = et_contact.getStr()
            params.linkTel = et_phone.getStr()
            params.email = et_mailbox.getStr()
            params.qq = et_qq.getStr()
            params.weixin = et_wechat.getStr()

            if(params.isNotEmpty){
                processPhoto()
            }

        }

        rl_release_photo.visibility = View.GONE
    }



    /**
     * 发布房屋求租
     */
    override fun release(photoUrl: String) {
        params.oid = commonOid
        apiService.releaseTransactionMarket(params)
            .compose(RxUtil.normalSchedulers<ResultBase<LifeHouseDemandInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeHouseDemandInfo>>() {
                override fun onSuccess(result: ResultBase<LifeHouseDemandInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_HOUSE_DEMAND_INFO, result.data.oid)
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