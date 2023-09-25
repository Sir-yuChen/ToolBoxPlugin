package com.github.toolboxplugin.utils;

import com.intellij.icons.AllIcons;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Administrator
 * https://houbb.github.io/2017/10/13/idea-plugin-dev-03-02-popup
 */
public class NotificationUtils {
    private static NotificationGroup notificationGroup_STICKY_BALLOON = new NotificationGroup("ToolboxPlugin", NotificationDisplayType.STICKY_BALLOON, true);
    private static NotificationGroup notificationGroup_BALLOON = new NotificationGroup("ToolboxPlugin", NotificationDisplayType.BALLOON, true);
    private static NotificationGroup notificationGroup_NONE = new NotificationGroup("ToolboxPlugin", NotificationDisplayType.NONE, true);
    private static NotificationGroup notificationGroup_TOOL_WINDOW = new NotificationGroup("ToolboxPlugin", NotificationDisplayType.TOOL_WINDOW, true);

    public static void setNotification(String title, String message, NotificationDisplayType notificationType,
                                       NotificationType messageType, Project project, int delay) {
        Notification notification = null;
        switch (notificationType) {
            case BALLOON:
                // 设置消息的图标
                notification = notificationGroup_BALLOON.createNotification(title, message,
                        NotificationType.INFORMATION, new NotificationListener.UrlOpeningListener(false));
                break;
            case NONE:
                notification = notificationGroup_NONE.createNotification(title, message,
                        NotificationType.INFORMATION, new NotificationListener.UrlOpeningListener(false));
                break;
            case TOOL_WINDOW:
                notification = notificationGroup_TOOL_WINDOW.createNotification(title, message,
                        NotificationType.INFORMATION, new NotificationListener.UrlOpeningListener(false));
                break;
            default:
                //默认
                notification = notificationGroup_STICKY_BALLOON.createNotification(title, message,
                        NotificationType.INFORMATION, new NotificationListener.UrlOpeningListener(false));
        }
        //设置提示图标
        if (notification != null) {
            try {
                Icon icon = getIcon(messageType);
                // 设置消息的图标
                notification.setIcon(icon);
                Notifications.Bus.notify(notification, project);
                if (delay > 0) {
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
