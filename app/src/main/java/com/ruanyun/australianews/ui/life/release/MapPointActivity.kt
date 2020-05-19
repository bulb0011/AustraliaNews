package com.ruanyun.australianews.ui.life.release

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import kotlinx.android.synthetic.main.activity_map_poi.*
import com.baidu.location.LocationClientOption
import com.baidu.location.LocationClient
import com.baidu.location.BDLocation
import com.baidu.location.BDAbstractLocationListener
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.ruanyun.imagepicker.permissions.PermissionsManager
import com.ruanyun.imagepicker.permissions.PermissionsResultAction
import com.ruanyun.australianews.model.AddressInfo
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.LocationHelper
import com.ruanyun.australianews.util.LogX


/**
 * @description
 * @author hdl 选择地点
 * @date 2019/5/24
 */
class MapPointActivity : BaseActivity() {
    companion object {
        fun start(fragment: Fragment, requestCode: Int) {
            val starter = Intent(fragment.context, MapPointActivity::class.java)
            fragment.startActivityForResult(starter, requestCode)
        }

        fun start(activity: Activity, requestCode: Int) {
            val starter = Intent(activity, MapPointActivity::class.java)
            activity.startActivityForResult(starter, requestCode)
        }
    }

    lateinit var mBaiduMap: BaiduMap
    lateinit var mLocationClient: LocationClient
    var mReverseGeoCodeResult: ReverseGeoCodeResult? = null
    var mCoder: GeoCoder? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_map_poi)
        mapView.onCreate(mContext, bundle)
        initView()
        requestLocationJurisdiction()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        mLocationClient.stop()
        mBaiduMap.isMyLocationEnabled = false
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState)
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        mBaiduMap = mapView.map
        mBaiduMap.setOnMapStatusChangeListener(listener)
        //初始化定位
        mBaiduMap.setMyLocationEnabled(true)

        //定位初始化
        mLocationClient = LocationClient(this)

        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(1000)

        //设置locationClientOption
        mLocationClient.setLocOption(option)

        //注册LocationListener监听器
        val myLocationListener = MyLocationListener()
        mLocationClient.registerLocationListener(myLocationListener)

    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null) {
                return
            }
            mLocationClient.stop()
            val locData = MyLocationData.Builder()
                    .accuracy(location.radius)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.direction).latitude(location.latitude)
                    .longitude(location.longitude).build()
            mBaiduMap.setMyLocationData(locData)

            val latLng = LatLng(location.getLatitude(), location.getLongitude())
            val mMapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(latLng, 16f)
            mBaiduMap.animateMapStatus(mMapStatusUpdate)
            getReverseGeoCode(latLng)
        }
    }

    internal var listener: BaiduMap.OnMapStatusChangeListener = object : BaiduMap.OnMapStatusChangeListener {
        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         * @param status 地图状态改变开始时的地图状态
         */
        override fun onMapStatusChangeStart(status: MapStatus) {}

        /** 因某种操作导致地图状态开始改变。
         * @param status 地图状态改变开始时的地图状态
         * @param reason表示地图状态改变的原因，取值有：
         * 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
         * 2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
         * 3：开发者调用,导致的地图状态改变
         */
        override fun onMapStatusChangeStart(status: MapStatus, reason: Int) {

        }

        /**
         * 地图状态变化中
         * @param status 当前地图状态
         */
        override fun onMapStatusChange(status: MapStatus) {}

        /**
         * 地图状态改变结束
         * @param status 地图状态改变结束后的地图状态
         */
        override fun onMapStatusChangeFinish(status: MapStatus) {
            getReverseGeoCode(status.target)
        }
    }

    private fun getReverseGeoCode(latLng: LatLng) {
        if (mCoder == null) {
            mCoder = GeoCoder.newInstance()
            mCoder!!.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
                override fun onGetGeoCodeResult(p0: GeoCodeResult?) {

                }

                override fun onGetReverseGeoCodeResult(reverseGeoCodeResult: ReverseGeoCodeResult?) {
                    if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有找到检索结果
                        return
                    } else {
                        //详细地址
                        mReverseGeoCodeResult = reverseGeoCodeResult
                        LogX.e("retrofit", "onGetReverseGeoCodeResult() : reverseGeoCodeResult = [$reverseGeoCodeResult]")
                    }
                }
            })
        }
        mCoder!!.reverseGeoCode(ReverseGeoCodeOption()
                .location(latLng)
                // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(100))
    }


    override fun onTopBarRightTextClick() {
        mReverseGeoCodeResult?.apply {
            val address = addressDetail.district + addressDetail.town + addressDetail.street + sematicDescription
            setResult(Activity.RESULT_OK, Intent().putExtra(C.IntentKey.ADDRESS_INFO,
                    AddressInfo(addressDetail.city, address, location.longitude.toString(), location.latitude.toString())))
            finish()
        }
    }

    /**
     * 请求定位权限
     */
    private fun requestLocationJurisdiction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, LocationHelper.PERMISSIONS,
                    object : PermissionsResultAction() {

                        override fun onGranted() {
                            startLocation()
                        }

                        override fun onDenied(permission: String) {
                            showToast("获取定位权限被拒绝")
                        }
                    })
        } else {
            startLocation()
        }
    }

    /**
     * 启动定位
     */
    private fun startLocation() {
        //开启地图定位图层
        mLocationClient.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }


}