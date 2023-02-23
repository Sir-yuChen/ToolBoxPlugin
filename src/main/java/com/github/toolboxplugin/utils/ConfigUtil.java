package com.github.toolboxplugin.utils;

import com.github.toolboxplugin.config.GlobalConstant;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

public class ConfigUtil {

    public static void setRunning(Project project, boolean value) {
        PropertiesComponent.getInstance(project).setValue(GlobalConstant.RUNNING_KEY, value);
    }

    public static boolean getRunning(Project project) {
        //配置文件设置参数
        boolean aBoolean = PropertiesComponent.getInstance(project).getBoolean(GlobalConstant.RUNNING_KEY);
        return true;
    }
}