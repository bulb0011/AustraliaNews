package com.ruanyun.australianews.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.ruanyun.australianews.App;

/**
 * @author hdl
 * @description
 * @date 2018/12/27
 */
public class NotificationManagerUtil {
    private static NotificationManager notificationManager;
    public static String MESSAGE_NOTIFICATION = "消息通知";

    public static NotificationManager getNotificationManager() {
        if(notificationManager==null){
            notificationManager = (NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(MESSAGE_NOTIFICATION, MESSAGE_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(true); //设置开启指示灯，如果设备有的话
                channel.setLightColor(Color.WHITE); //设置指示灯颜色
                channel.setShowBadge(true); //设置是否显示角标
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);//设置是否应在锁定屏幕上显示此频道的通知
                channel.setVibrationPattern(new long[]{300, 400, 300, 400});//设置震动频率
                channel.setBypassDnd(true);//设置是否绕过免打扰模式
                notificationManager.createNotificationChannel(channel);
            }
        }
        return notificationManager;
    }
}
