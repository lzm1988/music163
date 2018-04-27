package top.ornobug.music163.service;

import top.ornobug.music163.domain.Music163Comments;

import java.util.List;
import java.util.Map;

public interface Music163CommentsService {

    int insert(Music163Comments pojo);

    int insertList(List<Music163Comments> pojos);

    List<Music163Comments> select(Map<String, Object> param);

    int update(Music163Comments pojo);

}
