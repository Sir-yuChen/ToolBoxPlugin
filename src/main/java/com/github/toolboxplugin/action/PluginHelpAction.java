package com.github.toolboxplugin.action;

import com.github.toolboxplugin.utils.NotificationUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author Administrator
 * @date 2023/9/19 16:09
 * @desc 帮助
 * @since v1.0
 */
public class PluginHelpAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        String site = "https://github.com/Sir-yuChen/ToolBoxPlugin";
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isDesktopSupported()
                    && desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(site);
                desktop.browse(uri);
            }
        } catch (IOException ex) {
            NotificationUtils.setNotification("打开帮助文档失败,请手动浏览器打开:" + site,
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, e.getProject(), -1);
        } catch (URISyntaxException ex) {
            NotificationUtils.setNotification("打开帮助文档失败,请手动浏览器打开:" + site,
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, e.getProject(), -1);
        }
    }
}
