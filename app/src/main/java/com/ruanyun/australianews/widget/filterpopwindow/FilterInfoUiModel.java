package com.ruanyun.australianews.widget.filterpopwindow;

import java.util.List;

/**
 * 筛选数据 UiModel
 * Created by hdl on 2017/5/13.
 */

public interface FilterInfoUiModel {
    /**
     * 名称
     */
    String getFilterName();
    /**
     * 名称
     */
    String getFilterShowName();
    /**
     * code
     */
    String getFilterCode();
    /**
     * 子集
     */
    List<? extends FilterInfoUiModel> getFilterChild();
}
