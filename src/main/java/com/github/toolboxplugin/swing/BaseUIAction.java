package com.github.toolboxplugin.swing;

import javax.swing.*;


public interface BaseUIAction {
    JComponent getComponent();

    //所属窗口ID
    String getToolWindowId();

    //前置方法
    void setBefore();

    //前置方法
    void setAfter();
}
