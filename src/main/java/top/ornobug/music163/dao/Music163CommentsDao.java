package top.ornobug.music163.dao;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import top.ornobug.music163.domain.Music163Comments;

@Repository
public interface Music163CommentsDao {

    int insert(@Param("pojo") Music163Comments pojo);

    int insertList(@Param("pojos") List< Music163Comments> pojo);

    List<Music163Comments> select(@Param("pojo")Map<String, Object> param);

    int update(@Param("pojo") Music163Comments pojo);

}
