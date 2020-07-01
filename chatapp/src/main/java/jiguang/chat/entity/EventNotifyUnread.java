package jiguang.chat.entity;

/**
 * @author hdl
 * @description 未读消息刷新
 * @date 2019/3/29
 */
public class EventNotifyUnread {

    public static void updateUnreadCount(){
        cn.jpush.im.android.eventbus.EventBus.getDefault().post(new EventNotifyUnread());
    }
}
