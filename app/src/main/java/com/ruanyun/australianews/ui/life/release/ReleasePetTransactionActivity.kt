package com.ruanyun.australianews.ui.life.release

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ruanyun.imagepicker.bean.ImageItem
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.LifePetTransactionInfo
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.model.params.ReleaseLifePetTransactionParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.ui.MultipleSelectionActivity
import com.ruanyun.australianews.ui.life.release.ReleasePetTransactionSelectTypeActivity.Companion.ALL_PET_TYPE_CAT
import com.ruanyun.australianews.ui.life.release.ReleasePetTransactionSelectTypeActivity.Companion.ALL_PET_TYPE_DOG
import com.ruanyun.australianews.ui.life.release.ReleasePetTransactionSelectTypeActivity.Companion.ALL_PET_TYPE_FISH
import com.ruanyun.australianews.ui.life.release.ReleasePetTransactionSelectTypeActivity.Companion.ALL_PET_TYPE_PET_SUPPLIES
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_life_pet_transaction.*
import kotlinx.android.synthetic.main.activity_release_life_pet_transaction.topbar
import kotlinx.android.synthetic.main.activity_release_life_pet_transaction.tv_city
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import java.io.File
import javax.inject.Inject

/**
 * @description 发布宠物交易
 * @author hdl
 * @date 2019/5/21
 */
class ReleasePetTransactionActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifePetTransactionInfo(app.userOid, commonOid)
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : ApiSuccessAction<ResultBase<LifePetTransactionInfo>>() {
                    override fun onSuccess(result: ResultBase<LifePetTransactionInfo>) {
                        disMissLoadingView()
                        val t = result.data
                        petType = t.type
                        updateUiMode()
                        setReleasePhoto(t.mainPhoto, t.title)
//                        setReleaseDetails(t.petDetail)
                        setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                        tv_identity.text = DbHelper.getInstance().getItemName(t.transactionNature, C.ParentCode.HOUSING_SOURCE)
                        params.transactionNature = t.transactionNature
                        tv_city.text = t.city
                        params.city = t.city
                        tv_variety.text = DbHelper.getInstance().getItemName(t.petType, petTypeParentCode)
                        params.petType = t.petType
                        tv_gender.text = DbHelper.getInstance().getItemName(t.petSex, C.ParentCode.PET_SEX)
                        params.petSex = t.petSex
                        tv_blood_certificate.text = DbHelper.getInstance().getItemName(t.lineage, C.ParentCode.LINEAGE)
                        params.lineage = t.lineage
                        tv_vaccine_situation.text = DbHelper.getInstance().getItemName(t.vaccinum, C.ParentCode.VACCINUM)
                        params.vaccinum = t.vaccinum
                        tv_whether_deworming.text = DbHelper.getInstance().getItemName(t.isExpellingParasite, C.ParentCode.ISEXPELLING_PARASITE)
                        params.isExpellingParasite = t.isExpellingParasite
                        tv_whether_sterilization.text = DbHelper.getInstance().getItemName(t.isSterilize, C.ParentCode.IS_STERILIZE)
                        params.isSterilize = t.isSterilize
                        tv_release_type.text = DbHelper.getInstance().getItemName(t.publishType, releaseType)
                        params.publishType = t.publishType
                        tv_provide_services.text = DbHelper.getInstance().getItemNames(t.provideService, C.ParentCode.PROVIDE_SERVICE)
                        params.provideService = t.provideService

                        et_number_of_pets.setText(t.petCount)
                        if(!isNegotiable(t.price)) {
                            et_price.setText(t.price)
                        }
                        et_description.setText(t.petDetail)
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
        const val PROVIDE_SERVICE = 1006
        fun start(activity: Activity, petType: String, requestCode: Int) {
            val starter = Intent(activity, ReleasePetTransactionActivity::class.java)
            starter.putExtra(C.IntentKey.PET_TYPE, petType)
            activity.startActivityForResult(starter, requestCode)
        }


        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleasePetTransactionActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService: ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifePetTransactionParams()
    private var petType: String? = null
    private var petTypeParentCode: String? = null
    var releaseType: String? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_pet_transaction)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_CITY -> {
                    val city = data?.getStringExtra(C.IntentKey.CITY_NAME)
                    tv_city.text = city
                    params.city = city
                }
                PROVIDE_SERVICE -> {
                    val list = data?.getParcelableArrayListExtra<ParentCodeInfo>(MultipleSelectionActivity.MULTIPLE_SELECTION_LIST)
                    var name = ""
                    var code = ""
                    list?.forEach {
                        if(CommonUtil.isNotEmpty(name)){
                            name += "、"
                        }
                        if(CommonUtil.isNotEmpty(code)){
                            code += ","
                        }
                        name += it.name
                        code += it.code
                    }
                    tv_provide_services.text = name
                    params.provideService = code
                }
            }
        }
    }

    private fun initView() {
        petType = intent.getStringExtra(C.IntentKey.PET_TYPE)
        updateUiMode()
        topbar.setTopBarClickListener(this)
        params.userOid = app.userOid

        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_identity.clickWithTrigger {//身份
            singleSelectPopWindow.apply {
                showPopupWindow(tv_identity, DbHelper.getInstance().getParentCodeList(C.ParentCode.HOUSING_SOURCE))
                block = {
                    tv_identity.text = it.showName
                    params.transactionNature = it.showCode
                }
            }
        }
        tv_variety.clickWithTrigger {//品种
            singleSelectPopWindow.apply {
                petTypeParentCode?:return@apply
                showPopupWindow(tv_variety, DbHelper.getInstance().getParentCodeList(petTypeParentCode))
                block = {
                    tv_variety.text = it.showName
                    params.petType = it.showCode
                }
            }
        }
        tv_gender.clickWithTrigger {//性别
            singleSelectPopWindow.apply {
                showPopupWindow(tv_gender, DbHelper.getInstance().getParentCodeList(C.ParentCode.PET_SEX))
                block = {
                    tv_gender.text = it.showName
                    params.petSex = it.showCode
                }
            }
        }
        tv_blood_certificate.clickWithTrigger {//血统证明
            singleSelectPopWindow.apply {
                showPopupWindow(tv_blood_certificate, DbHelper.getInstance().getParentCodeList(C.ParentCode.LINEAGE))
                block = {
                    tv_blood_certificate.text = it.showName
                    params.lineage = it.showCode
                }
            }
        }
        tv_vaccine_situation.clickWithTrigger {//疫苗情况
            singleSelectPopWindow.apply {
                showPopupWindow(tv_vaccine_situation, DbHelper.getInstance().getParentCodeList(C.ParentCode.VACCINUM))
                block = {
                    tv_vaccine_situation.text = it.showName
                    params.vaccinum = it.showCode
                }
            }
        }
        tv_whether_deworming.clickWithTrigger {//是否驱虫
            singleSelectPopWindow.apply {
                showPopupWindow(tv_whether_deworming, DbHelper.getInstance().getParentCodeList(C.ParentCode.ISEXPELLING_PARASITE))
                block = {
                    tv_whether_deworming.text = it.showName
                    params.isExpellingParasite = it.showCode
                }
            }
        }
        tv_whether_sterilization.clickWithTrigger {//是否绝育
            singleSelectPopWindow.apply {
                showPopupWindow(tv_whether_sterilization, DbHelper.getInstance().getParentCodeList(C.ParentCode.IS_STERILIZE))
                block = {
                    tv_whether_sterilization.text = it.showName
                    params.isSterilize = it.showCode
                }
            }
        }
        tv_release_type.clickWithTrigger {//发布类型
            singleSelectPopWindow.apply {
                showPopupWindow(tv_release_type, DbHelper.getInstance().getParentCodeList(releaseType))
                block = {
                    tv_release_type.text = it.showName
                    params.publishType = it.showCode
                }
            }
        }
        tv_provide_services.clickWithTrigger {//提供服务
            MultipleSelectionActivity.start(this, "选择提供服务",
                DbHelper.getInstance().getParentCodeList(C.ParentCode.PROVIDE_SERVICE), PROVIDE_SERVICE)
        }
        tv_release.clickWithTrigger {
            if (isPictureDoesNotPass(1)) {
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.petCount = et_number_of_pets.getStr()
            params.price = et_price.getStr()

            params.petDetail = et_description.getStr()
            params.linkMan = et_contact.getStr()
            params.linkTel = et_phone.getStr()
            params.email = et_mailbox.getStr()
            params.qq = et_qq.getStr()
            params.weixin = et_wechat.getStr()

            if (params.isNotEmpty) {
                processPhoto()
            }

        }
    }

    private fun updateUiMode() {
        params.type = petType
        when (petType) {
            ALL_PET_TYPE_CAT -> {
                petTypeParentCode = C.ParentCode.PET_CAT_TYPE
                releaseType = C.ParentCode.PUBLISH_TYPE
            }
            ALL_PET_TYPE_DOG -> {
                petTypeParentCode = C.ParentCode.PET_DOG_TYPE
                releaseType = C.ParentCode.PUBLISH_TYPE
            }
            ALL_PET_TYPE_FISH -> {
                petTypeParentCode = C.ParentCode.PET_FLOWER_TYPE
                releaseType = C.ParentCode.PUBLISH_TYPE
                hidePetUi()
            }
            ALL_PET_TYPE_PET_SUPPLIES -> {
                petTypeParentCode = C.ParentCode.PET_CWYP_TYPE
                releaseType = C.ParentCode.PUBLISH_TYPE_CWYP
                hidePetUi()
            }
        }
    }

    private fun hidePetUi() {
        //性别、血统证明、疫苗情况、是否驱虫、是否绝育、宠物数量、提供服务
        ll_gender.visibility = View.GONE
        ll_blood_certificate.visibility = View.GONE
        ll_vaccine_situation.visibility = View.GONE
        ll_whether_deworming.visibility = View.GONE
        ll_whether_sterilization.visibility = View.GONE
        ll_number_of_pets.visibility = View.GONE
        ll_provide_services.visibility = View.GONE
    }


    /**
     * 发布
     */
    override fun release(photoUrl: String) {
        params.mainPhoto = photoUrl
        params.oid = commonOid
        apiService.releasePetTransaction(params)
                .compose(RxUtil.normalSchedulers<ResultBase<LifePetTransactionInfo>>())
                .subscribe(object : ApiSuccessAction<ResultBase<LifePetTransactionInfo>>() {
                    override fun onSuccess(result: ResultBase<LifePetTransactionInfo>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        setResult(Activity.RESULT_OK)
                        finish()
                        EventNotifier.getInstance().updateLifeReleaseList()
                        showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_PET_TRANSACTION_INFO, result.data.oid)
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