package com.ruanyun.australianews.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ruanyun.imagepicker.bean.SelectListImpl;
import com.ruanyun.australianews.R;
import com.ruanyun.australianews.base.BaseActivity;
import com.ruanyun.australianews.widget.TopBar;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表选择activity
 * Created by hdl on 2016/12/20.
 */
public class ListSelectActivity extends BaseActivity {
    public static final int REQUEST_CODE = 1001;
    private TopBar topbar;
    private TextView tvSecondaryTitle;
    private ListView list;
    private SelectAdapter adapter;
    private List<SelectListImpl> itemList;
    private String title;
    private String secondaryTitle;

    public static final String TITLE = "title";
    public static final String SECONDARY_TITLE = "secondary_title";
    /** 列表item drawablesLeftId 不传为-1 不设置 */
    public static final String DRAWABLES_LEFT_ID = "drawables_left_id";
    /** 选中返回的数据 */
    public static final String SELECTED_INFO = "selected_info";
    /** 筛选的集合 */
    public static final String SELECT_LIST = "select_list";
    int drawablesLeftId;

    public static void start(Activity activity, String title, List<? extends SelectListImpl> selectInfo, int drawablesLeftId, int requestCode) {
        Intent starter = new Intent(activity, ListSelectActivity.class);
        starter.putExtra(TITLE, title);
        starter.putParcelableArrayListExtra(SELECT_LIST, (ArrayList<? extends Parcelable>) selectInfo);
        starter.putExtra(DRAWABLES_LEFT_ID, drawablesLeftId);
        activity.startActivityForResult(starter, requestCode);
    }

    public static void start(Activity activity, String title, String secondaryTitle, List<? extends SelectListImpl> selectInfo, int requestCode) {
        Intent starter = new Intent(activity, ListSelectActivity.class);
        starter.putExtra(TITLE, title);
        starter.putExtra(SECONDARY_TITLE, secondaryTitle);
        starter.putParcelableArrayListExtra(SELECT_LIST, (ArrayList<? extends Parcelable>) selectInfo);
        activity.startActivityForResult(starter, requestCode);
    }

    public static void start(Fragment fragment, String title, List<? extends SelectListImpl> selectInfo, int requestCode) {
        Intent starter = new Intent(fragment.getActivity(), ListSelectActivity.class);
        starter.putExtra(TITLE, title);
        starter.putParcelableArrayListExtra(SELECT_LIST, (ArrayList<? extends Parcelable>) selectInfo);
        fragment.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_list_select);
        immerse();
        topbar = findViewById(R.id.topbar);
        tvSecondaryTitle = findViewById(R.id.tv_secondary_title);
        list = findViewById(R.id.lv_select);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode ==REQUEST_CODE) {
            setResult(RESULT_OK,data);
            finish();
        }
    }

    private void initView() {
        title = getIntent().getStringExtra(TITLE);
        itemList = getIntent().getParcelableArrayListExtra(SELECT_LIST);
        drawablesLeftId = getIntent().getIntExtra(DRAWABLES_LEFT_ID,-1);
        secondaryTitle = getIntent().getStringExtra(SECONDARY_TITLE);
        if(!TextUtils.isEmpty(secondaryTitle)){
            tvSecondaryTitle.setText(secondaryTitle);
            tvSecondaryTitle.setVisibility(View.VISIBLE);
        }
        if(itemList == null){
            itemList = new ArrayList<>();
        }
        topbar.setTitleText(title).setTopBarClickListener(this);
        adapter = new SelectAdapter(mContext,R.layout.item_list_select,itemList,drawablesLeftId);
        list.setAdapter(adapter);
        list.setDividerHeight(1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectListImpl currentItem = itemList.get(position);
                List<SelectListImpl> childList = (List<SelectListImpl>) currentItem.getChild();
                if(childList!=null&&childList.size()>0){

//                    Intent intent = new Intent(mContext, ListSelectActivity.class);
//                    intent.putExtra(ListSelectActivity.TITLE,currentItem.getShowName());
//                    intent.putExtra(ListSelectActivity.DRAWABLES_LEFT_ID,drawablesLeftId);
//                    intent.putParcelableArrayListExtra(SELECT_LIST, (ArrayList<? extends Parcelable>) childList);
//                    startActivityForResult(intent, REQUEST_CODE);
                    ListSelectActivity.start(ListSelectActivity.this, title,
                            TextUtils.isEmpty(secondaryTitle)?"":secondaryTitle+" > "+currentItem.getShowName(), childList, REQUEST_CODE);

                }else {
                    Intent intent = getIntent();
                    intent.putExtra(SELECTED_INFO,currentItem);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    /**
     * 列表选择adapter
     * Created by hdl on 2016/12/20.
     */

    public class SelectAdapter extends CommonAdapter<SelectListImpl> {
        int drawableLeftId;
        public SelectAdapter(Context context, int layoutId, List<SelectListImpl> datas, int drawableLeftId) {
            super(context, layoutId, datas);
            this.drawableLeftId = drawableLeftId;
        }
        public void setData(List<SelectListImpl> datas){
            mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(ViewHolder viewHolder, SelectListImpl item, int position) {
            TextView textView = viewHolder.getView(R.id.tv_select);
            textView.setText(item.getShowName());
            textView.setSelected(item.isSelect());
            if(drawableLeftId!=-1){
                Drawable drawableLeft = mContext.getResources().getDrawable(drawableLeftId);
                drawableLeft.setBounds(0,0,drawableLeft.getIntrinsicWidth(),drawableLeft.getIntrinsicHeight());
                textView.setCompoundDrawables(drawableLeft,null,null,null);
            }else {
                Drawable drawableRight = mContext.getResources().getDrawable(R.drawable.icon_arrow_right);
                drawableRight.setBounds(0,0,drawableRight.getIntrinsicWidth(),drawableRight.getIntrinsicHeight());
                textView.setCompoundDrawables(null,null,drawableRight,null);
            }
        }
    }

}
