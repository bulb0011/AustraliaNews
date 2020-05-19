package com.ruanyun.australianews.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanyun.australianews.App
import com.ruanyun.australianews.R
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.util.C
import jiguang.chat.activity.fragment.ConversationListFragment
import kotlinx.android.synthetic.main.fragment_message_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @description 消息
 * @author hdl
 * @date 2019/3/26
 */
class MessageFragment : ConversationListFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        EventBus.getDefault().register(this)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_message_list
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        val isShowBack = arguments?.getBoolean("isShowBack", false)?:false
        topbar.setLeftImgEnable(isShowBack).setTopBarClickListener{
            if(it.id == R.id.topbar_left_img){
                activity?.finish()
            }
        }
    }

    /**
     * 用户信息更新、退出登录
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateUserInfo(event: Event<String>) {
        if (C.EventKey.UPDATE_USER_INFO == event.key) {
            userLoginChange()
        }
    }
}