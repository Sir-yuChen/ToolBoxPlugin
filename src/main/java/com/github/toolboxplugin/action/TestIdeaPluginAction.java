package com.github.toolboxplugin.action;

import com.github.toolboxplugin.executor.RunExecutor;
import com.github.toolboxplugin.swing.TestIdeaPlugin;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class TestIdeaPluginAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //调用CustomExecutor 自定义的执行器，run方法被调用之后，会构建一个tool window并展示
        new RunExecutor().runExecutor(e.getProject(),new TestIdeaPlugin().getComponent());
    }
}
