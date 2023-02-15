package com.github.toolboxplugin.swing.realize;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel {
    //单元格元素类型
    private List<Class> cellType;
    //表头
    private List<String> title;
    //模拟数据
    private List<List<Object>> data;
    //可以被编辑的列
    private Integer editoredRowAndColumn[];

    public TableModel(List<Class> type, List<String> title, List<List<Object>> data, Integer[] editoredRowAndColumn) {
        this.cellType = type;
        this.data = data;
        this.title = title;
        this.editoredRowAndColumn = editoredRowAndColumn;
    }

   /* @Override
    public Class<?> getColumnClass(int arg0) {
        // TODO Auto-generated method stub
        return cellType[arg0];
    }*/

    @Override
    public String getColumnName(int arg0) {
        // TODO Auto-generated method stub
        return title.get(arg0);
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return title.size();
    }

    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getValueAt(int r, int c) {
        // TODO Auto-generated method stub
        Object o = data.get(r).get(c);
        return o;
    }

    //重写isCellEditable方法
    @Override
    public boolean isCellEditable(int r, int c) {
        if (find(editoredRowAndColumn, c) != -1) {
            return true;
        }
        return false;
    }

    //重写setValueAt方法
    @Override
    public void setValueAt(Object value, int r, int c) {
        Object o = data.get(r).get(c);
        o = value;
        this.fireTableCellUpdated(r, c);
    }

    public static int find(Integer[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if (num == arr[i]) {
                return i;
            }
        }
        return -1;
    }

}
