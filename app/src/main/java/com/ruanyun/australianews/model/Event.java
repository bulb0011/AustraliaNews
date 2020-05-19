package com.ruanyun.australianews.model;



/**
 * Description:
 * author: zhangsan on 16/8/26 上午9:21.
 */
public class Event<T>  {
    public int keyInt;
    public String key;
    public T  value;

    public Event() {
    }

    public Event(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public Event(int keyInt, T value) {
        this.keyInt = keyInt;
        this.value = value;
    }

    public Event(String key, int keyInt, T value) {
        this.key = key;
        this.keyInt = keyInt;
        this.value = value;
    }
}
