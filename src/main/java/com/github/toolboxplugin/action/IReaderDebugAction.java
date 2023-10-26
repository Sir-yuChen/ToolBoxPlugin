package com.github.toolboxplugin.action;

import com.github.toolboxplugin.swing.IReaderDebug;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/**
 * @author Administrator
 * @date 2023/9/19 16:10
 * @desc 自定义调试模式
 * @since v1.0
 */
public class IReaderDebugAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        IReaderDebug dialog = new IReaderDebug(project);
        dialog.pack();
        dialog.setVisible(true);
    }

}
