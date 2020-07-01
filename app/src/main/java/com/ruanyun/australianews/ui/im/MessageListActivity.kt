package com.ruanyun.australianews.ui.im

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ui.main.MessageFragment

/**
 * @description
 * @author hdl
 * @date 2019/3/26
 */
class MessageListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MessageListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_message_list)
        immerse()

        val fragment = MessageFragment()
        val args = Bundle()
        args.putBoolean("isShowBack", true)
        fragment.arguments = args

        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

}