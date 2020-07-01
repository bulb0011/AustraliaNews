package com.ruanyun.australianews.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ruanyun.australianews.R
import com.ruanyun.australianews.base.BaseActivity
import com.ruanyun.australianews.base.refreshview.impl.RvCommonAdapter
import com.ruanyun.australianews.data.ApiService
import com.ruanyun.australianews.ext.click
import com.ruanyun.australianews.ext.clickWithTrigger
import com.ruanyun.australianews.ext.getStr
import com.ruanyun.australianews.model.Event
import com.ruanyun.australianews.model.ParentCodeInfo
import com.ruanyun.australianews.util.*
import com.ruanyun.australianews.widget.TipDialog
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * @description 搜索界面
 * @author hdl
 * @date 2019/5/8
 */
class SearchActivity : BaseActivity() {
    companion object {
        const val HOME_SEARCH = 1
        const val LIFE_SEARCH = 2
        fun start(context: Context, searchType: Int) {
            val starter = Intent(context, SearchActivity::class.java)
            starter.putExtra(C.IntentKey.SEARCH_TYPE, searchType)
            context.startActivity(starter)
        }
    }

    private var newsFragment: SearchNewsListFragment? = null
    private var lifeFragment: SearchLifeListFragment? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_search)
        initView()
    }

    @Inject
    lateinit var apiService: ApiService
    lateinit var adapter: LifeTypeAdapter
    private val tipDialog by lazy { TipDialog(mContext) }
    var searchType: Int = HOME_SEARCH

    private fun initView() {
        searchType = intent.getIntExtra(C.IntentKey.SEARCH_TYPE, HOME_SEARCH)
        if(searchType == LIFE_SEARCH){
            iv_home_type.visibility = View.GONE
            tv_life_type.visibility = View.VISIBLE
            adapter = LifeTypeAdapter(mContext, R.layout.item_list_search_life_type, getList())
            rv_life_type.adapter = adapter
            rv_life_type.layoutManager = GridLayoutManager(mContext, 3)
            adapter.block = {
                tv_life_type.text = it.name
                tv_life_type.tag = it.code
                requestSearch(et_search.getStr())
            }

            ll_tab.visibility = View.GONE
            setFragment(1)
        }else {
            tv_tab_news.isSelected = true
            setFragment(0)
        }
        tv_tab_news.click {
            if(!tv_tab_news.isSelected) {
                tv_tab_news.isSelected = true
                tv_tab_life.isSelected = false
                setFragment(0)
            }
        }
        tv_tab_life.click {
            if(!tv_tab_life.isSelected) {
                tv_tab_news.isSelected = false
                tv_tab_life.isSelected = true
                setFragment(1)
            }
        }
        iv_back.clickWithTrigger { finish() }
        refreshHistoryData()
        setSearchListener()
    }

    /**
     * 搜索监听及点击事件处理
     */
    private fun setSearchListener() {
        et_search.setOnKeyListener { _, keyCode, event ->
            // 回车监听
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val name = et_search.getStr()
                if (CommonUtil.isNotEmpty(name)) {
                    requestSearch(name)
                }
            }
            false
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                newsFragment?.setTitle(s.toString())
                lifeFragment?.setTitle(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        Handler().postDelayed({
            showSoftInputFromWindow(et_search)
        }, 200)

        tv_search.clickWithTrigger {
            val name = et_search.getStr()
            if (searchType == LIFE_SEARCH || CommonUtil.isNotEmpty(name)) {
                requestSearch(name)
            }
        }
        tv_close_history.clickWithTrigger {
            if(DbHelper.getInstance().searchHistory.size<=0){
                return@clickWithTrigger
            }
//            tipDialog.show("温馨提示", "是否确认清空搜索历史?", "确认")
//            tipDialog.setOkClick {
//                tipDialog.dismiss()
                DbHelper.getInstance().clearSearchHistory()
                flaxbox_history.removeAllViews()
//            }
        }
        tv_life_type.click {
            if(rl_life_type.visibility == View.VISIBLE){
                rl_life_type.visibility = View.GONE
            }else {
                rl_life_type.visibility = View.VISIBLE
            }
        }
        rl_life_type.clickWithTrigger {
            rl_life_type.visibility = View.GONE
        }
    }

    /**
     * 添加搜索记录到数据库
     */
    private fun addSearchHistory(name: String) {
        if (CommonUtil.isNotEmpty(name)) {
            DbHelper.getInstance().insertHistorySearchInfo(name)
            refreshHistoryData()
        }
    }

    /**
     * 刷新历史搜索信息
     */
    private fun refreshHistoryData() {
        flaxbox_history.removeAllViews()
        val historyList = DbHelper.getInstance().searchHistory
        historyList.forEach {item ->
            val view: TextView = LayoutInflater.from(mContext).inflate(R.layout.item_flaxbox_data_hot_history_search, null) as TextView
            view.text = item.keyWord
            view.clickWithTrigger {
                et_search.setText(item.keyWord)
                et_search.setSelection(et_search.length())
                requestSearch(item.keyWord)
            }
            flaxbox_history?.addView(view)
        }
    }

    /**
     * 请求搜索结果
     */
    private fun requestSearch(name: String) {
        addSearchHistory(name)
        if(searchType == LIFE_SEARCH){
            EventBus.getDefault().post(Event(C.EventKey.UPDATE_NEWS_LIFE_SEARCH_LIST, tv_life_type.tag))
        }else {
            EventBus.getDefault().post(Event(C.EventKey.UPDATE_NEWS_LIFE_SEARCH_LIST, ""))
        }
        rl_life_type.visibility = View.GONE
        if(rl_search_list.visibility != View.VISIBLE) {
            rl_search_list.visibility = View.VISIBLE
        }
    }

    /**
     * 获取生活类型数据
     */
    private fun getList():List<ParentCodeInfo>{
        val datas = mutableListOf<ParentCodeInfo>()
        datas.add(ParentCodeInfo("全部类型", ""))
        datas.add(ParentCodeInfo("房屋出租", "2"))
        datas.add(ParentCodeInfo("招聘信息", "3"))
        datas.add(ParentCodeInfo("汽车买卖", "4"))
        datas.add(ParentCodeInfo("宠物交易", "5"))
        datas.add(ParentCodeInfo("交易市场", "6"))
        datas.add(ParentCodeInfo("房屋求租", "7"))
        datas.add(ParentCodeInfo("生意转让", "8"))
        datas.add(ParentCodeInfo("教科书", "9"))
        datas.add(ParentCodeInfo("餐饮美食", "10"))
        datas.add(ParentCodeInfo("黄页", "100"))
        return datas
    }



    /**
     * 显示对应Fragment
     */
    private fun setFragment(index: Int) {
        supportFragmentManager.beginTransaction().apply {
//            if(index==0) {
                newsFragment?: let {
                    SearchNewsListFragment().also {
                        newsFragment = it
                        add(R.id.container, it)
                    }
                }
//            }
//            if(index==1) {
                lifeFragment?: let {
                    SearchLifeListFragment().also {
                        lifeFragment = it
                        add(R.id.container, it)
                    }
                }
//            }
            when (index) {
                0 -> {
                    newsFragment?.let {
                        this.show(it)
                    }
                    lifeFragment?.let {
                        this.hide(it)
                    }
                }
                1 -> {
                    newsFragment?.let {
                        this.hide(it)
                    }
                    lifeFragment?.let {
                        this.show(it)
                    }
                }
            }
        }.commitAllowingStateLoss()
    }


}

