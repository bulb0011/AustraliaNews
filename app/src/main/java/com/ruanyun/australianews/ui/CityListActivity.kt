package com.ruanyun.australianews.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.google.android.flexbox.FlexboxLayout
import com.ruanyun.imagepicker.permissions.PermissionsManager
import com.ruanyun.imagepicker.permissions.PermissionsResultAction
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.ext.*
import com.ruanyun.australianews.model.CityInfo
import com.ruanyun.australianews.mvpview.CityListMvpView
import com.ruanyun.australianews.presenter.CityListPresenter
import com.ruanyun.australianews.ui.adapter.CityListAdapter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.CommonUtil
import com.ruanyun.australianews.util.EventNotifier
import com.ruanyun.australianews.util.LocationHelper
import kotlinx.android.synthetic.main.activity_city_list.*
import javax.inject.Inject

/**
 * @author hdl
 * @description 城市列表
 * @date 2019/3/12
 */
class CityListActivity : BaseActivity(), CityListMvpView {

    var allCityList = listOf<CityInfo>()

    override fun getAllCityOnSuccess(citys: List<CityInfo>) {
        allCityList = presenter.sortUserList(citys)
        adapter.setData(presenter.getLetterList(allCityList))
    }

    override fun getPopularCitiesOnSuccess(citys: List<CityInfo>) {
        citys.forEach {city ->
            val view: TextView = LayoutInflater.from(mContext).inflate(R.layout.item_flaxbox_data_hot_city, null) as TextView
            view.text = city.cityName
            view.clickWithTrigger {
                finishData(city.cityName)
            }
            flaxboxLayout?.addView(view)
        }
    }

    private lateinit var headView: View
    private var locationCity: TextView?=null
    private var textHotCity: TextView?=null
    private var flaxboxLayout: FlexboxLayout?=null

    var locationHelper:LocationHelper?=null

    @Inject
    lateinit var presenter: CityListPresenter
    lateinit var adapter: CityListAdapter

    var isSelectCity = false

    companion object {
        const val TYPE = "type"
        /** 切换城市 */
        fun start(context: Context) {
            val starter = Intent(context, CityListActivity::class.java)
            context.startActivity(starter)
        }
        /** 选择城市 */
        fun start(activity: Activity, requestCode: Int) {
            val starter = Intent(activity, CityListActivity::class.java)
            starter.putExtra(TYPE, true)
            activity.startActivityForResult(starter, requestCode)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_city_list)
        presenter.attachView(this)
        initView()
        presenter.getAllCityList()
//        if(!isSelectCity) {
//            presenter.getHotCityList()
//            Handler().postDelayed({
//                requestJurisdiction()
//            }, 500)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        locationHelper?.stop()
    }


    private fun initView() {
        isSelectCity = intent.getBooleanExtra(TYPE, false)
        adapter = CityListAdapter(mContext, arrayListOf())
        list.adapter = adapter
//        sidebar.isHot = !isSelectCity
        sidebar.setListView(list)
//        if(isSelectCity) {
            headView = View(mContext)
//        }else {
//            headView = LayoutInflater.from(mContext).inflate(R.layout.head_item_list_location_city, null, false)
//            locationCity = headView.findViewById(R.id.tv_city_content)
//            textHotCity = headView.findViewById(R.id.text_hot_city)
//            flaxboxLayout = headView.findViewById(R.id.flaxbox_layout)
//            locationCity?.click {
//                val cityName = locationCity?.getStr()
//                if(CommonUtil.isNotEmpty(cityName) && cityName != getString(R.string.in_location) && cityName != getString(R.string.location_fail)) {
//                    finishData(cityName)
//                }
//            }
//        }
        list.addHeaderView(headView)
        list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                return@OnItemClickListener
            }
            val cityInfo = adapter.getItem(position - 1)
            if (cityInfo.itemType == CityInfo.CONTENT) {
                finishData(cityInfo.cityName)
            }
        }
        iv_back.click {
            finish()
        }

        et_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(et_search.isEmpty()){
                    adapter.setData(presenter.getLetterList(allCityList))
                }else {
                    val searchContent = et_search.getStr()
                    val list = allCityList.filter {
                        it.cityName.contains(searchContent)
                    }
                    adapter.setData(presenter.getLetterList(list))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun finishData(cityName: String?) {
        cityName?:return
        if(isSelectCity){
            setResult(Activity.RESULT_OK, Intent().putExtra(C.IntentKey.CITY_NAME, cityName))
            finish()
        }else {
            app.cityName = cityName
            EventNotifier.getInstance().switchCitySuccess()
        }
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }

    private fun requestJurisdiction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, LocationHelper.PERMISSIONS,
                object : PermissionsResultAction() {

                    override fun onGranted() {
                        initLocation()
                    }

                    override fun onDenied(permission: String) {
                        showToast("获取定位权限被拒绝")
                        locationCity?.text = resources.getString(R.string.location_fail)
                    }
                })
        } else {
            initLocation()
        }
    }

    private fun initLocation() {
        locationCity?.text = resources.getString(R.string.in_location)
        locationHelper = LocationHelper(mContext, 3000)
        locationHelper?.setLocationListener(object : BDAbstractLocationListener(){
            override fun onReceiveLocation(it: BDLocation?) {
                it?.apply {
                    if(CommonUtil.isNotEmpty(city)) {
                        locationCity?.text = city
                        app.cityName = city
                        app.longitude = longitude.toString()
                        app.latitude = latitude.toString()
                        locationHelper?.stop()
                    }
                }
            }
        })
        locationHelper?.start()
    }


}
