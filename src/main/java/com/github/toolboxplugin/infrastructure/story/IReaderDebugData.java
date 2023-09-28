package com.github.toolboxplugin.infrastructure.story;


import com.github.toolboxplugin.model.DTO.IReaderDebugDTO;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(name = "IReaderDebugData", storages = @Storage("IReaderDebugData.xml"))  // 存放的文件名
public class IReaderDebugData implements PersistentStateComponent<IReaderDebugData.IReaderDebugState> {

    private IReaderDebugState state = new IReaderDebugState();


    static class IReaderDebugState {
        //属性必须被public修饰
        public List<IReaderDebugDTO> datas = new ArrayList<>();

        public List<IReaderDebugDTO> getDataList() {
            return datas;
        }

        public void saveData(List<IReaderDebugDTO> data) {
            if (CollectionUtils.isEmpty(datas)) {
                this.datas = data;
            } else {
                this.datas = Stream.concat(datas.stream(), data.stream()).collect(Collectors.toList());
            }
        }

        public Boolean delData(IReaderDebugDTO param) {
            try {
                List<IReaderDebugDTO> collect = datas.stream().filter(dto -> !param.getBookFictionId().equals(dto.getBookFictionId())).collect(Collectors.toList());
                this.datas = collect;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static IReaderDebugData getInstance() {
        return ServiceManager.getService(IReaderDebugData.class);
    }

    @Nullable
    @Override
    public IReaderDebugState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull IReaderDebugState state) {
        this.state = state;
    }

    public IReaderDebugDTO getDataById(String fId) {
        List<IReaderDebugDTO> dataList = state.getDataList();
        IReaderDebugDTO r = dataList.stream().filter(item -> item.getBookFictionId().equals(fId)).findFirst().orElse(null);
        return r;
    }

    public void save(List<IReaderDebugDTO> storys) {
        state.saveData(storys);
    }

    public Boolean del(IReaderDebugDTO param) {
        if (param == null) {
            return false;
        }
        Boolean aBoolean = state.delData(param);
        return aBoolean;
    }

    public List<IReaderDebugDTO> getDatas() {
        List<IReaderDebugDTO> d = state.getDataList();
        return d;
    }

}
