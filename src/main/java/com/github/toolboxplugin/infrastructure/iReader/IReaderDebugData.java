package com.github.toolboxplugin.infrastructure.iReader;


import com.github.toolboxplugin.model.DTO.IReaderDebugDTO;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(name = "IReaderDebugData", storages = @Storage("IReaderDebugData.xml"))  // 存放的文件名
public class IReaderDebugData implements PersistentStateComponent<IReaderDebugData.IReaderDebugState> {

    private IReaderDebugState state = new IReaderDebugState();


    static class IReaderDebugState {
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

        public Boolean delData(String param) {
            try {
                List<IReaderDebugDTO> collect = datas.stream().filter(dto -> !param.equals(dto.getBookFictionId())).collect(Collectors.toList());
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

    public Boolean del(String bookId) {
        if (StringUtil.isBlank(bookId)) {
            return false;
        }
        Boolean aBoolean = state.delData(bookId);
        return aBoolean;
    }

    public List<IReaderDebugDTO> getDatas() {
        List<IReaderDebugDTO> d = state.getDataList();
        return d;
    }

}
