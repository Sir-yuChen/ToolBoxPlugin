package com.github.toolboxplugin.swing;

import com.github.toolboxplugin.swing.realize.ProgressBarColumn;
import com.github.toolboxplugin.swing.realize.TableModel;
import com.github.toolboxplugin.swing.realize.test.TestButtonColumn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestIdeaPlugin {
    private static Logger logger = LogManager.getLogger(TestIdeaPlugin.class);

    private JPanel searchPanl;
    private JTextField searchInput;

/*    public static void main(String[] args) {
        JFrame frame = new JFrame("TestIdeaPlugin");
        frame.setContentPane(new TestIdeaPlugin().searchPanl);//添加面板
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭模式
        frame.pack();//使用 jFrame.pack(); 替换 jFrame.setSize(800,400); 。。。可以很好的包裹住界面。
        frame.setVisible(true);//可见性
    }*/

    private JButton searchButton;
    private JPanel searchJpanel;
    private JScrollPane resultScrollPane;
    private JTable resultTable;
    private JLabel selectLabel;

    public TestIdeaPlugin() {
        searchButton.addActionListener(e -> {
            selectLabel.setText("双击查看详细信息");

            //可以被编辑的列
            Integer editoredRowAndColumn[] = {2, 4};
            String img = "https://image.baidu.com/search/detail?tn=baiduimagedetail&word=%E5%9F%8E%E5%B8%82%E5%BB%BA%E7%AD%91%E6%91%84%E5%BD%B1%E4%B8%93%E9%A2%98&album_tab=%E5%BB%BA%E7%AD%91&album_id=7&ie=utf-8&fr=albumsdetail&cs=1595072465,3644073269&pi=3977&pn=0&ic=0&objurl=https%3A%2F%2Ft7.baidu.com%2Fit%2Fu%3D1595072465%2C3644073269%26fm%3D193%26f%3DGIF";
            //单元格元素类型
            List<Class> cellType = new ArrayList<>(Arrays.asList(String.class, Icon.class, Boolean.class, JProgressBar.class, JButton.class));
            //表头
            List<String> title = new ArrayList<>(Arrays.asList("编号", "图标", "是否选中", "进度", "操作"));
            List<List<Object>> data = new ArrayList<>();
            data.add(new ArrayList<>(Arrays.asList("1", new ImageIcon(img), new Boolean(true), 0, new JButton("start1"))));
            data.add(new ArrayList<>(Arrays.asList("2", new ImageIcon(img), new Boolean(false), 60, new JButton("start2"))));
            data.add(new ArrayList<>(Arrays.asList("3", new ImageIcon(img), new Boolean(false), 25, new JButton("start3"))));

            TableModel lryStoryTableModel = new TableModel(cellType, title, data, editoredRowAndColumn);
            resultTable.setModel(lryStoryTableModel);
            ProgressBarColumn progressBarColumn = new ProgressBarColumn(resultTable, 3);
            TestButtonColumn buttonColumn = new TestButtonColumn(resultTable, 4);

            resultTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            //resultTable.setEnabled(false); //表格只能看不能编辑，不能选中一行
            //将表格放到滚动面板上
            resultScrollPane.setViewportView(resultTable);
        });
        // 使用selection监听器来监听table的哪个条目被选中
        // 当选择了某一行的时候触发该事件
        resultTable.getSelectionModel().addListSelectionListener(e1 -> {
            // 获取哪一行被选中了
            int row = resultTable.getSelectedRow();
            selectLabel.setText("当前选择了第" + row + "行");
        });
    }

    public JComponent getComponent() {
        searchPanl.setName("SEO");
        return searchPanl;
    }

}