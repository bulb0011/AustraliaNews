package com.ruanyun.australianews.base;

import java.util.List;

/**
 * Description:
 * author: jery on 2016/4/29 10:26.
 */
public  class PageInfoBase<T> {
    public int pageNumber;//页码
    public int pageSize;//页面显示大小
    public int totalRows;//总数量
    public int maxPageNumber;//最大页数

    public int getMaxPage(){
        if(totalRows%pageSize==0){
            return totalRows/pageSize;
        }
        return (totalRows/pageSize)+1;
    }
    public List<T> datas;
}
