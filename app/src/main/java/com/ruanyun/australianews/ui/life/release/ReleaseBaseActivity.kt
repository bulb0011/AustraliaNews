package com.ruanyun.australianews.ui.life.release

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.imagepicker.bean.ImageItem
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.AttachInfo
import com.ruanyun.australianews.model.HelpInfo
import com.ruanyun.australianews.ui.WebViewActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.imagepicker.compressimage.CompressImageProxyService
import com.ruanyun.imagepicker.compressimage.CompressTaskCallback
import com.ruanyun.imagepicker.widget.RYAddPictureView
import kotlinx.android.synthetic.main.layout_life_release_contact.*
import kotlinx.android.synthetic.main.layout_life_release_details_fill_in.*
import kotlinx.android.synthetic.main.layout_life_release_photo.*
import okhttp3.RequestBody
import java.util.HashMap
import javax.inject.Inject

/**
 * @description 生活服务-发布信息基类
 * @author hdl
 * @date 2019/3/2
 */
abstract class ReleaseBaseActivity : BaseActivity() {

    var SIZE_LIMIT = 9//控制图片显示的个数
    protected var commonOid: String? = null
    @Inject
    lateinit var imageProxyService : CompressImageProxyService

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ryPicView?.setOnListeners()
        ryPicView?.sizeLimit = SIZE_LIMIT
        commonOid = intent.getStringExtra(C.IntentKey.LIFE_COMMON_OID)
        if(CommonUtil.isNotEmpty(commonOid)){
            requestDetails(commonOid!!)
        }
        iv_add_photo.clickWithTrigger {
            ryPicView?.handleAddImageClick()
        }
        ryPicView?.setOnPickResultChangedListener(object : RYAddPictureView.onPickResultChangedListener{
            override fun onPicDelete(item: ImageItem?) {

            }

            override fun onPicChanged(hasNum: Int) {
                if (ryPicView?.imageList?.size?:0 <= 1) {
                    ryPicView?.visibility = View.GONE
                    iv_add_photo?.visibility = View.VISIBLE
                    tv_photo_tip?.visibility = View.VISIBLE
                }else {
                    ryPicView?.visibility = View.VISIBLE
                    iv_add_photo?.visibility = View.GONE
                    tv_photo_tip?.visibility = View.GONE
                }
            }

        })

        et_contact?.setText(app.user?.name)
        et_phone?.setText(app.user?.mobile)
        et_mailbox?.setText(app.user?.email)

        val sp = SpannableString(getString(R.string.release_agreement))
        sp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.theme_color)), 19, 34, SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
        tv_release_agreement?.text = sp
        tv_release_agreement?.clickWithTrigger {
            requestData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ryPicView?.destroyListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ryPicView?.onImageActivityResult(requestCode, resultCode, data)
    }

    abstract fun requestDetails(commonOid : String)

    protected fun setInputDetailsName(detailsName: String) {
        text_details_name?.text = detailsName
    }

    /**
     * 显示图片和标题
     */
    protected fun setReleasePhoto(mainPhoto: String?, title: String?){
        mainPhoto?.let {
            val imageItemList = mutableListOf<ImageItem>()
            it.split(",").forEach { path ->
                imageItemList.add(ImageItem(path, path.toImgUrl(), ImageItem.TYPE_REMOTE))
            }
            if(imageItemList.isNotEmpty()) {
                ryPicView?.refreshImage(imageItemList)
                ryPicView?.visibility = View.VISIBLE
                iv_add_photo?.visibility = View.GONE
                tv_photo_tip?.visibility = View.GONE
            }
        }
        et_title?.setText(title?:"")
    }

    /**
     * 详情
     */
    protected fun setReleaseDetails(details: String?){
        et_details?.setText(details)
    }

    /**
     * 联系人、邮箱、手机号、QQ、微信
     */
    protected fun setReleaseContact(linkMan: String?, linkTel: String?, email: String?, qq: String?, weixin: String?){
        et_contact?.setText(linkMan)
        et_phone?.setText(linkTel)
        et_mailbox?.setText(email)
        et_qq?.setText(qq)
        et_wechat?.setText(weixin)
    }

    protected fun isNegotiable(money: String?): Boolean{
        return if(money.isNullOrEmpty()){
            true
        }else {
            if(money.toDoubleOrNull() == null){
                return true
            }else {
                money.toDouble() <=0
            }
        }
    }

    /**
     * 图片不通过
     */
    protected fun isPictureDoesNotPass(count: Int): Boolean {
        if (ryPicView?.imageList?.size?:0 <= count) {
            showToast("请至少选择${count}张图片")
            return true
        }
        return false
    }

    protected fun processPhoto() {
        val remoteList = ryPicView?.imageList?.filter { !it.isAdd && it.type == ImageItem.TYPE_REMOTE }
        val localList = ryPicView?.imageList?.filter { !it.isAdd && it.type == ImageItem.TYPE_LOCAL }
        showLoadingView(R.string.in_submit)
        if(localList?.isNotEmpty() == true){
            val map = HashMap<String, RequestBody>()
            imageProxyService.getCompressTask("mainPhoto", localList).start(object :
                    CompressTaskCallback<HashMap<String, RequestBody>> {
                override fun onCompresComplete(compressResults: HashMap<String, RequestBody>) {
                    map.putAll(compressResults)
                    uploadPhoto(map, remoteList)
                }

                override fun onCompresFail(throwable: Throwable) {
                    disMissLoadingView()
                    showToast("压缩图片失败，请重新选择图片！")
                }
            })
        }else if(remoteList?.isNotEmpty() == true){
            var urls = ""
            remoteList.forEach {
                if(CommonUtil.isNotEmpty(urls)){
                    urls += ","
                }
                urls += it.name
            }
            release(urls)
        }else {
            release("")
        }

    }

    private fun uploadPhoto(map: HashMap<String, RequestBody>, remoteList: List<ImageItem>?) {
        val subscription = ApiManger.getApiService().uploadImage(map)
                .compose(RxUtil.normalSchedulers<ResultBase<String>>())
                .subscribe(object : ApiSuccessAction<ResultBase<String>>() {
                    override fun onSuccess(result: ResultBase<String>) {
                        var urls = ""
                        remoteList?.forEach {
                            urls += "${it.name},"
                        }
                        release(urls + result.data)
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


    open fun release(photoUrl: String){}

    protected fun showLifeDetailsWeb(type:Int, oid: String){
        WebViewUrlUtil.showLifeDetailsWeb(mContext, type, oid)
    }


    /**
     * 获取协议信息
     */
    private fun requestData() {
        showLoading()
        val subscribe = ApiManger.getApiService().getAppCommonProblem("402881fb6a6315f3016a631c7bc60005")
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<HelpInfo>>() {
                override fun onSuccess(result: ResultBase<HelpInfo>) {
                    disMissLoadingView()
                    WebViewActivity.startHtml(mContext, result.data.title, result.data.content)
                }

                override fun onError(errorCode: Int, errorMsg: String) {
                    disMissLoading()
                    showToast(errorMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    disMissLoading()
                    showToast(msg)
                }
            })
        addSubscrebe(subscribe)
    }

}