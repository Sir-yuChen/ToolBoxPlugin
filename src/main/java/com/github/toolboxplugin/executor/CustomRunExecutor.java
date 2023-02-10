package com.github.toolboxplugin.executor;

import com.github.toolboxplugin.utils.IconUtil;
import com.github.toolboxplugin.utils.StringConst;
import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CustomRunExecutor extends Executor {

    @Override
    public String getToolWindowId() {
        return StringConst.TOOL_WINDOW_ID;
    }

    @Override
    public Icon getToolWindowIcon() {
        return IconUtil.ICON;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return IconUtil.ICON;
    }

    @Override
    public Icon getDisabledIcon() {
        return IconUtil.ICON;
    }

    @Override
    public String getDescription() {
        return StringConst.TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getActionName() {
        return StringConst.TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getId() {
        return StringConst.PLUGIN_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return StringConst.TOOL_WINDOW_ID;
    }

    @Override
    public String getContextActionId() {
        return "custom context action id";
    }

    @Override
    public String getHelpId() {
        return StringConst.TOOL_WINDOW_ID;
    }

    public static Executor getRunExecutorInstance() {
        return ExecutorRegistry.getInstance().getExecutorById(StringConst.PLUGIN_ID);
    }
}