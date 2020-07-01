package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.AndroidRuntimeException
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.params.FeedbackParams
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.util.Token2UrlFunc
import com.ruanyun.imagepicker.compressimage.CompressImageProxyService
import com.ruanyun.imagepicker.compressimage.CompressTaskCallback
import kotlinx.android.synthetic.main.activity_feedback.*
import okhttp3.RequestBody
import java.io.File
import java.util.HashMap
import javax.inject.Inject


/**
 * @description 意见反馈
 * @author hdl
 * @date 2019/5/9
 */
class FeedbackActivity : BaseActivity() {

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var imageProxyService : CompressImageProxyService
    var params = FeedbackParams()

    companion object {
        const val SIZE_LIMIT = 3//控制图片显示的个数
        fun start(context: Context) {
            val starter = Intent(context, FeedbackActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_feedback)
        ryPicView.setOnListeners()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        ryPicView.destroyListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ryPicView.onImageActivityResult(requestCode, resultCode, data)
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        ryPicView.sizeLimit = SIZE_LIMIT
        tv_submit.clickWithTrigger {
            params.contactInformation = et_select_feedback.getStr()
            params.content = et_feedback.getStr()
            params.userOid = app.userOid
            if (params.isNotEmpty) {
                submitPhoto()
            }
        }

    }

    private fun submitPhoto() {
        val list = ryPicView.imageList.filter { !it.isAdd }
        showLoadingView(R.string.in_submit)
        if(list.isEmpty()){
            addFeedbackNoPhoto()
        }else {
            val map = HashMap<String, RequestBody>()
            imageProxyService.getCompressTask("mainPhoto", list).start(object :
                    CompressTaskCallback<HashMap<String, RequestBody>> {
                override fun onCompresComplete(compressResults: HashMap<String, RequestBody>) {
                    map.putAll(compressResults)
                    addFeedback(map)
                }

                override fun onCompresFail(throwable: Throwable) {
                    disMissLoadingView()
                    showToast("压缩图片失败，请重新选择图片！")
                }
            })
        }
    }

    private fun addFeedback(map : HashMap<String, RequestBody>) {
        val subscription = apiService.uploadImage(map)
                .flatMap { t ->
                    if(t==null || TextUtils.isEmpty(t.data)){
                        throw AndroidRuntimeException("上传图片失败")
                    }else {
                        params.mainPhoto = t.data
                        apiService.addFeedback(params)
                    }
                }
                .compose(RxUtil.normalSchedulers<ResultBase<*>>())
                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                    override fun onSuccess(result: ResultBase<*>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        finish()
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

    private fun addFeedbackNoPhoto() {
        val subscription = apiService.addFeedback(params)
                .compose(RxUtil.normalSchedulers<ResultBase<*>>())
                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                    override fun onSuccess(result: ResultBase<*>) {
                        disMissLoadingView()
                        showToast(result.msg)
                        finish()
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
}