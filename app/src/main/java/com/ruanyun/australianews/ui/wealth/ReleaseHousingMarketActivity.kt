package com.ruanyun.australianews.ui.wealth

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
import com.ruanyun.australianews.ext.toYMD
import com.ruanyun.australianews.model.ActivitysInfo
import com.ruanyun.australianews.model.AddressInfo
import com.ruanyun.australianews.model.HousingMarketInfo
import com.ruanyun.australianews.model.params.ReleaseWealthActivitysParams
import com.ruanyun.australianews.model.params.ReleaseWealthHousingMarketParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.life.release.MapPointActivity
import com.ruanyun.australianews.ui.life.release.ReleaseBaseActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.SelectDateDialog
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_housing_market.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布房市
 * @author hdl
 * @date 2020/3/26
 */
class ReleaseHousingMarketActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getHousingMarketInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<HousingMarketInfo>>() {
                override fun onSuccess(result: ResultBase<HousingMarketInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.image, t.title)
                    tv_type.text = DbHelper.getInstance().getItemNames(t.type, C.ParentCode.HOUSING_MARKET)
                    params.type = t.type
                    tv_address.text = t.address
                    params.address = t.address
                    params.longitude = t.longitude
                    params.latitude = t.latitude
                    et_title.setText(t.title)
                    et_acreage.setText(t.acreage)
                    et_agentName.setText(t.agentName)
                    et_company.setText(t.company)
                    et_phone.setText(t.contactDetails)
                    et_mailbox.setText(t.mailbox)
                    et_introduction.setText(t.introduction)

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
        const val SELECT_ADDRESS = 1006
        fun start(context: Context) {
            val starter = Intent(context, ReleaseHousingMarketActivity::class.java)
            context.startActivity(starter)
        }

        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseHousingMarketActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService: ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseWealthHousingMarketParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_housing_market)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        params.userId = app.userOid

        tv_type.clickWithTrigger {
            //类型
            singleSelectPopWindow.apply {
                showPopupWindow(
                    tv_type,
                    DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSING_MARKET)
                )
                block = {
                    tv_type.text = it.showName
                    params.type = it.showCode
                }
            }
        }
        tv_address.clickWithTrigger {
            MapPointActivity.start(this, SELECT_ADDRESS)
        }

        tv_release.clickWithTrigger {
            if (isPictureDoesNotPass(1)) {
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.acreage = et_acreage.getStr()
            params.agentName = et_agentName.getStr()
            params.company = et_company.getStr()
            params.contactDetails = et_phone.getStr()
            params.mailbox = et_mailbox.getStr()
            params.introduction = et_introduction.getStr()

            if (params.isNotEmpty) {
                processPhoto()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                SELECT_ADDRESS -> {
                    val info = data?.getParcelableExtra<AddressInfo>(C.IntentKey.ADDRESS_INFO)
                    info?.apply {
                        tv_address.text = addressName
                        params.address = addressName
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
        params.image = photoUrl
        params.oid = commonOid
        apiService.releaseHousingMarketInfo(params)
            .compose(RxUtil.normalSchedulers<ResultBase<HousingMarketInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<HousingMarketInfo>>() {
                override fun onSuccess(result: ResultBase<HousingMarketInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.WEALTH_HOUSING_MARKET, result.data.oid)
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