package com.github.toolboxplugin.utils;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;

import java.io.File;


public class PathUtils {

    public static String getRootResources() {
        //通过自己插件的id获取pluginId
        PluginId pluginId = PluginId.getId("com.github.toolboxplugin");
        IdeaPluginDescriptor plugin = PluginManager.getPlugin(pluginId);
        File path = plugin.getPath();
        String pluginPath = path.getAbsolutePath();
        System.out.println(pluginPath);
        return pluginPath;
    }

}
