package com.github.toolboxplugin.swing.realize.story;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 自定义的JTextPane，扩展了JTextPane没有的一些功能（java关键字、单行注释、多行注释加亮）
 https://blog.csdn.net/octdream/article/details/100849379
 **/
public class StoryJtextPane extends JTextPane implements DocumentListener {

    /**
     *  TODO: 2023/2/15
     *  1. 选择行，展示简介
     *  2. 添加边框
     *  3. 点击搜索 如果已经存在着隐藏，正常未选择行的情况下隐藏，只有选择了一行数据，才显示
     *  4. 关闭符合
     *  5. 高亮关键字，书名
     *  6. 实现自动换行
     **/
    // 给出对文档执行了插入操作的通知
    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    // 给出移除了一部分文档的通知
    @Override
    public void removeUpdate(DocumentEvent e) {

    }
    // 给出属性或属性集发生了更改的通知
    @Override
    public void changedUpdate(DocumentEvent e) {

    }

}
