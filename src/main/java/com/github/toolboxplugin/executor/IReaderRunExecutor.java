package com.github.toolboxplugin.executor;

import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.utils.IconUtil;
import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class IReaderRunExecutor extends Executor {

    @Override
    public String getToolWindowId() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
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
        return GlobalConstant.TOOL_WINDOW_ID_iReader + "窗口";
    }

    @NotNull
    @Override
    public String getActionName() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
    }

    @NotNull
    @Override
    public String getId() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
    }

    @Override
    public String getContextActionId() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
    }

    @Override
    public String getHelpId() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
    }

    public Executor getRunExecutorInstance(String id) {
        return ExecutorRegistry.getInstance().getExecutorById(id);
    }
}