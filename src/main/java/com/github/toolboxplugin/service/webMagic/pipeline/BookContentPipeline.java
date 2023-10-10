package com.github.toolboxplugin.service.webMagic.pipeline;

import com.github.toolboxplugin.model.DTO.DirectoryChapter;
import com.github.toolboxplugin.model.DTO.IReaderDebugDTO;
import com.github.toolboxplugin.utils.NotificationUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.util.List;

/**
 * 用于将XXX html页面解析出的数据存储
 **/
@Component("bookContentPipeline")
public class BookContentPipeline implements Pipeline {

    private IReaderDebugDTO iReaderDebugDTO;
    private DirectoryChapter chapter;
    private JTextPane contentPane;

    public BookContentPipeline(IReaderDebugDTO iReaderDebugDTO, DirectoryChapter chapter, JTextPane contentPane) {
        this.iReaderDebugDTO = iReaderDebugDTO;
        this.chapter = chapter;
        this.contentPane = contentPane;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> content = resultItems.get("bookContent");
        if (CollectionUtils.isEmpty(content)) {
            contentPane.setText("未加载到章节内容,请刷新后重试");
            return;
        }
        //阅读框不可以编辑
        contentPane.setEditable(false);

        HTMLEditorKit htmledit = new HTMLEditorKit();
        //实例化一个HTMLEditorkit工具包，用来编辑和解析用来显示在jtextpane中的内容。
        HTMLDocument text_html = (HTMLDocument) htmledit.createDefaultDocument();
        //使用HTMLEditorKit类的方法来创建一个文档类，HTMLEditorKit创建的类型默认为htmldocument。
        contentPane.setEditorKit(htmledit);
        //设置jtextpane组件的编辑器工具包，是其支持html格式。
        contentPane.setContentType("text/html");
        //设置编辑器要处理的文档内容类型，有text/html,text/rtf.text/plain三种类型。
        contentPane.setDocument(text_html);
        //设置编辑器关联的一个文档。
        SimpleAttributeSet attr = new SimpleAttributeSet();
        //实例化一个simpleAttributeSet类。
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
        //使用StyleConstants工具类来设置attr属性，这里设置居中属性。
        contentPane.setParagraphAttributes(attr, false);
        String html = "";
        for (int i = 0; i < content.size(); i++) {
            if (i == 0) {
                html += "<div>";
                html += "<h3>" + chapter.getTitle() + "</h3>";
            }
            html += "<p>" + content.get(i) + "</p>";
            if (i == content.size() - 1) {
                html += "</div>";
            }
        }
        try {
            htmledit.insertHTML(text_html, contentPane.getCaretPosition(),
                    html, 0, 0, HTML.Tag.HTML);
        } catch (BadLocationException e) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "章节内容加载失败请重试！",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, iReaderDebugDTO.getProject(), 3000);
        } catch (IOException e) {
            NotificationUtils.setNotification("ToolboxPlugin IReader", "章节内容加载失败请重试！",
                    NotificationDisplayType.STICKY_BALLOON, NotificationType.ERROR, iReaderDebugDTO.getProject(), 3000);
        }

    }
}
