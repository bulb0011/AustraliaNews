package com.ruanyun.australianews.widget.filterpopwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.ruanyun.australianews.R;
import com.ruanyun.australianews.model.Event;
import com.ruanyun.australianews.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.ruanyun.australianews.util.CommonUtil.dp2px;


/**
 * 支持多级筛选
 * Created by hdl on 2017/5/13.
 */
public class FilterListPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private Context mContext;
    /** 数据列表*/
    private List<? extends FilterInfoUiModel> filterInfoUiModels;
    /** 最大值*/
    private int maxLevel;
    /** listview 的集合*/
    private List<ListView> mListViews = new ArrayList<>();
    /** listview 左边的线*/
    private List<View> mViewLines = new ArrayList<>();
    /** adapter 的集合*/
    private List<FilterSelectAdapter> mFilterSelectAdapters = new ArrayList<>();
    /** 点击回调监听 */
    private OnFilterClickListener mOnFilterListener;
    /** 用于区分一个界面使用多个PopWindow时的OnFilterClickListener监听 */
    private int requestCode;

    /**
     * 设置返回数据的接口
     * @param onFilterListener 当前选中数据
     * @param requestCode   区分OnFilterClickListener监听
     */
    public void setOnFilterListener(OnFilterClickListener onFilterListener, int requestCode) {
        mOnFilterListener = onFilterListener;
        this.requestCode = requestCode;
    }

    /**
     *
     * @param context
     * @param maxLevel 可选多少级
     * @param itemHeightCount pop的高度显示几个item的高度 3
     */
    public FilterListPopupWindow(Context context, int maxLevel, float itemHeightCount) {
        this.mContext = context;
        this.maxLevel = maxLevel;
        initView(dp2px((36 * itemHeightCount)), 0, 0);
    }

    /**
     *
     * @param context
     * @param maxLevel 可选多少级
     * @param popHeight pop的高度 dp
     */
    public FilterListPopupWindow(Context context, int maxLevel, float popHeight, int firstItemWidth, int firstLevelItemLayoutId) {
        this.mContext = context;
        this.maxLevel = maxLevel;
        initView(dp2px(popHeight), dp2px(firstItemWidth),firstLevelItemLayoutId);
    }

    private void initView(int popHeight, int firstItemWidth, int firstLevelItemLayoutId) {
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(this);

        //popu的背景
        LinearLayout popLayout = new LinearLayout(mContext);
        popLayout.setOrientation(LinearLayout.VERTICAL);
        popLayout.setBackgroundColor(Color.parseColor("#33000000"));
        popLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        popLayout.setFocusable(true);
//        popLayout.setFocusableInTouchMode(true);
//        popLayout.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });

        //存放ListView的固定高度的 Layout
        LinearLayout listLayout = new LinearLayout(mContext);
        listLayout.setOrientation(LinearLayout.HORIZONTAL);
        listLayout.setBackgroundColor(mContext.getResources().getColor(R.color.bg_white));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, popHeight);

        //listview的参数
        LinearLayout.LayoutParams params;
        // divider参数
        LinearLayout.LayoutParams lpDivider = new LinearLayout.LayoutParams(1, ViewGroup
                .LayoutParams.MATCH_PARENT);

        for (int i = 1; i <= maxLevel; i++) {
            ListView listView = new ListView(mContext);
//            listView.setVerticalScrollBarEnabled(false);
            listView.setVisibility(View.GONE);
            listView.setDivider(null);
            FilterSelectAdapter adapter;
            if(i == 1 && firstLevelItemLayoutId!=0) {
                adapter = new FilterSelectAdapter(mContext, new ArrayList<FilterInfoUiModel>(), firstLevelItemLayoutId, i, maxLevel);
                params = new LinearLayout.LayoutParams(firstItemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            }else {
                adapter = new FilterSelectAdapter(mContext, new ArrayList<FilterInfoUiModel>(), R.layout.list_item_filter_item, i, maxLevel);
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            }
            listView.setAdapter(adapter);
            mListViews.add(listView);
            mFilterSelectAdapters.add(adapter);
            listLayout.addView(listView, params);

            // 不是最后一个的时候添加分割线
            if (i < maxLevel) {
                View divider = new View(mContext);
                divider.setLayoutParams(lpDivider);
                divider.setBackgroundColor(mContext.getResources().getColor(R.color.color_gray_line));
                listLayout.addView(divider);
                mViewLines.add(divider);
            }
        }
        listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        popLayout.addView(listLayout, layoutParams);
        setContentView(popLayout);
    }

    /**
     * popuwindow
     *
     * @param filterInfoUiModels 需要显示的数据源
     */
    public void setData(List<? extends FilterInfoUiModel> filterInfoUiModels) {
        if (filterInfoUiModels == null || filterInfoUiModels.size() == 0) {
            if (maxLevel > 0) {//加载1级数据
                mListViews.get(0).setVisibility(View.INVISIBLE);
            }
            return;
        }
        this.filterInfoUiModels = filterInfoUiModels;
        if (maxLevel > 0) {//加载1级数据
            for (int i = 0; i < maxLevel; i++) {
                if (i == 0) {
                    mListViews.get(i).setVisibility(View.VISIBLE);
                } else {
                    mListViews.get(i).setVisibility(View.INVISIBLE);
                    mViewLines.get(i-1).setVisibility(View.INVISIBLE);
                }
            }
            mFilterSelectAdapters.get(0).setData(filterInfoUiModels);
            mListViews.get(0).setVisibility(View.VISIBLE);
            mFilterSelectAdapters.get(0).notifyDataSetChanged();
        }
    }

    /**
     * popuwindow
     *
     * @param filterInfoUiModels 需要显示的数据源
     * @param selectPosition 默认一级列表初始化时选中的item
     */
    public void setData(List<? extends FilterInfoUiModel> filterInfoUiModels, int selectPosition) {
        setData(filterInfoUiModels);
        if (maxLevel > 0) {
            mFilterSelectAdapters.get(0).setSelectPosition(selectPosition);
            mFilterSelectAdapters.get(0).notifyDataSetChanged();
            if(maxLevel>1 && selectPosition>=0 && selectPosition<filterInfoUiModels.size()){
                if(filterInfoUiModels.get(selectPosition).getFilterChild()!=null) {
                    mFilterSelectAdapters.get(1).setData(filterInfoUiModels.get(selectPosition).getFilterChild(), -1);
                    mListViews.get(1).setVisibility(View.VISIBLE);
                    mViewLines.get(0).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public List<? extends FilterInfoUiModel> getData() {
        return filterInfoUiModels;
    }

    /**
     * 列表点击处理 如果有子级，则显示数据，没有则返回
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleClickEvent(Event<FilterInfoUiModel> event) {
        if (event.key.equals(FilterSelectAdapter.FILTER_LIST_CLICK)) {
            FilterInfoUiModel filterInfo = event.value;
            //  是否拥有下一级 listView ,下一级数据是否为空
            if (event.keyInt < maxLevel && filterInfo.getFilterChild() != null && filterInfo.getFilterChild().size() > 0) {
                mListViews.get(event.keyInt).setVisibility(View.VISIBLE);
                mViewLines.get(event.keyInt-1).setVisibility(View.VISIBLE);
                mFilterSelectAdapters.get(event.keyInt).setData(filterInfo.getFilterChild(), -1);
                hideChildListView(event.keyInt);
            } else {
                if (mOnFilterListener != null) {
                    mOnFilterListener.onPopItemSelected(filterInfo, requestCode);
                }
                hideChildListView(event.keyInt-1);
                dismiss();
            }
        }
    }

    /**
     * 隐藏当前级别下的所有子级
     * 比如：点击1级列表 改变2级列表数据 隐藏2级以下的所有列表
     *
     * @param level
     */
    private void hideChildListView(int level) {
        for (int i = level + 1; i < maxLevel; i++) {
            mListViews.get(i).setVisibility(View.INVISIBLE);
            mViewLines.get(i-1).setVisibility(View.INVISIBLE);
        }
    }

    public void unRegisterBus() {
        EventBus.getDefault().unregister(this);
    }

    public void registerBus() {
        EventBus.getDefault().register(this);
    }


    /**
     * 显示pop 窗口
     *
     * @param view
     */
    public void show(View view) {
        registerBus();
        showAsDropDown(view);
        if(mListViews.size()>0 && mFilterSelectAdapters.size()>0 && mFilterSelectAdapters.get(0).getSelectPosition()>3){
            mListViews.get(0).setSelection(mFilterSelectAdapters.get(0).getSelectPosition()-1);
        }
        //   5.1  有效
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);

            DisplayMetrics dm = new DisplayMetrics();
            ((Activity)anchor.getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            int height = dm.heightPixels - rect.bottom;

            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);

            DisplayMetrics dm = new DisplayMetrics();
            ((Activity)anchor.getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            int height = dm.heightPixels - rect.bottom;

            setHeight(height);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void onDismiss() {
        if (mOnFilterListener != null) {
            mOnFilterListener.onPopWindowDismissed(requestCode);
        }
        unRegisterBus();
    }

}
