package com.github.toolboxplugin.model.music;

import java.util.List;

public class PlayLisitsDto {
    private String cat;
    private Boolean more;
    private String code;
    private Integer total;
    private List<SongListDto> playlists;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<SongListDto> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<SongListDto> playlists) {
        this.playlists = playlists;
    }
}
