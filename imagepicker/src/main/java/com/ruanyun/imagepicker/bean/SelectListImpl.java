package com.ruanyun.imagepicker.bean;

import android.os.Parcelable;

import java.util.List;

public interface SelectListImpl extends Parcelable {

    String getShowName();

    String getShowCode();

    String getSuperCode();

    boolean isSelect();

    void setSelect(boolean isSelect);

    List<? extends SelectListImpl> getChild();
}