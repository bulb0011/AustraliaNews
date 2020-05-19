package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import com.ruanyun.australianews.model.CivilEstateInfo
import com.ruanyun.australianews.util.C
import kotlinx.android.synthetic.main.activity_civil_estate_list.*

/**
 * @description 民用地产
 * @author hdl
 * @date 2020/3/26
 */
class CivilEstateListActivity : BaseActivity() {
    companion object {
        fun start(context: Context, type: String) {
            val starter = Intent(context, CivilEstateListActivity::class.java)
            starter.putExtra(C.IntentKey.TYPE, type)
            context.startActivity(starter)
        }
    }

    lateinit var type: String

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_civil_estate_list)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        type = intent.getStringExtra(C.IntentKey.TYPE)
        val title = when(type){
            CivilEstateInfo.ESTATEINFO_TYPE1 -> {
                "民用地产"
            }
            CivilEstateInfo.ESTATEINFO_TYPE2 -> {
                "商业地产"
            }
            CivilEstateInfo.ESTATEINFO_TYPE3 -> {
                "农场"
            }
            else->{
                ""
            }
        }
        topbar.setTitleText(title)
        supportFragmentManager.beginTransaction().add(R.id.container, CivilEstateListFragment.newFragment(type)).commit()
    }

    override fun onTopBarRightImgClick() {
        if(isLoginToActivityByIsRelease) {
            ReleaseCivilEstateActivity.start(mContext, type)
        }
    }
}