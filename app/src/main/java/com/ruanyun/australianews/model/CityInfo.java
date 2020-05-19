package com.ruanyun.australianews.model;

import android.text.TextUtils;
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel;

import java.util.List;

/**
 * @author hdl
 * @description 城市信息
 * @date 2018/11/26
 */
public class CityInfo implements FilterInfoUiModel {

    public String cityCode;
    public String cityName;

    public String classify = "";
    public int itemType = CONTENT;
    public static final int CLASSIFY = 1;
    public static final int CONTENT = 0;


    public String unlimitedShowName;//添加一个全部条件时使用

    public CityInfo(String cityName, int itemType) {
        this.cityName = cityName;
        this.itemType = itemType;
    }

    public CityInfo(String cityName, String unlimitedShowName, String cityCode) {
        this.cityName = cityName;
        this.unlimitedShowName = unlimitedShowName;
        this.cityCode = cityCode;
    }

    @Override
    public String getFilterName() {
        return cityName;
    }

    @Override
    public String getFilterShowName() {
        if(TextUtils.isEmpty(unlimitedShowName)) {
            return cityName;
        }else {
            return unlimitedShowName;
        }
    }

    @Override
    public String getFilterCode() {
        return cityCode;
    }

    @Override
    public List<? extends FilterInfoUiModel> getFilterChild() {
        return null;
    }
}
