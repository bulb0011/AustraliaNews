package com.ruanyun.australianews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ruanyun.australianews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdl
 * @description 手机号码，国家区号
 * @date 2019/5/8
 */
public class PhoneAreaInfo implements Parcelable {
    public String countryName;//国家
    public String areaCode;//区号
    public int drawableId;//图标

    private PhoneAreaInfo(String countryName, String areaCode, int drawableId) {
        this.countryName = countryName;
        this.areaCode = areaCode;
        this.drawableId = drawableId;
    }

    public static List<PhoneAreaInfo> getCountryAreaList(){
        List<PhoneAreaInfo> infos = new ArrayList<>();
        infos.add(new PhoneAreaInfo("澳大利亚", "+61", R.drawable.login_icon_australia));
        infos.add(new PhoneAreaInfo("中国大陆", "+86", R.drawable.login_icon_china));
        infos.add(new PhoneAreaInfo("中国台湾", "+886", R.drawable.login_icon_taiwan));
        infos.add(new PhoneAreaInfo("中国香港", "+852", R.drawable.login_icon_hongkong));
        infos.add(new PhoneAreaInfo("中国澳门", "+853", R.drawable.login_icon_macao));
        infos.add(new PhoneAreaInfo("新西兰", "+64", R.drawable.login_icon_newzealand));
        return infos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryName);
        dest.writeString(this.areaCode);
        dest.writeInt(this.drawableId);
    }

    protected PhoneAreaInfo(Parcel in) {
        this.countryName = in.readString();
        this.areaCode = in.readString();
        this.drawableId = in.readInt();
    }

    public static final Creator<PhoneAreaInfo> CREATOR = new Creator<PhoneAreaInfo>() {
        @Override
        public PhoneAreaInfo createFromParcel(Parcel source) {
            return new PhoneAreaInfo(source);
        }

        @Override
        public PhoneAreaInfo[] newArray(int size) {
            return new PhoneAreaInfo[size];
        }
    };
}
