package com.github.toolboxplugin.factory;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;

/**
 *
 **/
public class ToolViewBars extends SimpleToolWindowPanel {
    private Project project;

    public ToolViewBars(Project project) {
        super(true, true); //控制按钮位置
        this.project = project;
        //TODO-zy 添加展示组件
//        add();
        // 设置窗体侧边栏按钮
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new RefreshBar(this));
        group.add(new SettingBar(this));

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, group, true);
        toolbar.setTargetComponent(this);
        setToolbar(toolbar.getComponent());
        // 添加
        JBSplitter splitter = new JBSplitter(true);
        splitter.setSplitterProportionKey("main.splitter.key");
        //TODO-zy 添加展示组件
//        splitter.setFirstComponent();
        splitter.setProportion(0.3f);
        setContent(splitter);

    }

    public Project getProject() {
        return project;
    }
}
