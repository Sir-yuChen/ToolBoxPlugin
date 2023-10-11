package com.github.toolboxplugin.modules.story;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * 实现表格内容自动换行
 **/
public class TableViewRenderer extends JTextArea implements TableCellRenderer {
    public TableViewRenderer() {
        //将表格设为自动换行
        setLineWrap(true); //利用JTextArea的自动换行方法
    }

    public Component getTableCellRendererComponent(JTable jtable, Object obj, //obj指的是单元格内容
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText(obj == null ? "" : obj.toString()); //利用JTextArea的setText设置文本方法
        return this;
    }
}

