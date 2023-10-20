package com.github.toolboxplugin.utils;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ResourceBundle;

/**
 * @author Administrator
 */
public class PropertiesUtil {
    private static Logger logger = LogManager.getLogger(PropertiesUtil.class);

    /**
     * 读取 properties 测试项目配置文件
     */
    public static String readProperties(String propertiesPath, String key) {
        //放src目录下的.properties
        ResourceBundle resource = ResourceBundle.getBundle(propertiesPath);
        String v = resource.getString(key);
//        logger.info("获取配置文件 key={} value={}", key, v);
        return v;
    }

    /**
     * idea插件路径
     */
    public static String getPropertiesPath() {
        PluginId pluginId = PluginId.getId("com.github.toolbox");
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(pluginId);
        File path = plugin.getPath();
        String pluginPath = path.getAbsolutePath();
        return pluginPath;
    }
}