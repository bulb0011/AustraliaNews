package com.ruanyun.australianews.ui.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.jpush.im.android.api.JMessageClient
import com.flyco.tablayout.listener.CustomTabEntity
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate
import com.ruanyun.australianews.base.refreshview.data.IDataSource
import com.ruanyun.australianews.base.refreshview.impl.PageDataSource
import com.ruanyun.australianews.base.refreshview.impl.PtrRefreshViewHolder
import com.ruanyun.australianews.base.refreshview.impl.RvHeaderFootViewAdapter
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.NotificationInfo
import com.ruanyun.australianews.model.TabEntity
import com.ruanyun.australianews.model.params.NotificationListParams
import com.ruanyun.australianews.ui.adapter.NotificationListAdapter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.OnTabDebounceSelectListener
import com.ruanyun.australianews.util.RxUtil
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import jiguang.chat.activity.fragment.ConversationListFragment
import kotlinx.android.synthetic.main.activity_my_message_list.*
import kotlinx.android.synthetic.main.layout_refresh_rv_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import javax.inject.Inject

/**
 * @description 消息通知
 * @author hdl
 * @date 2018/11/27
 */
class MyMessageListActivity : BaseActivity() {
    class NotificationPageDataSource @Inject constructor(apiService: ApiService) : PageDataSource<ResultBase<*>, NotificationListParams>(apiService) {
        override fun fetchData(loadType: Int, handler: IDataSource.IDataSourceResultHandler<ResultBase<*>>?): Observable<ResultBase<*>> {
            return apiService.getNotificationList(params).compose(RxUtil.normalSchedulers())
        }
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MyMessageListActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var dataSource: NotificationPageDataSource
    @Inject
    lateinit var iRefreshViewHolder: PtrRefreshViewHolder
    private lateinit var delegate: IDataDelegate
    private val params = NotificationListParams()
    lateinit var adapter: NotificationListAdapter
    lateinit var headerAdapter: RvHeaderFootViewAdapter<NotificationInfo>

    private val onTabSelectListener = object : OnTabDebounceSelectListener() {
        override fun onTabDebounceSelect(position: Int) {
            when (position) {
                0 -> {
                    container.visibility = View.GONE
                    fr_root.visibility = View.VISIBLE
                }
                1 -> {
                    clickMessageUpdate()
                }
            }
        }
    }

    private fun clickMessageUpdate() {
        if (isLoginToActivity) {
            container.visibility = View.VISIBLE
            fr_root.visibility = View.GONE
            if (supportFragmentManager.fragments.size == 0) {
                supportFragmentManager.beginTransaction().add(R.id.container, ConversationListFragment()).commit()
            }
        } else {
            Handler().postDelayed({
                tabLayout.currentTab = 0
            }, 300)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_my_message_list)
        iRefreshViewHolder.init(this)
        registerBus()
        initView()
        initDataSource()
        updateMsgCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterBus()
    }

    /**
     * 有新消息，刷新
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUnreadCount(event: Event<String>) {
        if (C.EventKey.UPDATE_UNREAD_COUNT == event.key) {
            updateMsgCount()
        }
    }

    private fun updateMsgCount() {
        val unreadMsgCount = JMessageClient.getAllUnReadMsgCount()
        tv_unread_count.text = String.format("%s", unreadMsgCount)
        tv_unread_count.visibility = if (unreadMsgCount > 0) View.VISIBLE else View.INVISIBLE
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)

        val arrayList = ArrayList<CustomTabEntity>()
        arrayList.apply {
            add(TabEntity(resources.getString(R.string.gonggao)))
            add(TabEntity(resources.getString(R.string.sixin)))
        }
        tabLayout.setTabData(arrayList)
        tabLayout.setOnTabSelectListener(onTabSelectListener)

    }

    private fun initDataSource() {
        adapter = NotificationListAdapter(mContext, R.layout.item_list_notification, arrayListOf())
        headerAdapter = RvHeaderFootViewAdapter(adapter, mContext)
        list.adapter = headerAdapter
        list.layoutManager = LinearLayoutManager(mContext)
        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                MyMessageNotificationDetailsActivity.start(mContext, adapter.datas[position].content)
//                if (adapter.datas[position].status == 0) {
//                    adapter.datas[position].status = 1
//                    adapter.notifyItemChanged(position)
//                    headerAdapter.notifyItemChanged(position)
//                    msgRead(adapter.datas[position].oid)
//                }
            }

            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }
        })
        dataSource.setParams(params)
        delegate = iRefreshViewHolder
                .setLoadMoreEnable(true)
                .setRefreshViewEnable(true)
                .setDataAdapter(headerAdapter)
                .setEmptyView(emptyview)
                .createDataDelegate()
        delegate.setDataSource(dataSource).refreshWithLoading()
    }

    /**
     * 修改消息未读状态
     */
    private fun msgRead(smsOid: String?) {
//        apiService.updateMsgReadStatus(smsOid)
//                .compose(RxUtil.normalSchedulers())
//                .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
//                    override fun onSuccess(datas: ResultBase<*>) {
//                    }
//
//                    override fun onError(erroCode: Int, erroMsg: String?) {
//                    }
//                }, object : ApiFailAction() {
//                    override fun onFail(msg: String) {
//                    }
//                })
    }


}