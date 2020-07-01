package com.ruanyun.australianews.model;


import android.os.Parcel;
import android.os.Parcelable;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;


/**
 * Created by yuelin on 2016/5/27.
 */
@Entity
public class ChannelInfo implements Comparable<ChannelInfo>, Parcelable {

    @Id(autoincrement = true)
    @Unique
    public Long id;
    public boolean isLogin;//true:登录时获取的个人频道，false:未登录时显示的频道

    public int sort;//用于排序的id
    public boolean isSubscribe;//用于判断是否已经订阅
    public String title;//系统或个人频道名称
    public String oid;//系统或个人频道oid
    public int type;//类型，1.系统频道，2个人频道
    public int isTop;//是否置顶，1是，2.否 (前端不可更改排序)
    public int isFixed;//是否固定，1.是，2否 （固定在频道中，不可被取消订阅）

    @Override
    public final int compareTo(ChannelInfo another) {
        return this.sort - another.sort;
    }

    public ChannelInfo() {
    }

    public ChannelInfo(boolean isLogin, int sort, boolean isSubscribe, String title, String oid, int type, int isTop, int isFixed) {
        this.isLogin = isLogin;
        this.sort = sort;
        this.isSubscribe = isSubscribe;
        this.title = title;
        this.oid = oid;
        this.type = type;
        this.isTop = isTop;
        this.isFixed = isFixed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeByte(this.isLogin ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sort);
        dest.writeByte(this.isSubscribe ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeString(this.oid);
        dest.writeInt(this.type);
        dest.writeInt(this.isTop);
        dest.writeInt(this.isFixed);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsLogin() {
        return this.isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean getIsSubscribe() {
        return this.isSubscribe;
    }

    public void setIsSubscribe(boolean isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOid() {
        return this.oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsTop() {
        return this.isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsFixed() {
        return this.isFixed;
    }

    public void setIsFixed(int isFixed) {
        this.isFixed = isFixed;
    }

    protected ChannelInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.isLogin = in.readByte() != 0;
        this.sort = in.readInt();
        this.isSubscribe = in.readByte() != 0;
        this.title = in.readString();
        this.oid = in.readString();
        this.type = in.readInt();
        this.isTop = in.readInt();
        this.isFixed = in.readInt();
    }

    @Generated(hash = 1558160640)
    public ChannelInfo(Long id, boolean isLogin, int sort, boolean isSubscribe,
            String title, String oid, int type, int isTop, int isFixed) {
        this.id = id;
        this.isLogin = isLogin;
        this.sort = sort;
        this.isSubscribe = isSubscribe;
        this.title = title;
        this.oid = oid;
        this.type = type;
        this.isTop = isTop;
        this.isFixed = isFixed;
    }

    public static final Creator<ChannelInfo> CREATOR = new Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(Parcel source) {
            return new ChannelInfo(source);
        }

        @Override
        public ChannelInfo[] newArray(int size) {
            return new ChannelInfo[size];
        }
    };

    public static boolean isDataSame(List<ChannelInfo> list1, List<ChannelInfo> list2){
        if(list1.size()!=list2.size()){
            return false;
        }
        for (ChannelInfo info1 : list1) {
            boolean isExist = false;
            for (ChannelInfo info2 : list2) {
                if(info1.oid.equals(info2.oid) && info1.title.equals(info2.title)){
                    isExist = true;
                    break;
                }
            }
            if(!isExist){
                return false;
            }
        }
        return true;
    }
}
