package com.github.toolboxplugin.service.webMagic.pipeline;

import com.github.toolboxplugin.infrastructure.story.IReaderDebugData;
import com.github.toolboxplugin.model.DTO.IReaderDebugDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 用于将XXX html页面解析出的数据存储
 **/
@Component("bookChapterPipeline")
public class BookChapterPipeline implements Pipeline {
    private static Logger logger = LogManager.getLogger(BookChapterPipeline.class);

    private IReaderDebugData instance;

    public BookChapterPipeline(IReaderDebugData iReaderDebugDataInstance) {
        this.instance = iReaderDebugDataInstance;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        IReaderDebugDTO dto = resultItems.get("iReaderDebugDTO");
        if (dto == null) {
            return;
        }
        instance.save(new ArrayList<>(Arrays.asList(dto)));
        logger.info("BookChapterPipeline 解析页面得到数据 params={}", dto);
    }
}
