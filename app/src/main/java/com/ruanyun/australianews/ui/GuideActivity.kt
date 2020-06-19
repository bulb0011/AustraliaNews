package com.ruanyun.australianews.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.util.CacheHelper
import kotlinx.android.synthetic.main.activity_guide.*




/**
 * @description 引导页
 * @author hdl
 * @date 2019/3/28
 */
class GuideActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    //引导图片资源
    private val imgs = intArrayOf(
        R.drawable.bootpage_1_1,
        R.drawable.bootpage_2,
        R.drawable.bootpage_3
    )

    var imageViews = mutableListOf<View>()
//    var dotViews = mutableListOf<ImageView>()


    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_guide)
        if(Build.VERSION.SDK_INT >= 23){
            immerse()
        }
//        PixelSizeUtil.setStatusBarHeightView(view)
        initImgs()
        initDots()
        // 初始化Adapter
        val vpAdapter = ViewPagerAdapter(imageViews)
        guide_vp.adapter = vpAdapter
        guide_vp.addOnPageChangeListener(this)
//        tv_jump_over.clickWithTrigger {
//            determine()
//        }
    }

    private fun determine() {
        // 设置已经引导
        CacheHelper.getInstance().setFirstInStatus(false)
        skipActivity(Intent(mContext, MainActivity::class.java))
    }


    /**
     * 初始化图片
     */
    private fun initImgs() {
        val params = ViewPager.LayoutParams()
        for (i in 0 until imgs.size) {
            val imageView = View(this)
            imageView.layoutParams = params
            imageView.setBackgroundResource(imgs[i])
            if(i==imgs.size-1){
                imageView.setOnClickListener { determine() }
            }
            imageViews.add(imageView)
        }
    }

    /**
     * 初始化底部圆点指示器
     */
    private fun initDots() {
//        val params = LinearLayout.LayoutParams(dp2px(11f), dp2px(11f))
//        params.setMargins(dp2px(6f), 0, dp2px(6f), 0)
//        for (i in 0 until imageViews.size) {
//            val imageView = ImageView(this)
//            imageView.layoutParams = params
//            imageView.setImageResource(R.drawable.dotselector)
//            imageView.isSelected = i == 0
//            dotViews.add(imageView)
//            guide_ll.addView(imageView)
//        }
    }


    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(arg0: Int) {
//        for (i in 0 until dotViews.size) {
//            dotViews[i].isSelected = arg0 == i
//            if (arg0 == dotViews.size - 1) {
//                rl_guide.visibility = View.VISIBLE
//            } else {
//                rl_guide.visibility = View.GONE
//            }
//        }
    }


    class ViewPagerAdapter(private val views: List<View>?) : PagerAdapter() {

        // 销毁arg1位置的界面
        override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
            (arg0 as ViewPager).removeView(views!![arg1])
        }

        override fun finishUpdate(arg0: View) {}

        // 获得当前界面数
        override fun getCount(): Int {
            return views?.size ?: 0
        }

        // 初始化arg1位置的界面
        override fun instantiateItem(arg0: View, arg1: Int): Any {
            (arg0 as ViewPager).addView(views!![arg1], 0)
            return views[arg1]
        }

        // 判断是否由对象生成界面
        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun restoreState(arg0: Parcelable?, arg1: ClassLoader?) {}

        override fun saveState(): Parcelable? {
            return null
        }

        override fun startUpdate(arg0: View) {}

    }

}