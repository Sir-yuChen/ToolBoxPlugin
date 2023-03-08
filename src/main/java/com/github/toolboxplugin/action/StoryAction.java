package com.github.toolboxplugin.action;

import com.github.toolboxplugin.executor.CustomExecutor;
import com.github.toolboxplugin.swing.StorySearch;
import com.github.toolboxplugin.utils.ConfigUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/*
 * 搜索按钮
 *   1. 触发toolwindow框，实现搜索功能
 * */
public class StoryAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //调用CustomExecutor 自定义的执行器，run方法被调用之后，会构建一个tool window并展示
        runExecutor(e.getProject());
    }

    public void runExecutor(Project project) {
        if (project == null) {
            return;
        }
        CustomExecutor executor = new CustomExecutor(project);
        StorySearch storySearch = new StorySearch();
        // 设置restart和stop
        executor.withReturn(() -> runExecutor(project)).withRefresh(() -> ConfigUtil.setRunning(project, false), () ->
                ConfigUtil.getRunning(project));
        executor.run(storySearch.getComponent(), storySearch.getToolWindowId());
    }
}
