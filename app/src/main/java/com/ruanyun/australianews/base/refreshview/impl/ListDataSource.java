package com.ruanyun.australianews.base.refreshview.impl;


import com.ruanyun.australianews.data.ApiService;

/**
 * Description:
 * author: zhangsan on 2017/8/18 下午4:03.
 */

public  abstract class ListDataSource extends RxDataSource {


    public ListDataSource(ApiService apiService) {
        super(apiService);
    }


    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void setTotalPage(int totalPage) {

    }

    @Override
    public void setCurrentPage(int page) {

    }

    @Override
    public int getCurrentPage() {
        return 0;
    }
}
