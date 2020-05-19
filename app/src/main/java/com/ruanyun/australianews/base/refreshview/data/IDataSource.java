package com.ruanyun.australianews.base.refreshview.data;

/**
 * Description:
 * author: zhangsan on 2017/8/11 下午2:38.
 */

public interface IDataSource<Data> {

    int FIRST_PAGENUM = 1;
    int REFRESH=111;
    int LOADMORE=112;

    /**
     * @param loadAction  :REFRESH  LOADMORE
     * @param handler
     */
    void requestData(int loadAction, IDataSourceResultHandler<Data> handler);

    void cancel();

    boolean hasMore();

    void setTotalPage(int totalPage);

    void setCurrentPage(int page);

    int getCurrentPage();


 //   IDataSourceResultHandler<Data> getResultHandler();
    /**
     * Description:
     * author: zhangsan on 2017/8/12 上午10:28.
     */

    interface IDataSourceResultHandler<Data> {

        void handleResult(int loadAction, Data data);

        void handleError(int loadAction, Throwable throwable);
    }
}
