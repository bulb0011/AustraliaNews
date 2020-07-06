package com.ruanyun.australianews.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.Platform.SHARE_WEBPAGE
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.tencent.qzone.QZone
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.model.*
import com.ruanyun.australianews.model.params.NewsCommentParams
import com.ruanyun.australianews.mvpview.WebViewActivityMvpView
import com.ruanyun.australianews.presenter.WebViewActivityPresenter
import com.ruanyun.australianews.ui.im.P2pChatActivity
import com.ruanyun.australianews.ui.life.release.ReleaseShopGoodsActivity
import com.ruanyun.australianews.ui.login.LoginActivity
import com.ruanyun.australianews.ui.my.UserHomePageActivity
import com.ruanyun.australianews.ui.news.NewsCommentActivity
import com.ruanyun.australianews.ui.news.NewsDetailsActivity
import com.ruanyun.australianews.ui.news.SecondaryCommentActivity
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.RYSelectPopWindow
import com.ruanyun.australianews.widget.ReWebChromeClient
import com.ruanyun.imagepicker.AndroidImagePicker
import com.ruanyun.imagepicker.bean.ImageItem
import com.ruanyun.imagepicker.compressimage.CompressImageProxy
import com.ruanyun.imagepicker.compressimage.CompressImageProxyService
import com.ruanyun.imagepicker.compressimage.CompressTaskCallback
import com.ruanyun.imagepicker.compressimage.CompressTaskResult
import com.ruanyun.imagepicker.imagelist.ImageJson
import com.ruanyun.imagepicker.imagelist.ImageListUtil
import com.ruanyun.imagepicker.permissions.PermissionsManager
import com.ruanyun.imagepicker.permissions.PermissionsResultAction
import com.ruanyun.imagepicker.ui.ImagesGridActivity
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import jiguang.chat.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_web_view.*
import me.jessyan.autosize.internal.CancelAdapt
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject


