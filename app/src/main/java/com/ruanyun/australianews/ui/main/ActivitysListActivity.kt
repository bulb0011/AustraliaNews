package com.ruanyun.australianews.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_activitys_list.*

/**
 * @description 活动
 * @author hdl
 * @date 2020/3/26
 */
class ActivitysListActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val starter = Intent(context, ActivitysListActivity::class.java)
            context.startActivity(starter)
        }
    }
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_activitys_list)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
    }

    override fun onTopBarRightImgClick() {
        if(isLoginToActivityByIsRelease) {
            ReleaseActivitysActivity.start(mContext)
        }
    }
}