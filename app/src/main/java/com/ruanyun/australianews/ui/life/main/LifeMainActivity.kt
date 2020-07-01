package com.ruanyun.australianews.ui.life.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentTransaction
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.model.TabEntity
import com.ruanyun.australianews.util.CacheHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * @description 生活主页
 * @author hdl
 * @date 2019/5/5
 */
class LifeMainActivity : BaseActivity() {

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private val mTitles = arrayOf("首页", "黄页", "我的")
    private val mIconUnselectIds = intArrayOf(
        R.drawable.life_home,
        R.drawable.life_yellowpages,
        R.drawable.life_my
    )
    private val mIconSelectIds = intArrayOf(
        R.drawable.life_home_pre,
        R.drawable.life_yellowpages_pre,
        R.drawable.life_my_pre
    )


    private var lifeHomeFragment: LifeHomeFragment? = null
    private var yellowPageFragment: YellowPageFragment? = null
    private var myFragment: LifeMyFragment? = null

    private val tabSelectListener = object : OnTabSelectListener {
        override fun onTabSelect(position: Int) {
            setFragment(position)
        }

        override fun onTabReselect(position: Int) {
        }
    }

    companion object {
        const val POSITION = "POSITION"
        const val LOGIN = "LOGIN"
        const val REGISTERED = "REGISTERED"
        fun start(context: Context, position: Int) {
            val starter = Intent(context, LifeMainActivity::class.java)
            starter.putExtra(POSITION, position)
            context.startActivity(starter)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_life_main)
        for (i in mTitles.indices) {
            mTabEntities.add(TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
        bottom_tabLayout.setTabData(mTabEntities)

        bottom_tabLayout.setOnTabSelectListener(tabSelectListener)
        setFragment(0)

        Handler().postDelayed({
            CacheHelper.getInstance().getAllCityInfoListAndUnlimited("")
        }, 200)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val position = intent.getIntExtra(POSITION, -1)
        if(position in 0..2){
            setFragment(position)
            bottom_tabLayout.currentTab = position
        }

    }

    /**
     * 显示对应Fragment
     */
    private fun setFragment(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            lifeHomeFragment ?: let {
                LifeHomeFragment().also {
                    lifeHomeFragment = it
                    add(R.id.container, it)
                }
            }
            yellowPageFragment ?: let {
                YellowPageFragment().also {
                    yellowPageFragment = it
                    add(R.id.container, it)
                }
            }
            myFragment ?: let {
                LifeMyFragment().also {
                    myFragment = it
                    add(R.id.container, it)
                }
            }
            hideFragment(this)
            when (index) {
                0 -> {
                    lifeHomeFragment?.let {
                        this.show(it)
                    }
                }
                1 -> {
                    yellowPageFragment?.let {
                        this.show(it)
                    }
                }
                2 -> {
                    myFragment?.let {
                        this.show(it)
                    }
                }
            }
        }.commitAllowingStateLoss()
    }


    /**
     * 隐藏所有fragment
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        supportFragmentManager.fragments.apply {
            this.forEach {
                supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
            }
        }
        lifeHomeFragment?.let {
            transaction.hide(it)
        }
        yellowPageFragment?.let {
            transaction.hide(it)
        }
        myFragment?.let {
            transaction.hide(it)
        }
    }

}