open class WebViewActivity : BaseActivity(), AndroidImagePicker.OnPictureTakeCompleteListener,
        RYSelectPopWindow.OnSelectListener, WebViewActivityMvpView, CancelAdapt,
    PlatformActionListener {
    override fun updateNewsInfo(info: NewsCommentCountInfo) {

    }

    private var mUploadMsg: ValueCallback<Uri>? = null
    private var mUploadMsg5Plus: ValueCallback<Array<Uri>>? = null

    private var sourcePath: String? = null
    protected lateinit var mRYSelectPopWindow: RYSelectPopWindow
    protected lateinit var webViewUrl: String
    private var isShowLoad = true
    protected var isLoadError = false
    lateinit var webView: WebView

    @Inject
    lateinit var presenter: WebViewActivityPresenter

    private val compressImageProxy = CompressImageProxy()
    private var compressImageProxyService: CompressImageProxyService? = null

    override fun onDestroy() {
        presenter.detachView()
        unRegisterBus()
        AndroidImagePicker.getInstance().removeOnPictureTakeCompleteListener(this)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewId())
        presenter.attachView(this)
        registerBus()
        initCompress()
        initView()
    }

    open fun getContentViewId(): Int {
        return R.layout.activity_web_view
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        initView()
    }

    private fun initCompress() {
        compressImageProxyService = compressImageProxy.getProxyService(CompressImageProxyService::class.java)
        AndroidImagePicker.getInstance().selectMode = AndroidImagePicker.Select_Mode.MODE_SINGLE
        AndroidImagePicker.getInstance().isShouldShowCamera = false
        AndroidImagePicker.getInstance().addOnPictureTakeCompleteListener(this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_PHOTO_REQUEST_CODE -> compressImage(data!!.getStringExtra(ImagesGridActivity.SELECTED_PIC))
                AndroidImagePicker.REQ_CAMERA -> compressImage(AndroidImagePicker.getInstance().currentPhotoPath)
            }
        } else {
            receiveValueNull()
        }
    }

    /**
     * 取消选择图片请求
     */
    private fun receiveValueNull() {
        if (mUploadMsg != null) {
            mUploadMsg!!.onReceiveValue(null)
            mUploadMsg = null
        }
        if (mUploadMsg5Plus != null) {
            mUploadMsg5Plus!!.onReceiveValue(null)
            mUploadMsg5Plus = null
        }
    }

    private fun compressImage(imagePath: String) {
        if (compressImageProxyService != null) {
            showLoading("处理中...")
            compressImageProxyService!!.getCompressTask("", ImageItem(imagePath))
                    .start(object : CompressTaskCallback<Array<CompressTaskResult>> {
                        override fun onCompresComplete(compressResults: Array<CompressTaskResult>) {
                            disMissLoading()
                            if (compressResults.isNotEmpty())
                                onImageSelectComplete(compressResults[0].imageFile)
                        }

                        override fun onCompresFail(throwable: Throwable) {
                            disMissLoading()
                        }
                    })
        }
    }

    var isYc=false

    open fun initView() {
        webViewUrl = intent.getStringExtra(C.IntentKey.WEB_VIEW_URL)
        presenter.newsInfoOid = intent.getStringExtra(C.IntentKey.NEWS_INFO_OID)
        presenter.newsInfoType = intent.getIntExtra(C.IntentKey.TYPE, 1)

        isYc = intent.getBooleanExtra(C.IntentKey.WEB_VIEW_724,false)


        if (!webViewUrl.startsWith(ApiManger.API_URL) && TextUtils.isEmpty(presenter.newsInfoOid)) {
            topbar?.visibility = View.VISIBLE
            topbar?.setTitleText(intent.getStringExtra(C.IntentKey.WEB_VIEW_TITLE))?.setTopBarClickListener(this)
        }
        mRYSelectPopWindow = object : RYSelectPopWindow(this, this) {
            override fun onDismiss() {
                mRYSelectPopWindow.backgroundAlpha(1f)
                //如果没有选择照片，则应该取消请求(针对PopupWindow.dismiss()时:1、点击了取消按钮 2、点击了PopupWindow界面外)
                if (!mRYSelectPopWindow.isSelectedPic) {
                    receiveValueNull()
                }
            }
        }
        setWebView()
        emptyview.bind(webView)
        emptyview.setOnReloadListener {
            isShowLoad = true
            isLoadError = false
            loadUrl()
        }
        loadUrl()
    }

    private fun setWebView() {
        webView = findViewById(R.id.webview)
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)

        webView.webChromeClient = object : ReWebChromeClient(object : ReWebChromeClient.OpenFileChooserCallBack {
            override fun openFileChooserCallBack(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                mUploadMsg = uploadMsg
                mRYSelectPopWindow.showPopupWindow(emptyview)
            }

            override fun showFileChooserCallBack(filePathCallback: ValueCallback<Array<Uri>>) {
                mUploadMsg5Plus = filePathCallback
                mRYSelectPopWindow.showPopupWindow(emptyview)
            }
        }) {

            override fun onReceivedTitle(webview: WebView?, title: String?) {
                super.onReceivedTitle(webview, title)
//                LogX.e("retrofit", "onReceivedTitle()=$commonTitle")
                isLoadError = CommonUtil.isNotEmpty(title) &&
                        ("找不到网页" == title || "HTTP Status 404 – Not Found" == title || title!!.toLowerCase().contains("error"))
            }

            override fun onProgressChanged(webview: WebView?, progress: Int) {
                if (isShowLoad) {
                    if (progress >= 100) {
                        if(isLoadError) {
                            emptyview.showLoadFail()
                        }else {
                            emptyview.loadSuccess()
                            webView.postDelayed({
                                webView.getView().scrollTo(0, 0)
                            }, 300)
                        }
                        isShowLoad = false
                    } else {
                        emptyview.showLoading()
                    }
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(webview: WebView?, url: String?): Boolean {
//                LogX.e("retrofit", "shouldOverrideUrlLoading() : webview = [$webview], url = [$url]")
                if (url!!.startsWith("http:") || url.startsWith("https:")) {
                    webview!!.loadUrl(url)
                } else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } catch (e: Exception) {
                        return false
                    }
                }
                return true
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                if(isLoadError){
                    emptyview.showLoadFail()
                }

                if (isYc)conceal()


            }

            override fun onReceivedError(p0: WebView?, p1: Int, p2: String?, p3: String?) {
                super.onReceivedError(p0, p1, p2, p3)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return
                }
                isLoadError = true
                // 在这里显示自定义错误页
                LogX.e("retrofit", "onReceivedError() : p0 = [$p0], p1 = [$p1], p2 = [$p2], p3 = [$p3]")
            }

            override fun onReceivedError(p0: WebView?, request: WebResourceRequest?, p2: WebResourceError?) {
                super.onReceivedError(p0, request, p2)
                LogX.e("retrofit", "onReceivedError2() : p0 = [$p0], request = [$request], p2 = [$p2]")
                isLoadError = request?.isForMainFrame?:false
            }

