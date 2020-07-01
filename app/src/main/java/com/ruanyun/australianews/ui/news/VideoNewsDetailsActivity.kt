package com.ruanyun.australianews.ui.news

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.KeyEvent
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.toImgUrl
import com.ruanyun.australianews.model.NewsInfo
import com.ruanyun.australianews.model.ShareJsonInfo
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.FileUtil
import com.ruanyun.australianews.util.GsonUtil
import com.ruanyun.australianews.util.WebViewUrlUtil
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.android.synthetic.main.activity_video_web_view.*

/**
 * @description 视频新闻详情
 * @author hdl
 * @date 2019/5/14
 */
class VideoNewsDetailsActivity : NewsDetailsActivity(){

    companion object {
        fun start(context: Context, info: NewsInfo) {
            val starter = Intent(context, VideoNewsDetailsActivity::class.java)
            starter.putExtra(C.IntentKey.WEB_VIEW_URL, FileUtil.getWebViewUrl(WebViewUrlUtil.NEWS_DETAILS, App.getInstance().cityName, info.oid, App.getInstance().userOid))
            starter.putExtra(C.IntentKey.NEWS_INFO_OID, info.oid)
            starter.putExtra(C.IntentKey.NEWS_IMAGE_COVER, info.mainPhoto.toImgUrl())
            starter.putExtra(C.IntentKey.NEWS_VIDEO_PATH, info.videoUrl.toImgUrl())

            val shareJsonInfo = ShareJsonInfo()
            shareJsonInfo.share_title = info.title
            shareJsonInfo.share_image = info.commonMainPhoto
            val json = GsonUtil.toJson(shareJsonInfo)
            starter.putExtra(C.IntentKey.SHARE_INFO_JSON, json)

            context.startActivity(starter)
        }
        fun startNewTask(context: Context, info: NewsInfo) {
            val starter = Intent(context, VideoNewsDetailsActivity::class.java)
            starter.putExtra(C.IntentKey.WEB_VIEW_URL, FileUtil.getWebViewUrl(WebViewUrlUtil.NEWS_DETAILS, App.getInstance().cityName, info.oid, App.getInstance().userOid))
            starter.putExtra(C.IntentKey.NEWS_INFO_OID, info.oid)
            starter.putExtra(C.IntentKey.NEWS_IMAGE_COVER, info.mainPhoto.toImgUrl())
            starter.putExtra(C.IntentKey.NEWS_VIDEO_PATH, info.videoUrl.toImgUrl())

            val shareJsonInfo = ShareJsonInfo()
            shareJsonInfo.share_title = info.title
            shareJsonInfo.share_image = info.commonMainPhoto
            val json = GsonUtil.toJson(shareJsonInfo)
            starter.putExtra(C.IntentKey.SHARE_INFO_JSON, json)

            starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(starter)
        }
    }

    lateinit var videoPath: String
    lateinit var imageCover: String


    private var isPlay: Boolean = false
    private var isPause: Boolean = false

    var orientationUtils: OrientationUtils?=null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        initData()
//    }

    override fun getContentViewId(): Int {
        return R.layout.activity_video_web_view
    }

    override fun initView() {
        super.initView()

        videoPath = intent.getStringExtra(C.IntentKey.NEWS_VIDEO_PATH)
        imageCover = intent.getStringExtra(C.IntentKey.NEWS_IMAGE_COVER)

        sharePopWindow.share_url= videoPath
//        sharePopWindow.share_image=imageCover


        detailPlayer.loadCoverImage(imageCover, 0)
        detailPlayer.enlargeImageRes = R.drawable.video_btn_fullscreen
        detailPlayer.shrinkImageRes = R.drawable.video_btn_narrow
        detailPlayer.backButton.clickWithTrigger { finish() }

        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, detailPlayer)
        //初始化不打开外部的旋转
        orientationUtils?.isEnable = false

        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption.setRotateViewAuto(false).setAutoFullWithSize(true).setShowFullAnimation(false)
            .setNeedLockFull(true).setUrl(videoPath).setCacheWithPlay(true).setFullHideStatusBar(true)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any) {
                    Debuger.printfError("***** onPrepared **** " + objects[0])
                    Debuger.printfError("***** onPrepared **** " + objects[1])
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
//                    orientationUtils?.isEnable = false
                    isPlay = true
                }

                override fun onEnterFullscreen(url: String?, vararg objects: Any) {
                    super.onEnterFullscreen(url, *objects)
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[0])//title
                    Debuger.printfError("***** onEnterFullscreen **** " + objects[1])//当前全屏player
                }

                override fun onQuitFullscreen(url: String?, vararg objects: Any) {
                    super.onQuitFullscreen(url, *objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0])//title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1])//当前非全屏player
                    orientationUtils?.backToProtVideo()
                }
            }).setLockClickListener { _, lock ->
                //配合下方的onConfigurationChanged
                orientationUtils?.isEnable = !lock
            }.setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
                Debuger.printfLog(" progress $progress secProgress $secProgress currentPosition $currentPosition duration $duration")
            }.build(detailPlayer)



        detailPlayer.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils?.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            detailPlayer.startWindowFullscreen(this@VideoNewsDetailsActivity, true, true)
        }
//        Handler().postDelayed({
            detailPlayer.startAfterPrepared()
//        },1000)
    }

//    override fun onBackPressed() {
//        orientationUtils?.backToProtVideo()
//        if (GSYVideoManager.backFromWindowFull(this)) {
//            return
//        }
//        super.onBackPressed()
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (GSYVideoManager.backFromWindowFull(this)) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onPause() {
        getCurPlay().onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        getCurPlay().onVideoResume(false)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            getCurPlay().release()
        }
        orientationUtils?.releaseListener()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
//        if (isPlay && !isPause) {
//            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
//        }
    }

    private fun getCurPlay(): GSYVideoPlayer {
        return if (detailPlayer.fullWindowPlayer != null) {
            detailPlayer.fullWindowPlayer
        } else detailPlayer
    }


}