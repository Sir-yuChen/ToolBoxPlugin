package com.github.toolboxplugin.swing;

import javax.swing.*;


public interface BaseUIAction {
    /**
     * @return
     * @author zhangyu
     * @description 功能模块实现类
     * @date 2023/9/14 9:39
     */
    JComponent getComponent();

    /**
     * @return
     * @author zhangyu
     * @description 所属窗口ID
     * @date 2023/9/14 9:39
     */
    String getToolWindowId();

    /**
     * @return
     * @author zhangyu
     * @description 前置方法
     * @date 2023/9/14 9:39
     */
    void setBefore();

    /**
     * @return
     * @author zhangyu
     * @description 后置方法
     * @date 2023/9/14 9:39
     */
    void setAfter();
}
