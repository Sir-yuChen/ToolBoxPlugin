package com.github.toolboxplugin.swing;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class CommonFun {

    /**
     * 设置列表某一列的宽度
     */
    public static void setColumnSize(JTable table, int index, int preferedWidth, int maxWidth, int minWidth) {
        //表格的列模型
        JTableHeader tableHeader = table.getTableHeader();
        //得到第i个列对象
        TableColumn column = tableHeader.getColumnModel().getColumn(index);
        column.setPreferredWidth(preferedWidth);
        column.setMaxWidth(maxWidth);
        column.setMinWidth(minWidth);
        column.setWidth(preferedWidth);
    }
}
