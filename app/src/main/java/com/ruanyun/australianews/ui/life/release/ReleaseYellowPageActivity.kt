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
import com.ruanyun.australianews.model.LifeYellowPageInfo
import com.ruanyun.australianews.model.params.ReleaseYellowPageParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_yellow_page.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_details_fill_in.*
import javax.inject.Inject

/**
 * @description 发布黄页
 * @author hdl
 * @date 2019/5/17
 */
class ReleaseYellowPageActivity : ReleaseBaseActivity() {
    companion object {
        const val SELECT_CITY = 1003
        fun start(context: Context, typeOid: String?) {
            val starter = Intent(context, ReleaseYellowPageActivity::class.java)
            starter.putExtra(C.IntentKey.YELLOW_PAGE_TYPE_OID, typeOid)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseYellowPageActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        val subscription = apiService.getYellowPageInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeYellowPageInfo>>() {
                override fun onSuccess(result: ResultBase<LifeYellowPageInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.mainPhoto, t.title)
                    setReleaseDetails(t.description)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_city.text = t.city
                    params.city = t.city
                    et_company_name.setText(t.companyName)
                    et_company_address.setText(t.companyAddress)
                    et_company_url.setText(t.companyWebsite)
                    tv_nature_transaction.text = DbHelper.getInstance().getItemNames(t.transactionNature, C.ParentCode.HOUSING_SOURCE)
                    params.transactionNature = t.transactionNature
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
        addSubscrebe(subscription)
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseYellowPageParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_yellow_page)
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
        params.typeOid = intent.getStringExtra(C.IntentKey.YELLOW_PAGE_TYPE_OID)

        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_nature_transaction.clickWithTrigger {//交易性质
            singleSelectPopWindow.apply {
                showPopupWindow(tv_nature_transaction, DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSING_SOURCE))
                block = {
                    tv_nature_transaction.text = it.showName
                    params.transactionNature = it.showCode
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
            params.companyWebsite = et_company_url.getStr()

            params.description = et_details.getStr()
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
        apiService.releaseYellowPage(params)
                .compose(RxUtil.normalSchedulers<ResultBase<LifeYellowPageInfo>>())
                .subscribe(object : ApiSuccessAction<ResultBase<LifeYellowPageInfo>>() {
                    override fun onSuccess(result: ResultBase<LifeYellowPageInfo>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        finish()
                        EventNotifier.getInstance().updateYellowPageList()
                        showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO, result.data.oid)
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