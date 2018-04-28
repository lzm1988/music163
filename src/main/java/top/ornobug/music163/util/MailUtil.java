package top.ornobug.music163.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service(value = "mailUtil")
public class MailUtil {

    @Async
    public void sendToOneWithAnnex(String toAddrs,String fromAddr,String sign,String senderName,String sendBody
            , String sendTite,MultipartFile[] annexs){

        File[] files = null;
        if (annexs != null && annexs.length>0){
            files = new File[annexs.length];
            for(int i = 0;i<annexs.length;i++){
                files[i] = (File) annexs[i];
            }
        }

        try {
            MailWithAnnexSender.sendHtmlMail(fromAddr,sign,toAddrs,sendTite,sendBody,files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
