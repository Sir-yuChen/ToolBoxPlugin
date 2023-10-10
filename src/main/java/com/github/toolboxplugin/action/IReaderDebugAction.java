package com.github.toolboxplugin.action;

import com.github.toolboxplugin.swing.IReaderDebug;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;

/**
 * @author Administrator
 * @date 2023/9/19 16:10
 * @desc 自定义调试模式
 * @since v1.0
 */
public class IReaderDebugAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // 获取 JBPopupFactory
        JBPopupFactory instance = JBPopupFactory.getInstance();
        /*   JBPopupFactory 自定义窗口
         * createComponentPopupBuilder() 允许您在弹出窗口中显示任何Swing组件
         * createPopupChooserBuilder() 创建一个多选/单选框
         * createConfirmation() 创建一个确认框
         * createActionGroupPopup() 创建一个显示方法组的窗口,选中会执行方法。
         *
         * 创建弹出窗口后，需要 通过调用show() 方法之一来显示它。您可以通过调用showInBestPositionFor() 让IntelliJ平台根据上下文自动选择位置，
         * 或者通过showUnderneathOf() 和ShowInCenter() 等方法显式指定位置。show() 方法立即返回，
         * 不等待弹出窗口关闭。如果需要在弹出窗口关闭时执行某些操作，可以使用addListener() 方法将侦听器附加到它，
         * 然后重写弹出试的方法,例如onChosen()，或在弹出窗口中将事件处理程序附加到您自己的组件
         **/
     /*   IReaderDebug storyDebug = new IReaderDebug();
        JBPopup popup = instance.createComponentPopupBuilder(storyDebug.getComponent(), null).createPopup();
        popup.setSize(new Dimension(500, 500));
        popup.showInBestPositionFor(event.getDataContext());*/
        Project project = event.getProject();
        IReaderDebug dialog = new IReaderDebug(project);
        dialog.pack();
        dialog.setVisible(true);
    }

}
