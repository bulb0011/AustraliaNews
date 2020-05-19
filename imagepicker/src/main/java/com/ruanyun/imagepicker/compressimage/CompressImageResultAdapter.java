package com.ruanyun.imagepicker.compressimage;

/**
 * Description:
 * author: zhangsan on 16/11/28 下午5:06.
 */
public interface CompressImageResultAdapter<T> {

    T convert(CompressImageInfoGetter... compressImageInfoGetters);

    abstract class Factory {
        public abstract CompressImageResultAdapter<?> creat();
    }
}
