package com.github.toolboxplugin.factory;

import com.github.toolboxplugin.swing.MusicMain;
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
public class MusicViewBars extends SimpleToolWindowPanel {
    private Project project;
    private MusicMain musicMain;

    public MusicViewBars(Project project) {
        super(true, true); //控制按钮位置
        this.project = project;
        musicMain = new MusicMain();
        add(musicMain.getComponent());
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
        splitter.setFirstComponent(musicMain.getComponent());
        splitter.setProportion(0.3f);
        setContent(splitter);

    }

    public Project getProject() {
        return project;
    }

    public MusicMain getMusicMainUI() {
        return musicMain;
    }

}
