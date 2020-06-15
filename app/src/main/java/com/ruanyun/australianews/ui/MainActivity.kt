package com.ruanyun.australianews.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.LoginStateChangeEvent
import cn.jpush.im.android.api.event.MessageEvent
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.dp2px
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.TabEntity
import com.ruanyun.australianews.ui.login.LoginActivity
import com.ruanyun.australianews.ui.main.MyFragment
import com.ruanyun.australianews.ui.main.NewsFragment
import com.ruanyun.australianews.ui.wealth.WealthActivity
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CacheHelper
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.LogX
import com.tencent.bugly.beta.Beta
import jiguang.chat.entity.EventNotifyUnread
import jiguang.chat.utils.FileHelper
import jiguang.chat.utils.SharePreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * @description
 * @author hdl
 * @date 2018/11/16
 */
class MainActivity : BaseActivity() {

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private val mIconUnselectIds = intArrayOf(
        R.drawable.tab_news,
        R.drawable.tab_life,
        R.drawable.tab_my
    )
    private val mIconSelectIds = intArrayOf(
        R.drawable.tab_news_pre,
        R.drawable.tab_life_pre,
        R.drawable.tab_my_pre
    )


    private var newsFragment: NewsFragment? = null
    private var myFragment: MyFragment? = null
    private var currentPosition = 0

    private val tabSelectListener = object : OnTabSelectListener {
        override fun onTabSelect(position: Int) {
            when(position){
                0 -> {
                    setFragment(0)
                }
                1 -> {
//                    showActivity(LifeMainActivity::class.java)

                    showActivity(WealthActivity::class.java)

//                    showActivity(TextActivi::class.java)

//                    Handler().postDelayed({
//                        bottom_tabLayout.currentTab = if(currentPosition==0) 0 else 2
//                    }, 300)
                }
                2 -> {
                    setFragment(1)
                }
            }
        }

        override fun onTabReselect(position: Int) {
        }
    }

