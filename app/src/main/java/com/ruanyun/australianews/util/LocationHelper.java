package com.ruanyun.australianews.util;

import android.Manifest;
import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * @author hdl
 * @description 百度定位帮助类
 * @date 2019/2/13
 */
public class LocationHelper {
    private int span;//定位间隔时间毫秒
    private LocationClient mLocationClient = null;
    private BDAbstractLocationListener locationListener;
    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public LocationHelper(Context context, int span) {
        mLocationClient = new LocationClient(context.getApplicationContext());
        this.span = span;
        initLocation();
    }

    public LocationHelper setSpan(int span) {
        this.span = span;
        return this;
    }

    public LocationHelper setLocationListener(BDAbstractLocationListener locationListener) {
        this.locationListener = locationListener;
        mLocationClient.registerLocationListener(locationListener);
        return this;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocationClient.setLocOption(option);
    }

    public LocationHelper start() {
        mLocationClient.start();
        return this;
    }

    public void stop() {
        if (locationListener != null) {
            mLocationClient.unRegisterLocationListener(locationListener);
        }
        mLocationClient.stop();
    }
}
