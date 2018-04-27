package top.ornobug.music163.thread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import top.ornobug.music163.domain.Music163Comments;
import top.ornobug.music163.domain.Music163CommentsResponse;
import top.ornobug.music163.domain.Music163CommentsUser;
import top.ornobug.music163.service.Music163CommentsService;
import top.ornobug.music163.util.HttpUtil;
import top.ornobug.music163.util.SpringUtil;
import top.ornobug.music163.util.UnicodeUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SpiderMusic163HotCommentsThread implements Callable<String> {

    private String songId;

    private static final Gson GSON = new Gson();

    private Music163CommentsService music163CommentsService = (Music163CommentsService) SpringUtil.getBean("music163CommentsService");

    public void setSongId(String songId) {
        this.songId = songId;
    }

    @Override
    public String call() throws Exception {

        String proxy = HttpUtil.getProxy();
        String songPrefix = "http://www.ornobug.top:4000/comment/music?id=";
        String songUrl = songPrefix +songId + "&proxy=http://" + proxy;
        String commentsJson = HttpUtil.sendGet(songUrl);

        Type listType = new TypeToken<ArrayList<Music163CommentsResponse>>() {
        }.getType();
        Music163CommentsResponse commentResponse = GSON.fromJson(commentsJson, Music163CommentsResponse.class);

        int count = 0;
        if (commentResponse != null) {
            List<Music163Comments> commentsList = commentResponse.getHotComments();
            if (commentsList != null && commentsList.size() > 0) {


                for (Music163Comments comments : commentsList) {
                    Music163CommentsUser commentsUser = comments.getUser();
                    String content = UnicodeUtil.filterEmoji(comments.getContent());
                    comments.setContent(content);
                    comments.setCommentUserId(commentsUser.getUserId());
                    comments.setSongId(songId);
                }

                count = count + music163CommentsService.insertList(commentsList);
            }
        }
        return String.valueOf(count);
    }
}
