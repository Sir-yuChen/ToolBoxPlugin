package com.github.toolboxplugin.model.DTO;

/**
 * @param $
 * @author zhangyu
 * @description $
 * @date $ $
 * @return $
 * @since
 **/
public class IReaderDebugDTO {
    public StoryBookshelfDTO bookshelfDTO;
    public IReaderDebugRuleDTO iReaderDebugRuleDTO;

    public StoryBookshelfDTO getBookshelfDTO() {
        return bookshelfDTO;
    }

    public IReaderDebugDTO setBookshelfDTO(StoryBookshelfDTO bookshelfDTO) {
        this.bookshelfDTO = bookshelfDTO;
        return this;
    }

    public IReaderDebugRuleDTO getiReaderDebugRuleDTO() {
        return iReaderDebugRuleDTO;
    }

    public IReaderDebugDTO setiReaderDebugRuleDTO(IReaderDebugRuleDTO iReaderDebugRuleDTO) {
        this.iReaderDebugRuleDTO = iReaderDebugRuleDTO;
        return this;
    }
}
