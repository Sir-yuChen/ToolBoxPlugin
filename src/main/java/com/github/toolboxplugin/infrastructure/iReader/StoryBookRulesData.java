package com.github.toolboxplugin.infrastructure.iReader;


import com.github.toolboxplugin.model.DTO.IReaderDebugRuleDTO;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//https://www.cnblogs.com/kancy/p/10654569.html
@State(name = "StoryBookRulesData", storages = @Storage("StoryBookRulesData.xml"))  // 存放的文件名
public class StoryBookRulesData implements PersistentStateComponent<StoryBookRulesData.StoryBookRuleState> {

    private StoryBookRuleState state = new StoryBookRuleState();


    static class StoryBookRuleState {
        //属性必须被public修饰
        public List<IReaderDebugRuleDTO> rules = new ArrayList<>();

        public List<IReaderDebugRuleDTO> getStoryBookRuleData() {
            return rules;
        }

        public void setStoryBookRuleData(List<IReaderDebugRuleDTO> dtos) {
            this.rules = dtos;
        }

        public Boolean delStoryBookRule(String ruleName) {
            try {
                List<IReaderDebugRuleDTO> collect = rules.stream().filter(dto -> !ruleName.equals(dto.getRuleName())).collect(Collectors.toList());
                this.rules = collect;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static StoryBookRulesData getInstance() {
        return ApplicationManager.getApplication().getService(StoryBookRulesData.class);
    }

    @Nullable
    @Override
    public StoryBookRuleState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull StoryBookRuleState state) {
        this.state = state;
    }

    public IReaderDebugRuleDTO getStoryBookRuleByName(String ruleName) {
        List<IReaderDebugRuleDTO> ruleDTOList = state.getStoryBookRuleData();
        if (ruleDTOList == null) {
            return null;
        }
        //java.util.NoSuchElementException: No value present
        IReaderDebugRuleDTO iReaderDebugRuleDTO = ruleDTOList.stream().filter(item -> item.getRuleName().equals(ruleName)).findFirst().orElse(null);
        return iReaderDebugRuleDTO;
    }

    public void setStoryBookRules(List<IReaderDebugRuleDTO> ruleList) {
        state.setStoryBookRuleData(ruleList);
    }

    public Boolean delStoryBookRule(String ruleName) {
        if (StringUtils.isEmpty(ruleName)) {
            return false;
        }
        Boolean aBoolean = state.delStoryBookRule(ruleName);
        return aBoolean;
    }

    public List<IReaderDebugRuleDTO> getStoryBookRules() {
        List<IReaderDebugRuleDTO> r = state.getStoryBookRuleData();
        return r;
    }

}
