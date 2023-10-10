package com.github.toolboxplugin.action;

import com.github.toolboxplugin.executor.CustomExecutor;
import com.github.toolboxplugin.swing.IReader;
import com.github.toolboxplugin.utils.ConfigUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;


/**
 * @author Administrator
 * @date 2023/9/19 16:09
 * @since v1.0
 * @desc 默认搜索模式 触发toolwindow框，实现搜索展示等功能
 */
public class IReaderSearchAction extends AnAction {

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
        IReader storySearch = new IReader(0);
        // 设置restart和stop
        executor.withReturn(() -> runExecutor(project)).withRefresh(() -> ConfigUtil.setRunning(project, false), () ->
                ConfigUtil.getRunning(project));
        executor.run(storySearch.getComponent(), storySearch.getToolWindowId());
    }
}
