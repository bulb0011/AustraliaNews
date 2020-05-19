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
import com.ruanyun.australianews.model.LifeRecruitmentInfo
import com.ruanyun.australianews.model.params.ReleaseLifeRecruitmentParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_life_recruitment.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_details_fill_in.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布招聘信息
 * @author hdl
 * @date 2019/5/21
 */
class ReleaseRecruitmentActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeRecruitmentInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeRecruitmentInfo>>() {
                override fun onSuccess(result: ResultBase<LifeRecruitmentInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.mainPhoto, t.title)
                    setReleaseDetails(t.recruiyDetail)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_city.text = t.city
                    params.city = t.city
                    et_company_profile.setText(t.companyDescription)
                    tv_nature_of_the_work.text = DbHelper.getInstance().getItemNames(t.jobFunctions, C.ParentCode.JOB_FUNCTIONS)
                    params.jobFunctions = t.jobFunctions
                    if(!isNegotiable(t.salary)) {
                        et_wages.setText(t.salary)
                    }
                    et_work_place.setText(t.address)
                    et_company_name.setText(t.companyName)
                    tv_gender_requirements.text = DbHelper.getInstance().getItemName(t.sexRequirements, C.ParentCode.SEX_REQUIREMENTS)
                    params.sexRequirements = t.sexRequirements
                    et_recruitment_count.setText(t.recruiyCount)
                    tv_academic_requirements.text = DbHelper.getInstance().getItemName(t.educationalRequirements, C.ParentCode.EDUCATIONAL_REQUIREMENTS)
                    params.educationalRequirements = t.educationalRequirements
                    tv_visa_requirements.text = DbHelper.getInstance().getItemName(t.visaRequirements, C.ParentCode.VISA)
                    params.visaRequirements = t.visaRequirements
                    tv_experience_requirement.text = DbHelper.getInstance().getItemName(t.experienceRequirements, C.ParentCode.EXPERIENCE_REQUIREMENTS)
                    params.experienceRequirements = t.experienceRequirements
                    tv_industry_requirements.text = DbHelper.getInstance().getItemName(t.industryRequirements, C.ParentCode.INDUSTRY_REQUIREMENTS)
                    params.industryRequirements = t.industryRequirements

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
            val starter = Intent(context, ReleaseRecruitmentActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseRecruitmentActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifeRecruitmentParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_recruitment)
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
        setInputDetailsName("工作要求")
        params.userOid = app.userOid

        tv_nature_of_the_work.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_nature_of_the_work, DbHelper.getInstance().getParentCodeList(C.ParentCode.JOB_FUNCTIONS))
                block = {
                    tv_nature_of_the_work.text = it.showName
                    params.jobFunctions = it.showCode
                }
            }
        }
        tv_industry_requirements.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_industry_requirements, DbHelper.getInstance().getParentCodeList(C.ParentCode.INDUSTRY_REQUIREMENTS))
                block = {
                    tv_industry_requirements.text = it.showName
                    params.industryRequirements = it.showCode
                }
            }
        }
        tv_experience_requirement.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_experience_requirement, DbHelper.getInstance().getParentCodeList(C.ParentCode.EXPERIENCE_REQUIREMENTS))
                block = {
                    tv_experience_requirement.text = it.showName
                    params.experienceRequirements = it.showCode
                }
            }
        }
        tv_city.clickWithTrigger {
            CityListActivity.start(this, SELECT_CITY)
        }
        tv_gender_requirements.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_gender_requirements, DbHelper.getInstance().getParentCodeList(C.ParentCode.SEX_REQUIREMENTS))
                block = {
                    tv_gender_requirements.text = it.showName
                    params.sexRequirements = it.showCode
                }
            }
        }
        tv_academic_requirements.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_academic_requirements, DbHelper.getInstance().getParentCodeList(C.ParentCode.EDUCATIONAL_REQUIREMENTS))
                block = {
                    tv_academic_requirements.text = it.showName
                    params.educationalRequirements = it.showCode
                }
            }
        }
        tv_visa_requirements.clickWithTrigger {
            singleSelectPopWindow.apply {
                showPopupWindow(tv_visa_requirements, DbHelper.getInstance().getParentCodeList(C.ParentCode.VISA))
                block = {
                    tv_visa_requirements.text = it.showName
                    params.visaRequirements = it.showCode
                }
            }
        }


        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(0)){
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.companyDescription = et_company_profile.getStr()
            params.companyName = et_company_name.getStr()
            params.salary = et_wages.getStr()
            params.address = et_work_place.getStr()
            params.recruiyCount = et_recruitment_count.getStr()

            params.recruiyDetail = et_details.getStr()
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
     * 发布招聘信息
     */
    override fun release(photoUrl: String) {
        params.mainPhoto = photoUrl
        params.oid = commonOid
        apiService.releaseRecruitment(params)
            .compose(RxUtil.normalSchedulers<ResultBase<LifeRecruitmentInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeRecruitmentInfo>>() {
                override fun onSuccess(result: ResultBase<LifeRecruitmentInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_RECRUITMENT_INFO, result.data.oid)
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