package com.ruanyun.australianews.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


/**
 * description:
 * <p/>
 * Created by ycw on 2019/12/23.
 */
public class CommentInfo implements Parcelable {
    @SerializedName("commonOid")
    public String commonOid; // 402881fb6a4900d3016a49029f8b0001
    @SerializedName("createTime")
    public String createTime; // 2019-04-26 14:45:12
    @SerializedName("dateTime")
    public String dateTime; // 2小时前
    @SerializedName("mainPhoto")
    public String mainPhoto; // 20190423150429781.jpg
    @SerializedName("name")
    public String name; // app
    @SerializedName("oid")
    public String oid; // 402881f56a584156016a586409ee0000
    @SerializedName("replyCount")
    public int replyCount; // 12
    @SerializedName("replyMsg")
    public String replyMsg; // ç»å¯¹æ¹è½¦VB
    @SerializedName("title")
    public String title; // 抖音
    @SerializedName("type")
    public int type; // 1
    @SerializedName("userOid")
    public String userOid; // 402881fb6a581d0e016a5821af4b0000
    @SerializedName("userPhoto")
    public String userPhoto; // 20190426133243232.jpg
    public UserInfo sysUser;//

    public CommentInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commonOid);
        dest.writeString(this.createTime);
        dest.writeString(this.dateTime);
        dest.writeString(this.mainPhoto);
        dest.writeString(this.name);
        dest.writeString(this.oid);
        dest.writeInt(this.replyCount);
        dest.writeString(this.replyMsg);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeString(this.userOid);
        dest.writeString(this.userPhoto);
        dest.writeParcelable(this.sysUser, flags);
    }

    protected CommentInfo(Parcel in) {
        this.commonOid = in.readString();
        this.createTime = in.readString();
        this.dateTime = in.readString();
        this.mainPhoto = in.readString();
        this.name = in.readString();
        this.oid = in.readString();
        this.replyCount = in.readInt();
        this.replyMsg = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
        this.userOid = in.readString();
        this.userPhoto = in.readString();
        this.sysUser = in.readParcelable(UserInfo.class.getClassLoader());
    }

    public static final Creator<CommentInfo> CREATOR = new Creator<CommentInfo>() {
        @Override
        public CommentInfo createFromParcel(Parcel source) {
            return new CommentInfo(source);
        }

        @Override
        public CommentInfo[] newArray(int size) {
            return new CommentInfo[size];
        }
    };
}