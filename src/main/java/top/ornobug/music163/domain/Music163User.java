package top.ornobug.music163.domain;

import java.util.List;

public class Music163User {

    private String level;
    private String listenSongs;
    private String mobileSign;
    private String pcSign;
    private String peopleCanSeeMyPlayRecord;
    private String adValid;
    private String code;
    private String createTime;
    private String createDays;

    private Music163UserProfile profile;

    private List<Music163UserBindings> bindings;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getListenSongs() {
        return listenSongs;
    }

    public void setListenSongs(String listenSongs) {
        this.listenSongs = listenSongs;
    }

    public String getMobileSign() {
        return mobileSign;
    }

    public void setMobileSign(String mobileSign) {
        this.mobileSign = mobileSign;
    }

    public String getPcSign() {
        return pcSign;
    }

    public void setPcSign(String pcSign) {
        this.pcSign = pcSign;
    }

    public String getPeopleCanSeeMyPlayRecord() {
        return peopleCanSeeMyPlayRecord;
    }

    public void setPeopleCanSeeMyPlayRecord(String peopleCanSeeMyPlayRecord) {
        this.peopleCanSeeMyPlayRecord = peopleCanSeeMyPlayRecord;
    }

    public String getAdValid() {
        return adValid;
    }

    public void setAdValid(String adValid) {
        this.adValid = adValid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDays() {
        return createDays;
    }

    public void setCreateDays(String createDays) {
        this.createDays = createDays;
    }

    public Music163UserProfile getProfile() {
        return profile;
    }

    public void setProfile(Music163UserProfile profile) {
        this.profile = profile;
    }

    public List<Music163UserBindings> getBindings() {
        return bindings;
    }

    public void setBindings(List<Music163UserBindings> bindings) {
        this.bindings = bindings;
    }
}
