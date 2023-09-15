package com.github.toolboxplugin.modules.story;


import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Administrator
 * @desc 自定义JButton列，点击阅读图书按钮
 * @date 2023/9/14 10:49
 * @since v1.0
 */
public class StoryButtonColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener {
    //按钮的两种状态
    private JButton rb, eb;
    private int row;
    private int column;
    private JTable table;
    private JLabel label;
    private JTabbedPane tabbedPane;
    private String text = "阅读";

    public StoryButtonColumn(JTable table, int column, JLabel label, JTabbedPane tabbedPane) {
        super();
        this.table = table;
        this.label = label;
        this.column = column;
        this.tabbedPane = tabbedPane;
        rb = new JButton(text);
        eb = new JButton(text);
        eb.setFocusPainted(false);
        eb.addActionListener(this);
        //设置该单元格渲染和编辑样式
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }


    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return null;
    }

    //监听器方法
    @Override
    public void actionPerformed(ActionEvent arg0) {
        //点击查看详情获取当前数据的唯一ID
        label.setText("正在阅读:《" + table.getValueAt(row, 3).toString() + "》目录");
        //点击查看阅读，切换到阅读卡片
        tabbedPane.setSelectedIndex(2);
        //填充数据

    }

    @Override
    public Component getTableCellRendererComponent(JTable arg0, Object value,
                                                   boolean arg2, boolean arg3, int arg4, int arg5) {
        rb.setText(text);
        return rb;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        eb.setText(text);
        //编辑行，行号
        this.row = row;
        return eb;
    }
}