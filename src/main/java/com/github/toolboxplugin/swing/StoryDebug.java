package com.github.toolboxplugin.swing;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.github.toolboxplugin.config.GlobalConstant;
import com.github.toolboxplugin.infrastructure.story.IReaderDebugData;
import com.github.toolboxplugin.infrastructure.story.StoryBookRulesData;
import com.github.toolboxplugin.model.DTO.DirectoryChapter;
import com.github.toolboxplugin.model.DTO.IReaderDebugDTO;
import com.github.toolboxplugin.model.DTO.IReaderDebugRuleDTO;
import com.github.toolboxplugin.modules.story.StoryDirJListRender;
import com.github.toolboxplugin.service.webMagic.BookChapterProcessor;
import com.github.toolboxplugin.service.webMagic.BookContentProcessor;
import com.github.toolboxplugin.service.webMagic.pipeline.BookChapterPipeline;
import com.github.toolboxplugin.service.webMagic.pipeline.BookContentPipeline;
import com.github.toolboxplugin.utils.NotificationUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.net.MalformedURLException;
import java.util.List;

public class StoryDebug extends JDialog implements BaseUIAction {
    /**
     * JDialog配置
     * modal           : true,     //是否模态窗口
     * title           : null,     //窗口标题
     * content         : null,     //内容
     * width           : 300,      //对话框默认宽度：300px
     * height          : null,     //自适应
     * minWidth        : 200,      //窗口最小宽度
     * minHeight       : 60,       //窗口最小高度
     * maxWidth        : null,     //窗口最大宽度：默认无限大
     * maxHeight       : null,     //窗口最大高度：默认无限大
     * padding         : '10px',   //内边距，默认10px，可以配置上右下左四个值
     * fixed           : true ,    //是否使用fix属性定位窗口
     * left            : null,     //初始显示位置
     * top             : null,     //初始显示位置
     * closeable       : true,     //是否可关闭
     * hideOnClose     : false,    //关闭时是否只隐藏窗口，而不是删除，可通过show方法再次显示
     * draggable       : true,     //是否可拖拽
     * contentType     : null,     //如果是iframe,请指定url
     * zIndex          : 1024,     //默认z-index为1024
     * resizable       : false,    //是否可以通过拖拽改变大小
     * autoShow        : true,     //是否自动显示
     * autoMiddle      : true,     //窗口大小改变时，保持居中
     * autoClose       : 0,        //自动关闭，单位毫秒，0表示不自动关闭
     * showShadow      : true,     //是否显示阴影
     * showTitle       : true,     //是否显示标题
     * textAlign       : 'inherit',//内容对其方式，默认：inherit
     * buttonAlign     : 'right',  //按钮对齐方式，可选值：left / right / center，默认：right
     * dialogClassName : null,     //对话框的自定义class
     * maskClassName   : null,     //遮罩层的自定义class
     * wobbleEnable    : false,    //模态下，点击空白处，是否允许对话框呈现动画摆动
     * closeOnBodyClick: false,    //点击对话框之外的地方自动关闭
     * buttons         : [],       //对话框中包含的按钮
     * events          : {}        //事件集合，可选项有：show / close / hide / resize / enterKey / escKey
     */
    private JPanel popPane;
    private JButton operationOK;
    private JButton operationTestRule;
    private JPanel popSearchContentPane;
    private JPanel popOperationPane;
    private JTextField chaptersUrlValue;
    private JLabel chaptersUrlText;
    private JLabel chapterTitleRuleText;
    private JPanel popDisplayPane;
    private JButton collectionButton;
    private JButton refreshButton;
    private JPanel displayOperationpane;
    private JScrollPane chaptersJscrollpane;
    private JList chaptersList;
    private JLabel ruleNameText;
    private JTextField ruleNameValue;
    private JLabel bookNameText;
    private JTextField bookNameValue;
    private JTextField bookAuthorValue;
    private JLabel bookAuthorText;
    private JTextField bookTypeValue;
    private JLabel bookTypeText;
    private JLabel bookContentRuleText;
    private JComboBox chaptersRuleTitleBoxValue;
    private JComboBox bookContentRuleBoxValue;
    private JTextArea chaptersTitleRuleValue;
    private JScrollPane chaptersRuleScrollPane;
    private JTextArea bookContentRuleValue;
    private JScrollPane bookContentRuleScrollPane;
    private JComboBox chaptersRuleUrlBoxValue;
    private JTextArea chaptersUrlRuleValue;
    private JLabel chaptersUrlRuleText;
    private JTextPane contentValuePane;

