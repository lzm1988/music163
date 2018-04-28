package top.ornobug.music163.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

/**
 * 带附件的邮件发送
 */
public class MailWithAnnexSender {

    /**
     * 以html格式发送邮件
     */
    public static void sendHtmlMail(final String senderAddr, final String token, String receiverAddrs, String title, String content, File[] annex) throws Exception{
        //用于读取配置文件
        Properties props=new Properties();
        //开启Debug调试
        props.setProperty("mail.debug", "false");
        //发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        //发送邮件协议
        props.setProperty("mail.transport.protocol", "smtp");

        if (senderAddr.endsWith("@qq.com")){
            //发送邮件服务器的主机名
            props.setProperty("mail.smtp.host", "smtp.qq.com");
            //端口号
            props.setProperty("mail.smtp.port", "465");
            //开启ssl加密（并不是所有的邮箱服务器都需要，但是qq邮箱服务器是必须的）
            MailSSLSocketFactory msf= new MailSSLSocketFactory();
            msf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory",msf);
        }else if(senderAddr.endsWith("@163.com")){
            //发送邮件服务器的主机名
            props.setProperty("mail.smtp.host", "smtp.163.com");
            //端口号
            props.setProperty("mail.smtp.port", "25");
        }

        //获取Session会话实例（javamail Session与HttpSession的区别是Javamail的Session只是配置信息的集合）
        Session session=Session.getInstance(props,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                //用户名密码验证（取得的授权吗）
                return new PasswordAuthentication (senderAddr,token);
            }
        });

        //抽象类MimeMessage为实现类 消息载体封装了邮件的所有消息
        Message message=new MimeMessage(session);
        //设置邮件主题
        message.setSubject(title);
        //封装需要发送电子邮件的信息
        message.setText(content);
        //设置发件人地址
        message.setFrom(new InternetAddress(senderAddr));

        // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        Multipart multipart = new MimeMultipart();
        // 添加邮件正文
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(content, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentPart);

        // 添加附件的内容
        if (annex != null) {
            for (File file :annex ) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachmentBodyPart.setDataHandler(new DataHandler(source));

                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");

                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
        }
        // 将multipart对象放到message中
        message.setContent(multipart);

        //此类的功能是发送邮件 又会话获得实例
        Transport transport=session.getTransport();
        //开启连接
        transport.connect();
        //设置收件人地址邮件信息
        String mailAddress[] = receiverAddrs.split(",");
        for (int i = 0; i < mailAddress.length; i++) {
            transport.sendMessage(message,new Address[]{new InternetAddress(mailAddress[i])});
            //邮件发送后关闭信息
            transport.close();
        }

    }


    public static void main(String[] args){
        //Mail mail = new Mail();
        //mail.setFromAddress("myworld20171227@163.com");
        //mail.setUserName("myworld20171227@163.com");
        //mail.setPassword("sign2017");
        //mail.setContent("test");
        //mail.setMailServerHost("smtp.163.com");
        //mail.setMailServerPort("25");
        //mail.setSubject("test sub");
        //mail.setToAddress("1432217150@qq.com");
        //mail.setValidate(true);
        //File[] annexs = new File[2];
        //annexs[0] = new File("D:\\nginx-1.12.2\\html\\product\\img\\3.png");
        //annexs[1] = new File("D:\\nginx-1.12.2\\html\\product\\img\\4.png");
        //mail.setAnnexs(annexs);
        //sendHtmlMail(mail);

        //try {
        //    //send_email("title","content","myworld20171227@163.com",annexs);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        //System.out.println("end");
    }

}