class LifeTypeAdapter(context: Context, layoutId: Int, datas: List<ParentCodeInfo>) : RvCommonAdapter<ParentCodeInfo>(context, layoutId, datas) {
    var selectBgColor = ContextCompat.getColor(context, R.color.theme_color)
    var unselectBgColor = ContextCompat.getColor(context, R.color.bg_white)
    var selectTextColor = ContextCompat.getColor(context, R.color.white)
    var unselectTextColor = ContextCompat.getColor(context, R.color.text_black)

    var selectPosition = 0

    override fun convert(holder: ViewHolder?, t: ParentCodeInfo, position: Int) {
        holder?.setText(R.id.tv_name, t.name)
        val tvName = holder?.getView<TextView>(R.id.tv_name)
        tvName?.text = t.name
        tvName?.isSelected = selectPosition == position
        if(tvName?.isSelected==true){
            tvName.setBackgroundColor(selectBgColor)
            tvName.setTextColor(selectTextColor)
        }else {
            tvName?.setBackgroundColor(unselectBgColor)
            tvName?.setTextColor(unselectTextColor)
        }
        holder?.convertView?.clickWithTrigger {
            selectPosition = position
            notifyDataSetChanged()
            block.invoke(t)
        }
    }

    var block = fun(_ : ParentCodeInfo){}

}