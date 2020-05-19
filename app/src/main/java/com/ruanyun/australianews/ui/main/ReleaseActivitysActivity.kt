package com.ruanyun.australianews.ui.main

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
import com.ruanyun.australianews.model.params.ReleaseWealthActivitysParams
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel
import com.ruanyun.australianews.ui.life.release.ReleaseBaseActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.widget.SelectDateDialog
import com.ruanyun.australianews.widget.SingleSelectPopWindow
import kotlinx.android.synthetic.main.activity_release_activitys.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import javax.inject.Inject

/**
 * @description 发布活动
 * @author hdl
 * @date 2020/3/26
 */
class ReleaseActivitysActivity : ReleaseBaseActivity() {
    override fun requestDetails(commonOid: String) {
        showLoadingView(R.string.in_load)
        apiService.getActivitysInfo(app.userOid, commonOid)
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<ActivitysInfo>>() {
                override fun onSuccess(result: ResultBase<ActivitysInfo>) {
                    disMissLoadingView()
                    val t = result.data
                    setReleasePhoto(t.image, t.title)
                    tv_type.text = DbHelper.getInstance().getItemNames(t.type, C.ParentCode.ACTIVITY_TYPE)
                    params.type = t.type
                    tv_time.text = t.eventDate.toYMD()
                    params.eventDate = tv_time.getStr()
                    et_eventOrganizer.setText(t.eventOrganizer)
                    et_address.setText(t.address)
                    et_activityDescription.setText(t.activityDescription)
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
            val starter = Intent(context, ReleaseActivitysActivity::class.java)
            context.startActivity(starter)
        }

        fun startModify(context: Context, commonOid: String) {
            val starter = Intent(context, ReleaseActivitysActivity::class.java)
            starter.putExtra(C.IntentKey.LIFE_COMMON_OID, commonOid)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService: ApiService
    private val singleSelectPopWindow by lazy { SingleSelectPopWindow(mContext) }
    private val dialog by lazy { SelectDateDialog(mContext) }
    val params = ReleaseWealthActivitysParams()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_release_activitys)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        params.userId = app.userOid

        tv_type.clickWithTrigger {
            //活动类型
            singleSelectPopWindow.apply {
                showPopupWindow(
                    tv_type,
                    DbHelper.getInstance().getParentCodeList(C.ParentCode.ACTIVITY_TYPE)
                )
                block = {
                    tv_type.text = it.showName
                    params.type = it.showCode
                }
            }
        }
        dialog.block = { tag, time ->
            tv_time.text = time
            params.eventDate = time
        }
        tv_time.clickWithTrigger {
            dialog.show("", tv_time.getStr())
        }

        tv_release.clickWithTrigger {
            if (isPictureDoesNotPass(1)) {
                return@clickWithTrigger
            }
            params.title = et_title.getStr()
            params.eventOrganizer = et_eventOrganizer.getStr()
            params.address = et_address.getStr()
            params.activityDescription = et_activityDescription.getStr()

            if (params.isNotEmpty) {
                processPhoto()
            }

        }
    }


    /**
     * 发布
     */
    override fun release(photoUrl: String) {
        params.image = photoUrl
        params.oid = commonOid
        apiService.releaseActivitysInfo(params)
            .compose(RxUtil.normalSchedulers<ResultBase<ActivitysInfo>>())
            .subscribe(object : ApiSuccessAction<ResultBase<ActivitysInfo>>() {
                override fun onSuccess(result: ResultBase<ActivitysInfo>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    finish()
                    EventNotifier.getInstance().updateLifeReleaseList()
                    showLifeDetailsWeb(LifeReleaseCommonUiModel.WEALTH_ACTIVITYS, result.data.oid)
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