package com.ruanyun.australianews.util;


import com.ruanyun.australianews.model.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * @author hdl
 * @description 发送EventBus统一管理
 * @date 2018/10/29
 */
public class EventNotifier {


    private static class InstanceHolder {
        private static EventNotifier instance = new EventNotifier();
    }

    public static EventNotifier getInstance() {
        return InstanceHolder.instance;
    }


    /**
     * 清除图片缓存成功
     **/
    public void clearImageCache() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.CLEAR_IMAGE_CACHE, ""));
    }

    /**
     * 切换城市成功
     **/
    public void switchCitySuccess() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.SWITCH_CITY_SUCCESS, ""));
    }

    /**
     * 刷新用户信息，登录和退出都会调用，所以接收需要处理用户为null的情况
     **/
    public void updateUserInfo() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_USER_INFO, ""));
    }

    /**
     * 改变语言
     **/
    public void changeAppLanguage() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.CHANGE_APP_LANGUAGE, ""));
    }

    /**
     * 刷新购物车列表
     **/
    public void updateShopCartList() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_SHOP_CART_LIST, ""));
    }

    /**
     * 刷新新闻布列表
     **/
    public void updateNewsList(String title) {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_NEWS_LIST, title));
    }

    /**
     * 刷新未读消息数
     **/
    public void updateUnreadCount() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_UNREAD_COUNT, ""));
    }

    /**
     * 刷新频道列表
     **/
    public void updateChannelList() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_CHANNEL_LIST, ""));
    }

    /**
     * 刷新生活服务列表
     **/
    public void updateLifeReleaseList() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_LIFE_RELEASE_LIST, ""));
    }

    /**
     * 刷新黄页列表
     **/
    public void updateYellowPageList() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_YELLOW_PAGE_LIST, ""));
    }

    /**
     * 刷新商品列表页面
     **/
    public void updateShopGoodsPage() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.UPDATE_SHOP_GOODS_PAGE, ""));
    }

    /**
     * 刷新webview页面
     **/
    public void addSuccessSuccess() {
        EventBus.getDefault().post(new Event<String>(C.EventKey.ADD_COMMENT_SUCCESS, ""));
    }

}
