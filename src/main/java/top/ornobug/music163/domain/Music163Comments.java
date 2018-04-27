package top.ornobug.music163.domain;

public class Music163Comments {

    private Long id;
    private String pendantData;
    private String likedCount;
    private String commentId;
    private String liked;
    private String time;
    private String content;
    private String commentUserId;
    private String songId;

    private Music163CommentsUser user;

    public String getPendantData() {
        return pendantData;
    }

    public void setPendantData(String pendantData) {
        this.pendantData = pendantData;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(String likedCount) {
        this.likedCount = likedCount;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Music163CommentsUser getUser() {
        return user;
    }

    public void setUser(Music163CommentsUser user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
