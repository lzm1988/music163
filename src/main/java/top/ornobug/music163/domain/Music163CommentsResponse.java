package top.ornobug.music163.domain;

import java.util.List;

public class Music163CommentsResponse {

    private String isMusician;
    private String userId;
    private String moreHot;
    private String code;
    private String total;
    private String more;

    private List<Music163Comments> topComments;
    private List<Music163Comments> hotComments;
    private List<Music163Comments> comments;

    public String getIsMusician() {
        return isMusician;
    }

    public void setIsMusician(String isMusician) {
        this.isMusician = isMusician;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoreHot() {
        return moreHot;
    }

    public void setMoreHot(String moreHot) {
        this.moreHot = moreHot;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public List<Music163Comments> getTopComments() {
        return topComments;
    }

    public void setTopComments(List<Music163Comments> topComments) {
        this.topComments = topComments;
    }

    public List<Music163Comments> getHotComments() {
        return hotComments;
    }

    public void setHotComments(List<Music163Comments> hotComments) {
        this.hotComments = hotComments;
    }

    public List<Music163Comments> getComments() {
        return comments;
    }

    public void setComments(List<Music163Comments> comments) {
        this.comments = comments;
    }
}
