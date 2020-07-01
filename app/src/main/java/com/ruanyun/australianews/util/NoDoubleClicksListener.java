package com.ruanyun.australianews.util;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;

public abstract class NoDoubleClicksListener implements OnClickListener {

	public static final int MIN_CLICK_DELAY_TIME = 1000;
	private long lastClickTime = 0;

	@Override
	public void onClick(View v) {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
			lastClickTime = currentTime;
			noDoubleClick(v);
		}
	}

	public abstract void noDoubleClick(View v);
}
