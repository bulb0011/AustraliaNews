package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.util.C
import kotlinx.android.synthetic.main.activity_my_release.*

/**
 * @description 我的发布
 * @author hdl
 * @date 2019/6/11
 */
class MyReleaseActivity : BaseActivity() {
    companion object {
        const val MODE_NEWS = 1
        const val MODE_LIFE = 2
        fun start(context: Context) {
            val starter = Intent(context, MyReleaseActivity::class.java)
            context.startActivity(starter)
        }
        fun startMode(context: Context, mode: Int) {
            val starter = Intent(context, MyReleaseActivity::class.java)
            starter.putExtra(C.IntentKey.MODE, mode)
            context.startActivity(starter)
        }
    }

    private var newsFragment: Fragment? = null
    private var lifeFragment: MyReleaseLifeListFragment? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_release)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        tv_tab_news.isSelected = true
        tv_tab_news.click {
            if (!tv_tab_news.isSelected) {
                tv_tab_news.isSelected = true
                tv_tab_life.isSelected = false
                setFragment(0)
            }
        }
        tv_tab_life.click {
            if (!tv_tab_life.isSelected) {
                tv_tab_news.isSelected = false
                tv_tab_life.isSelected = true
                setFragment(1)
            }
        }
        when (intent.getIntExtra(C.IntentKey.MODE, 0)) {
            1 -> {
                setFragment(0)
                ll_tab.visibility = View.GONE
            }
            2 -> {
                setFragment(1)
                ll_tab.visibility = View.GONE
            }
            else -> {
                setFragment(0)
            }
        }
    }

    override fun onTopBarRightTextClick() {
        if (topbar.topBarRightTitle.getStr() == "管理") {
//            if(tv_tab_news.isSelected){
//                if(newsFragment?.adapter?.itemCount?:0 > 0){
//                    topbar.topBarRightTitle.text = "完成"
//                    newsFragment?.setEditMode(true)
//                }else {
//                    showToast("暂无数据")
//                }
//            }else {
                if(lifeFragment?.adapter?.itemCount?:0 > 0){
                    topbar.topBarRightTitle.text = "完成"
                    lifeFragment?.setEditMode(true)
                }else {
                    showToast("暂无数据")
//                }
            }
        } else {
            topbar.topBarRightTitle.text = resources.getString(R.string.guangli)
//            newsFragment?.setEditMode(false)
            lifeFragment?.setEditMode(false)
        }
    }

    /**
     * 显示对应Fragment
     */
    private fun setFragment(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            newsFragment ?: let {
                Fragment().also {
                    newsFragment = it
                    add(R.id.container, it)
                }
            }
            lifeFragment ?: let {
                MyReleaseLifeListFragment().also {
                    lifeFragment = it
                    add(R.id.container, it)
                }
            }
            when (index) {
                0 -> {
                    lifeFragment?.let {
                        this.hide(it)
                    }
                    newsFragment?.let {
                        this.show(it)
                    }
                }
                1 -> {
                    newsFragment?.let {
                        this.hide(it)
                    }
                    lifeFragment?.let {
                        this.show(it)
                    }
                }
            }
        }.commitAllowingStateLoss()
    }

}