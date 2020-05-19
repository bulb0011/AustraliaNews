package com.ruanyun.australianews.base;

/**
 * Created by ycw on 2016/8/29.
 */
public class PageParamsBase {
    private Integer pageNumber;//页码
    private Integer pageSize;//分页 每页显示的条数

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    public Integer getPageNumber() {
        return pageNumber;
    }
}
