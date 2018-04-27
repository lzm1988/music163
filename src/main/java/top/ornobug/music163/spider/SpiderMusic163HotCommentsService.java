package top.ornobug.music163.spider;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.ornobug.music163.domain.Music163PlayList;
import top.ornobug.music163.domain.Music163PlayListListResponse;
import top.ornobug.music163.domain.Music163PlayListResponse;
import top.ornobug.music163.domain.Music163PlayListTrack;
import top.ornobug.music163.thread.SpiderMusic163HotCommentsThread;
import top.ornobug.music163.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class SpiderMusic163HotCommentsService {

    private ExecutorService executorService = null;

    private static final Gson GSON = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderMusic163HotCommentsService.class);

    private void init(int threadNum) {
        executorService = Executors.newFixedThreadPool(threadNum);
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void start() {
        init(5);

        // 先根据用户接口获得下边的歌单列表
        String proxy = HttpUtil.getProxy();
        String userPlayListUrl = "http://www.ornobug.top:4000/user/playlist?uid=98570102&proxy=http://"+proxy;
        String playListListResponseJson = HttpUtil.sendGet(userPlayListUrl);

        //Type listType = new TypeToken<ArrayList<Music163PlayListListResponse>>() {
        //}.getType();
        Music163PlayListListResponse playListListResponse = GSON.fromJson(playListListResponseJson, Music163PlayListListResponse.class);

        List<Music163PlayList> playListList = playListListResponse.getPlaylist();
        // 然后获得歌单里面的歌

        List<String> songIdList = new ArrayList<String>();
        for (Music163PlayList playList : playListList) {

            String playListId = playList.getId();
            String playListUrl = "http://www.ornobug.top:4000/playlist/detail?id="+playListId;
            proxy = HttpUtil.getProxy();
            playListUrl = playListUrl+"&proxy=http://"+proxy;
            String playListResponseJson = HttpUtil.sendGet(playListUrl);
            Music163PlayListResponse playListResponse = GSON.fromJson(playListResponseJson,Music163PlayListResponse.class);
            Music163PlayList playList1 = playListResponse.getResult();
            List<Music163PlayListTrack> trackList = playList1.getTracks();
            for (Music163PlayListTrack track : trackList ) {
                if (!songIdList.contains(track.getId())) {
                    songIdList.add(track.getId());
                }
            }
        }

        // 然后获得歌的评论
        LOGGER.info("待抓取评论的歌曲数量为 "+songIdList.size());
        List<Future<String>> futureList = new ArrayList<Future<String>>();
        for (String songId : songIdList) {
            SpiderMusic163HotCommentsThread thread = new SpiderMusic163HotCommentsThread();
            thread.setSongId(songId);
            Future<String> future = executorService.submit(thread);
            futureList.add(future);
        }

        for (Future<String> future : futureList) {
            try {
                String count = future.get();
                LOGGER.info("新增"+count+"条评论");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {


    }
}
