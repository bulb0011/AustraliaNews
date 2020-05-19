package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService



/**
 * @description 财富-我的
 * @author hdl
 * @date 2020/01/20
 */
class WealthMyActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val starter = Intent(context, WealthMyActivity::class.java)
            context.startActivity(starter)
        }
    }
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_wealth_my)
        initView()
    }

    private fun initView() {
    }
}