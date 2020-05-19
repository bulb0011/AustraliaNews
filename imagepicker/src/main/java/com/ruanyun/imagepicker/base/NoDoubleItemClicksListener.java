package com.ruanyun.imagepicker.base;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.Calendar;

public abstract class NoDoubleItemClicksListener implements OnItemClickListener {

	public static final int MIN_CLICK_DELAY_TIME = 1000;
	private long lastClickTime = 0;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
			lastClickTime = currentTime;
			noDoubleClick(parent, view, position, id);
		}
	}

	public abstract void noDoubleClick(AdapterView<?> parent, View view, int position, long id);
}
