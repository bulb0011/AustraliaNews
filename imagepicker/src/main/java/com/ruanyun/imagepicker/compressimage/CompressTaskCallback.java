package com.ruanyun.imagepicker.compressimage;

public interface CompressTaskCallback<T> {
    void onCompresComplete(T compressResults);
    //void onCompressProcess(int percent);
    void onCompresFail(Throwable throwable);
}