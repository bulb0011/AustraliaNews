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
import kotlinx.android.synthetic.main.activity_my_browse_record.*

/**
 * @description 我的浏览历史
 * @author hdl
 * @date 2019/5/15
 */
class MyBrowseRecordActivity : BaseActivity() {
    companion object {
        const val MODE_NEWS = 1
        const val MODE_LIFE = 2
        fun start(context: Context) {
            val starter = Intent(context, MyBrowseRecordActivity::class.java)
            context.startActivity(starter)
        }
        fun startMode(context: Context, mode: Int) {
            val starter = Intent(context, MyBrowseRecordActivity::class.java)
            starter.putExtra(C.IntentKey.MODE, mode)
            context.startActivity(starter)
        }
    }

    private var newsFragment: MyBrowseRecordNewListFragment? = null
    private var lifeFragment: MyBrowseRecordLifeListFragment? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_browse_record)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        ll_tab.visibility = View.GONE
        tv_tab_news.click {
            if (!tv_tab_news.isSelected) {
                tv_tab_news.isSelected = true
                tv_tab_life.isSelected = false
                setFragment(0)
                topbar.topBarRightTitle.text = "管理"
                newsFragment?.setEditMode(false)
                lifeFragment?.setEditMode(false)
            }
        }
        tv_tab_life.click {
            if (!tv_tab_life.isSelected) {
                tv_tab_news.isSelected = false
                tv_tab_life.isSelected = true
                setFragment(1)
                topbar.topBarRightTitle.text = "管理"
                newsFragment?.setEditMode(false)
                lifeFragment?.setEditMode(false)
            }
        }
        when (intent.getIntExtra(C.IntentKey.MODE, 0)) {
            1 -> {
                setFragment(0)
                ll_tab.visibility = View.GONE
                tv_tab_news.isSelected = true
            }
            2 -> {
                setFragment(1)
                topbar.setTitleText("我的足迹")
                ll_tab.visibility = View.GONE
                tv_tab_life.isSelected = true
            }
            else -> {
                setFragment(0)
                tv_tab_news.isSelected = true
            }
        }
    }


    override fun onTopBarRightTextClick() {
        if (topbar.topBarRightTitle.getStr() == "管理") {
            if(tv_tab_news.isSelected){
                if(newsFragment?.adapter?.itemCount?:0 > 0){
                    topbar.topBarRightTitle.text = "完成"
                    newsFragment?.setEditMode(true)
                }else {
                    showToast("暂无数据")
                }
            }else {
                if(lifeFragment?.adapter?.itemCount?:0 > 0){
                    topbar.topBarRightTitle.text = "完成"
                    lifeFragment?.setEditMode(true)
                }else {
                    showToast("暂无数据")
                }
            }
        } else {
            topbar.topBarRightTitle.text = "管理"
            newsFragment?.setEditMode(false)
            lifeFragment?.setEditMode(false)
        }
    }

    /**
     * 显示对应Fragment
     */
    private fun setFragment(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            newsFragment ?: let {
                MyBrowseRecordNewListFragment().also {
                    newsFragment = it
                    add(R.id.container, it)
                }
            }
            lifeFragment ?: let {
                MyBrowseRecordLifeListFragment().also {
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