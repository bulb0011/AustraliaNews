package com.ruanyun.australianews.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.dp2Px
import com.ruanyun.australianews.model.AdvertInfoBase
import com.ruanyun.australianews.util.CacheHelper
import com.ruanyun.australianews.util.PixelSizeUtil
import com.ruanyun.australianews.util.RxUtil
import com.ruanyun.australianews.util.WebViewUrlUtil
import kotlinx.android.synthetic.main.activity_splash.*
import me.jessyan.autosize.internal.CancelAdapt


/**
 * @author hdl
 * @description 启动页
 * @date 2019/3/25
 */
class SplashActivity : BaseActivity(), CancelAdapt {

    var isShow = false
    internal var time = 5

    @SuppressLint("HandlerLeak")
    internal var handler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (time == 0) {
                immediatelyShowActivity()
            } else {
                tv_jump.text = "${time}s跳过"
                //重新把进程加入到进程队列中
                if (updateThread != null) {
                    this.postDelayed(updateThread, 1000)
                }
            }
        }
    }

    internal var updateThread: Runnable? = Runnable {
        time -= 1
        if (handler != null) {
            handler!!.sendMessage(handler!!.obtainMessage())
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_splash)
        immerse()
        CacheHelper.getInstance().getInitCacheList()
        initWebView()
        val flp = FrameLayout.LayoutParams(tv_jump.layoutParams)
        flp.gravity = Gravity.RIGHT
        flp.topMargin = dp2Px(4f) + PixelSizeUtil.getStatusHeight(mContext)
        flp.rightMargin = dp2Px(7f)
        tv_jump.layoutParams = flp
        tv_jump.clickWithTrigger {
            immediatelyShowActivity()
        }
        if (CacheHelper.getInstance().isFirstInApp) {
            skipActivity(GuideActivity::class.java)
        } else {
            getOpenScreenAdvert()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler = null
        updateThread = null
    }

    private fun immediatelyShowActivity() {
        if(!isShow) {
            isShow = true
            if (CacheHelper.getInstance().isFirstInApp) {
                skipActivity(GuideActivity::class.java)
            } else {
                skipActivity(MainActivity::class.java)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        //提前初始化一下webview，提升第一次加载速度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.domStorageEnabled = true
        webview.loadUrl("file:///android_asset/dist/index.html")
    }

    var photoGetSuccess = false

    /**
     * 获取开屏广告
     */
    private fun getOpenScreenAdvert() {
        Handler().postDelayed({
            if(!photoGetSuccess){
                immediatelyShowActivity()
            }
        }, 5000)

        ApiManger.getApiService().getOpenScreenAdvert()
            .compose(RxUtil.normalSchedulers())
            .subscribe(object : ApiSuccessAction<ResultBase<AdvertInfoBase>>() {
                override fun onSuccess(result: ResultBase<AdvertInfoBase>) {
                    val advertInfo = result.data
                    Glide.with(this@SplashActivity)
                        .load(advertInfo.getAdvertMainPhoto())
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                immediatelyShowActivity()
                                showToast("开屏广告加载失败")
                                return false
                            }
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                photoGetSuccess = true
                                ivAdvert.clickWithTrigger {
                                    if (advertInfo.jumpType == 2 || advertInfo.jumpType == 4) {
                                        immediatelyShowActivity()
                                        WebViewUrlUtil.showAdvertDetailsWeb(mContext, advertInfo)
                                    }
                                }
                                startCountdown()
                                return false
                            }
                        })
                        .into(ivAdvert)
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    super.onError(erroCode, erroMsg)
                    immediatelyShowActivity()
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    super.onFail(msg)
                    immediatelyShowActivity()
                }
            })
    }

    private fun startCountdown() {
        tv_jump.visibility = View.VISIBLE
        handler?.postDelayed(updateThread, 1000)
    }
}