//            override fun shouldInterceptRequest(webView: WebView?, url: String?): WebResourceResponse? {
//                var response = super.shouldInterceptRequest(webView,url)
//                val name = WebViewAssetsUtil.getAssetsName(url)
//                if(!TextUtils.isEmpty(name)){
//                    try {
//                        response = WebResourceResponse("image/png", "UTF-8", assets.open(name))
//                    } catch (e : IOException) {
//                        e.printStackTrace()
//                    }
//                }
//                if(response==null){
//                    LogX.e("retrofit", "shouldInterceptRequest() : url = [$url]")
//                }
//                return  response
//            }

        }

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setJavaScriptEnabled(true);
//        webView.removeJavascriptInterface("searchBoxJavaBridge_")
        webView.addJavascriptInterface(object : JsMethodListener(webView){}, "ruanyun")
        // init webview settings
        webSettings.allowContentAccess = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.setAppCacheEnabled(false)
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.isLongClickable = false
        webView.setOnLongClickListener {
            true
        }
        webView.requestFocus()

    }

    private fun loadUrl() {
        if (CommonUtil.isNetworkAvailable()) {
            if(webViewUrl.startsWith("http")){
                webView.loadUrl(webViewUrl)
                LogX.e("retrofit", "++++loadUrl = $webViewUrl")
            }else if(webViewUrl.startsWith("file")){//本地html界面
                webView.loadUrl(webViewUrl)
                LogX.e("retrofit", "loadUrl = $webViewUrl")
            }else {
                webView.loadDataWithBaseURL(null, HtmlFormat.getHtmlContent(webViewUrl), "text/html", "utf-8", null)
            }

        } else {
            emptyview.showLoadFail("检查网络连接")
        }

//        webView.loadUrl("http://192.168.0.101:8848/test/an.html")

//        webView.loadUrl("file:///android_asset/aaa.html");
    }

    override fun onPictureTakeComplete(picturePath: String, requestCode: Int) {
        compressImage(picturePath)
    }

    override fun onAlbumSelectClick() {
        val intent = Intent()
        intent.setClass(mContext, ImagesGridActivity::class.java)
        intent.putExtra("isCrop", false)
        startActivityForResult(intent, SELECT_PHOTO_REQUEST_CODE)
        mRYSelectPopWindow.dismiss()
    }

    override fun takePicClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsManager.getInstance()
                    .requestPermissionsIfNecessaryForResult(this, PERMISSIONS, object : PermissionsResultAction() {
                        override fun onGranted() {
                            takePicClick2()
                        }

                        override fun onDenied(permission: String) {
                            runOnUiShowToast("请求相机权限被拒绝")
                        }
                    })
        } else {
            takePicClick2()
        }
    }

    private fun takePicClick2() {
        try {
            AndroidImagePicker.getInstance().takePicture(this, AndroidImagePicker.REQ_CAMERA)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mRYSelectPopWindow.dismiss()
    }

    protected fun onImageSelectComplete(file: File) {
        try {
            if (mUploadMsg == null && mUploadMsg5Plus == null) {
                return
            }
            sourcePath = file.path
            if (TextUtils.isEmpty(sourcePath) || !File(sourcePath!!).exists()) {
                return
            }
            val uri = Uri.fromFile(File(sourcePath!!))
            sourcePath = null//请求上传后，把sourcePath置空，防止影响下次请求的判断
            if (mUploadMsg != null) {
                mUploadMsg!!.onReceiveValue(uri)
                mUploadMsg = null
            } else {
                mUploadMsg5Plus!!.onReceiveValue(arrayOf(uri))
                mUploadMsg5Plus = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onTopBarLeftImgClick() {
        goBack()
    }

    fun goBack() {
        runOnUiThread {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }
    }

    fun javascript(s: String) {
        runOnUiThread {
            val jsStr = "javascript:$s"
            LogX.e("retrofit", "javascript()=$jsStr")
            webView.evaluateJavascript(jsStr, null)
        }
    }

    fun textile(s: String) {
        runOnUiThread {
        val jsStr = "javascript:$s"
        webView.evaluateJavascript(jsStr, null)
        }
    }


    fun conceal() {
        val js="\$('.jin-timeline_title a').hide();"
        runOnUiThread {
            webView.evaluateJavascript(js, null)
        }
    }


    override fun aliPaySuccess() {
        runOnUiShowToast("支付成功")
    }

    override fun aliPayCancel() {
        runOnUiShowToast("取消支付")
    }

    override fun payFail(msg: String) {
        runOnUiShowToast(msg)
    }


    /**
     * js监听
     */
    internal open inner class JsMethodListener(webView : WebView) {

       lateinit var webView : WebView

        init {
            this.webView=webView
        }

        /**
         *
         */
        @JavascriptInterface
        fun showToast(s: String) {
            LogX.e("retrofit", "showToast() : s = [$s]")

            runOnUiThread {

               var skipBean= GsonUtil.parseJson(s,SkipBean::class.java)

                LogX.e("retrofit", "showToast() : s = [$s]")

                LogX.e("retrofit", skipBean.basewebsite)
                LogX.e("retrofit", ""+skipBean.commentcount)
                LogX.e("retrofit", skipBean.url)
                LogX.e("retrofit", skipBean.userOid)
                LogX.e("retrofit", skipBean.commenttime)
                LogX.e("retrofit", skipBean.basewebsite)
                LogX.e("retrofit", skipBean.title)

//                webView.loadUrl(s)

//                http://afn.resolr.com/aozhoucaijing/app/newsinfo/getNewsInfoDetails?newsInfoOid=01234567890123456785effd0824b523&userOid=&language=zh&fa=android
//
//                http://afn.resolr.com/aozhoucaijing/app/newsinfo/getNewsInfoDetails?newsInfoOid=

                var url=skipBean.url+skipBean.newsinfooid+"&city=悉尼&userOid="+skipBean.userOid+"&fa=android"

                val url_zh=url+"&language=zh"
                val url_en=url+"&language=en"

                val shareJsonInfo = ShareJsonInfo()
                shareJsonInfo.share_title = skipBean.title
                shareJsonInfo.share_image = skipBean.mainphoto
                shareJsonInfo.share_url = skipBean.url+skipBean.newsinfooid+"&city=悉尼&userOid="+skipBean.userOid

                val json = GsonUtil.toJson(shareJsonInfo)

                NewsDetailsActivity.startNewsDetails(context, url_zh, skipBean.newsinfooid, NewsCommentParams.NEWS, json,skipBean.commentcount,skipBean.watchcount,skipBean.title,skipBean.commenttime,url_en)


            }

        }



        /**
         * 去首页
         */
        @JavascriptInterface
        fun goHome(s: String) {
//            LogX.e("retrofit", "goHome() : s = [$s]")
//            runOnUiThread {
//                MainActivity.start(mContext, 0)
//                finish()
//            }
        }


        /**
         * 消息单聊
         */
        @JavascriptInterface
        fun privateLetter(thirdNum: String) {
//            LogX.e("retrofit", "goOnline() : thirdNum = [$thirdNum]")
//            if(TextUtils.isEmpty(thirdNum) || "undefined" == thirdNum){
//                showToast("请稍后再试")
//                return
//            }
//            if (isLoginToActivity) {
//                if(CacheHelper.getInstance().isImLoginSuccess) {
//                    if(thirdNum != app.userOid) {
//                        P2pChatActivity.start(mContext, thirdNum)
//                    }else {
//                        runOnUiShowToast("您不能给自己发私信哦")
//                    }
//                }else {
//                    runOnUiShowToast("私信异常，请联系客服！")
//                    CacheHelper.getInstance().imLogin()
//                }
//            }
        }

        /**
         * 个人主页
         */
        @JavascriptInterface
        fun jumpHomepage(userOid: String) {
//            LogX.e("retrofit", "jumpHomepage() : userOid = [$userOid]")
//            UserHomePageActivity.start(mContext, userOid)
        }

        /**
         * 去登陆
         */
        @JavascriptInterface
        fun judgeLogin(s: String) {
            LogX.e("retrofit", "judgeLogin() : s = [$s]")
//            if(app.user==null) {
//                LoginActivity.start(mContext)
//            }
        }

        /**
         * 返回历史界面
         */
        @JavascriptInterface
        fun historyBack(s: String) {
//            LogX.e("retrofit", "historyBack() : s = [$s]")
//            goBack()
        }

        /**
         * 添加商品
         */
        @JavascriptInterface
        fun addingGoods(shopNum: String) {
//            LogX.e("retrofit", "addingGoods() : shopNum = [$shopNum]")
//            ReleaseShopGoodsActivity.start(mContext, shopNum)
        }

        /**
         * 更多留言
         */
        @JavascriptInterface
        fun moreComments(s: String) {
//            LogX.e("retrofit", "moreComments() : s = [$s]")
//            NewsCommentActivity.start(mContext, presenter.newsInfoOid, presenter.newsInfoType)
        }

        /**
         * 跳转二级评论列表
         */
        @JavascriptInterface
        fun detailsLeaveFeedback(json: String) {
//            LogX.e("retrofit", "moreComments() : json = [$json]")
//            val info = GsonUtil.parseJson(json, CommentInfo::class.java)
//            info.userPhoto = info.sysUser?.userPhoto
//            info.name = info.sysUser?.name
//            SecondaryCommentActivity.start(mContext, info, presenter.newsInfoType)
        }

        /**
         * 跳转广告详情
         */
        @JavascriptInterface
        fun jumpAdvertisement(json: String) {
//            LogX.e("retrofit", "jumpAdvertisement() : json = [$json]")
//            val info = GsonUtil.parseJson(json, AdvertInfoBase::class.java)
//            WebViewUrlUtil.showAdvertDetailsWeb(mContext, info)
        }

        /**
         * 复制
         */
        @JavascriptInterface
        fun clickCopy(name: String) {
//            LogX.e("retrofit", "jumpAdvertisement() : name = [$name]")
//            copyText(name)
        }

        /**
         * 查看大图
         */
        @JavascriptInterface
        fun clickBigPicture(json: String) {
            LogX.d("retrofit", "toGoodsImgEnlarge() : json = $json")
            val imageJson = GsonUtil.parseJson(json, ImageJson::class.java)
            val datas = ArrayList<String>()
            var currentPic = 0
            imageJson.imgList.split(",").forEachIndexed { index, s ->
                if(s == imageJson.filePath){
                    currentPic = index
                }
                datas.add(FileUtil.getImageUrl(s))
            }
//            ImageListUtil.showBigImagesWithStringList(mContext, datas, currentPic)
        }

        /**
         * 分享js
         */
        @JavascriptInterface
        fun share(jsonStr: String) {
            LogX.e("retrofit", "share() : jsonStr = [$jsonStr]")
            val shareInfo = GsonUtil.parseJson(jsonStr, ShareInfo::class.java)
            var name = ""
            when(shareInfo.type){
                1 -> name = Wechat.NAME
                2 -> name = WechatMoments.NAME
                3 -> name = QQ.NAME
                4 -> name = QZone.NAME
            }
//            share(name, shareInfo.title, shareInfo.content, shareInfo.imgUrl, shareInfo.url)
        }

    }


    /**
     * 分享链接
     */
    private fun share(name: String, title: String, content: String, imgUrl: String, shareUrl: String) {
        val platform = ShareSDK.getPlatform(name)
        val sp = Platform.ShareParams()
        sp.shareType = SHARE_WEBPAGE// 一定要设置分享属性
        sp.title = title
        sp.text = content

        sp.titleUrl = shareUrl
        val bmp = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        sp.imageData = bmp
        sp.imageUrl = imgUrl
        sp.url = shareUrl
        platform.platformActionListener = this // 设置分享事件回调
        platform.share(sp)
    }

    override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
    }

    override fun onCancel(p0: Platform?, p1: Int) {
    }

    override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
    }

    fun runOnUiShowToast(msg: String?){
        msg?:return
        runOnUiThread {
            showToast(msg)
        }
    }

    companion object {
        val PERMISSIONS = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        )
        const val SELECT_PHOTO_REQUEST_CODE = 98

        fun start(context: Context, url: String?) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra(C.IntentKey.WEB_VIEW_URL, url)
            context.startActivity(starter)
        }
        fun startNewTask(context: Context, url: String?) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra(C.IntentKey.WEB_VIEW_URL, url)
            starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(starter)
        }
        fun startHtml(context: Context, title: String?, html: String?) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra(C.IntentKey.WEB_VIEW_TITLE, title)
            starter.putExtra(C.IntentKey.WEB_VIEW_URL, html)
            context.startActivity(starter)
        }
        fun startHtml(context: Context, title: String?, html: String?,isYinC : Boolean ) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra(C.IntentKey.WEB_VIEW_TITLE, title)
            starter.putExtra(C.IntentKey.WEB_VIEW_URL, html)
            starter.putExtra(C.IntentKey.WEB_VIEW_724, isYinC)
            context.startActivity(starter)
        }
    }

    /**
     * 用户信息更新、退出登录
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun updateUserInfo(event: Event<String>) {
        if (C.EventKey.UPDATE_USER_INFO == event.key) {
            if (CommonUtil.isNotEmpty(app.userOid)) {
                if (webViewUrl.endsWith("userOid=")) {
                    webViewUrl += app.userOid
                } else if (webViewUrl.contains("userOid=")) {
                    val index = webViewUrl.indexOf("userOid=")
                    webViewUrl = webViewUrl.substring(0, index + 8) + app.userOid
                } else if (webViewUrl.contains("?")) {
                    webViewUrl += "&userOid=" + app.userOid
                } else {
                    webViewUrl += "?userOid=" + app.userOid
                }
                if (presenter.newsInfoType == NewsCommentParams.NEWS) {
                    loadUrl()
                } else {
                    WebViewUrlUtil.showLifeDetailsWeb(mContext, presenter.newsInfoType, presenter.newsInfoOid)
                    finish()
                }
            }
        }
    }

    /**
     * 刷新商品列表页面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updatePage(event: Event<String>) {
        if (C.EventKey.UPDATE_SHOP_GOODS_PAGE == event.key) {
            javascript("cateringGoods('${presenter.newsInfoOid}','${app.userOid}')")
        }
    }

    /**
     * 刷新页面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun addCommentSuccess(event: Event<String>) {

    }

}
