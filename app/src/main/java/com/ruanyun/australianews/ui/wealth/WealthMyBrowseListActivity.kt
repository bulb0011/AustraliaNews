package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.getStr
import kotlinx.android.synthetic.main.activity_my_wealth_browse.*

/**
 * @description 财富-我的浏览
 * @author hdl
 * @date 2020/3/28
 */
class WealthMyBrowseListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, WealthMyBrowseListActivity::class.java)
            context.startActivity(starter)
        }
    }

    private var fragment = WealthMyBrowseListFragment()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_wealth_browse)
        setStatusBarWhite()
        initView()
    }

    private fun initView() {
        topbar.setTitleText(resources.getString(R.string.wodezuj)).setTopBarClickListener(this)
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
