
package com.ruanyun.australianews.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ruanyun.imagepicker.Util;
import com.ruanyun.australianews.R;


public class EaseSidebar extends View{
	public Paint paint;
	private TextView header;
	private float height;
	private ListView mListView;
	private Context context;

	private int default_color = Color.parseColor("#4071ff");
	private SectionIndexer sectionIndexter = null;
	private boolean isHot = false;

	public void setListView(ListView listView){
		mListView = listView;
	}

	public boolean isHot() {
		return isHot;
	}

	public void setHot(boolean hot) {
		isHot = hot;
		init();
	}

	public EaseSidebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseSidebar , 0, 0);
		if (ta != null) {
			paint.setColor(ta.getColor(R.styleable.EaseSidebar_ease_sidebar_color, default_color));
		}
		ta.recycle();
		init();
	}

	private String[] sections; 

	public void init(){
		if(isHot) {
			sections = new String[]{"历史", "热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		}else {
			sections = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		}
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(Util.sp2px(context, 10));
		paint.setFakeBoldText(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float center = getWidth() / 2;
		height = getHeight() / sections.length;
		for (int i = sections.length - 1; i > -1; i--) {
			canvas.drawText(sections[i], center, height * (i+0.8f), paint);
		}
	}
	
	private int sectionForPoint(float y) {
		int index = (int) (y / height);
		if(index < 0) {
			index = 0;
		}
		if(index > sections.length - 1){
			index = sections.length - 1;
		}
		return index;
	}
	
	private void setHeaderTextAndscroll(MotionEvent event){
		 if (mListView == null) {
		        //check the mListView to avoid NPE. but the mListView shouldn't be null
		        //need to check the call stack later
		        return;
		    }
		String headerString = sections[sectionForPoint(event.getY())];
		header.setText(headerString);
		ListAdapter adapter = mListView.getAdapter();
		if(sectionIndexter == null){
    		if(adapter instanceof HeaderViewListAdapter){
    		    sectionIndexter = (SectionIndexer) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
    		}else if(adapter instanceof SectionIndexer){
    		    sectionIndexter = (SectionIndexer)adapter;
    		}else{
    		    throw new RuntimeException("listview sets adpater does not implement SectionIndexer interface");
    		}
		}
		String[] adapterSections = (String[]) sectionIndexter.getSections();
		try {
			if("#".equals(headerString) || "历史".equals(headerString) || "热门".equals(headerString)){
				mListView.setSelection(0);
			}else {
				for (int i = 0; i < adapterSections.length; i++) {
					if (adapterSections[i].equals(headerString)) {
						mListView.setSelection(sectionIndexter.getPositionForSection(i));
						break;
					}
				}
			}
		} catch (Exception e) {
			Log.e("setHeaderTextAndscroll", e.getMessage());
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			if(header == null){
				header = (TextView) ((View)getParent()).findViewById(R.id.floating_header);
			}
			setHeaderTextAndscroll(event);
			header.setVisibility(View.VISIBLE);
			setBackgroundResource(R.drawable.ease_sidebar_background_pressed);
			return true;
		}
		case MotionEvent.ACTION_MOVE:{
			setHeaderTextAndscroll(event);
			return true;
		}
		case MotionEvent.ACTION_UP:
			header.setVisibility(View.INVISIBLE);
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		case MotionEvent.ACTION_CANCEL:
			header.setVisibility(View.INVISIBLE);
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		}
		return super.onTouchEvent(event);
	}

}
