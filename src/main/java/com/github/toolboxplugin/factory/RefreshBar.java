package com.github.toolboxplugin.factory;

import com.github.toolboxplugin.swing.MusicMain;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

/**
 * 刷新
 */
public class RefreshBar extends DumbAwareAction {

    private MusicViewBars panel;

    public RefreshBar(MusicViewBars panel) {
        super("刷新页面", "Click to refresh", AllIcons.Actions.Refresh);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        MusicMain musicMainUI = panel.getMusicMainUI();
        musicMainUI.refresh();
    }

}