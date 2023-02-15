package com.github.toolboxplugin.action;

import com.github.toolboxplugin.executor.RunExecutor;
import com.github.toolboxplugin.swing.StorySearch;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/*
 * 搜索按钮
 *   1. 触发toolwindow框，实现搜索功能
 * */
public class SearchAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        new RunExecutor().runExecutor(e.getProject(),new StorySearch().getComponent());
    }

}
