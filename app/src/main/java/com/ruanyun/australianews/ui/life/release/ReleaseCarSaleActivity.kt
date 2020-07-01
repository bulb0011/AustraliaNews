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
import com.ruanyun.australianews.model.LifeCarSaleInfo
import com.ruanyun.australianews.model.params.ReleaseLifeCarSaleParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_life_car_sale.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_details_fill_in.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布汽车买卖
 * @author hdl
 * @date 2019/5/22
 */
class ReleaseCarSaleActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeCarSaleInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeCarSaleInfo>>() {
                override fun onSuccess(result: ResultBase<LifeCarSaleInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.mainPhoto, t.title)
                    setReleaseDetails(t.carDetail)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_city.text = t.city
                    params.city = t.city
                    tv_nature_transaction.text = DbHelper.getInstance().getItemName(t.jiaoyiMethod, C.ParentCode.CAR_JIAOYI_METHOD)
                    params.jiaoyiMethod = t.jiaoyiMethod
                    et_kilometers.setText(t.kilometer)
                    tv_gearbox.text = DbHelper.getInstance().getItemName(t.transmission, C.ParentCode.CAR_TRANSMISSION)
                    params.transmission = t.transmission
                    et_years.setText(t.year)
                    tv_vehicle_source.text = DbHelper.getInstance().getItemName(t.transactionNature, C.ParentCode.VEHICLE_SOURCE)
                    params.transactionNature = t.transactionNature
                    tv_brand.text = DbHelper.getInstance().getItemName(t.brand, C.ParentCode.CAR_BRAND)
                    params.brand = t.brand
                    if(!isNegotiable(t.price)) {
                        et_price.setText(t.price)
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

    companion object {
        const val SELECT_CITY = 1003
        fun start(context: Context) {
            val starter = Intent(context, ReleaseCarSaleActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseCarSaleActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifeCarSaleParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_car_sale)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
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
        params.userOid = app.userOid

        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_nature_transaction.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_nature_transaction, DbHelper.getInstance().getParentCodeList(C.ParentCode.CAR_JIAOYI_METHOD))
                block = {
                    tv_nature_transaction.text = it.showName
                    params.jiaoyiMethod = it.showCode
                }
            }
        }
        tv_gearbox.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_gearbox, DbHelper.getInstance().getParentCodeList(C.ParentCode.CAR_TRANSMISSION))
                block = {
                    tv_gearbox.text = it.showName
                    params.transmission = it.showCode
                }
            }
        }
        tv_price_range.clickWithTrigger {
//            singleSelectPopWindow.apply {
//                showPopupWindow(tv_price_range, DbHelper.getInstance().getParentCodeList(C.ParentCode.价格区间))
//                block = {
//                    tv_price_range.text = it.showName
//                    params.价格区间 = it.showCode
//                }
//            }
        }
        tv_vehicle_source.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_vehicle_source, DbHelper.getInstance().getParentCodeList(C.ParentCode.VEHICLE_SOURCE))
                block = {
                    tv_vehicle_source.text = it.showName
                    params.transactionNature = it.showCode
                }
            }
        }
        tv_brand.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_brand, DbHelper.getInstance().getParentCodeList(C.ParentCode.CAR_BRAND))
                block = {
                    tv_brand.text = it.showName
                    params.brand = it.showCode
                }
            }
        }

        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(1)){
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.price = et_price.getStr()
            params.year = et_years.getStr()
            params.kilometer = et_kilometers.getStr()

            params.carDetail = et_details.getStr()
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
     * 发布
     */
    override fun release(photoUrl: String) {
        params.mainPhoto = photoUrl
        params.oid = commonOid
        apiService.releaseCarSale(params)
            .compose(RxUtil.normalSchedulers<ResultBase<LifeCarSaleInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeCarSaleInfo>>() {
                override fun onSuccess(result: ResultBase<LifeCarSaleInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_CAR_SALE_INFO, result.data.oid)
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