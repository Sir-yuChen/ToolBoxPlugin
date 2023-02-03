package com.github.toolboxplugin.toolWindowFactory;

import com.github.toolboxplugin.swing.SearchBar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.sun.istack.NotNull;

public class SearchFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SearchBar searchBar = new SearchBar();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(searchBar.getComponent(), "ToolBoxPlugin--搜索", false);
        toolWindow.getContentManager().addContent(content);
    }
}
