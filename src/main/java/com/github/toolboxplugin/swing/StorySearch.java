package com.github.toolboxplugin.swing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.model.DTO.LryStoryDTO;
import com.github.toolboxplugin.model.DTO.LryStoryResultDTO;
import com.github.toolboxplugin.swing.realize.ComboBoxModels;
import com.github.toolboxplugin.swing.realize.TableModel;
import com.github.toolboxplugin.swing.realize.story.StoryButtonColumn;
import com.github.toolboxplugin.utils.LambadaTools;
import com.github.toolboxplugin.utils.OkHttpUtil;
import com.github.toolboxplugin.utils.PropertiesUtil;
import okhttp3.Call;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class StorySearch {
    private static Logger logger = LogManager.getLogger(StorySearch.class);

    private JPanel allJpanel;
    private JPanel searchJpanel;
    private JScrollPane resultJscrollpanel;
    private JTextField searchInput;
    private JButton searchButton;
    private JComboBox selectedBox;
    private JTable resultTable;
    private JLabel label;
    private JTextPane introducePane;

    public StorySearch() {
        List<Map<String, Object>> arrayList = new ArrayList<>();
        arrayList.add(new HashMap<>() {{
            put("title", "标题");
        }});
        arrayList.add(new HashMap<>() {{
            put("author", "作者");
        }});
        arrayList.add(new HashMap<>() {{
            put("fictionType", "分类");
        }});
        ComboBoxModels comboBoxModels = new ComboBoxModels(arrayList, null);
        selectedBox.setModel(comboBoxModels);
        searchButton.addActionListener(e -> {
            label.setText("搜索中....");
            String searchWord = searchInput.getText();
            String selectBoxText = selectedBox.getSelectedItem().toString();
            for (int i = 0; i < arrayList.size(); i++) {
                String finalSelectBoxText = selectBoxText;
                Set<String> collect = arrayList.get(i).entrySet()
                        .stream()
                        .filter(kvEntry -> Objects.equals(kvEntry.getValue(), finalSelectBoxText))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
                if (collect != null && collect.size() >= 1) {
                    selectBoxText = collect.stream().findFirst().get();
                }
            }
            String ipApiUrl = PropertiesUtil.readProperties(GlobalConstant.SEO_HTTP_URL, "toolBoxPlugin.seo.lryApi");
            OkHttpUtil.builder().url(ipApiUrl + "/fiction/search/")
                    .addHeader("Content-Type", "text/html;charset=utf-8")
                    .addParam("option", selectBoxText == null ? "title" : selectBoxText)
                    .addParam("key", searchWord)
                    .addParam("from", "1")
                    .addParam("size", "100")
                    .addParameter("/")
                    .get()
                    .sync(new OkHttpUtil.ICallBack() {
                        @Override
                        public void onSuccessful(Call call, String ret) {
                            LryStoryResultDTO lryStoryResultDTO = JSONObject.toJavaObject(JSON.parseObject(ret), LryStoryResultDTO.class);
                            if (lryStoryResultDTO.getData() == null || lryStoryResultDTO.getData().size() <= 0) {
                                label.setText("未查询到符合条件的数据");
                                return;
                            } else {
                                label.setText("点击查看,即可查看详细目录");
                            }
                            //单元格元素类型
                            List<Class> cellType = new ArrayList<>(Arrays.asList(Integer.class, String.class, String.class, String.class,
                                    String.class, String.class, String.class, JButton.class));
                            //表头
                            List<String> titles = new ArrayList<>(Arrays.asList("序号", "唯一ID", "书名", "作者", "类型", "描述", "封面", "最后更新时间", "操作"));
                            List<List<Object>> resultTableData = new ArrayList<>();
                            List<LryStoryDTO> storyInfo = lryStoryResultDTO.getData();
                            storyInfo.stream()
                                    .filter(item -> item.getFictionId() != null)
                                    .forEach(LambadaTools.forEachWithIndex((item, index) -> {
                                        List<Object> objectArrayList = new ArrayList<>();
                                        objectArrayList.add(index);
                                        objectArrayList.add(item.getFictionId());
                                        objectArrayList.add(item.getTitle());
                                        objectArrayList.add(item.getAuthor());
                                        objectArrayList.add(item.getFictionType());
                                        objectArrayList.add(item.getDescs());
                                        objectArrayList.add(new ImageIcon(item.getCover()));
                                        objectArrayList.add(item.getUpdateTime());
                                        objectArrayList.add(new JButton("start1"));
                                        resultTableData.add(objectArrayList);
                                    }));
                            //可以被编辑的列
                            Integer editoredRowAndColumn[] = {resultTableData.get(0).size() - 1};
                            TableModel lryStoryTableModel = new TableModel(cellType, titles, resultTableData, editoredRowAndColumn);
                            resultTable.setModel(lryStoryTableModel);
                            StoryButtonColumn buttonColumn = new StoryButtonColumn(resultTable, resultTableData.get(0).size() - 1, label);

                            resultTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                            //resultTable.setEnabled(false); //表格只能看不能编辑，不能选中一行
                            //将表格放到滚动面板上
                            resultJscrollpanel.setViewportView(resultTable);
                        }

                        @Override
                        public void onFailure(Call call, String errorMsg) {
                            label.setText(errorMsg);
                        }
                    });
        });
        // 使用selection监听器来监听table的哪个条目被选中 当选择了某一行的时候触发该事件
        resultTable.getSelectionModel().addListSelectionListener(e -> {
            // 获取哪一行被选中了
            int row = resultTable.getSelectedRow();
            if (row >= 0) { //row为-1时说明在搜索中
                introducePane.setText(resultTable.getValueAt(row, 5).toString());
                label.setText("第" + row + "行");
            }
        });

    }

    public JComponent getComponent() {
        allJpanel.setName("搜书");
        return allJpanel;
    }
}
