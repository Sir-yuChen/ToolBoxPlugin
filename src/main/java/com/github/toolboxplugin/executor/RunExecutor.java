package com.github.toolboxplugin.executor;

import com.github.toolboxplugin.swing.TestIdeaPlugin;
import com.github.toolboxplugin.utils.ConfigUtil;
import com.intellij.openapi.project.Project;

import javax.swing.*;

public class RunExecutor {
    public void runExecutor(Project project, JComponent component) {
        if (project == null) {
            return;
        }
        CustomExecutor executor = new CustomExecutor(project);
        // 设置restart和stop
        executor.withReturn(() -> runExecutor(project, new TestIdeaPlugin().getComponent())).withStop(() -> ConfigUtil.setRunning(project, false), () ->
                ConfigUtil.getRunning(project));
        executor.run(component);
    }
}
