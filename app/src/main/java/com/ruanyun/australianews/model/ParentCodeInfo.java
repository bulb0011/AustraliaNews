package com.ruanyun.australianews.model;

import android.os.Parcel;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.ruanyun.australianews.widget.filterpopwindow.FilterInfoUiModel;
import com.ruanyun.imagepicker.bean.SelectListImpl;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * @author hdl
 * @description 字典表信息
 * @date 2017/12/15
 */
@Entity
public class ParentCodeInfo implements SelectListImpl, FilterInfoUiModel, Cloneable {
    @Id
    @Unique
    public String oid;//传参使用
    public String name;
    public String code;//类型
    public String parentCode;
    @Transient
    public boolean isSelect = false;
    @Transient
    public String unlimitedShowName;//添加一个全部条件时使用
    @Transient
    public List<ParentCodeInfo> childList;


    @Override
    public String getShowName() {
        return name;
    }

    @Override
    public String getShowCode() {
        return code;
    }

    @Override
    public String getSuperCode() {
        return parentCode;
    }

    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public List<? extends SelectListImpl> getChild() {
        return childList;
    }

    public ParentCodeInfo() {
    }

    public ParentCodeInfo(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public ParentCodeInfo(String name, String unlimitedShowName, String code) {
        this.name = name;
        this.unlimitedShowName = unlimitedShowName;
        this.code = code;
    }

    public ParentCodeInfo(String name, Boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public ParentCodeInfo(String name, String parentCode, Boolean isSelect) {
        this.name = name;
        this.parentCode = parentCode;
        this.isSelect = isSelect;
    }

    public String getOid() {
        return this.oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        //picker选择使用
        return name;
    }

    @Override
    public String getFilterName() {
        return name;
    }

    @Override
    public String getFilterShowName() {
        if(TextUtils.isEmpty(unlimitedShowName)) {
            return name;
        }else {
            return unlimitedShowName;
        }
    }

    @Override
    public String getFilterCode() {
        return code;
    }

    @Override
    public List<? extends FilterInfoUiModel> getFilterChild() {
        return childList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return this.parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.oid);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.parentCode);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.childList);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    protected ParentCodeInfo(Parcel in) {
        this.oid = in.readString();
        this.name = in.readString();
        this.code = in.readString();
        this.parentCode = in.readString();
        this.isSelect = in.readByte() != 0;
        this.childList = in.createTypedArrayList(ParentCodeInfo.CREATOR);
    }

    @Generated(hash = 1443286809)
    public ParentCodeInfo(String oid, String name, String code, String parentCode) {
        this.oid = oid;
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
    }

    public static final Creator<ParentCodeInfo> CREATOR = new Creator<ParentCodeInfo>() {
        @Override
        public ParentCodeInfo createFromParcel(Parcel source) {
            return new ParentCodeInfo(source);
        }

        @Override
        public ParentCodeInfo[] newArray(int size) {
            return new ParentCodeInfo[size];
        }
    };

    @Override
    public ParentCodeInfo clone() {
        ParentCodeInfo clone = null;
        try{
            clone = (ParentCodeInfo) super.clone();
        }catch(CloneNotSupportedException e){
            return this;
        }
        return clone;
    }

    public String getName() {
        return this.name;
    }
}
