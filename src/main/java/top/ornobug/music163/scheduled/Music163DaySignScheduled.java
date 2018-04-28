package top.ornobug.music163.scheduled;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.ornobug.music163.domain.Music163SignReponse;
import top.ornobug.music163.util.HttpUtil;
import top.ornobug.music163.util.StringUtil;

@Component
public class Music163DaySignScheduled {

    private static final Gson GSON = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(Music163DaySignScheduled.class);

    @Scheduled(cron = "0 6 * * *")
    public void start() {

        String androidSignUrl = "http://api.ornobug.top/163music/daily_signin";
        String webSignUrl = "http://api.ornobug.top/163music/daily_signin";
        String androidSignResponseJson = "";
        Music163SignReponse signReponse = null;
        for (int i = 0; i < 3; i++) {
            androidSignResponseJson = HttpUtil.sendGet(androidSignUrl);
            if (!StringUtil.isEmpty(androidSignResponseJson) && androidSignResponseJson.startsWith("{")) {
                signReponse = GSON.fromJson(androidSignResponseJson, Music163SignReponse.class);
            }
            if (signReponse != null && "200".equals(signReponse.getCode())) {
                LOGGER.info("安卓端成功签到，经验+" + signReponse.getPoint());
                break;
            }
        }

        String webSignResponseJson = "";
        for (int i = 0; i < 3; i++) {
            webSignResponseJson = HttpUtil.sendGet(webSignUrl);
            if (!StringUtil.isEmpty(webSignResponseJson) && webSignResponseJson.startsWith("{")) {
                signReponse = GSON.fromJson(webSignResponseJson, Music163SignReponse.class);
            }
            if (signReponse != null && "200".equals(signReponse.getCode())) {
                LOGGER.info("web端成功签到，经验+" + signReponse.getPoint());
                break;
            }
        }
    }
}
