package com.ruanyun.australianews.ui.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.ResultBase
import com.ruanyun.australianews.data.ApiFailAction
import com.ruanyun.australianews.data.ApiManger
import com.ruanyun.australianews.data.ApiSuccessAction
import com.ruanyun.australianews.model.ChannelInfo
import com.ruanyun.australianews.model.params.UpdateChannelParams
import com.ruanyun.australianews.ui.channel.ChannelAdapter
import com.ruanyun.australianews.util.C
import com.ruanyun.australianews.util.DbHelper
import com.ruanyun.australianews.util.GsonUtil
import com.ruanyun.australianews.util.RxUtil
import kotlinx.android.synthetic.main.activity_channel_manage.*

/**
 * @description 频道管理
 * @author hdl
 * @date 2019/5/6
 */
class ChannelManagerActivity : BaseActivity(), ChannelAdapter.DataChangeListener {
    companion object {
        fun start(fragment: Fragment, requestCode: Int) {
            val starter = Intent(fragment.context, ChannelManagerActivity::class.java)
            fragment.startActivityForResult(starter, requestCode)
        }
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_channel_manage)
        initView()
    }

    private fun initView() {
        topbar.setTopBarClickListener(this)
        cmv.setDataChangeListenter(this)
    }


    override fun onTopBarLeftImgClick() {
        onBackPressed()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun saveData(myChannels: List<ChannelInfo>, otherChannels: List<ChannelInfo>, customizeChannels: List<ChannelInfo>) {
        val channels = ArrayList<ChannelInfo>()
        var i = 1
        for (entity in myChannels) {
            entity.sort = i++
            entity.isSubscribe = true
            channels.add(entity)
        }
        for (entity in otherChannels) {
            entity.sort = i++
            entity.isSubscribe = false
            channels.add(entity)
        }
        for (entity in customizeChannels) {
            entity.sort = i++
            entity.isSubscribe = false
            channels.add(entity)
        }
        if (app.isLogin){
            updateChannelList(channels)
        }else {
            DbHelper.getInstance().insertChannelList(channels)
        }
    }



    override fun getMyChannel(): List<ChannelInfo> {
        return getChannels(1)
    }

    override fun getOtherChannel(): List<ChannelInfo> {
        return getChannels(2)
    }

    override fun getCustomizeChannel(): List<ChannelInfo> {
        return getChannels(3)
    }

    private fun getChannels(type: Int): List<ChannelInfo> {
        return when(type) {
            1 -> DbHelper.getInstance().getSubscribedList(app.isLogin)
            2 -> DbHelper.getInstance().getUnSubscribedList(app.isLogin)
            3 -> DbHelper.getInstance().getUnSubscribedCustomizeList(app.isLogin)
            else -> arrayListOf()
        }
    }

    override fun OnItemClick(v: View, position: Int) {
        if (position != -1) {
            setResult(Activity.RESULT_OK, Intent().putExtra(C.IntentKey.CLICK_POSITION, position))
            finish()
        }
    }



    /**
     * 修改我的频道信息
     */
    private fun updateChannelList(channels : List<ChannelInfo>) {
        val paramsList = mutableListOf<UpdateChannelParams>()
        channels.forEach {
            paramsList.add(UpdateChannelParams(it.sort, if(it.isSubscribe) 1 else 2, it.oid, it.type))
        }

        showLoadingView(R.string.in_submit)
        val subscription = ApiManger.getApiService().updateChannelList(app.userOid, GsonUtil.toJson(paramsList))
            .compose(RxUtil.normalSchedulers<ResultBase<*>>())
            .subscribe(object : ApiSuccessAction<ResultBase<*>>() {
                override fun onSuccess(result: ResultBase<*>) {
                    disMissLoadingView()
                    showToast(result.msg)
                    DbHelper.getInstance().insertChannelList(channels)
                }

                override fun onError(erroCode: Int, erroMsg: String) {
                    disMissLoadingView()
                    showToast(erroMsg)
                }
            }, object : ApiFailAction() {
                override fun onFail(msg: String) {
                    disMissLoadingView()
                    showToast(msg)
                }
            })
        addSubscrebe(subscription)
    }

}