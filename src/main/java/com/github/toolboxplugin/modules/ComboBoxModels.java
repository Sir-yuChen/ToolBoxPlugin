package com.github.toolboxplugin.modules;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class ComboBoxModels extends AbstractListModel implements ComboBoxModel {
    List<Map<String, Object>> items = null;
    String item = null;

    /**
     * @author zhangyu
     * @desc items 所有的可选项 ，item默认选择项
     * @date 2023/9/14 9:43
     */
    public ComboBoxModels(List<Map<String, Object>> items, String item) {
        this.items = items;
        this.item = item;
    }

    /**
     * @author zhangyu
     * @desc 由于我们实现了ComboBoxModel interface.因此我们必须在程序中操作setSelectedItem()与getSelectedItem()方法.
     * @date 2023/9/14 9:44
     */
    @Override
    public void setSelectedItem(Object anItem) {
        item = (String) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return item;
    }
    /**
     * @author Administrator
     * @date 2023/9/14 9:46
     * @desc 由于继承AbstractListModel抽象类。因此我们分别在程序中实作了getElementAt()与getSize()方法。
     */
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
