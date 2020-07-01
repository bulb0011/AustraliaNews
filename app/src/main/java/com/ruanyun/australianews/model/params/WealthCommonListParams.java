package com.ruanyun.australianews.model.params;

import com.ruanyun.australianews.base.PageParamsBase;

/**
 * @author hdl
 * @description
 * @date 2020/3/26
 */
public class WealthCommonListParams extends PageParamsBase {
    private String type;//地产类型(ESTATEINFO_TYPE1 民用 ESTATEINFO_TYPE2 商业 ESTATEINFO_TYPE3 农场)

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
