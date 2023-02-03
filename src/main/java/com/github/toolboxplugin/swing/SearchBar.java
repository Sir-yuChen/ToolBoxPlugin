package com.github.toolboxplugin.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchBar {
    private JPanel searchPanl;
    private JTextPane searchResult;
    private JTextField searchInput;

    public static void main(String[] args) {
        JFrame frame = new JFrame("SearchBar");
        frame.setContentPane(new SearchBar().searchPanl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JButton searchButton;


    public SearchBar() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = searchInput.getText();

                /// 弹出框
                JDialog jDialog = new JDialog();
                // 设置相对位置. null代表位于屏幕居中
                jDialog.setLocationRelativeTo(null);
                // 设置标题
                jDialog.setTitle("监听搜索按钮事件");
                // 设置可见性
                jDialog.setVisible(true);
                // 设置大小
                jDialog.setSize(200, 80);
                // 设置弹出框图标
            /*    try {
                    InputStream inputStream = Objects.requireNonNull(DemoGUI.class.getResourceAsStream("/information.png"));
                    jDialog.setIconImage(ImageIO.read(inputStream));
                } catch (Exception exc) {
                    throw new UndeclaredThrowableException(exc);
                }*/

                // 给弹出框面板添加组件
                Container contentPane = jDialog.getContentPane();
                contentPane.add(new JLabel("搜索内容为：" + inputText));
            }
        });
    }
    public JComponent getComponent() {
        return searchPanl;
    }

    public JTextField getUrlTextField() {
        return searchInput;
    }

}
