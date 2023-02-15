package com.github.toolboxplugin.swing.realize;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

//自定义ProgressBar 列，实现方式与按钮类似
public class ProgressBarColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    JProgressBar rjsb, ejsb;
    JTable table;
    private int row;

    public ProgressBarColumn(JTable table, int column) {
        super();
        this.table = table;
        rjsb = new JProgressBar();
        rjsb.setMaximum(100);
        rjsb.setBackground(Color.BLUE);
        rjsb.setForeground(Color.LIGHT_GRAY);
        rjsb.setStringPainted(true);
        rjsb.setBorderPainted(false);
        ejsb = new JProgressBar();
        ejsb.setMaximum(100);
        ejsb.setBackground(Color.DARK_GRAY);
        ejsb.setForeground(Color.LIGHT_GRAY);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable arg0, Object value,
                                                   boolean arg2, boolean arg3, int arg4, int arg5) {
        // TODO Auto-generated method stub
        rjsb.setValue(Integer.parseInt(value.toString()));
        return rjsb;
    }

    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // TODO Auto-generated method stub
        this.row = row;
        ejsb.setValue(ejsb.getValue() + 5);
        return ejsb;
    }
}
