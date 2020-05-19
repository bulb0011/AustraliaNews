package com.ruanyun.australianews.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author hdl
 * @description 历史搜索信息
 * @date 2018/9/7
 */
@Entity
public class HistorySearchInfo {
    @Unique
    public String keyWord;
    public long time;
    @Generated(hash = 2117220)
    public HistorySearchInfo(String keyWord, long time) {
        this.keyWord = keyWord;
        this.time = time;
    }
    @Generated(hash = 124297367)
    public HistorySearchInfo() {
    }
    public String getKeyWord() {
        return this.keyWord;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
}
