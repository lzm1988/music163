package top.ornobug.music163.domain;

import java.util.List;

public class Music163PlayListListResponse {

    private String more;
    private String code;
    private List<Music163PlayList> playlist;

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Music163PlayList> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Music163PlayList> playlist) {
        this.playlist = playlist;
    }
}
