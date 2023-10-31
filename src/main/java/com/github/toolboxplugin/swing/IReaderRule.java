package com.github.toolboxplugin.swing;

import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.infrastructure.iReader.StoryBookRulesData;
import com.github.toolboxplugin.model.DTO.IReaderDebugRuleDTO;
import com.github.toolboxplugin.modules.story.IReaderButton;
import com.github.toolboxplugin.modules.story.TableModel;
import com.github.toolboxplugin.utils.LambadaTools;
import com.github.toolboxplugin.utils.NotificationUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jsoup.internal.StringUtil;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.toolboxplugin.swing.CommonFun.setColumnSize;

/***
 * IReader调试模式-规则详情
 */
public class IReaderRule implements BaseUIAction {
    private JPanel IReaderRulePanel;
    private JScrollPane ruleScrollPane;
    private JTable rulesTable;
    private JTextPane ruleDisplayPane;

    private Project project;

    public IReaderRule(Project project) {
        this.project = project;
        processingRulesTable();
    }

    @Override
    public JComponent getComponent() {
        IReaderRulePanel.setName("iReader debug");
        return IReaderRulePanel;
    }

    @Override
    public String getToolWindowId() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
    }

    @Override
    public void setBefore() {

    }

    @Override
    public void setAfter() {

    }

    /***
     * 页面刷新方法
     */
    public Runnable Refresh() {
        return () -> {
            processingRulesTable();
            IReaderRulePanel.updateUI();
            NotificationUtils.setNotification( "刷新成功！",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.INFORMATION, project, 2000);
        };
    }

    private void processingRulesTable() {
        List<IReaderDebugRuleDTO> rules = StoryBookRulesData.getInstance().getStoryBookRules();
        List<String> titles = new ArrayList<>(Arrays.asList("序号", "规则名称", "章节标题规则", "章节地址规则", "图书内容规则", "操作项"));
        if (CollectionUtils.isEmpty(rules)) {
            NotificationUtils.setNotification( "一条规则都不存在！",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.INFORMATION, project, 2000);
            return;
        }
        List<List<Object>> resultTableData = new ArrayList<>();
        rules.stream()
                .filter(item -> !StringUtil.isBlank(item.getRuleName()))
                .forEach(LambadaTools.forEachWithIndex((item, index) -> {
                    List<Object> objectArrayList = new ArrayList<>();
                    objectArrayList.add(index + 1);
                    objectArrayList.add(item.getRuleName());
                    objectArrayList.add(item.getChapterTitleRuleInfo());
                    objectArrayList.add(item.getChapterUrlRuleInfo());
                    objectArrayList.add(item.getContentRuleInfo());
                    objectArrayList.add(item.getContentRuleInfo());
                    resultTableData.add(objectArrayList);
                }));
        Integer editoredRowAndColumn[] = {5};
        //可以被编辑的列
        TableModel tableModel = new TableModel(null, titles, resultTableData, editoredRowAndColumn);
        rulesTable.setModel(tableModel);
        rulesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //禁止鼠标拖动列
        rulesTable.getTableHeader().setReorderingAllowed(false);
        //设置操作列
        IReaderButton iReaderButton = new IReaderButton(rulesTable, 5, "删 除") {
            //当前操作按钮监听
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = rulesTable.getSelectedRow();
                if (row >= 0) {
                    String ruleName = rulesTable.getValueAt(row, 1).toString();
                    StoryBookRulesData.getInstance().delStoryBookRule(ruleName);
                    NotificationUtils.setNotification( "规则:" + ruleName + " 删除成功",
                            NotificationDisplayType.STICKY_BALLOON, NotificationType.INFORMATION, project, 3000);
                    processingRulesTable();
                }
            }
        };
        rulesTable.getColumnModel().getColumn(5).setCellRenderer(iReaderButton);
        //渲染JTable的自定义绘制器 实现表格内容自动换行
//        rulesTable.setDefaultRenderer(Object.class, new TableViewRenderer());
        //刷新表数据
        rulesTable.repaint();
        setColumnSize(rulesTable, 0, 50, 60, 40);
        setColumnSize(rulesTable, 5, 100, 100, 100);
        ruleScrollPane.setViewportView(rulesTable);
    }

}
