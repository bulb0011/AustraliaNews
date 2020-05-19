package com.ruanyun.australianews.widget.filterpopwindow;


/**
 * @author Daniel
 * @ClassName: ${file_name}
 * @Description:
 * @date ${date}${commonTime}
 */
public interface OnFilterClickListener {

    /**
     * @description: 选中具体筛选条件回调，position为点开popWindow的位置
     * @author: Daniel
     */
    void onPopItemSelected(FilterInfoUiModel filterInfo, int requestCode);

    /**
     * @description: 关闭popWindow回调，position为点开popWindow的位置
     * @author: Daniel
     */
    void onPopWindowDismissed(int requestCode);
}