    companion object {
        const val POSITION = "POSITION"
        const val LOGIN = "LOGIN"
        const val REGISTERED = "REGISTERED"
        fun start(context: Context, position: Int) {
            val starter = Intent(context, MainActivity::class.java)
            starter.putExtra(POSITION, position)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        EventBus.getDefault().register(this)

        setContentView(R.layout.activity_main)
        if(Build.VERSION.SDK_INT >= 23){
            immerse()
        }

        val mTitles = arrayOf(getResources().getString(R.string.xinwen),getResources().getString(R.string.caifu),getResources().getString(R.string.gerenzxin))

        //注册sdk的event用于接收各种event事件
        JMessageClient.registerEventReceiver(this)
        for (i in mTitles.indices) {
            mTabEntities.add(TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
        bottom_tabLayout.setTabData(mTabEntities)

        bottom_tabLayout.setOnTabSelectListener(tabSelectListener)
        bottom_tabLayout.post {
            bottom_tabLayout.currentTab = 0
        }
        setFragment(0)
        Beta.checkUpgrade(false, false)

        CacheHelper.getInstance().imLogin()
    }

    override fun onDestroy() {
        super.onDestroy()
        JMessageClient.unRegisterEventReceiver(this)
        EventBus.getDefault().unregister(this)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        LogX.e("retrofit", "onNewIntent() : intent = [$intent]")
        val position = intent.getIntExtra(POSITION, -1)
        if(position in 0..1){
            setFragment(position)
            bottom_tabLayout.currentTab = position
        }

    }

    /**
     * 显示对应Fragment
     */
    private fun setFragment(index: Int) {
        currentPosition = index
        supportFragmentManager.beginTransaction().apply {
            LogX.e("retrofit", "setFragment().newsFragment=" + newsFragment)
            newsFragment ?: let {
                NewsFragment().also {
                    newsFragment = it
                    add(R.id.container, it)
                }
            }
            myFragment ?: let {
                LogX.e("retrofit", "setFragment().MyFragment=" + myFragment)
                MyFragment().also {
                    myFragment = it
                    add(R.id.container, it)
                }
            }
            hideFragment(this)
            when (index) {
                0 -> {
                    newsFragment?.let {
                        this.show(it)
                    }
                }
                1 -> {
                    myFragment?.let {
                        this.show(it)
                    }
                }
            }
        }.commitAllowingStateLoss()
    }


    /**
     * 隐藏所有fragment
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        supportFragmentManager.fragments.apply {
            this.forEach {
                supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
            }
        }
        newsFragment?.let {
            transaction.hide(it)
        }
        myFragment?.let {
            transaction.hide(it)
        }
    }

    private var backPressedToExitOnce = false

    private val mHandler = Handler()

    override fun onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed()
        } else {
            backPressedToExitOnce = true
            showToast("再按一次退出应用")
            mHandler.postDelayed({ backPressedToExitOnce = false }, 2000)
        }
    }



    fun onEventMainThread(event: LoginStateChangeEvent) {
        val reason = event.reason
        val myInfo = event.myInfo
        if (myInfo != null) {
            val path: String
            val avatar = myInfo.avatarFile
            path = if (avatar != null && avatar.exists()) {
                avatar.absolutePath
            } else {
                FileHelper.getUserAvatarPath(myInfo.userName)
            }
            SharePreferenceManager.setCachedUsername(myInfo.userName)
            SharePreferenceManager.setCachedAvatarPath(path)
            CacheHelper.getInstance().imLogout()
        }
        when (reason) {
            LoginStateChangeEvent.Reason.user_logout -> {
                app.exitApp(localClassName)
                showExceptionDialog(this)
            }
//            LoginStateChangeEvent.Reason.user_password_change -> {
//                app.user = null
//                LoginActivity.start(mContext)
//            }
            else -> {}
        }
    }


    private var dialog: AlertDialog? = null
    lateinit var view: View

    /**
     * 显示异地登录弹框
     */
    private fun showExceptionDialog(mContext: Activity) {
        app.user = null
        EventNotifier.getInstance().updateUserInfo()
        if (dialog == null) {
            dialog = AlertDialog.Builder(mContext, R.style.dialog).create()
            view = LayoutInflater.from(mContext).inflate(R.layout.view_exception_dialog, null)
            val tvDetermine = view.findViewById<TextView>(R.id.tv_determine)

            tvDetermine.clickWithTrigger {
                dialog!!.dismiss()
                LoginActivity.start(mContext)
            }
        }

        dialog!!.show()
        dialog!!.setContentView(view)
        val lp = dialog!!.window!!.attributes
        lp.width = dp2px(290f)
        dialog!!.window!!.attributes = lp
    }

    /**
     * 收到新消息处理
     * @param messageEvent
     */
    fun onEventMainThread(messageEvent: MessageEvent) {
        updateMsgCount()
    }

    /**
     * 通知未读消息变化
     * @param event
     */
    fun onEventMainThread(event: EventNotifyUnread) {
        updateMsgCount()
    }

    private fun updateMsgCount() {
        EventNotifier.getInstance().updateUnreadCount()
        val unreadMsgCount = JMessageClient.getAllUnReadMsgCount()
        tv_unread_count.text = String.format("%s", unreadMsgCount)
        tv_unread_count.visibility = if (unreadMsgCount > 0) View.VISIBLE else View.INVISIBLE
    }

    /**
     * 用户信息更新、退出登录
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun AppLanguage(event: Event<String>) {
        if (C.EventKey.CHANGE_APP_LANGUAGE == event.key) {

            LogX.e("dengpao","++++++++++++++++++++++++++++++")

            changeAppLanguage()

//            val intent = Intent(this, MainActivity::class.java)
            val intent = Intent(this, SplashActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // 杀掉进程
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)


        }
    }

//    fun changeAppLanguage() {
//        val yuyan = SharePreferenceManager.getSystemLanguage()
//
//        LogX.e("dengpao", "取出来的" + SharePreferenceManager.getSystemLanguage()!!)
//
//        if (yuyan != null) {
//
//            val resources = context.resources
//            val dm = resources.displayMetrics
//            val config = resources.configuration
//
//            if ("zh" == yuyan) {
//                config.locale = Locale.CHINESE
//                LogX.e("dengpao", "中文$yuyan")
//            } else {
//                // 应用用户选择语言
//                config.locale = Locale.ENGLISH
//                LogX.e("dengpao", "英文$yuyan")
//            }
//
//            LogX.e("dengpao", "语言$yuyan")
//            resources.updateConfiguration(config, dm)
//        }
//    }

}