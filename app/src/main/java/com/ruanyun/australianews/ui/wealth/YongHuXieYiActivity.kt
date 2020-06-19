package com.ruanyun.australianews.ui.wealth

import android.os.Bundle
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import kotlinx.android.synthetic.main.avtivity_yonghuxieyi.*

class YongHuXieYiActivity :BaseActivity(){

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.avtivity_yonghuxieyi)

        tv_context.text=resources.getString(R.string.xieyi)

        image_que.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }
        })

    }

}