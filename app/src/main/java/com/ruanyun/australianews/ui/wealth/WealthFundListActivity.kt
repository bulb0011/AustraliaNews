package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_wealth_fund.*

/**
 * @description 财富-基金
 * @author hdl
 * @date 2020/3/26
 */
class WealthFundListActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val starter = Intent(context, WealthFundListActivity::class.java)
            context.startActivity(starter)
        }
    }
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_wealth_fund)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
    }
}