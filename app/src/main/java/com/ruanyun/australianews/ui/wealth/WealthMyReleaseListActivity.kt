package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.getStr
import kotlinx.android.synthetic.main.activity_my_wealth_browse.*
import kotlin.concurrent.thread

/**
 * @description 财富-我的发布
 * @author hdl
 * @date 2020/3/28
 */
class WealthMyReleaseListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, WealthMyReleaseListActivity::class.java)
            context.startActivity(starter)
        }
    }

    private var fragment = WealthMyReleaseListFragment()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_wealth_browse)
        setStatusBarWhite()
        initView()
    }

    private fun initView() {
        topbar.setTitleText("我的发布").setTopBarClickListener(this)
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    override fun onTopBarRightTextClick() {
        if (topbar.topBarRightTitle.getStr() == "管理") {
            topbar.topBarRightTitle.text = "完成"
            if(fragment.adapter.itemCount > 0){
                fragment.setEditMode(true)
            }else {
                showToast(resources.getString(R.string.zanwushuju))
            }
        } else {
            topbar.topBarRightTitle.text = resources.getString(R.string.guangli)
            fragment.setEditMode(false)
        }
    }
}
