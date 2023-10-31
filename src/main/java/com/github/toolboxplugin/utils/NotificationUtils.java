package com.github.toolboxplugin.utils;

import com.intellij.icons.AllIcons;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import javax.swing.*;

/**
 * @author Administrator
 */
public class NotificationUtils {
    // 获取通知组管理器
    private static NotificationGroupManager manager = NotificationGroupManager.getInstance();
    // 获取注册的通知组
    private static NotificationGroup notificationGroup_BALLOON = manager.getNotificationGroup("toolbox.notification.balloon");
    private static NotificationGroup notificationGroup_STICKY_BALLOON = manager.getNotificationGroup("toolbox.notification.sticky_balloon");
    private static NotificationGroup notificationGroup_NONE = manager.getNotificationGroup("toolbox.notification.none");
    private static NotificationGroup notificationGroup_TOOL_WINDOW = manager.getNotificationGroup("toolbox.notification.tool_window");

    public static void setNotification(String message, NotificationDisplayType notificationType,
                                       NotificationType messageType, Project project, int delay) {
        Notification notification = null;
        switch (notificationType) {
            case BALLOON:
                // 设置消息的图标
                notification = notificationGroup_BALLOON.createNotification(message, NotificationType.INFORMATION);
                break;
            case NONE:
                notification = notificationGroup_NONE.createNotification(message, NotificationType.INFORMATION);
                break;
            case TOOL_WINDOW:
                notification = notificationGroup_TOOL_WINDOW.createNotification(message, NotificationType.INFORMATION);
                break;
            default:
                //默认 手动关闭
                notification = notificationGroup_STICKY_BALLOON.createNotification(message, NotificationType.INFORMATION);
        }
        //设置提示图标
        if (notification != null) {
            try {
                Icon icon = getIcon(messageType);
                // 设置消息的图标
                notification.setIcon(icon);
                Notifications.Bus.notify(notification, project);
                if (delay > 0 && notificationType.equals(NotificationDisplayType.STICKY_BALLOON)) {
                    //设置延迟关闭时间 毫秒
                    Notification finalNotification = notification;
                    Timer timer = new Timer(delay, e -> {
                        // 关闭通知栏消息
                        finalNotification.expire();
                    });
                    // 设置定时器只触发一次
                    timer.setRepeats(false);
                    // 启动定时器
                    timer.start();
                }
            } catch (Exception e) {
                System.out.println("e = " + e);
            }
        }
    }

    private static @NotNull Icon getIcon(NotificationType messageType) {
        Icon icon = null;
        switch (messageType) {
            case ERROR:
                icon = AllIcons.General.NotificationError;
                break;
            case WARNING:
                icon = AllIcons.General.NotificationWarning;
                break;
            default:
                //INFORMATION
                icon = AllIcons.General.NotificationInfo;
        }
        return icon;
    }

}
