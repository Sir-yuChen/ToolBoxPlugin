package com.github.toolboxplugin.action;

import com.github.toolboxplugin.executor.CustomExecutor;
import com.github.toolboxplugin.swing.IReaderRule;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/***
 * IReader调试模式-规则详情
 */
public class IReaderRulesAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        //调用CustomExecutor 自定义的执行器，run方法被调用之后，会构建一个tool window并展示
        runExecutor(event.getProject());
    }

    public void runExecutor(Project project) {
        CustomExecutor executor = new CustomExecutor(project);
        IReaderRule iReaderRule = new IReaderRule(project);
        executor.withReturn(() -> runExecutor(project)).withRefresh(iReaderRule.Refresh(), () -> true);
        executor.run(iReaderRule.getComponent(), iReaderRule.getToolWindowId());
    }
}
