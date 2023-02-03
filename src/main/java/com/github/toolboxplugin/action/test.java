package com.github.toolboxplugin.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.MessageType;

public class test extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        NotificationGroup notificationGroup = new NotificationGroup("test.toolboxplugin", NotificationDisplayType.BALLOON, false);
        /**
         * content :  通知内容
         * type  ：通知的类型，warning,info,error
         */
        Notification notification = notificationGroup.createNotification("测试通知", MessageType.INFO);
        Notifications.Bus.notify(notification);
        System.out.println("使用了插件！！！");
    }
}
