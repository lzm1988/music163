package top.ornobug.music163.domain;

import java.util.List;

public class Music163PlayList {

    //private Music163UserProfile creator;
    // 歌单下的歌的列表
    private List<Music163PlayListTrack> tracks;
    // 用户id
    private String userId;
    // 歌单id
    private String id;

    public List<Music163PlayListTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<Music163PlayListTrack> tracks) {
        this.tracks = tracks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
