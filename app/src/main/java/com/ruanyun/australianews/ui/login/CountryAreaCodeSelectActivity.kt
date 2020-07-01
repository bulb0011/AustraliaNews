package com.ruanyun.australianews.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.refreshview.impl.RvCommonAdapter
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.model.PhoneAreaInfo
import com.ruanyun.australianews.util.C
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_country_area_code_select.*

/**
 * @description 选择手机国家区号
 * @author hdl
 * @date 2019/5/8
 */
class CountryAreaCodeSelectActivity : BaseActivity() {
    companion object {
        fun start(activity: Activity, requestCode: Int) {
            val starter = Intent(activity, CountryAreaCodeSelectActivity::class.java)
            activity.startActivityForResult(starter, requestCode)
        }
    }


    val adapter by lazy { CountryAreaCodeAdapter(mContext, R.layout.item_list_country_area_code, PhoneAreaInfo.getCountryAreaList()) }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_country_area_code_select)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        adapter.block = {
            setResult(Activity.RESULT_OK, Intent().putExtra(C.IntentKey.PHONE_AREA_INFO, it))
            finish()
        }
    }


}

class CountryAreaCodeAdapter(context: Context, layoutId: Int, datas: List<PhoneAreaInfo>) : RvCommonAdapter<PhoneAreaInfo>(context, layoutId, datas) {
    override fun convert(holder: ViewHolder?, t: PhoneAreaInfo, position: Int) {
        holder?.getView<ImageView>(R.id.iv_area)?.setImageResource(t.drawableId)
        holder?.setText(R.id.tv_name, t.countryName)
        holder?.setText(R.id.tv_code, t.areaCode)
        holder?.convertView?.clickWithTrigger {
            block.invoke(t)
        }
    }

    var block = fun(_ : PhoneAreaInfo){}
}