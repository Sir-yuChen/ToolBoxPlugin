package com.github.toolboxplugin.factory;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;


public class SettingBar extends DumbAwareAction {

    private MusicViewBars panel;

    public SettingBar(MusicViewBars panel) {
        super("设置", "Click to setting", AllIcons.Actions.SetDefault);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        ShowSettingsUtil.getInstance().editConfigurable(panel.getProject(), new GidConfig(panel.getConsoleUI()));
    }

}