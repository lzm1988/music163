package top.ornobug.music163.domain;

import java.util.List;

public class Music163CommentsUser {

    private String locationInfo;
    private String authStatus;
    private String remarkName;
    private String avatarUrl;
    //private String experts;
    private String vipType;
    private String userId;
    private String nickname;
    private String userType;
    private List<String> expertTags;

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    //public String getExperts() {
    //    return experts;
    //}
    //
    //public void setExperts(String experts) {
    //    this.experts = experts;
    //}

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getExpertTags() {
        return expertTags;
    }

    public void setExpertTags(List<String> expertTags) {
        this.expertTags = expertTags;
    }
}