    private Project project;
    private IReaderDebugDTO iReaderDebugDTO;

    public StoryDebug(Project project) {
        this.project = project;
        setContentPane(popPane);
        // 置顶显示
//         setAlwaysOnTop(true);
        // 设为模态 通常单独设置是无效的 还要设置父组件 即弹框出现时，父组件不允许被点击
        // 如果在操作允许的情况 要偷懒的话 可以让弹框置顶显示 点击弹窗外 直接把窗口关闭 这样不需要设置父组件
        setModal(true);
        setTitle("iReader调试模式");
        getRootPane().setDefaultButton(operationOK);
        operationOK.addActionListener(e -> onOK());
        operationTestRule.addActionListener(e -> onTest());
        /**
         * setDefaultCloseOperation 参数：
         * 1. DO_NOTHING_ON_CLOSE：do nothing什么都不做不执行任何操作；要求程序在已注册的WindowListener对象的windowClosing方法中处理该操作，窗口无法关闭。
         * 2. HIDE_ON_CLOSE: 隐藏 调用任意已注册的WindowListener对象后自动隐藏该窗体。未关闭。
         * 3. DISPOSE_ON_CLOSE: dispose销毁释放  调用任意已注册WindowListener的对象后自动隐藏并释放该窗体。
         * 4. EXIT_ON_CLOSE :（在JFrame中定义）：exit退出
         */
        //关闭窗口事件
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        //非模态下 焦点监听 失去后关闭（即点击窗口外 关闭JDialog）
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                //失去焦点
               /* setVisible(false);
                dispose();*/
            }
        });
        // call onCancel() on ESCAPE
        popPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //目录监听事件
        chaptersList.addListSelectionListener(e -> {
            DirectoryChapter selectedValue = (DirectoryChapter) chaptersList.getSelectedValue();
            if (selectedValue == null) {
                return;
            }
            contentValuePane.setText("章节内容加载中。。。");
            //获取选中的目录，获取当前章节的内容
            processBookContent(selectedValue);
        });
    }

    @Override
    public JComponent getComponent() {
        return popPane;
    }

    @Override
    public String getToolWindowId() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader_debug;
    }

    @Override
    public void setBefore() {
        contentValuePane.setCaretPosition(0);
        contentValuePane.setEditable(false);
    }

    @Override
    public void setAfter() {

    }

    /**
     * 保存按钮
     */
    private void onOK() {

    }

    /**
     * 测试按钮
     */
    private void onTest() {
        IReaderDebugDTO dto = processMain();
        if (dto != null) {
            //清除数据
            IReaderDebugData.getInstance().del(dto);
            StoryBookRulesData.getInstance().delStoryBookRule(dto.getiReaderDebugRuleDTO().ruleName);
        }
    }

    /**
     * 取消按钮
     */
    private void onCancel() {
        // add your code here
        dispose();
        this.setVisible(false);
    }

    /***
     * 主业务处理
     * @return
     */
    private IReaderDebugDTO processMain() {
        iReaderDebugDTO = getDtoAndCheck();
        if (iReaderDebugDTO == null) {
            return null;
        }
        IReaderDebugData iReaderDebugDataInstance = IReaderDebugData.getInstance();
        try {
            try {
                //完善图书目录 同步
                Spider.create(new BookChapterProcessor(iReaderDebugDTO))
                        //添加初始的URL
                        .addUrl(iReaderDebugDTO.getBookChapterInfoUrl())
                        //添加一个Pipeline，一个Spider可以有多个Pipeline
                        .addPipeline(new BookChapterPipeline(iReaderDebugDataInstance))
                        // 设置布隆过滤器去重操作（默认使用HashSet来进行去重，占用内存较大；使用BloomFilter来进行去重，占用内存较小，但是可能漏抓页面）
                        .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)))
                        .run();
            } catch (Exception ex) {
                NotificationUtils.setNotification("ToolboxPlugin IReader", "加载章节内容失败,请重试！",
                        NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 3000);
            }
            iReaderDebugDTO = iReaderDebugDataInstance.getDataById(iReaderDebugDTO.getBookFictionId());
            if (iReaderDebugDTO == null || CollectionUtils.isEmpty(iReaderDebugDTO.getChapters())) {
                NotificationUtils.setNotification("ToolboxPlugin IReader", "调试失败,请重试！",
                        NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 3000);
                return null;
            }
            //展示目录
            DefaultListModel dlm = new DefaultListModel<>();
            StoryDirJListRender dirJListRender = new StoryDirJListRender();
            IReaderDebugDTO finalIReaderDebugDTO = iReaderDebugDTO;
            iReaderDebugDTO.getChapters().stream().forEach(item -> {
                //处理章节目录地址前缀
                java.net.URL aURL;
                try {
                    aURL = new java.net.URL(finalIReaderDebugDTO.getBookChapterInfoUrl());
                    item.setChapterPrefixUrl("http://".concat(aURL.getHost()));
                } catch (MalformedURLException e) {
                    NotificationUtils.setNotification("ToolboxPlugin IReader", "获取章节目录地址前缀失败！",
                            NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 3000);
                }
                dlm.addElement(item);
            });
            chaptersList.setModel(dlm);
            //允许选择模式，只能选择一个
            chaptersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            chaptersList.setCellRenderer(dirJListRender);
            // 执行保存逻辑  保存规则到规则栏中
            List<IReaderDebugRuleDTO> rules = StoryBookRulesData.getInstance().getStoryBookRules();
            rules.add(iReaderDebugDTO.getiReaderDebugRuleDTO());
            StoryBookRulesData.getInstance().setStoryBookRules(rules);
            NotificationUtils.setNotification("ToolboxPlugin IReader", "调试内容成功！",
                    NotificationDisplayType.BALLOON, NotificationType.INFORMATION, project, 2000);
        } catch (Exception e) {
            //删除已经保存成功的数据
            IReaderDebugRuleDTO rule = StoryBookRulesData.getInstance().getStoryBookRuleByName(iReaderDebugDTO.getiReaderDebugRuleDTO().getRuleName());
            if (rule != null) {
                StoryBookRulesData.getInstance().delStoryBookRule(rule.ruleName);
            }
            iReaderDebugDataInstance.del(iReaderDebugDTO);
            NotificationUtils.setNotification("ToolboxPlugin IReader", "调试失败,请重试！",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 3000);
        }
        return iReaderDebugDTO;
    }

    /***
     * 章节内容
     * @param chapter
     */
    private void processBookContent(DirectoryChapter chapter) {
        try {
            Spider.create(new BookContentProcessor(iReaderDebugDTO))
                    .addUrl(chapter.getChapterPrefixUrl().concat(chapter.getChapterUrl()))
                    .addPipeline(new BookContentPipeline(iReaderDebugDTO, chapter, contentValuePane))
                    .run();
        } catch (Exception ex) {
            contentValuePane.setText("章节内容加载失败,请重试。。。");
            NotificationUtils.setNotification("ToolboxPlugin IReader", "加载章节内容失败,请重试！",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 3000);
        }
    }

    /**
     * 点击保存/测试按钮的前置校验
     */
    private IReaderDebugDTO getDtoAndCheck() {
        IReaderDebugDTO iReaderDebugDTO = new IReaderDebugDTO();
        if (StringUtils.isEmpty(bookNameValue.getText())) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "请填写图书名",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
            return null;
        } else if (StringUtils.isEmpty(bookAuthorValue.getText())) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "请填写图书作者",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
            return null;
        } else if (StringUtils.isEmpty(chaptersUrlValue.getText())) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "请填写图书章节链接",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
            return null;
        } else if (StringUtils.isEmpty(ruleNameValue.getText())) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "请填写图书名称匹配规则",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
            return null;
        } else if (StringUtils.isEmpty(chaptersTitleRuleValue.getText())) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "请填写章节匹配规则",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
            return null;
        } else if (StringUtils.isEmpty(bookContentRuleValue.getText())) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "请填写正文匹配规则",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
            return null;
        } else if (StringUtils.isEmpty(chaptersUrlRuleValue.getText())) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "请填写图书章节路径匹配规则",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, project, 2000);
            return null;
        }
        //校验字符串是否为正则表达式
        iReaderDebugDTO.setiReaderDebugRuleDTO(new IReaderDebugRuleDTO()
                .setRuleName(ruleNameValue.getText())
                .setChapterTitleRuleInfo(chaptersTitleRuleValue.getText())
                .setChapterUrlRuleInfo(chaptersUrlRuleValue.getText())
                .setContentRuleInfo(bookContentRuleValue.getText())
        )
                .setBookAuthor(bookAuthorValue.getText())
                .setBookFictionId(IdUtil.simpleUUID())
                .setBookFictionType(bookTypeValue.getText())
                .setBookLastReadTime(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"))
                .setBookTitle(bookNameValue.getText())
                .setBookChapterInfoUrl(chaptersUrlValue.getText())
                .setProject(project);
        return iReaderDebugDTO;
    }

}