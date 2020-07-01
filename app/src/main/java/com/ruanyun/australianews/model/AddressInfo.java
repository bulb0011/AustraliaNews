package com.ruanyun.australianews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hdl
 * @description 收货地址信息
 * @date 2019/2/20
 */
public class AddressInfo implements Parcelable {
    public String city;//城市
    private String addressName;//局域街道信息
    private String address;//详细地址
    private String longitude;//经度
    private String latitude;//纬度

    public AddressInfo(String city, String addressName, String longitude, String latitude) {
        this.city = city;
        this.addressName = addressName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public AddressInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.addressName);
        dest.writeString(this.address);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
    }

    protected AddressInfo(Parcel in) {
        this.city = in.readString();
        this.addressName = in.readString();
        this.address = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
    }

    public static final Creator<AddressInfo> CREATOR = new Creator<AddressInfo>() {
        @Override
        public AddressInfo createFromParcel(Parcel source) {
            return new AddressInfo(source);
        }

        @Override
        public AddressInfo[] newArray(int size) {
            return new AddressInfo[size];
        }
    };
}
