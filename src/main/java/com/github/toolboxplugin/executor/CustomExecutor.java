package com.github.toolboxplugin.executor;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.content.Content;
import com.sun.istack.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.UUID;

/**
 * @Author zhangyu
 * @Date 14:11 2023/2/6
 * @Description 自定义执行器
 **/

public class CustomExecutor implements Disposable {

    private ConsoleView consoleView = null;
    private Project project = null;
    private Runnable rerunAction;
    private Runnable refreshAction;

    private Computable<Boolean> refreshEnabled;


    public CustomExecutor withReturn(Runnable returnAction) {
        this.rerunAction = returnAction;
        return this;
    }

    public CustomExecutor withRefresh(Runnable refreshAction, Computable<Boolean> refreshEnabled) {
        this.refreshAction = refreshAction;
        this.refreshEnabled = refreshEnabled;
        return this;
    }

    public CustomExecutor(@NotNull Project project) {
        this.project = project;
        this.consoleView = createConsoleView(project);
    }

    private ConsoleView createConsoleView(Project project) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        ConsoleView console = consoleBuilder.getConsole();
        return console;
    }

    @Override
    public void dispose() {
        Disposer.dispose(this);
    }

    public void run(JComponent jComponent, String toolWindowId) {
        String uuid = UUID.randomUUID().toString();
        //传递一个对象
        if (project.isDisposed()) {
            return;
        }
        Executor executor = ExecutorRegistry.getInstance().getExecutorById(toolWindowId);
        if (executor == null) {
            return;
        }

        final RunnerLayoutUi.Factory factory = RunnerLayoutUi.Factory.getInstance(project);
        RunnerLayoutUi layoutUi = factory.create(uuid, "runnerTitle", "sessionName", project);
        final JPanel consolePanel = createConsolePanel(consoleView);

        RunContentDescriptor descriptor = new RunContentDescriptor(new RunProfile() {
            @Nullable
            @Override
            public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
                return null;
            }

            @NotNull
            @Override
            public String getName() {
                return jComponent.getName();
            }

            @Nullable
            @Override
            public Icon getIcon() {
                return null;
            }
        }, new DefaultExecutionResult(), layoutUi);
        descriptor.setExecutionId(System.nanoTime());
//        StringConst.SEO_CONTENT_ID
        final Content content = layoutUi.createContent(uuid, jComponent, jComponent.getName(), AllIcons.Debugger.Console, consolePanel);
        content.setCloseable(true);
        layoutUi.getOptions().setLeftToolbar(createActionToolbar(consolePanel, consoleView, layoutUi, descriptor, executor), "RunnerToolbar");

        layoutUi.addContent(content);

        Disposer.register(descriptor, this);

        Disposer.register(content, consoleView);

        ExecutionManager.getInstance(project).getContentManager().showRunContent(executor, descriptor);
    }

    private ActionGroup createActionToolbar(JPanel consolePanel, ConsoleView consoleView, RunnerLayoutUi layoutUi, RunContentDescriptor descriptor, Executor executor) {
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new RerunAction(consolePanel, consoleView));
        actionGroup.add(new RefreshAction());
        //自定义左侧操作项
//        actionGroup.add(new CustomAction("custom action", "custom action", IconUtil.ICON));
        return actionGroup;
    }

    private JPanel createConsolePanel(ConsoleView consoleView) {
        JPanel panel = new JPanel();
        panel.add(consoleView.getComponent(), BorderLayout.CENTER);
        return panel;
    }

    private class RerunAction extends AnAction implements DumbAware {
        private final ConsoleView consoleView;

        public RerunAction(JComponent consolePanel, ConsoleView consoleView) {
            super("Rerun", "Rerun", AllIcons.Actions.Restart);
            this.consoleView = consoleView;
            registerCustomShortcutSet(CommonShortcuts.getRerun(), consolePanel);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            Disposer.dispose(consoleView);
            rerunAction.run();
        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setVisible(rerunAction != null);
            e.getPresentation().setIcon(AllIcons.Actions.Restart);
        }
    }

    //刷新页面
    private class RefreshAction extends AnAction implements DumbAware {

        public RefreshAction() {
            super("Refresh", "刷新当前页面", AllIcons.Actions.Refresh);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            refreshAction.run();
        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setVisible(refreshAction != null);
            e.getPresentation().setEnabled(refreshEnabled != null && refreshEnabled.compute());
        }
    }

}