package com.ruanyun.imagepicker.compressimage;

public interface CompressImageInfoGetter {
    void setParamsName(String paramsName);

    String imageFilePath();

    String imageFileName();

    String requestPramsName();

}