package com.github.toolboxplugin.factory;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

/**
 * 刷新
 */
public class RefreshBar extends DumbAwareAction {

    private ToolViewBars panel;

    public RefreshBar(ToolViewBars panel) {
        super("刷新页面", "Click to refresh", AllIcons.Actions.Refresh);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //TODO-zy  ToolViewBars 获取添加的组件调用刷新方法
    }

}