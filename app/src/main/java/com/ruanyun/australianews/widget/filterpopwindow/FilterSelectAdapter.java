package com.ruanyun.australianews.widget.filterpopwindow;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ruanyun.imagepicker.base.NoDoubleClicksListener;
import com.ruanyun.australianews.R;
import com.ruanyun.australianews.model.Event;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 筛选选中的  adapter
 * date 2016/5/25
 */
public class FilterSelectAdapter extends CommonAdapter<FilterInfoUiModel> {
    public static final String FILTER_LIST_CLICK = "filter_list_click";
    private int maxLevel;
    private int level;
    private int selectPosition = -1 ;
    public FilterSelectAdapter(Context context, List<FilterInfoUiModel> datas, int layoutId, int level, int maxLevel) {
        super(context, layoutId, datas);
        this.level = level;
        this.maxLevel = maxLevel;
    }

    @Override
    public void onViewHolderCreated(ViewHolder holder, View itemView) {
        super.onViewHolderCreated(holder, itemView);
    }

    @Override
    public void convert(ViewHolder viewHolder, final FilterInfoUiModel item, final int position) {
        final TextView textView = viewHolder.getView(R.id.tv_select);
        textView.setText(item.getFilterShowName());
        textView.setOnClickListener(new NoDoubleClicksListener() {
            @Override
            public void noDoubleClick(View v) {
                if(textView.isSelected()&&item.getFilterChild()!=null&&item.getFilterChild().size()>0&&level < maxLevel) {
                    //已经选中的项，如果有子级并且小于最大等级就不做操作
                    return;
                }
                EventBus.getDefault().post(new Event<FilterInfoUiModel>(FILTER_LIST_CLICK, level, item));
                selectPosition = position;
                notifyDataSetChanged();

            }
        });

        textView.setSelected(selectPosition == position);

    }

    public void setData(List<? extends FilterInfoUiModel> filterInfoUiModels) {
        mDatas.clear();
        mDatas.addAll(filterInfoUiModels);
        notifyDataSetChanged();
    }
    public void setData(List<? extends FilterInfoUiModel> filterInfoUiModels, int selectPosition) {
        this.selectPosition = selectPosition;
        setData(filterInfoUiModels);
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return selectPosition;
    }
}
