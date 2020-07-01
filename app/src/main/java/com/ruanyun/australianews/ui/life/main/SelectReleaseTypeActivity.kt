package com.ruanyun.australianews.ui.life.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.clickWithTrigger
import kotlinx.android.synthetic.main.activity_select_release_type.*

/**
 * @description 选择发布类型
 * @author hdl
 * @date 2019/5/9
 */
class SelectReleaseTypeActivity : BaseActivity(){
    companion object {
        fun start(context: Context) {
            val starter = Intent(context, SelectReleaseTypeActivity::class.java)
            context.startActivity(starter)
        }
    }
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_select_release_type)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        cv_life.clickWithTrigger {
            SelectReleaseLifeTypeActivity.start(mContext, true)
        }
        cv_yellow_page.clickWithTrigger {
            SelectReleaseLifeTypeActivity.start(mContext, false)
        }
    }
}