package com.github.toolboxplugin.swing;

import cn.hutool.core.date.DateUtil;
import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.infrastructure.iReader.IReaderDebugData;
import com.github.toolboxplugin.infrastructure.iReader.StoryBookshelfSetting;
import com.github.toolboxplugin.model.DTO.*;
import com.github.toolboxplugin.modules.ComboBoxModels;
import com.github.toolboxplugin.modules.story.IReaderButton;
import com.github.toolboxplugin.modules.story.StoryDirJListRender;
import com.github.toolboxplugin.modules.story.TableModel;
import com.github.toolboxplugin.service.StoryService;
import com.github.toolboxplugin.service.webMagic.BookContentProcessor;
import com.github.toolboxplugin.service.webMagic.pipeline.BookContentPipeline;
import com.github.toolboxplugin.utils.LambadaTools;
import com.github.toolboxplugin.utils.NotificationUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Spider;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.toolboxplugin.swing.CommonFun.setColumnSize;

public class IReader implements BaseUIAction {

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
    //上一页
    private JButton upperPageButton;
    //下一页
    private JButton underPageButton;
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
    private JPanel contentComponent;

    private IReaderButton storyButtonColumn;

    private HTMLDocument text_html;
    private HTMLEditorKit htmledit;

    private String searchWord;
    private String selectBoxText;
    private Integer currentPage;
    private Integer pageSize = 30;
    private Integer totalPage;
    private List<Map<String, Object>> comboBoxValues;
    //继续阅读的章节ID
    private String keepChapterId;
    //当前选择的图书唯一ID
    private String currentFictionId;
    //最后阅读的图书唯一ID
    private String fictionIdLast;
    //上次被选择的面板下标
    private int lastTabbedPaneIndex;

    private Project project;

