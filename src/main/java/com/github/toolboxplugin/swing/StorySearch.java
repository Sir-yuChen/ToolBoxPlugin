package com.github.toolboxplugin.swing;

import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.infrastructure.story.StoryBookshelfSetting;
import com.github.toolboxplugin.model.DTO.*;
import com.github.toolboxplugin.modules.ComboBoxModels;
import com.github.toolboxplugin.modules.TableModel;
import com.github.toolboxplugin.modules.story.StoryButtonColumn;
import com.github.toolboxplugin.modules.story.StoryDirJListRender;
import com.github.toolboxplugin.service.StoryService;
import com.github.toolboxplugin.utils.LambadaTools;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StorySearch implements BaseUIAction {

    //搜索
    private JPanel allJpanel;
    private JPanel searchJpanel;
    private JScrollPane resultJscrollpanel;
    private JTextField searchInput;
    private JButton searchButton;
    private JComboBox selectedBox;
    private JTable resultTable;
    private JLabel label;
    private JTextArea introductionTextArea;
    private JPanel promptJpanel;
    private JLabel pageLabel;
    private JButton upperPageButton; //上一页
    private JButton underPageButton; //下一页
    //阅读
    private JTabbedPane tabbedPane;
    private JPanel readPromptJpanel;
    private JScrollPane readDirectory;
    private JScrollPane readContent;
    private JLabel readPromptLabel;
    private JList dirlist;
    private JTextPane contentPanel;
    private JButton collectButton;
    //书架
    private JPanel bookshelfTopPanel;
    private JLabel bookshelfPromptLabel;
    private JTable bookshelfTable;
    private JScrollPane bookshelfJscrollpane;
    private JButton keepReadbutton;
    private JButton delCollect;

    private HTMLDocument text_html;
    private HTMLEditorKit htmledit;

    private String searchWord;
    public String fictionIdLast;
    private String selectBoxText;
    private Integer currentPage;
    private Integer pageSize = 30;
    private Integer totalPage;
    private List<Map<String, Object>> comboBoxValues;
    private String keepChapterId;//继续阅读的章节ID
    private String currentChapterId;//当前阅读的章节ID

    public StorySearch() {
        setBefore();
        //搜索按钮点击监听
        searchButton.addActionListener(e -> {
            searchWord = searchInput.getText();
            selectBoxText = selectedBox.getSelectedItem().toString();
            currentPage = 1;
            searchStory();
            upperPageButton.setVisible(true);
            underPageButton.setVisible(true);
            keepChapterId = null;
        });
        // 使用selection监听器来监听table的哪个条目被选中 当选择了某一行的时候触发该事件
        resultTable.getSelectionModel().addListSelectionListener(e -> {
            // 获取哪一行被选中了
            int row = resultTable.getSelectedRow();
            if (row >= 0) { //row为-1时说明在搜索中
                introductionTextArea.setText(resultTable.getValueAt(row, 5).toString());
                //换行
                introductionTextArea.setLineWrap(true);
                //启用自动换行
                introductionTextArea.setWrapStyleWord(true);
                introductionTextArea.setEditable(false);
                label.setText("第" + row + "行");
            }
        });
        //上一页
        upperPageButton.addActionListener(e -> {
            //判断是否存在上一页
            if (currentPage <= 1) {
                label.setText("当前已经是第一页");
                upperPageButton.setVisible(false);
                return;
            }
            currentPage = currentPage - 1;
            searchStory();
            underPageButton.setVisible(true);
        });
        //下一页
        underPageButton.addActionListener(e -> {
            //判断是否存在下一页
            if (totalPage.equals(currentPage) || totalPage < currentPage) {
                label.setText("当前已经是最后一页");
                underPageButton.setVisible(false);
                return;
            }
            currentPage = currentPage + 1;
            searchStory();
            upperPageButton.setVisible(true);
        });
        // 添加选项卡选中状态改变的监听器
        tabbedPane.addChangeListener(e -> {
            //阅读选项卡
            if (tabbedPane.getSelectedIndex() == 1) {
                setTextPromptLabel(keepChapterId == null ? resultTable : bookshelfTable, readPromptLabel, "read_init", null, null);
                //加载目录
                getStoryDirectory();
                //查询当前图书是否已经被收藏
                checkAndUpdateBookCollected();
            }
            //书架
            if (tabbedPane.getSelectedIndex() == 2) {
                setTextPromptLabel(bookshelfTable, bookshelfPromptLabel, "book_init", null, null);
                //读取文件信息展示当前用户收集的图书
                showBookCollected();
            }
        });
        //目录被选中后加装内容监听
        dirlist.addListSelectionListener(e -> {
            DirectoryChapter value = (DirectoryChapter) dirlist.getSelectedValue();
            if (value == null) {//切换了书本，展示首页
                return;
            } else {
                getStoryDirContent(value.getChapterId(), value.getTitle());
            }
            //如果为已经被收藏的图书，则需要更新当前图书的阅读章节
            checkAndUpdateBookCollected();
        });
        //收藏/取消收藏按钮监听
        collectButton.addActionListener(e -> {
            collectButtonHandle();
        });
        //继续阅读按钮监听
        keepReadbutton.addActionListener(e -> {
            keepRead();
        });
        //取消收藏监听
        delCollect.addActionListener(e -> {
            collectButtonHandle();
        });
    }

    @Override
    public JComponent getComponent() {
        allJpanel.setName("搜书");
        return allJpanel;
    }

    //所属窗口ID
    @Override
    public String getToolWindowId() {
        return GlobalConstant.TOOL_WINDOW_ID_SEARCH;
    }

    @Override
    public void setBefore() {
        //初始化上一页/下一页按钮不显示
        upperPageButton.setVisible(false);
        underPageButton.setVisible(false);

        comboBoxValues = new ArrayList<>();
        comboBoxValues.add(new HashMap<>() {{
            put("title", "标题");
        }});
        comboBoxValues.add(new HashMap<>() {{
            put("author", "作者");
        }});
        comboBoxValues.add(new HashMap<>() {{
            put("fictionType", "分类");
        }});
        ComboBoxModels comboBoxModels = new ComboBoxModels(comboBoxValues, null);
        selectedBox.setModel(comboBoxModels);
        selectedBox.setSelectedIndex(0);
        readDirectory.setMaximumSize(new Dimension(150, -1));
        contentPanel.setEditable(false);
        collectButton.setVisible(false);
        keepChapterId = null;
    }

    @Override
    public void setAfter() {

    }

    /**
     * 书架 继续阅读安装
     */
    private void keepRead() {
        int row = bookshelfTable.getSelectedRow();
        fictionIdLast = bookshelfTable.getValueAt(row, 1).toString();
        keepChapterId = bookshelfTable.getValueAt(row, 7).toString();
        //点击查看阅读，切换到阅读卡片
        tabbedPane.setSelectedIndex(1);
    }

    /**
     * 书架，展示收藏的图书
     */
    private void showBookCollected() {

        List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
        if (storys == null || storys.size() <= 0) {
            setTextPromptLabel(bookshelfTable, bookshelfPromptLabel, "book_null", null, null);
            return;
        }
        //表头
        List<String> titles = new ArrayList<>(Arrays.asList("序号", "唯一ID", "书名", "作者", "类型", "最后阅读时间", "阅读章节", "章节ID"));
        List<List<Object>> resultTableData = new ArrayList<>();
        storys.stream()
                .filter(item -> item.getFictionId() != null)
                .forEach(LambadaTools.forEachWithIndex((item, index) -> {
                    List<Object> objectArrayList = new ArrayList<>();
                    objectArrayList.add(index);
                    objectArrayList.add(item.getFictionId());
                    objectArrayList.add(item.getTitle());
                    objectArrayList.add(item.getAuthor());
                    objectArrayList.add(item.getFictionType());
                    objectArrayList.add(item.getLastReadTime());
                    objectArrayList.add(item.getChapter().getTitle());
                    objectArrayList.add(item.getChapter().getChapterId());
                    resultTableData.add(objectArrayList);
                }));
        //可以被编辑的列
        TableModel tableModel = new TableModel(null, titles, resultTableData, null);
        bookshelfTable.setModel(tableModel);
        bookshelfTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        bookshelfTable.repaint();//刷新表数据
        setColumnSize(bookshelfTable, 0, 50, 60, 40);
        bookshelfJscrollpane.setViewportView(bookshelfTable);
    }

    /**
     * 收藏按钮处理
     */
    private void collectButtonHandle() {
        if (collectButton.getText().equals("收 藏")) {
            StoryBookshelfDTO dto = new StoryBookshelfDTO();
            int row = resultTable.getSelectedRow();
            //设置日期格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // new Date()为获取当前系统时间，也可使用当前时间戳
            String t = df.format(new Date());
            dto.setFictionId(fictionIdLast);
            dto.setTitle(resultTable.getValueAt(row, 2).toString());
            dto.setAuthor(resultTable.getValueAt(row, 3).toString());
            dto.setFictionType(resultTable.getValueAt(row, 4).toString());
            dto.setLastReadTime(t);
            DirectoryChapter chapter = (DirectoryChapter) dirlist.getSelectedValue();
            if (chapter == null) {
                chapter = (DirectoryChapter) dirlist.getModel().getElementAt(0);
            }
            dto.setChapter(chapter);
            List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
            storys.add(dto);
            StoryBookshelfSetting.getInstance().setLocalStoryBookshelf(storys);
            collectButton.setText("取消收藏");
        } else {
            List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
            int row = bookshelfTable.getSelectedRow();
            List<StoryBookshelfDTO> dtos = storys.stream().filter(item -> !item.getFictionId().equals(bookshelfTable.getValueAt(row, 1))).collect(Collectors.toList());
            StoryBookshelfSetting.getInstance().setLocalStoryBookshelf(dtos);
            collectButton.setText("收 藏");
        }
        showBookCollected();
    }

    /**
     * 检查图书状态
     */
    private void checkAndUpdateBookCollected() {
        if (fictionIdLast == null) {
            return;
        }
        List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
        AtomicReference<Boolean> flg = new AtomicReference<>(false);
        storys.stream().forEach(item -> {
            if (item.getFictionId().equals(fictionIdLast)) {
                //更新当前选定章节
                DirectoryChapter chapter = (DirectoryChapter) dirlist.getSelectedValue();
                if (chapter != null) {
                    item.setChapter(chapter);
                }
                collectButton.setText("取消收藏");
                flg.set(true);
            }
        });
        if (!flg.get()) {
            collectButton.setText("收 藏");
        }
        collectButton.setVisible(true);
    }

    /**
     * 页面刷新功能
     */
    public Runnable Refresh() {
        return () -> {
            //搜索
            if (tabbedPane.getSelectedIndex() == 0 && StringUtils.isNotBlank(searchWord)) {
                currentPage = 1;
                searchStory();
            }
            //阅读选项卡
            if (tabbedPane.getSelectedIndex() == 1 && StringUtils.isNotBlank(fictionIdLast)) {
                setTextPromptLabel(keepChapterId == null ? resultTable : bookshelfTable, readPromptLabel, "read_init", null, null);
                getStoryDirectory(); //加载目录
            }
            //书架
            if (tabbedPane.getSelectedIndex() == 2) {
                setTextPromptLabel(bookshelfTable, bookshelfPromptLabel, "book_init", null, null);
                showBookCollected();
            }
        };
    }


    /**
     * 搜索展示表格
     */
    public void searchStory() {
        label.setText("搜索中....");
        for (int i = 0; i < comboBoxValues.size(); i++) {
            String finalSelectBoxText = selectBoxText;
            Set<String> collect = comboBoxValues.get(i).entrySet()
                    .stream()
                    .filter(kvEntry -> Objects.equals(kvEntry.getValue(), finalSelectBoxText))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            if (collect != null && collect.size() >= 1) {
                selectBoxText = collect.stream().findFirst().get();
            }
        }
        StoryService storyService = new StoryService();
        LryStoryResultDTO lryStoryResultDTO = storyService.searchStory(selectBoxText, searchWord, currentPage, pageSize);
        if (lryStoryResultDTO.getData() == null || lryStoryResultDTO.getData().size() <= 0) {
            label.setText("未查询到符合条件的数据");
            return;
        } else if (!lryStoryResultDTO.getCode().equals("0")) {
            label.setText(lryStoryResultDTO.getMsg());
            return;
        } else {
            label.setText("点击查看,即可查看详细目录");
            pageLabel.setText("共" + lryStoryResultDTO.getCount() + "条/每页" + pageSize + "条/当前第" + currentPage + "页");
            //计算总页数
            totalPage = lryStoryResultDTO.getCount() % pageSize == 0 ? lryStoryResultDTO.getCount() / pageSize : lryStoryResultDTO.getCount() / pageSize + 1;
        }

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
        TableModel lryStoryTableModel = new TableModel(null, titles, resultTableData, editoredRowAndColumn);
        resultTable.setModel(lryStoryTableModel);
        setColumnSize(resultTable, 0, 50, 60, 40);
        StoryButtonColumn buttonColumn = new StoryButtonColumn(resultTable, resultTableData.get(0).size() - 1, label, tabbedPane);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        resultTable.repaint();//刷新表数据
        //resultTable.setEnabled(false); //表格只能看不能编辑，不能选中一行
        //将表格放到滚动面板上
        resultJscrollpanel.setViewportView(resultTable);
    }

    /**
     * 获取小说目录
     */
    public void getStoryDirectory() {
        String fictionId = null;
        if (keepChapterId == null) { //keepChapterId 说明阅读的不是书架上的书
            int row = resultTable.getSelectedRow();
            if (row <= 0) {
                return;
            }
            fictionId = resultTable.getValueAt(row, 1).toString();
            if (fictionId == null) { //说明当前阅读的和搜索后选择的结果相同，不在调用搜索接口
                return;
            }
        }
        if (fictionId != null && fictionId.equals(fictionIdLast)) {
            return;
        }
        if (fictionId != null) {
            fictionIdLast = fictionId;
        }
        StoryService storyService = new StoryService();
        StoryDirectoryDTO storyDirectory = storyService.getStoryDirectory(fictionIdLast);
        if (storyDirectory == null) {
            setTextPromptLabel(keepChapterId == null ? resultTable : bookshelfTable, readPromptLabel, "dirNull", null, null);
            return;
        } else {
            setTextPromptLabel(keepChapterId == null ? resultTable : bookshelfTable, readPromptLabel, "dirY", storyDirectory.getCount(), null);
        }
        //渲染页面
        StoryDirectoryInfoDTO storyDirectoryData = storyDirectory.getData();
        List<DirectoryChapter> chapterList = storyDirectoryData.getChapterList();
        DefaultListModel dlm = new DefaultListModel<>();
        StoryDirJListRender dirJListRender = new StoryDirJListRender();
        dirlist.setCellRenderer(dirJListRender);
        chapterList.stream().filter(item -> item.getTitle() != "" && item.getChapterId() != "").forEach(item -> {
            dlm.addElement(item);
        });
        if (dlm == null) {
            setTextPromptLabel(keepChapterId == null ? resultTable : bookshelfTable, readPromptLabel, "dirNull", null, null);
        } else {
            dirlist.setModel(dlm);
        }
        dirlist.setFixedCellWidth(100);
        dirlist.setFixedCellHeight(20);
        readDirectory.setViewportView(dirlist);
        dirlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//允许选择模式，只能选择一个
        if (keepChapterId != null) { //继续阅读时定位到章节
            for (int i = 0; i < chapterList.size(); i++) {
                if (chapterList.get(i).getChapterId().equals(keepChapterId)) {
                    dirlist.setSelectedIndex(i);
                }
            }
        } else {
            //加载阅读首页
            showStoryHomeContent(storyDirectory);
        }
    }

    /**
     * 获取章节内容
     */
    public void getStoryDirContent(String chapterId, String title) {
        StoryService storyService = new StoryService();
        StoryContentDTO storyDirContent = storyService.getStoryDirContent(chapterId);
        if (storyDirContent == null) {
            setTextPromptLabel(keepChapterId == null ? resultTable : bookshelfTable, readPromptLabel, "dirNull", null, null);
            return;
        } else {
            setTextPromptLabel(keepChapterId == null ? resultTable : bookshelfTable, readPromptLabel, "dirR", storyDirContent.getCount(), title);
        }
        String strContent = "";
        for (int i = 0; i < storyDirContent.getData().size(); i++) {
            if (i == 0) {
                strContent += "   ";
            }
            strContent += storyDirContent.getData().get(i) + "\n";
        }
        contentPanel.setText(strContent);
        contentPanel.setCaretPosition(0);//保持到顶部
    }

    /**
     * Reading Home
     */
    private void showStoryHomeContent(StoryDirectoryDTO storyDirectory) {
        contentPanel.setText("");
        htmledit = new HTMLEditorKit();
        //实例化一个HTMLEditorkit工具包，用来编辑和解析用来显示在jtextpane中的内容。
        text_html = (HTMLDocument) htmledit.createDefaultDocument();
        //使用HTMLEditorKit类的方法来创建一个文档类，HTMLEditorKit创建的类型默认为htmldocument。
        contentPanel.setEditorKit(htmledit);
        //设置jtextpane组件的编辑器工具包，是其支持html格式。
        contentPanel.setContentType("text/html");
        //设置编辑器要处理的文档内容类型，有text/html,text/rtf.text/plain三种类型。
        contentPanel.setDocument(text_html);
        //设置编辑器关联的一个文档。
        SimpleAttributeSet attr = new SimpleAttributeSet();
        //实例化一个simpleAttributeSet类。
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
        //使用StyleConstants工具类来设置attr属性，这里设置居中属性。
        contentPanel.setParagraphAttributes(attr, false);
        //设置段落属性，第二个参数为false表示不覆盖以前的属性，如果选择true，会覆盖以前的属性。
    /*    Color color = JColorChooser.showDialog(null, "color title", Color.BLACK);
        //使用JColorChooser组件来提供一个颜色选择框。并返回选择的颜色，最后一个参数是缺省颜色。
        StyleConstants.setForeground(attr, color);
        //设置颜色属性，参数为color类型。
        contentPanel.setCharacterAttributes(attr, false);
        //jtextpane的方法，setCharacterAttributes，用来设置选择文本颜色，如果有选择文本，会设置这些文本的颜色属性。
     */
        int row = resultTable.getSelectedRow();
        String img = resultTable.getValueAt(row, 6).toString();
        String html = "<div>" +
//                "<div><img src='https://tse1-mm.cn.bing.net/th/id/OET.f060302a35ed43cf81a2014369bad7c0?w=272&h=272&c=7&rs=1&o=5&pid=1.9'/><div>" +
                "<div><img src='" + img + "'/><div>" +
                "<div>书名：" + storyDirectory.getData().getTitle() + "</div>" +
                "<div>作者：" + storyDirectory.getData().getAuthor() + "<div>" +
                "<div>类型：" + storyDirectory.getData().getFictionType() + "<div>" +
                "<div>最后更新时间：" + storyDirectory.getData().getUpdateTime() + "<div>" +
                "<div><p>  " + storyDirectory.getData().getDescs() + "</p><div>" +
                "</div>";
        try {
            htmledit.insertHTML(text_html, contentPanel.getCaretPosition(),
                    html, 0, 0, HTML.Tag.HTML);
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置阅读卡片中饿的提示内容
     * count 共多少章节
     * currentCount 当前在读第多少章节
     */
    public boolean setTextPromptLabel(JTable table, JLabel readPromptLabel, String type, Integer count, String title) {
        int row = table.getSelectedRow();
        switch (type) {
            case "read_init":
                if (row >= 0) {
                    String name = table.getValueAt(row, 2).toString();
                    readPromptLabel.setText("《" + name + "》目录");
                }
                break;
            case "dirNull":
                if (row >= 0) {
                    readPromptLabel.setText("未查询到《" + table.getValueAt(row, 2).toString() + "》目录,请刷新重试");
                }
                break;
            case "dirY":
                if (row >= 0) {
                    readPromptLabel.setText("《" + table.getValueAt(row, 2).toString() + "》目录,共" + count + "章节");
                }
                break;
            case "dirR":
                if (row >= 0) {
                    readPromptLabel.setText("《" + table.getValueAt(row, 2).toString() + "》目录,共" + count + "章节,当前为:" + title);
                }
                break;
            case "book_init":
                readPromptLabel.setText("我的图书架");
                break;
            case "book_null":
                readPromptLabel.setText("书架空空如也");
                break;
            default:
                readPromptLabel.setText("请选择需要查看目录的图书");
                return false;
        }
        return true;
    }


    /**
     * 设置列表某一列的宽度
     */
    public void setColumnSize(JTable table, int i, int preferedWidth, int maxWidth, int minWidth) {
        //表格的列模型
        TableColumnModel cm = table.getColumnModel();
        //得到第i个列对象
        TableColumn column = cm.getColumn(i);
        column.setPreferredWidth(preferedWidth);
        column.setMaxWidth(maxWidth);
        column.setMinWidth(minWidth);
    }

}
