package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.util.HtmlFormat
import kotlinx.android.synthetic.main.activity_my_message_list.topbar
import kotlinx.android.synthetic.main.activity_my_message_notification_details.*

/**
 * @description 公告详情
 * @author hdl
 * @date 2019/5/10
 */
class MyMessageNotificationDetailsActivity : BaseActivity() {
    companion object {
        private const val NOTIFICATION_DETAILS = "notification_details"
        fun start(context: Context, content: String) {
            val starter = Intent(context, MyMessageNotificationDetailsActivity::class.java)
            starter.putExtra(NOTIFICATION_DETAILS, content)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_message_notification_details)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        val content = intent.getStringExtra(NOTIFICATION_DETAILS)
        webview.loadDataWithBaseURL(null,HtmlFormat.getHtmlContent1(content), "text/html", "utf-8", null)
    }
}