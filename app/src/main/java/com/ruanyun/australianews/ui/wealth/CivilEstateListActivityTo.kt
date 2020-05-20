package com.ruanyun.australianews.ui.wealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.model.CivilEstateInfo
import com.ruanyun.australianews.util.C
import kotlinx.android.synthetic.main.activity_civil_estate_list.topbar
import kotlinx.android.synthetic.main.activity_civil_estate_list_to.*

/**
 * @description 民用地产
 * @author hdl
 * @date 2020/3/26
 */
class CivilEstateListActivityTo : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View) {

        when(v.id){

            R.id.rl_juminlou ->{
                topbar.setTitleText("居民楼")
                v_jumin.visibility=View.VISIBLE
                v_minsheng.visibility=View.INVISIBLE
                v_shangye.visibility=View.INVISIBLE
                v_nongc.visibility=View.INVISIBLE
                supportFragmentManager.beginTransaction().
                    add(R.id.container,HousingMarketListFragment()).commit()
            }

            R.id.rl_minshangyongdi ->{
                topbar.setTitleText("民生用地")
                v_jumin.visibility=View.INVISIBLE
                v_minsheng.visibility=View.VISIBLE
                v_shangye.visibility=View.INVISIBLE
                v_nongc.visibility=View.INVISIBLE
                supportFragmentManager.beginTransaction().
                    add(R.id.container, CivilEstateListFragment.newFragment(CivilEstateInfo.ESTATEINFO_TYPE1)).commit()
                type=CivilEstateInfo.ESTATEINFO_TYPE1
            }

            R.id.rl_shangyeyongdi ->{
                topbar.setTitleText("商业用地")
                v_jumin.visibility=View.INVISIBLE
                v_minsheng.visibility=View.INVISIBLE
                v_shangye.visibility=View.VISIBLE
                v_nongc.visibility=View.INVISIBLE
                supportFragmentManager.beginTransaction().
                    add(R.id.container, CivilEstateListFragment.newFragment(CivilEstateInfo.ESTATEINFO_TYPE2)).commit()
                type=CivilEstateInfo.ESTATEINFO_TYPE2
            }

            R.id.rl_nongchang ->{
                topbar.setTitleText("农场")
                v_jumin.visibility=View.INVISIBLE
                v_minsheng.visibility=View.INVISIBLE
                v_shangye.visibility=View.INVISIBLE
                v_nongc.visibility=View.VISIBLE
                supportFragmentManager.beginTransaction().
                    add(R.id.container, CivilEstateListFragment.newFragment(CivilEstateInfo.ESTATEINFO_TYPE3)).commit()

                type=CivilEstateInfo.ESTATEINFO_TYPE3

            }
        }

    }

    companion object {
        fun start(context: Context, type: String) {
            val starter = Intent(context, CivilEstateListActivityTo::class.java)
            starter.putExtra(C.IntentKey.TYPE, type)
            context.startActivity(starter)
        }
    }

    lateinit var type: String

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_civil_estate_list_to)
        initView()
    }

    private fun initView() {

        topbar.setTopBarClickListener(this)
//        type = intent.getStringExtra(C.IntentKey.TYPE)
//        val title = when(type){
//            CivilEstateInfo.ESTATEINFO_TYPE1 -> {
//                "民生用地"
//            }
//            CivilEstateInfo.ESTATEINFO_TYPE2 -> {
//                "商业用地"
//            }
//            CivilEstateInfo.ESTATEINFO_TYPE3 -> {
//                "农场"
//            }
//            else->{
//                "居民楼"
//            }
//        }

        type=CivilEstateInfo.ESTATEINFO_TYPE4
        topbar.setTitleText("居民楼")
//        supportFragmentManager.beginTransaction().add(R.id.container, CivilEstateListFragment.newFragment(type)).commit()
        supportFragmentManager.beginTransaction().add(R.id.container,HousingMarketListFragment()).commit()


        rl_juminlou.setOnClickListener(this)
        rl_minshangyongdi.setOnClickListener(this)
        rl_shangyeyongdi.setOnClickListener(this)
        rl_nongchang.setOnClickListener(this)


        v_jumin.visibility=View.VISIBLE
        v_minsheng.visibility=View.INVISIBLE
        v_shangye.visibility=View.INVISIBLE
        v_nongc.visibility=View.INVISIBLE


    }

    override fun onTopBarRightImgClick() {
        if(isLoginToActivityByIsRelease) {

            if(type.equals(CivilEstateInfo.ESTATEINFO_TYPE4)){
                ReleaseHousingMarketActivity.start(mContext)
            }else{
                ReleaseCivilEstateActivity.start(mContext, type)
            }




        }
    }
}