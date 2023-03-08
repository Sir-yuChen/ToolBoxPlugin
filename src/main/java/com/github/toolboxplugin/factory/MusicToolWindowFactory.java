package com.github.toolboxplugin.factory;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.sun.istack.NotNull;

//使用工厂方式创建ToolWindow
public class MusicToolWindowFactory implements ToolWindowFactory, Condition<Project> {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 窗体
        MusicViewBars viewPanel = new MusicViewBars(project);
        // 获取内容工厂的实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 获取 ToolWindow 显示的内容
        Content content = contentFactory.createContent(viewPanel, "音乐", false);
        // 设置 ToolWindow 显示的内容
        toolWindow.getContentManager().addContent(content,0);
    }

    /**
     * 控制tool window是否展示
     *
     * @param project
     * @return
     */
    @Override
    public boolean value(Project project) {
        return true;
    }
}
