package com.github.toolboxplugin.swing;

import com.github.toolboxplugin.config.GlobalConstant;
import com.intellij.notification.*;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StoryDebug extends JDialog implements BaseUIAction {
    private JPanel popPane;
    private JButton operationOK;
    private JButton operationTestRule;
    private JPanel popSearchContentPane;
    private JPanel popOperationPane;
    private JTextField chaptersUrlValue;
    private JLabel chaptersUrlText;
    private JLabel chaptersRuleText;
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
    private JComboBox chaptersRuleBoxValue;
    private JComboBox bookContentRuleBoxValue;
    private JTextArea chaptersRuleValue;
    private JScrollPane chaptersRuleScrollPane;
    private JTextArea bookContentRuleValue;
    private JScrollPane bookContentRuleScrollPane;


    public StoryDebug() {
        setContentPane(popPane);
        setModal(true);
        getRootPane().setDefaultButton(operationOK);

        operationOK.addActionListener(e -> onOK());

        operationTestRule.addActionListener(e -> System.out.println("测试 "));

        /*
         * setDefaultCloseOperation 参数：
         * 1. DO_NOTHING_ON_CLOSE：do nothing什么都不做不执行任何操作；要求程序在已注册的WindowListener对象的windowClosing方法中处理该操作，窗口无法关闭。
         * 2. HIDE_ON_CLOSE: 隐藏 调用任意已注册的WindowListener对象后自动隐藏该窗体。未关闭。
         * 3. DISPOSE_ON_CLOSE: dispose销毁释放  调用任意已注册WindowListener的对象后自动隐藏并释放该窗体。
         * 4. EXIT_ON_CLOSE :（在JFrame中定义）：exit退出
         * */
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        popPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public JComponent getComponent() {
        popPane.setName("iReader调试模式");
        return popPane;
    }

    @Override
    public String getToolWindowId() {
        return GlobalConstant.TOOL_WINDOW_ID_iReader_debug;
    }

    @Override
    public void setBefore() {

    }

    @Override
    public void setAfter() {

    }

    /**
     * 保存按钮
     */
    private void onOK() {
        //点击保存/测试按钮的前置校验
        Boolean paramsCheck = getParamsCheck();
        // add your code here
        if (paramsCheck) {
            dispose();
        }
    }

    /**
     * 取消按钮
     */
    private void onCancel() {
        // add your code here
        dispose();
    }

    /**
     * 点击保存/测试按钮的前置校验
     */
    private Boolean getParamsCheck() {
        //TODO-zy 提示弹窗无法弹出
        if (StringUtils.isEmpty(bookNameValue.getText())) {
           // ToolBoxPlugin_iReader'
            NotificationGroup notificationGroup = new NotificationGroup("notificationGroup", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("图书名称为必填项", NotificationType.ERROR);
            Notifications.Bus.notify(notification);
            return false;
        }
        return true;
    }


}
