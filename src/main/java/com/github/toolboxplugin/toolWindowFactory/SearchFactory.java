package com.github.toolboxplugin.toolWindowFactory;

import com.github.toolboxplugin.swing.SearchBar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.sun.istack.NotNull;
import com.intellij.openapi.util.Condition;

public class SearchFactory implements ToolWindowFactory, Condition<Project> {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SearchBar searchBar = new SearchBar();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(searchBar.getComponent(), "搜索", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * 控制tool window是否展示
     * @param project
     * @return
     */
    @Override
    public boolean value(Project project) {
        return false;
    }
}