    public IReader(Integer selectedTabbedPaneIndex, Project project) {
        try {
            this.project = project;
            //初始化操作
            setBefore();
            //搜索按钮点击监听
            searchButton.addActionListener(e -> {
                label.setText("搜索中....");
                searchStory();
                upperPageButton.setVisible(true);
                underPageButton.setVisible(true);
                keepChapterId = null;
            });
            // 鼠标点击监听器
            resultTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 获取哪一行被选中了
                    int row = resultTable.getSelectedRow();
                    //row为-1时说明在搜索中
                    if (row >= 0) {
                        introductionTextArea.setText(resultTable.getValueAt(row, 6).toString());
                        //换行
                        introductionTextArea.setLineWrap(true);
                        //启用自动换行
                        introductionTextArea.setWrapStyleWord(true);
                        introductionTextArea.setEditable(false);
                        label.setText("第" + (row + 1) + "行 " + "《" + resultTable.getValueAt(row, 3).toString() + "》");
                    }
                    //MouseEvent.BUTTON1代表左键，MouseEvent.BUTTON2代表中键，MouseEvent.BUTTON3 代表右键
                    if (e.getButton() == MouseEvent.BUTTON1) {
                    }
                }
            });
            //点击上一页
            upperPageButton.addActionListener(e -> {
                label.setText("搜索中....");
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
            //点击下一页
            underPageButton.addActionListener(e -> {
                label.setText("搜索中....");
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
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex) {
                    case 0:
                        break;
                    case 1: //书架
                        setTextPromptLabel(null, bookshelfPromptLabel, "book_init", null);
                        //读取文件信息展示当前用户收集的图书
                        showBookCollected();
                        break;
                    case 2://阅读选项卡
                        //阅读页收藏/取消收藏按钮逻辑
                        collectTextButtonHandle();
                        //加载目录
                        getStoryDirectory();
                        //查询当前图书是否已经被收藏
                        checkAndUpdateBookCollected();
                        break;
                    default:
                }
                if (selectedIndex != lastTabbedPaneIndex) {
                    lastTabbedPaneIndex = selectedIndex;
                }
            });
            //目录被选中后加装内容监听
            dirlist.addListSelectionListener(e -> {
                DirectoryChapter value = (DirectoryChapter) dirlist.getSelectedValue();
                if (value == null) {//切换了书本，展示首页
                    return;
                } else {
                    getStoryDirContent(value);
                }
                //如果为已经被收藏的图书，则需要更新当前图书的阅读章节
                checkAndUpdateBookCollected();
            });
            //书架表格监听
            bookshelfTable.getSelectionModel().addListSelectionListener(e -> {
                int row = bookshelfTable.getSelectedRow();
                if (row >= 0) {
                    StoryBookshelfDTO storyBookshelfDTO = new StoryBookshelfDTO().setFictionId(bookshelfTable.getValueAt(row, 6).toString())
                            .setTitle(bookshelfTable.getValueAt(row, 1).toString())
                            .setChapter(new DirectoryChapter().setTitle(bookshelfTable.getValueAt(row, 2).toString())
                                    .setChapterId(bookshelfTable.getValueAt(row, 7).toString()));
                    setTextPromptLabel(storyBookshelfDTO, bookshelfPromptLabel, "book_selected", null);
                }
            });
            //阅读页 ：收藏/取消收藏按钮监听
            collectButton.addActionListener(e -> {
                collectButtonHandle();
            });
            //继续阅读按钮监听
            keepReadbutton.addActionListener(e -> {
                keepRead();
            });
            //取消收藏监听
            delCollect.addActionListener(e -> {
                delCollectButtonHandle();
            });
            tabbedPane.setSelectedIndex(selectedTabbedPaneIndex);
        } catch (Exception e) {
            label.setText("运行异常,请刷新重试");
        }
    }

    @Override
    public JComponent getComponent() {
        //tool window 标题设置
        allJpanel.setName("iReader Book");
        return allJpanel;
    }


    @Override
    public String getToolWindowId() {
        //所属窗口ID
        return GlobalConstant.TOOL_WINDOW_ID_iReader;
    }

    @Override
    public void setBefore() {
        //初始化上一页/下一页按钮不显示
        upperPageButton.setVisible(false);
        underPageButton.setVisible(false);

        //初始化：搜索类型
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

        // 搜索类型默认被选择项
        selectedBox.setSelectedIndex(0);
        //阅读框不可以编辑
        contentPanel.setEditable(false);
        //初始化：收藏按钮状态值
        collectButton.setVisible(false);
        keepChapterId = null;
        currentFictionId = null;
        label.setText("操作提示框");
        setTextPromptLabel(null, readPromptLabel, "read_null", null);
    }

    @Override
    public void setAfter() {
    }

    /**
     * 页面刷新功能
     */
    public Runnable Refresh() {
        return () -> {
            //搜索
            if (tabbedPane.getSelectedIndex() == 0 && StringUtils.isNotBlank(searchInput.getText())) {
                searchStory();
            }
            //书架
            if (tabbedPane.getSelectedIndex() == 1) {
                setTextPromptLabel(null, bookshelfPromptLabel, "book_init", null);
                showBookCollected();
            }
            //阅读选项卡
            if (tabbedPane.getSelectedIndex() == 2 && StringUtils.isNotBlank(fictionIdLast)) {
                StoryBookshelfDTO storyBookshelfDTO = new StoryBookshelfDTO();
                if (checkBookCollected(fictionIdLast) == null && resultTable.getSelectedRow() >= 0) {
                    int row = resultTable.getSelectedRow();
                    storyBookshelfDTO.setFictionId(resultTable.getValueAt(row, 2).toString())
                            .setTitle(resultTable.getValueAt(row, 3).toString());
                } else {
                    storyBookshelfDTO = checkBookCollected(fictionIdLast);
                }
                setTextPromptLabel(storyBookshelfDTO, readPromptLabel, "read_init", null);
                getStoryDirectory(); //加载目录
            }
            label.setText("刷新成功");
        };
    }

    /**
     * 书架 继续阅读
     */
    private void keepRead() {
        int row = bookshelfTable.getSelectedRow();
        if (row >= 0) {
            fictionIdLast = bookshelfTable.getValueAt(row, 6).toString();
            keepChapterId = bookshelfTable.getValueAt(row, 7).toString();
            //点击查看阅读，切换到阅读卡片
            tabbedPane.setSelectedIndex(2);
        }
    }

    /**
     * 书架，展示收藏的图书
     */
    private void showBookCollected() {
        List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
        //表头
        List<String> titles = new ArrayList<>(Arrays.asList("序号", "书名", "阅读进度", "作者", "类型", "最后阅读时间", "唯一ID", "章节ID"));
        if (storys == null || storys.size() <= 0) {
            setTextPromptLabel(null, bookshelfPromptLabel, "book_null", null);
            TableModel tableModel = new TableModel(null, titles, new ArrayList<>(), null);
            bookshelfTable.setModel(tableModel);
            bookshelfJscrollpane.setViewportView(bookshelfTable);
            return;
        }
        List<List<Object>> resultTableData = new ArrayList<>();
        storys.stream()
                .filter(item -> item.getFictionId() != null)
                .forEach(LambadaTools.forEachWithIndex((item, index) -> {
                    List<Object> objectArrayList = new ArrayList<>();
                    objectArrayList.add(index + 1);
                    objectArrayList.add(item.getTitle());
                    objectArrayList.add(item.getChapter() == null ? "" : item.getChapter().getTitle());
                    objectArrayList.add(item.getAuthor());
                    objectArrayList.add(item.getFictionType() == null ? "" : item.getFictionType());
                    objectArrayList.add(item.getLastReadTime());
                    objectArrayList.add(item.getFictionId());
                    objectArrayList.add(item.getChapter() == null ? "" : item.getChapter().getChapterId());
                    resultTableData.add(objectArrayList);
                }));
        //可以被编辑的列
        TableModel tableModel = new TableModel(null, titles, resultTableData, null);
        bookshelfTable.setModel(tableModel);
        bookshelfTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookshelfTable.getTableHeader().setReorderingAllowed(false);
        //刷新表数据
        bookshelfTable.repaint();
        setColumnSize(bookshelfTable, 6, 0, 0, 0);
        setColumnSize(bookshelfTable, 7, 0, 0, 0);
        setColumnSize(bookshelfTable, 0, 50, 60, 40);
        bookshelfJscrollpane.setViewportView(bookshelfTable);
    }

    /**
     * 阅读：收藏/取消收藏 按钮处理
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
            dto.setTitle(resultTable.getValueAt(row, 3).toString());
            dto.setAuthor(resultTable.getValueAt(row, 4).toString());
            dto.setFictionType(resultTable.getValueAt(row, 5).toString());
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
            StoryBookshelfDTO dto = storys.stream().filter(item -> item.getFictionId().equals(bookshelfTable.getValueAt(row, 6))).findFirst().orElse(null);
            StoryBookshelfSetting.getInstance().delLocalStoryBookshelf(dto);
            collectButton.setText("收 藏");
        }
        showBookCollected();
    }

    /**
     * 阅读：收藏/取消收藏文本 逻辑
     */
    private void collectTextButtonHandle() {
        collectButton.setVisible(true);
        if (checkBookCollected(fictionIdLast) == null) {
            //说明不在书架上的图书
            collectButton.setText("收 藏");
        } else {
            collectButton.setText("取消收藏");
        }
    }

    /**
     * 取消收藏按钮
     */
    public void delCollectButtonHandle() {
        List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
        int row = bookshelfTable.getSelectedRow();
        StoryBookshelfDTO dto = storys.stream().filter(item -> item.getFictionId().equals(bookshelfTable.getValueAt(row, 6))).findFirst().orElse(null);
        //判断图书来源是否为debug，删除图书对应本地数据
        if (dto.getBookSource().equals("debug")) {
            //清除数据
            Boolean del = IReaderDebugData.getInstance().del(dto.getFictionId());
            if (del) {
                StoryBookshelfSetting.getInstance().delLocalStoryBookshelf(dto);
                NotificationUtils.setNotification("ToolboxPlugin IReader", "图书:" + dto.getTitle() + "删除成功",
                        NotificationDisplayType.STICKY_BALLOON, NotificationType.INFORMATION, project, 2000);
            } else {
                NotificationUtils.setNotification("ToolboxPlugin IReader", "图书:" + dto.getTitle() + "删除失败,请刷新重试",
                        NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 3000);
            }
        } else {
            //非来着debug调试的图书
            StoryBookshelfSetting.getInstance().delLocalStoryBookshelf(dto);
        }
        showBookCollected();
    }

    /**
     * 检查图书状态  更新阅读进度章节
     */
    private void checkAndUpdateBookCollected() {
        if (fictionIdLast == null) {
            return;
        }
        List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
        storys.stream().forEach(item -> {
            if (item.getFictionId().equals(fictionIdLast)) {
                //更新当前选定章节
                DirectoryChapter chapter = (DirectoryChapter) dirlist.getSelectedValue();
                if (chapter != null) {
                    item.setChapter(chapter);
                }
            }
        });
        StoryBookshelfSetting.getInstance().setLocalStoryBookshelf(storys);
    }

    /**
     * 图书搜索逻辑
     * 1. 分页条件搜索
     * 2. 页面表格展示搜索结果
     */
    public void searchStory() {
        searchWord = searchInput.getText();
        selectBoxText = selectedBox.getSelectedItem().toString();
        if (currentPage == null) {
            currentPage = 1;
        }
        //1. 搜索类型判断，默认为box中第一项
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
            if (i == comboBoxValues.size() - 1 && StringUtils.isBlank(finalSelectBoxText)) {
                selectBoxText = comboBoxValues.get(0).entrySet()
                        .stream()
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet()).stream().findFirst().get();
            }
        }
        //上一页 下一页 按钮显示
        upperPageButton.setEnabled(true);
        underPageButton.setEnabled(true);
        //2.发送接口请求第三方接口,解析分析结果
        List<String> titles = new ArrayList<>(Arrays.asList("操作", "序号", "唯一ID", "书名", "作者", "类型", "描述", "最后更新时间", "封面"));
        StoryService storyService = new StoryService();
        LryStoryResultDTO lryStoryResultDTO = storyService.searchStory(selectBoxText, searchWord, currentPage, pageSize);
        if (lryStoryResultDTO.getData() == null || lryStoryResultDTO.getData().size() <= 0) {
            //上一页 下一页 按钮隐藏
            upperPageButton.setEnabled(false);
            underPageButton.setEnabled(false);
            TableModel m = new TableModel(null, titles, new ArrayList<>(), null);
            resultTable.setModel(m);
            label.setText("未查询到符合条件的图书");
            pageLabel.setText("");
            resultJscrollpanel.setViewportView(resultTable);
            return;
        } else if (!lryStoryResultDTO.getCode().equals("0")) {
            label.setText(lryStoryResultDTO.getMsg());
            return;
        } else {
            label.setText("点击阅读,即可查看详细目录");
            pageLabel.setText("共" + lryStoryResultDTO.getCount() + "条/每页" + pageSize + "条/当前第" + currentPage + "页");
            //计算分页：总页数
            totalPage = lryStoryResultDTO.getCount() % pageSize == 0 ? lryStoryResultDTO.getCount() / pageSize : lryStoryResultDTO.getCount() / pageSize + 1;
        }
        //3.将搜索结果进行表格展示
        List<List<Object>> resultTableData = new ArrayList<>();
        List<LryStoryDTO> storyInfo = lryStoryResultDTO.getData();
        storyInfo.stream()
                .filter(item -> item.getFictionId() != null)
                .forEach(LambadaTools.forEachWithIndex((item, index) -> {
                    List<Object> objectArrayList = new ArrayList<>();
                    objectArrayList.add("1");
                    objectArrayList.add(index + 1);
                    objectArrayList.add(item.getFictionId());
                    objectArrayList.add(item.getTitle());
                    objectArrayList.add(item.getAuthor());
                    objectArrayList.add(item.getFictionType());
                    objectArrayList.add(item.getDescs());
                    String format = DateUtil.format(DateUtil.parse(item.getUpdateTime()), "yyyy-MM-dd");
                    objectArrayList.add(format);
                    objectArrayList.add(item.getCover());
                    resultTableData.add(objectArrayList);
                }));
        //设置可以被编辑的列：操作列 参数：列下标
        Integer editoredRowAndColumn[] = {0};
        TableModel lryStoryTableModel = new TableModel(null, titles, resultTableData, editoredRowAndColumn);
        resultTable.setModel(lryStoryTableModel);

        storyButtonColumn = new IReaderButton(resultTable, 0, "阅读") {
            //搜索结果阅读按钮监听
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = resultTable.getSelectedRow();
                if (row >= 0) {
                    //点击查看详情获取当前数据的唯一ID
                    label.setText("正在阅读:《" + resultTable.getValueAt(row, 3).toString() + "》目录");
                    //点击查看阅读，切换到阅读卡片
                    fictionIdLast = resultTable.getValueAt(row, 2).toString();
                    tabbedPane.setSelectedIndex(2);
                }
            }
        };
        resultTable.getColumnModel().getColumn(0).setCellRenderer(storyButtonColumn);
        /* JTable 表格选择模式
         *       1. SINGLE_INTERVAL_SELECTION 一次选择连续多行
         *       2. MULTIPLE_INTERVAL_SELECTION 一次选择任意多行
         *       2. SINGLE_SELECTION 一次选择一行
         * */
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //禁止鼠标拖动列
        resultTable.getTableHeader().setReorderingAllowed(false);
        //刷新表数据
        resultTable.repaint();
        //设置JTable列宽
        setColumnSize(resultTable, 0, 100, 100, 100);
        setColumnSize(resultTable, 1, 50, 60, 40);
        setColumnSize(resultTable, 7, 150, 180, 150);
        //隐藏列  设置隐藏列为0即可
        setColumnSize(resultTable, 2, 0, 0, 0);
        setColumnSize(resultTable, 8, 0, 0, 0);
        //表格只能看 不能编辑，不能选中
        //resultTable.setEnabled(false);
        //将表格放到滚动面板上
        resultJscrollpanel.setViewportView(resultTable);
    }

    /**
     * 获取图书目录
     */
    public void getStoryDirectory() {
        try {
            StoryBookshelfDTO dto = checkBookCollected(fictionIdLast);
            if (dto == null) {
                //keepChapterId 说明阅读的不是书架上的书
                int row = resultTable.getSelectedRow();
                if (row < 0) {
                    return;
                }
                currentFictionId = resultTable.getValueAt(row, 2).toString();
                if (currentFictionId == null) {
                    //说明当前阅读的和搜索后选择的结果相同，不在调用搜索接口
                    return;
                }
            } else {
                //是书架上的书相当于执行继续阅读逻辑
                keepChapterId = dto.getChapter().getChapterId();
            }
            if (fictionIdLast == null) {
                fictionIdLast = currentFictionId;
            }
            StoryBookshelfDTO storyBookshelfDTO = new StoryBookshelfDTO();
            if (checkBookCollected(fictionIdLast) == null && resultTable.getSelectedRow() >= 0) {
                //"操作", "序号", "唯一ID", "书名", "作者", "类型", "描述", "最后更新时间", "封面"
                int row = resultTable.getSelectedRow();
                storyBookshelfDTO.setFictionId(resultTable.getValueAt(row, 2).toString())
                        .setTitle(resultTable.getValueAt(row, 3).toString());
            } else {
                storyBookshelfDTO = checkBookCollected(fictionIdLast);
            }
            setTextPromptLabel(storyBookshelfDTO, readPromptLabel, "read_init", null);
            //debug调试下的图书 加载目录
            List<DirectoryChapter> chapterList = new ArrayList<>();
            StoryDirectoryDTO storyDirectory = new StoryDirectoryDTO();
            if (StringUtils.isNotBlank(storyBookshelfDTO.getBookSource()) && storyBookshelfDTO.getBookSource().equals("debug")) {
                //本地获取目录信息
                IReaderDebugDTO data = IReaderDebugData.getInstance().getDataById(fictionIdLast);
                chapterList = data.getChapters();
                storyDirectory.setData(new StoryDirectoryInfoDTO()
                        .setAuthor(data.getBookAuthor())
                        .setFictionId(data.getBookFictionId())
                        .setChapterList(data.getChapters())
                        .setDescs("")
                        .setFictionType(data.getBookFictionType())
                        .setUpdateTime(data.getBookLastReadTime())
                );
            } else {
                StoryService storyService = new StoryService();
                storyDirectory = storyService.getStoryDirectory(fictionIdLast);
                if (storyDirectory == null) {
                    setTextPromptLabel(storyBookshelfDTO, readPromptLabel, "dirNull", null);
                    return;
                } else {
                    setTextPromptLabel(storyBookshelfDTO, readPromptLabel, "dirY", storyDirectory.getCount());
                }
                StoryDirectoryInfoDTO storyDirectoryData = storyDirectory.getData();
                chapterList = storyDirectoryData.getChapterList();
            }
            DefaultListModel dlm = new DefaultListModel<>();
            StoryDirJListRender dirJListRender = new StoryDirJListRender();
            dirlist.setCellRenderer(dirJListRender);
            chapterList.stream().filter(item -> item.getTitle() != "" && item.getChapterId() != "").forEach(item -> {
                dlm.addElement(item);
            });
            if (dlm == null) {
                setTextPromptLabel(storyBookshelfDTO, readPromptLabel, "dirNull", null);
            } else {
                dirlist.setModel(dlm);
            }
            dirlist.setFixedCellWidth(160);
            dirlist.setFixedCellHeight(20);
            readDirectory.setViewportView(dirlist);
            //允许选择模式，只能选择一个
            dirlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if (checkBookCollected(fictionIdLast) != null && keepChapterId != null) {
                //已经存在书架上的书，继续阅读时定位到记录的章节
                for (int i = 0; i < chapterList.size(); i++) {
                    if (chapterList.get(i).getChapterId().equals(keepChapterId)) {
                        dirlist.setSelectedIndex(i);
                    }
                }
            } else {
                //加载阅读首页
                showStoryHomeContent(storyDirectory);
            }
        } catch (Exception e) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "图书目录加载失败,请刷新重试！",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
        }
    }

    /**
     * 获取章节内容
     */
    public void getStoryDirContent(DirectoryChapter chapter) {
        StoryBookshelfDTO bookshelf = StoryBookshelfSetting.getInstance().getStoryBookshelfById(fictionIdLast);
        if (bookshelf != null && StringUtils.isNotBlank(bookshelf.getBookSource()) && bookshelf.getBookSource().equals("debug")) {
            try {
                IReaderDebugDTO iReaderDebugDTO = IReaderDebugData.getInstance().getDataById(bookshelf.fictionId);
                String url = chapter.getChapterPrefixUrl().concat(chapter.getChapterUrl());
                Spider.create(new BookContentProcessor(iReaderDebugDTO, project))
                        .addUrl(url)
                        .addPipeline(new BookContentPipeline(iReaderDebugDTO, chapter, contentPanel, project))
                        .run();
            } catch (Exception ex) {
                contentPanel.setText("章节内容加载失败,请重试。。。\n" + chapter.getChapterPrefixUrl().concat(chapter.getChapterUrl()));
                NotificationUtils.setNotification("ToolboxPlugin IReader", "加载章节内容失败,请重试！",
                        NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 3000);
            }
        } else {
            StoryService storyService = new StoryService();
            StoryContentDTO storyDirContent = storyService.getStoryDirContent(chapter.getChapterId());
            StoryBookshelfDTO storyBookshelfDTO = new StoryBookshelfDTO();
            //获取目录，书架/搜索
            StoryBookshelfDTO dto = checkBookCollected(fictionIdLast);
            int row = resultTable.getSelectedRow();
            if (dto == null && row >= 0) {
                storyBookshelfDTO.setFictionId(resultTable.getValueAt(row, 2).toString())
                        .setTitle(resultTable.getValueAt(row, 3).toString())
                        .setChapter(new DirectoryChapter().setChapterId(chapter.getChapterId()).setTitle(chapter.getTitle()));
            } else {
                storyBookshelfDTO = dto;
            }
            if (storyDirContent == null) {
                setTextPromptLabel(storyBookshelfDTO, readPromptLabel, "dirNull", null);
                return;
            } else {
                setTextPromptLabel(storyBookshelfDTO, readPromptLabel, "dirR", dirlist.getModel().getSize());
            }
            htmledit = new HTMLEditorKit();
            //实例化一个HTMLEditorkit工具包，用来编辑和解析用来显示在jtextpane中的内容。
            text_html = (HTMLDocument) htmledit.createDefaultDocument();
            //使用HTMLEditorKit类的方法来创建一个文档类，HTMLEditorKit创建的类型默认为htmldocument。
            contentPanel.setEditorKit(htmledit);
            //设置jtextpane组件的编辑器工具包，是其支持html格式。
            contentPanel.setContentType("text/html");
            //设置编辑器要处理的文档内容类型，有text/html,text/rtf.text/plain三种类型。
            contentPanel.setDocument(text_html);
            String html = "";
            for (int i = 0; i < storyDirContent.getData().size(); i++) {
                if (i == 0) {
                    html += "<div>";
                    html += "<h3>" + chapter.getTitle() + "</h3><br/>";
                }
                html += "<p>" + storyDirContent.getData().get(i) + "</p><br/>";
                if (i == storyDirContent.getData().size() - 1) {
                    html += "</div>";
                }
            }
            try {
                htmledit.insertHTML(text_html, contentPanel.getCaretPosition(),
                        html, 0, 0, HTML.Tag.HTML);
            } catch (BadLocationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //检查图书是否存在与书架，更新阅读章节
        checkAndUpdateBookCollected();
        //保持到顶部
        contentPanel.setCaretPosition(0);
    }

    /**
     * 阅读，图书首页展示
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
        String img = resultTable.getValueAt(resultTable.getSelectedRow(), 8).toString();
        String html = "<div>" +
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
     * 设置阅读卡片中的提示内容
     * count 共多少章节
     * currentCount 当前在读第多少章节
     */
    public boolean setTextPromptLabel(StoryBookshelfDTO data, JLabel label, String type, Integer count) {
        switch (type) {
            case "read_init":
                if (data != null) {
                    label.setText("《" + data.getTitle() + "》目录");
                }
                break;
            case "read_null":
                label.setText("请选择需要阅读的图书");
                break;
            case "dirNull":
                if (data != null) {
                    label.setText("未查询到《" + data.getTitle() + "》目录,请刷新重试");
                }
                break;
            case "dirY":
                if (data != null) {
                    label.setText("《" + data.getTitle() + "》目录,共" + count + "章节");
                }
                break;
            case "dirR":
                if (data != null) {
                    label.setText("《" + data.getTitle() + "》目录,共 " + count + " 章节,当前为: " + data.chapter.getTitle());
                }
                break;
            case "book_init":
                label.setText("我的书架");
                break;
            case "book_null":
                label.setText("书架空空如也");
                break;
            case "book_selected":
                if (data != null) {
                    label.setText("《" + data.getTitle() + "》 阅读进度: [ " + data.chapter.getTitle() + " ]");
                }
                break;
            default:
                label.setText("请选择需要查看目录的图书");
                return false;
        }
        return true;
    }

    /**
     * 设置列表某一列的宽度
     */
    public StoryBookshelfDTO checkBookCollected(String fId) {
        List<StoryBookshelfDTO> storys = StoryBookshelfSetting.getInstance().getLocalStoryBookshelf();
        if (storys.stream().filter(dto -> dto.getFictionId().equals(fId)).findAny().isPresent()) {
            return storys.stream().filter(dto -> dto.getFictionId().equals(fId)).findAny().get();
        }
        return null;
    }

}
