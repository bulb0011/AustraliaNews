package jiguang.chat.entity;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import cn.jiguang.api.JCoreInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageBaseEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import jiguang.chat.R;
import jiguang.chat.activity.ChatActivity;
import jiguang.chat.application.JGApplication;
import jiguang.chat.pickerimage.utils.StringUtil;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationClickEventReceiver {
    private Context mContext;

    public NotificationClickEventReceiver(Context context) {
        mContext = context;
        //注册接收消息事件
        JMessageClient.registerEventReceiver(this);
    }

    /**
     * 收到消息处理
     * @param notificationClickEvent 通知点击事件
     */
    public void onEvent(NotificationClickEvent notificationClickEvent) {
        if (null == notificationClickEvent) {
            return;
        }
        Message msg = notificationClickEvent.getMessage();
        if (msg != null) {
            String targetId = msg.getTargetID();
            String appKey = msg.getFromAppKey();
            ConversationType type = msg.getTargetType();
            Conversation conv;
            Intent notificationIntent = new Intent(mContext, ChatActivity.class);
            if (type == ConversationType.single) {
                conv = JMessageClient.getSingleConversation(targetId, appKey);
                notificationIntent.putExtra(JGApplication.TARGET_ID, targetId);
                notificationIntent.putExtra(JGApplication.TARGET_APP_KEY, appKey);
            } else {
                conv = JMessageClient.getGroupConversation(Long.parseLong(targetId));
                notificationIntent.putExtra(JGApplication.GROUP_ID, Long.parseLong(targetId));
            }
            notificationIntent.putExtra(JGApplication.CONV_TITLE, conv.getTitle());
            conv.resetUnreadCount();
            notificationIntent.putExtra("fromGroup", false);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(notificationIntent);
        }
    }

    /**
     * 收到消息处理
     * @param messageEvent
     */
    public void onEvent(MessageEvent messageEvent) {
        Message message =  messageEvent.getMessage();
        if(message.getContentType() == ContentType.custom){
            String type = ((CustomContent)message.getContent()).getStringValue(StringUtil.TYPE);
            switch (type){
                case StringUtil.TYPE_CURRICULUM_VITAE:
                    showInspectorRecordNotification(message);
                    break;
                case StringUtil.TYPE_POSITION:
                    showInspectorRecordNotification(message);
                    break;
            }
        }
        EventBus.getDefault().post("is_new_msg");//C.EventKey.IS_NEW_MSG
    }

    private void showInspectorRecordNotification(Message message) {
        //创建通知管理类
        NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        //创建通知建设类
        Notification.Builder builder = new Notification.Builder(mContext);
        //设置跳转的页面
        Intent starter = new Intent(mContext, ChatActivity.class);
        starter.putExtra(JGApplication.TARGET_ID, message.getTargetID());
        starter.putExtra(JGApplication.TARGET_APP_KEY, JCoreInterface.getAppKey());
        starter.putExtra(JGApplication.CONV_TITLE, message.getFromName());
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, starter, PendingIntent.FLAG_CANCEL_CURRENT);
        //设置通知栏标题
        builder.setContentTitle(message.getFromName());
        //设置通知栏内容
        String type = ((CustomContent)message.getContent()).getStringValue(StringUtil.TYPE);
        String content = "";
        switch (type){
            case StringUtil.TYPE_CURRICULUM_VITAE:
                content = ":收到一份简历";
                break;
            case StringUtil.TYPE_POSITION:
                content = ":收到一份职位邀约";
                break;
        }
        builder.setContentText(message.getFromName()+content);
        //设置跳转
        builder.setContentIntent(pendingIntent);
        //设置图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(bmp);
        //设置
        builder.setDefaults(Notification.DEFAULT_ALL);
        //创建通知类
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //显示在通知栏
        manager.notify(0, notification);
        Conversation conv = JMessageClient.getSingleConversation(message.getTargetID(), message.getFromAppKey());
        conv.setUnReadMessageCnt(conv.getUnReadMsgCnt()+1);
    }


    /**
     类似MessageEvent事件的接收，上层在需要的地方增加OfflineMessageEvent事件的接收
     即可实现离线消息的接收。
     **/
    public void onEvent(OfflineMessageEvent event) {
        Log.e("retrofit", "onEvent() called with: OfflineMessageEvent = [" + event + "]");
        EventBus.getDefault().post("is_new_msg");//C.EventKey.IS_NEW_MSG
    }

}
