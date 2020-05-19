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
import com.ruanyun.australianews.model.LifeBusinessTransferInfo
import com.ruanyun.australianews.model.params.ReleaseLifeBusinessTransferParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_life_business_transfer.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布生意转让
 * @author hdl
 * @date 2019/5/27
 */
class ReleaseBusinessTransferActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeBusinessTransferInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeBusinessTransferInfo>>() {
                override fun onSuccess(result: ResultBase<LifeBusinessTransferInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.mainPhoto, t.title)
//                    setReleaseDetails(t.businessTransferDetail)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_identity.text = DbHelper.getInstance().getItemName(t.transactionNature, C.ParentCode.IDENTITY)
                    params.transactionNature = t.transactionNature
                    tv_service_type.text = DbHelper.getInstance().getItemName(t.serviceType, C.ParentCode.SERVICE_TYPE)
                    params.serviceType = t.serviceType
                    tv_city.text = t.city
                    params.city = t.city
                    tv_industry.text = DbHelper.getInstance().getItemName(t.businessTransferType, C.ParentCode.INDUSTRY)
                    params.businessTransferType = t.businessTransferType
                    et_company_name.setText(t.companyName)
                    et_company_address.setText(t.companyAddress)
                    if(!isNegotiable(t.price)) {
                        et_transfer_price.setText(t.price)
                    }
                    et_description.setText(t.businessTransferDetail)
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
            val starter = Intent(context, ReleaseBusinessTransferActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseBusinessTransferActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifeBusinessTransferParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_business_transfer)
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
        params.userOid = app.userOid

        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_identity.clickWithTrigger {//身份
            singleSelectPopWindow.apply {
                showPopupWindow(tv_identity, DbHelper.getInstance().getParentCodeList(C.ParentCode.IDENTITY))
                block = {
                    tv_identity.text = it.showName
                    params.transactionNature = it.showCode
                }
            }
        }
        tv_service_type.clickWithTrigger {//服务类型
            singleSelectPopWindow.apply {
                showPopupWindow(tv_service_type, DbHelper.getInstance().getParentCodeList(C.ParentCode.SERVICE_TYPE))
                block = {
                    tv_service_type.text = it.showName
                    params.serviceType = it.showCode
                }
            }
        }
        tv_industry.clickWithTrigger {//所属行业
            singleSelectPopWindow.apply {
                showPopupWindow(tv_industry, DbHelper.getInstance().getParentCodeList(C.ParentCode.INDUSTRY))
                block = {
                    tv_industry.text = it.showName
                    params.businessTransferType = it.showCode
                }
            }
        }

        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(1)){
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.companyName = et_company_name.getStr()
            params.companyAddress = et_company_address.getStr()
            params.price = et_transfer_price.getStr()

            params.businessTransferDetail = et_description.getStr()
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
        apiService.releaseBusinessTransfer(params)
                .compose(RxUtil.normalSchedulers<ResultBase<LifeBusinessTransferInfo>>())
                .subscribe(object : ApiSuccessAction<ResultBase<LifeBusinessTransferInfo>>() {
                    override fun onSuccess(result: ResultBase<LifeBusinessTransferInfo>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        finish()
                        EventNotifier.getInstance().updateLifeReleaseList()
                        showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_BUSINESS_TRANSFER_INFO, result.data.oid)
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