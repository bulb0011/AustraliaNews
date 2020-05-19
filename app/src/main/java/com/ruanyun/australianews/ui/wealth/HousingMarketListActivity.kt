package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_housing_market_list.*

/**
 * @description 房市
 * @author hdl
 * @date 2020/3/26
 */
class HousingMarketListActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val starter = Intent(context, HousingMarketListActivity::class.java)
            context.startActivity(starter)
        }
    }
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_housing_market_list)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
    }

    override fun onTopBarRightImgClick() {
        if(isLoginToActivityByIsRelease) {
            ReleaseHousingMarketActivity.start(mContext)
        }
    }
}