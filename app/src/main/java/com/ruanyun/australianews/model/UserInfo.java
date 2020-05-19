package com.ruanyun.australianews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ruanyun.australianews.util.FileUtil;

/**
 * @author hdl
 * @description 用户信息
 * @date 2018/9/3
 */
public class UserInfo implements Parcelable {


    private String headImageAttaOid;//"20190426133243232.jpg",  //用户头像
    public String oid;//用户oid
    public String loginPass;//登陆密码
    public String name;//用户名称
    public String sex;//用户性别   Y 男  N 女
    public String birthdate;//用户出生日期
    public String email;//电子邮箱
    public String mobile;//用户手机号
    public String phoneAreaCode;//手机号国家区号
    public String isLiveService;//用户是否允许发布生活服务   1是  2否
    public String isComment;//用户是否评价   1是  2否
    public String signature;//签名

    public String getUserPhoto() {
        return FileUtil.getImageUrl(headImageAttaOid);
    }

    public String getCompletePhone(){
        return phoneAreaCode + mobile;
    }

    public String getUserSexName() {
        if("Y".equals(sex)){
            return "男";
        }else if("N".equals(sex)){
            return "女";
        }else {
            return "";
        }
    }

    public boolean isUserSexMan() {
        return "Y".equals(sex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headImageAttaOid);
        dest.writeString(this.oid);
        dest.writeString(this.loginPass);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.birthdate);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.phoneAreaCode);
        dest.writeString(this.isLiveService);
        dest.writeString(this.isComment);
        dest.writeString(this.signature);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.headImageAttaOid = in.readString();
        this.oid = in.readString();
        this.loginPass = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.birthdate = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.phoneAreaCode = in.readString();
        this.isLiveService = in.readString();
        this.isComment = in.readString();
        this.signature = in.readString();
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
