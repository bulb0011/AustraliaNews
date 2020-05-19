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
import com.ruanyun.australianews.model.LifeTextbookInfo
import com.ruanyun.australianews.model.params.ReleaseLifeBusinessTransferParams
import com.ruanyun.australianews.model.params.ReleaseLifeTextbookParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.CityListActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_life_textbook.*
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布教科书
 * @author hdl
 * @date 2019/5/27
 */
class ReleaseTextbookActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getLifeTextbookInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<LifeTextbookInfo>>() {
                override fun onSuccess(result: ResultBase<LifeTextbookInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.photo, t.title)
//                    setReleaseDetails(t.remark)
                    setReleaseContact(t.linkMan, t.linkTel, t.email, t.qq, t.weixin)
                    tv_school_name.text = DbHelper.getInstance().getItemNames(t.school, C.ParentCode.BOOK_SCHOOL_NAME)
                    params.school = t.school
                    tv_books_belong.text = DbHelper.getInstance().getItemNames(t.belongTo, C.ParentCode.BOOK_SCHOOL)
                    params.belongTo = t.belongTo
                    et_books_belong.setText(t.course)
                    tv_book_type.text = DbHelper.getInstance().getItemNames(t.bookeType, C.ParentCode.BOOK_TYPE)
                    params.bookeType = t.bookeType
                    if(!isNegotiable(t.price)) {
                        et_price.setText(t.price)
                    }
                    tv_shape.text = DbHelper.getInstance().getItemNames(t.shape, C.ParentCode.BOOK_SHAPE)
                    params.shape = t.shape
                    tv_old_and_new.text = DbHelper.getInstance().getItemNames(t.newOldStandard, C.ParentCode.BOOK_NEW_OLD)
                    params.newOldStandard = t.newOldStandard
                    tv_notes.text = DbHelper.getInstance().getItemNames(t.isNotes, C.ParentCode.BOOK_IS_NOTE)
                    params.isNotes = t.isNotes
                    tv_trading_location.text = DbHelper.getInstance().getItemNames(t.transactionArea, C.ParentCode.BOOK_TRAN_AREA)
                    params.transactionArea = t.transactionArea
                    et_description.setText(t.remark)
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
        fun start(context: Context) {
            val starter = Intent(context, ReleaseTextbookActivity::class.java)
            context.startActivity(starter)
        }
        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseTextbookActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService:ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    val params = ReleaseLifeTextbookParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_life_textbook)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        params.userOid = app.userOid

        tv_school_name.clickWithTrigger {//学校名称
            singleSelectPopWindow.apply {
                showPopupWindow(tv_school_name, DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_SCHOOL_NAME))
                block = {
                    tv_school_name.text = it.showName
                    params.school = it.showCode
                }
            }
        }
        tv_books_belong.clickWithTrigger {//书籍属于
            singleSelectPopWindow.apply {
                showPopupWindow(tv_books_belong, DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_SCHOOL))
                block = {
                    tv_books_belong.text = it.showName
                    params.belongTo = it.showCode
                }
            }
        }
        tv_book_type.clickWithTrigger {//书本类型
            singleSelectPopWindow.apply {
                showPopupWindow(tv_book_type, DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_TYPE))
                block = {
                    tv_book_type.text = it.showName
                    params.bookeType = it.showCode
                }
            }
        }
        tv_shape.clickWithTrigger {//形式
            singleSelectPopWindow.apply {
                showPopupWindow(tv_shape, DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_SHAPE))
                block = {
                    tv_shape.text = it.showName
                    params.shape = it.showCode
                }
            }
        }
        tv_old_and_new.clickWithTrigger {//新旧程度
            singleSelectPopWindow.apply {
                showPopupWindow(tv_old_and_new, DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_NEW_OLD))
                block = {
                    tv_old_and_new.text = it.showName
                    params.newOldStandard = it.showCode
                }
            }
        }
        tv_notes.clickWithTrigger {//有无笔记
            singleSelectPopWindow.apply {
                showPopupWindow(tv_notes, DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_IS_NOTE))
                block = {
                    tv_notes.text = it.showName
                    params.isNotes = it.showCode
                }
            }
        }
        tv_trading_location.clickWithTrigger {//交易地点
            singleSelectPopWindow.apply {
                showPopupWindow(tv_trading_location, DbHelper.getInstance().getParentCodeList(C.ParentCode.BOOK_TRAN_AREA))
                block = {
                    tv_trading_location.text = it.showName
                    params.transactionArea = it.showCode
                }
            }
        }

        tv_release.clickWithTrigger {
            if(isPictureDoesNotPass(1)){
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.course = et_books_belong.getStr()
            params.price = et_price.getStr()

            params.remark = et_description.getStr()
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
        params.photo = photoUrl
        params.oid = commonOid
        apiService.releaseTextbook(params)
                .compose(RxUtil.normalSchedulers<ResultBase<LifeTextbookInfo>>())
                .subscribe(object : ApiSuccessAction<ResultBase<LifeTextbookInfo>>() {
                    override fun onSuccess(result: ResultBase<LifeTextbookInfo>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        finish()
                        EventNotifier.getInstance().updateLifeReleaseList()
                        showLifeDetailsWeb(LifeReleaseCommonUiModel.LIFE_TEXTBOOK_INFO, result.data.oid)
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