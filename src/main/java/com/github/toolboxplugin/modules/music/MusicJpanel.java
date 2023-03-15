package com.github.toolboxplugin.modules.music;

import com.github.toolboxplugin.swing.MusicMain;
import com.github.toolboxplugin.utils.IconConstant;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicJpanel extends JPanel implements ActionListener {

    static final private String REFRESH = "REFRESH";
    static final private String NEXT = "NEXT";
    static final private String UP = "UP";

    public MusicJpanel(Integer currentPage, JPanel homebelowPanel, MusicMain musicMain) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        constraints.gridheight = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;


        setOpaque(true);
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new RefreshBar(homebelowPanel, currentPage,musicMain));
        group.add(new NextPageBar(homebelowPanel, currentPage,musicMain));
        group.add(new UpPageBar(homebelowPanel, currentPage,musicMain));
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, group, true);
        toolbar.setTargetComponent(this);

        JToolBar toolBar = new JToolBar();
        //工具类不可以拖的动
        toolBar.setFloatable(false);
        // 设置工具栏方向，值为 wingConstants.HORIZONTAL 或 SwingConstants.VERTICAL
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        // 设置工具栏边缘和其内部工具组件之间的边距（内边距）
        toolBar.setMargin(new Insets(0, 0, 0, 5));
        // 是否需要绘制边框
        toolBar.setBorderPainted(true);
        toolBar.add(toolbar.getComponent());
        // 添加 工具栏 到 内容面板 的 顶部
        gridBagLayout.setConstraints(toolBar, constraints);
        toolBar.setPreferredSize(new Dimension(800, 30));
        add(toolBar);

       /*
        //创建工具栏 button/JLabel按钮 形式
        JToolBar toolBar2 = new JToolBar();
        addButtons(toolBar2);
        add(toolBar2, BorderLayout.PAGE_START);
        */

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /***
     * 监听事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    protected void addButtons(JToolBar toolBar) {
        JLabel jLabel;
//        JButton button = null;
        jLabel = makeNavigationButton(AllIcons.Actions.Refresh, REFRESH, "刷新", "刷新");
        toolBar.add(jLabel);
        jLabel = makeNavigationButton(AllIcons.Actions.NextOccurence, NEXT, "Next", "Next");
        toolBar.add(jLabel);
        jLabel = makeNavigationButton(AllIcons.Actions.Commit, UP, "Up", "Up");
        toolBar.add(jLabel);
    }

    protected JLabel makeNavigationButton(Icon image, String actionCommand, String toolTipText, String altText) {
        //初始化工具按钮
        JLabel jLabel = new JLabel();
        //设置提示信息
        jLabel.setToolTipText(toolTipText);
        if (image != null) {
            //图标
            jLabel.setIcon(image);
        } else {
            //没有图像
            jLabel.setText(altText);
        }
        return jLabel;
    }
}

class RefreshBar extends DumbAwareAction {
    private JPanel panel;
    private Integer currentPage;
    private MusicMain musicMain;
    public RefreshBar(JPanel panel, Integer currentPage, MusicMain musicMain) {
        super("刷新页面", "Click to refresh", AllIcons.Actions.Refresh);
        this.panel = panel;
        this.currentPage = currentPage;
        this.musicMain = musicMain;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        panel.removeAll();
        musicMain.fillHotPlaylistJPanel(panel, currentPage);
    }
}

class NextPageBar extends DumbAwareAction {
    private JPanel panel;
    private Integer page;
    private MusicMain musicMain;

    public NextPageBar(JPanel panel, Integer currentPage, MusicMain musicMain) {
        super("下一页", "Click to Next Page", IconConstant.MUSIC_PAGE_ARROW_DOWN);
        this.panel = panel;
        this.page = currentPage;
        this.musicMain = musicMain;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        panel.removeAll();
        musicMain.fillHotPlaylistJPanel(panel, page + 1);
    }
}

class UpPageBar extends DumbAwareAction {
    private JPanel panel;
    private Integer currentPage;
    private MusicMain musicMain;

    public UpPageBar(JPanel panel, Integer currentPage, MusicMain musicMain) {
        super("上一页", "Click to Up Page", IconConstant.MUSIC_PAGE_ARROW_UP);
        this.panel = panel;
        this.currentPage = currentPage;
        this.musicMain = musicMain;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        panel.removeAll();
        musicMain.fillHotPlaylistJPanel(panel, currentPage - 1);
    }
}

