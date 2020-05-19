package com.ruanyun.australianews.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ruanyun.australianews.model.ChannelInfo;
import com.ruanyun.australianews.util.C;
import com.ruanyun.australianews.model.Event;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hdl
 * @description
 * @date 2019/5/7
 */
public class TitleFragmentAdapter extends FragmentPagerAdapter {
    public static final String REFRESH_VIEW_ENABLE = "refresh_view_enable";
    List<ChannelInfo> channels;
    int id = 1;
    Map<String, Integer> IdsMap = new HashMap<>();
    List<String> preIds = new ArrayList<>();
    boolean isRefreshViewEnable = false;

    public TitleFragmentAdapter(FragmentManager fm, @NonNull List<ChannelInfo> channels) {
        super(fm);
        this.channels = channels;
    }

    public void setRefreshViewEnable(Boolean isRefreshViewEnable){
        this.isRefreshViewEnable = isRefreshViewEnable;
        EventBus.getDefault().post(new Event<Boolean>(REFRESH_VIEW_ENABLE, isRefreshViewEnable));
    }

    @Override
    public Fragment getItem(int position) {
        NewsListChildFragment fragment = new NewsListChildFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(C.IntentKey.CHANNEL_INFO, channels.get(position));
        bundle.putBoolean(C.IntentKey.IS_REFRESH_VIEW_ENABLE, isRefreshViewEnable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return channels.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return IdsMap.get(getPageTitle(position));
    }

    @Override
    public int getItemPosition(Object object) {
        NewsListChildFragment fragment = (NewsListChildFragment) object;
        String title = fragment.getTitle();
        int preId = preIds.indexOf(title);
        int newId = -1;
        int i = 0;
        int size = getCount();
        for (; i < size; i++) {
            if (getPageTitle(i).equals(title)) {
                newId = i;
                break;
            }
        }
        if (newId != -1 && newId == preId) {
            return POSITION_UNCHANGED;
        }
        if (newId != -1) {
            return newId;
        }
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        for (ChannelInfo info : channels) {
            if (!IdsMap.containsKey(info.getTitle())) {
                IdsMap.put(info.getTitle(), id++);
            }
        }
        super.notifyDataSetChanged();
        preIds.clear();
        int size = getCount();
        for (int i = 0; i < size; i++) {
            preIds.add((String) getPageTitle(i));
        }
    }
}
