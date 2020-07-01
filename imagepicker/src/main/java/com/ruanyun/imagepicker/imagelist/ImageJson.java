package com.ruanyun.imagepicker.imagelist;

import android.support.annotation.Keep;

/**
 * Created by hdl on 2017/1/10.
 */
@Keep
public class ImageJson {
    public String filePath;
    public String imgList;

    @Override
    public String toString() {
        return "ImageJson{" +
                "filePath='" + filePath + '\'' +
                ", imgList='" + imgList + '\'' +
                '}';
    }
}
