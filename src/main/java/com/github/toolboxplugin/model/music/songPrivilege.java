package com.github.toolboxplugin.model.music;

import java.util.List;

public class songPrivilege {
    private Boolean toast;
    private String subp;
    private String st;
    private String sp;
    private String rscl;
    private Boolean preSell;
    private String plLevel;
    private String playMaxBrLevel;
    private String playMaxbr;
    private String pl;
    private String payed;
    private String maxBrLevel;
    private String maxbr;
    private String id;
    private freeTrialPrivilegeObj freeTrialPrivilege;
    private String flLevel;
    private String flag;
    private String fl;
    private String fee;
    private String downloadMaxBrLevel;
    private String downloadMaxbr;
    private String dlLevel;
    private String dl;
    private String cs;
    private String cp;
    private List chargeInfoList;

    public Boolean getToast() {
        return toast;
    }

    public void setToast(Boolean toast) {
        this.toast = toast;
    }

    public String getSubp() {
        return subp;
    }

    public void setSubp(String subp) {
        this.subp = subp;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getRscl() {
        return rscl;
    }

    public void setRscl(String rscl) {
        this.rscl = rscl;
    }

    public Boolean getPreSell() {
        return preSell;
    }

    public void setPreSell(Boolean preSell) {
        this.preSell = preSell;
    }

    public String getPlLevel() {
        return plLevel;
    }

    public void setPlLevel(String plLevel) {
        this.plLevel = plLevel;
    }

    public String getPlayMaxBrLevel() {
        return playMaxBrLevel;
    }

    public void setPlayMaxBrLevel(String playMaxBrLevel) {
        this.playMaxBrLevel = playMaxBrLevel;
    }

    public String getPlayMaxbr() {
        return playMaxbr;
    }

    public void setPlayMaxbr(String playMaxbr) {
        this.playMaxbr = playMaxbr;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public String getMaxBrLevel() {
        return maxBrLevel;
    }

    public void setMaxBrLevel(String maxBrLevel) {
        this.maxBrLevel = maxBrLevel;
    }

    public String getMaxbr() {
        return maxbr;
    }

    public void setMaxbr(String maxbr) {
        this.maxbr = maxbr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public freeTrialPrivilegeObj getFreeTrialPrivilege() {
        return freeTrialPrivilege;
    }

    public void setFreeTrialPrivilege(freeTrialPrivilegeObj freeTrialPrivilege) {
        this.freeTrialPrivilege = freeTrialPrivilege;
    }

    public String getFlLevel() {
        return flLevel;
    }

    public void setFlLevel(String flLevel) {
        this.flLevel = flLevel;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDownloadMaxBrLevel() {
        return downloadMaxBrLevel;
    }

    public void setDownloadMaxBrLevel(String downloadMaxBrLevel) {
        this.downloadMaxBrLevel = downloadMaxBrLevel;
    }

    public String getDownloadMaxbr() {
        return downloadMaxbr;
    }

    public void setDownloadMaxbr(String downloadMaxbr) {
        this.downloadMaxbr = downloadMaxbr;
    }

    public String getDlLevel() {
        return dlLevel;
    }

    public void setDlLevel(String dlLevel) {
        this.dlLevel = dlLevel;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public List getChargeInfoList() {
        return chargeInfoList;
    }

    public void setChargeInfoList(List chargeInfoList) {
        this.chargeInfoList = chargeInfoList;
    }
}

class freeTrialPrivilegeObj {
    private Boolean userConsumable;
    private Boolean resConsumable;
    private String listenType;

    public Boolean getUserConsumable() {
        return userConsumable;
    }

    public void setUserConsumable(Boolean userConsumable) {
        this.userConsumable = userConsumable;
    }

    public Boolean getResConsumable() {
        return resConsumable;
    }

    public void setResConsumable(Boolean resConsumable) {
        this.resConsumable = resConsumable;
    }

    public String getListenType() {
        return listenType;
    }

    public void setListenType(String listenType) {
        this.listenType = listenType;
    }
}
