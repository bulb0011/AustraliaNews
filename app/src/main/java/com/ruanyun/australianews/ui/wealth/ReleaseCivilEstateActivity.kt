package com.ruanyun.australianews.ui.wealth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.ext.toYMD
import com.ruanyun.australianews.model.AddressInfo
import com.ruanyun.australianews.model.CivilEstateInfo
import com.ruanyun.australianews.model.params.ReleaseWealthCivilEstateParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.ui.life.release.MapPointActivity
import com.ruanyun.australianews.ui.life.release.ReleaseBaseActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.SelectDateDialog
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_civil_estate.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布民用地产
 * @author hdl
 * @date 2020/3/26
 */
class ReleaseCivilEstateActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getCivilEstateInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<CivilEstateInfo>>() {
                override fun onSuccess(result: ResultBase<CivilEstateInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.propertyImage, t.title)
                    tv_huxin.text = DbHelper.getInstance().getItemNames(t.huxin, C.ParentCode.ESTATEINFO_HUXING)
                    params.huxin = t.huxin
                    tv_propertyStatus.text = DbHelper.getInstance().getItemNames(t.propertyStatus, C.ParentCode.PROPETY_STATUS)
                    params.propertyStatus = t.propertyStatus
                    if(params.propertyStatus == "PROPETY_STATUS4"){
                        ll_auctionDate.visibility = View.VISIBLE
                        tv_auctionDate.text = t.auctionDate.toYMD()
                        params.auctionDate = tv_auctionDate.getStr()
                    }
                    tv_city.text = t.city
                    params.city = t.city
                    et_address.setText(t.address)
                    params.longitude = t.longitude
                    params.latitude = t.latitude
                    et_price.setText(t.price)
                    et_salesName.setText(t.salesName)
                    et_salesCall.setText(t.salesCall)
                    et_salesMailbox.setText(t.salesCall)
                    et_salesMailbox.setText(t.salesMailbox)
                    et_propertyIntroduction.setText(t.propertyIntroduction)
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
        const val SELECT_CITY = 1003
        const val SELECT_ADDRESS = 1006
        fun start(context: Context, type: String) {
            val starter = Intent(context, ReleaseCivilEstateActivity::class.java)
            starter.putExtra(C.IntentKey.TYPE, type)
            context.startActivity(starter)
        }

        fun startModify(context: Context, type: String, commonOid: String) {
            val starter = Intent(context, ReleaseCivilEstateActivity::class.java)
            starter.putExtra(C.IntentKey.TYPE, type)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService: ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    private val dialog by lazy { SelectDateDialog(mContext) }
    val params = ReleaseWealthCivilEstateParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_civil_estate)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        params.userId = app.userOid
        params.type = intent.getStringExtra(C.IntentKey.TYPE)
        val title = when(params.type){
            CivilEstateInfo.ESTATEINFO_TYPE1 -> {
                "发布民用地产"
            }
            CivilEstateInfo.ESTATEINFO_TYPE2 -> {
                "发布商业地产"
            }
            CivilEstateInfo.ESTATEINFO_TYPE3 -> {
                "发布农场"
            }
            else->{
                ""
            }
        }
        topbar.setTitleText(title)

        tv_huxin.clickWithTrigger {
            //类型
            singleSelectPopWindow.apply {
                showPopupWindow(
                    tv_huxin,
                    DbHelper.getInstance().getParentCodeList(C.ParentCode.ESTATEINFO_HUXING)
                )
                block = {
                    tv_huxin.text = it.showName
                    params.huxin = it.showCode
                }
            }
        }
        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_propertyStatus.clickWithTrigger {
            //物业状态
            singleSelectPopWindow.apply {
                showPopupWindow(
                    tv_propertyStatus,
                    DbHelper.getInstance().getParentCodeList(C.ParentCode.PROPETY_STATUS)
                )
                block = {
                    tv_propertyStatus.text = it.showName
                    params.propertyStatus = it.showCode
                    if(it.showCode == "PROPETY_STATUS4"){
                        ll_auctionDate.visibility = View.VISIBLE
                    }else {
                        ll_auctionDate.visibility = View.GONE
                    }
                }
            }
        }
        dialog.block = { tag, time ->
            tv_auctionDate.text = time
            params.auctionDate = time
        }
        tv_auctionDate.clickWithTrigger {
            dialog.show("", tv_auctionDate.getStr())
        }

        tv_release.clickWithTrigger {
            if (isPictureDoesNotPass(1)) {
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.price = et_price.getStr()
            params.address = et_address.getStr()
            params.salesName = et_salesName.getStr()
            params.salesCall = et_salesCall.getStr()
            params.salesMailbox = et_salesMailbox.getStr()
            params.propertyIntroduction = et_propertyIntroduction.getStr()

            //拍卖时间
            params.auctionDate = if(params.propertyStatus == "PROPETY_STATUS4"){
                tv_auctionDate.getStr()
            }else {
                null
            }
            if (params.isNotEmpty) {
                processPhoto()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
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


    /**
     * 发布
     */
    override fun release(photoUrl: String) {
        params.propertyImage = photoUrl
        params.oid = commonOid
        apiService.releaseCivilEstateInfo(params)
            .compose(RxUtil.normalSchedulers<ResultBase<CivilEstateInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<CivilEstateInfo>>() {
                override fun onSuccess(result: ResultBase<CivilEstateInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    when(params.type){
                        CivilEstateInfo.ESTATEINFO_TYPE1 -> {
                            showLifeDetailsWeb(LifeReleaseCommonUiModel.WEALTH_CIVIL_ESTATE, result.data.oid)
                        }
                        CivilEstateInfo.ESTATEINFO_TYPE2 -> {
                            showLifeDetailsWeb(LifeReleaseCommonUiModel.WEALTH_COMMERCE_ESTATE, result.data.oid)
                        }
                        CivilEstateInfo.ESTATEINFO_TYPE3 -> {
                            showLifeDetailsWeb(LifeReleaseCommonUiModel.WEALTH_FARM_ESTATE, result.data.oid)
                        }
                    }
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