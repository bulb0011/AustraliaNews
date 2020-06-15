package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.ruanyun.australianews.App
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_setlanguage.*


class SetLanguageActivity :BaseActivity() {

    companion object{
        fun start(context: Context) {
            val starter = Intent(context, SetLanguageActivity::class.java)
            context.startActivity(starter)
        }
    }

    var language_type="zh"


    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(com.ruanyun.australianews.R.layout.activity_setlanguage)

//        EventBus.getDefault().register(this)

       val yuyan=SharedPreferencesUtils.getParam(
            App.context,
            SharedPreferencesUtils.KEY_SYSTEM_LANGUAGE,
            ""
        ) as String

        if (TextUtils.isEmpty(yuyan)){
            image_zh.visibility=View.VISIBLE
            image_en.visibility=View.GONE
        }
        else if ("zh".equals(yuyan)){
            image_zh.visibility=View.VISIBLE
            image_en.visibility=View.GONE
        }
        else if ("en".equals(yuyan)){
            image_zh.visibility=View.GONE
            image_en.visibility=View.VISIBLE
        }

        Event()
    }

    private fun Event() {



        tv_zhongwen.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                image_zh.visibility=View.VISIBLE
                image_en.visibility=View.GONE
                language_type="zh"
            }
        })

        tv_yingwen.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                image_zh.visibility=View.GONE
                image_en.visibility=View.VISIBLE
                language_type="en"
            }
        })


        /**
         * 完成
         */
        tv_wancheng.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                SharedPreferencesUtils.setParam(
                    App.context,
                    SharedPreferencesUtils.KEY_SYSTEM_LANGUAGE,language_type
                )

                EventNotifier.getInstance().changeAppLanguage()
            }
        })

        tv_quxiao.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }
        })




    }

    override fun onDestroy() {
        super.onDestroy()
//        EventBus.getDefault().unregister(this)
    }



}