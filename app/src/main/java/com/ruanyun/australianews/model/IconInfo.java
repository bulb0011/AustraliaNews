package com.ruanyun.australianews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author hdl
 * @description
 * @date 2019/5/5
 */
public class IconInfo implements Parcelable {
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_ICON = 2;
    public static final int TYPE_FILLING = 3;
    public static final int TYPE_VIEW_PAGE = 4;

    public static final int TYPE_TITLE_YELLOW_PAGE = 5;
    public static final int TYPE_CLASSIFICATION = 6;

    public int itemType;
    public String oid;
    public String title;
    public int iconRes;
    public String mainPhoto;
    public List<IconInfo> iconInfoList;
    public boolean isShowGrayLine = true;

    public IconInfo(int itemType) {
        this.itemType = itemType;
    }

    public IconInfo(int itemType, String iconName) {
        this.itemType = itemType;
        this.title = iconName;
    }

    public IconInfo(int itemType, String iconName, boolean isShowGrayLine) {
        this.itemType = itemType;
        this.title = iconName;
        this.isShowGrayLine = isShowGrayLine;
    }

    public IconInfo(String iconName, int iconRes) {
        this.itemType = itemType;
        this.title = iconName;
        this.iconRes = iconRes;
    }

    public IconInfo(int itemType, String iconName, int iconRes) {
        this.itemType = itemType;
        this.title = iconName;
        this.iconRes = iconRes;
    }

    public IconInfo(int itemType, List<IconInfo> iconInfoList) {
        this.itemType = itemType;
        this.iconInfoList = iconInfoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
        dest.writeString(this.oid);
        dest.writeString(this.title);
        dest.writeInt(this.iconRes);
        dest.writeString(this.mainPhoto);
        dest.writeTypedList(this.iconInfoList);
        dest.writeByte(this.isShowGrayLine ? (byte) 1 : (byte) 0);
    }

    protected IconInfo(Parcel in) {
        this.itemType = in.readInt();
        this.oid = in.readString();
        this.title = in.readString();
        this.iconRes = in.readInt();
        this.mainPhoto = in.readString();
        this.iconInfoList = in.createTypedArrayList(IconInfo.CREATOR);
        this.isShowGrayLine = in.readByte() != 0;
    }

    public static final Creator<IconInfo> CREATOR = new Creator<IconInfo>() {
        @Override
        public IconInfo createFromParcel(Parcel source) {
            return new IconInfo(source);
        }

        @Override
        public IconInfo[] newArray(int size) {
            return new IconInfo[size];
        }
    };
}
