package com.github.toolboxplugin.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        logger.info("获取配置文件 key={} value={}", key,v);
        return v;
    }
}