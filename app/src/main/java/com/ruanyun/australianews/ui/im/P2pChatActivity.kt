package com.ruanyun.australianews.ui.im

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.jiguang.api.JCoreInterface
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation
import com.ruanyun.australianews.App
import jiguang.chat.activity.ChatActivity
import jiguang.chat.application.JGApplication

/**
 * @description
 * @author hdl
 * @date 2019/3/26
 */
class P2pChatActivity : ChatActivity() {

    companion object {

        /**
         * 单聊
         * @param context
         * @param targetId 对应服务端用户oid
         */
        fun start(context: Context, targetId: String) {
            var title = ""
            val conv: Conversation? = JMessageClient.getSingleConversation(targetId)
            if (conv != null) {
                title = conv.title
            }
            val starter = Intent(context, P2pChatActivity::class.java)
            starter.putExtra(JGApplication.TARGET_ID, targetId)
            starter.putExtra(JGApplication.TARGET_APP_KEY, JCoreInterface.getAppKey())
            starter.putExtra(JGApplication.CONV_TITLE, title)
            context.startActivity(starter)
        }
        /**
         * 单聊
         * @param context
         * @param targetId 对应服务端用户oid
         */
        fun start(context: Context, name: String, targetId: String) {
            var title = name
            val conv: Conversation? = JMessageClient.getSingleConversation(targetId)
            if (conv != null) {
                title = conv.title
            }
            val starter = Intent(context, P2pChatActivity::class.java)
            starter.putExtra(JGApplication.TARGET_ID, targetId)
            starter.putExtra(JGApplication.TARGET_APP_KEY, JCoreInterface.getAppKey())
            starter.putExtra(JGApplication.CONV_TITLE, title)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().pushActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        App.getInstance().popActivity(this)
    }
}