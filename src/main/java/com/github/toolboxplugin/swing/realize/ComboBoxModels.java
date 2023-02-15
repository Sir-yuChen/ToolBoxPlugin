package com.github.toolboxplugin.swing.realize;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class ComboBoxModels extends AbstractListModel implements ComboBoxModel {
    List<Map<String, Object>> items = null;
    String item = null;

    //items 所有的可选项 ，item默认选择项
    public ComboBoxModels(List<Map<String, Object>> items, String item) {
        this.items = items;
        this.item = item;
    }

    //由于我们实现了ComboBoxModel interface.因此我们必须在程序中实作setSelectedItem()与getSelectedItem()方法.
    @Override
    public void setSelectedItem(Object anItem) {
        item = (String) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return item;
    }

    //由于继承AbstractListModel抽象类。因此我们分别在程序中实作了getElementAt()与getSize()方法。
    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public Object getElementAt(int index) {
        Map<String, Object> map = items.get(index);
        Object v = map.values().stream().findFirst().get();
        Object k = map.keySet().stream().findFirst().get();
        return v;
    }
}
