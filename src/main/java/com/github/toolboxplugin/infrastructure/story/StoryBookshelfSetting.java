package com.github.toolboxplugin.infrastructure.story;


import com.github.toolboxplugin.model.DTO.StoryBookshelfDTO;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//https://www.cnblogs.com/kancy/p/10654569.html
@State(name = "StoryBookshelfLocal", storages = @Storage("StoryBookshelfLocal.xml"))  // 存放的文件名
public class StoryBookshelfSetting implements PersistentStateComponent<StoryBookshelfSetting.StoryBookshelfState> {

    private StoryBookshelfState state = new StoryBookshelfState();


    static class StoryBookshelfState {
        //属性必须被public修饰
        public List<StoryBookshelfDTO> storyBookshelfDTOs = new ArrayList<>();

        public List<StoryBookshelfDTO> getStoryBookshelfDTOs() {
            return storyBookshelfDTOs;
        }

        public void setStoryBookshelfDTOs(List<StoryBookshelfDTO> storyBookshelfDTOs) {
            this.storyBookshelfDTOs = storyBookshelfDTOs;
        }
    }

    public static StoryBookshelfSetting getInstance() {
        return ServiceManager.getService(StoryBookshelfSetting.class);
    }

    @Nullable
    @Override
    public StoryBookshelfState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull StoryBookshelfState state) {
        this.state = state;
    }

    public List<StoryBookshelfDTO> getStoryBookshelfById(String fId) {
        List<StoryBookshelfDTO> storyBookshelf = state.getStoryBookshelfDTOs();
        List<StoryBookshelfDTO> collect = storyBookshelf.stream().filter(item -> item.getFictionId().equals(fId)).collect(Collectors.toList());
        return collect;
    }

    public void setLocalStoryBookshelf(List<StoryBookshelfDTO> storys) {
        state.setStoryBookshelfDTOs(storys);
    }

    public List<StoryBookshelfDTO> getLocalStoryBookshelf() {
        List<StoryBookshelfDTO> storyBookshelf = state.getStoryBookshelfDTOs();
        return storyBookshelf;
    }

}
