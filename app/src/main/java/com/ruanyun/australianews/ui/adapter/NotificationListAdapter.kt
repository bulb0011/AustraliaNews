package com.ruanyun.australianews.ui.adapter

import android.content.Context
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.refreshview.impl.RvCommonAdapter
import com.ruanyun.australianews.model.NotificationInfo
import com.ruanyun.australianews.util.StringUtil
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @description 消息通知列表
 * @author hdl
 * @date 2018/11/27
 */
class NotificationListAdapter(context: Context?, layoutId: Int, datas: MutableList<NotificationInfo>?) : RvCommonAdapter<NotificationInfo>(context, layoutId, datas) {

    override fun convert(holder: ViewHolder?, item: NotificationInfo, position: Int) {
        holder?.setText(R.id.tv_time, StringUtil.getTimeStrFromFormatStr("yyyy-MM-dd HH:mm", item.createTime))
        holder?.setText(R.id.tv_title, item.title)
        val unread = holder?.getView<View>(R.id.unread)
        unread?.visibility = /*if (item.status == 0) View.VISIBLE else */View.GONE
    }

}
