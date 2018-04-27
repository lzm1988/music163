package top.ornobug.music163.service.impl;

import org.springframework.stereotype.Service;
import top.ornobug.music163.dao.Music163CommentsDao;
import top.ornobug.music163.domain.Music163Comments;
import top.ornobug.music163.service.Music163CommentsService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service(value = "music163CommentsService")
public class Music163CommentsServiceImpl implements Music163CommentsService {

    @Resource
    private Music163CommentsDao music163CommentsDao;

    public int insert(Music163Comments pojo){
        return music163CommentsDao.insert(pojo);
    }

    public int insertList(List< Music163Comments> pojos){
        return music163CommentsDao.insertList(pojos);
    }

    public List<Music163Comments> select(Map<String, Object> param){
        return music163CommentsDao.select(param);
    }

    public int update(Music163Comments pojo){
        return music163CommentsDao.update(pojo);
    }

}
