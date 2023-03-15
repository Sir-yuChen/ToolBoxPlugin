package com.github.toolboxplugin.model.music;

import java.util.List;

public class songAttribute {
    private String type;
    private String trackNumberUpdateTime;
    private List<song> song;
    private String picUrl;
    private String name;
    private String id;
    private String copywriter;
    private String canDislike;
    private String alg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrackNumberUpdateTime() {
        return trackNumberUpdateTime;
    }

    public void setTrackNumberUpdateTime(String trackNumberUpdateTime) {
        this.trackNumberUpdateTime = trackNumberUpdateTime;
    }

    public List getSong() {
        return song;
    }

    public void setSong(List song) {
        this.song = song;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCopywriter() {
        return copywriter;
    }

    public void setCopywriter(String copywriter) {
        this.copywriter = copywriter;
    }

    public String getCanDislike() {
        return canDislike;
    }

    public void setCanDislike(String canDislike) {
        this.canDislike = canDislike;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }
}
