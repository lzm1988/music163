package top.ornobug.music163.scheduled;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.ornobug.music163.util.HttpUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class SpiderProxyScheduled {

    private static final Logger logger = LoggerFactory.getLogger(SpiderProxyScheduled.class);

    private List<String> spiderManongHighProxy() {
        logger.info("爬取[码农高匿代理]免费代理开始");
        List<String> proxyList = new ArrayList<String>();
        String html = HttpUtil.sendGet("https://proxy.coderbusy.com/classical/anonymous-type/highanonymous.aspx?page=1");
        Document document = Jsoup.parse(html);
        String totalRecord = document.select("li.page-item.disabled").last().selectFirst("span").ownText()
                .replace("共", "").replace("条", "").trim();
        int totalNum = Integer.valueOf(totalRecord);

        Elements trList = document.select("table.table > tbody > tr");
        for (Iterator iterator = trList.iterator(); iterator.hasNext(); ) {
            Element tr = (Element) iterator.next();
            String ip = tr.selectFirst("td:nth-of-type(1)").ownText().trim();
            String port = tr.selectFirst("td:nth-of-type(3)").ownText();
            proxyList.add(ip + ":" + port);
        }
        int totalPage = totalNum % 50 == 0 ? totalNum / 50 : (totalNum / 50) + 1;
        for (int i = 1; i < totalPage; i++) {
            html = HttpUtil.sendGet("https://proxy.coderbusy.com/classical/anonymous-type/highanonymous.aspx?page=" + (i + 1));
            document = Jsoup.parse(html);
            trList = document.select("table.table > tbody > tr");
            for (Iterator iterator = trList.iterator(); iterator.hasNext(); ) {
                Element tr = (Element) iterator.next();
                String ip = tr.selectFirst("td:nth-of-type(1)").ownText().trim();
                String port = tr.selectFirst("td:nth-of-type(3)").ownText();
                proxyList.add(ip + ":" + port);
            }
        }
        logger.info("爬取[码农高匿代理]免费代理结束");
        return proxyList;
    }
    private List<String> spiderManongProxy() {
        //"https://proxy.coderbusy.com/classical/anonymous-type/highanonymous.aspx?page=1"
        //"https://proxy.coderbusy.com/classical/anonymous-type/anonymous.aspx?page=1"
        logger.info("爬取[码农匿名代理]免费代理开始");
        List<String> proxyList = new ArrayList<String>();
        String html = HttpUtil.sendGet("https://proxy.coderbusy.com/classical/anonymous-type/anonymous.aspx?page=1");
        Document document = Jsoup.parse(html);
        String totalRecord = document.select("li.page-item.disabled").last().selectFirst("span").ownText()
                .replace("共", "").replace("条", "").trim();
        int totalNum = Integer.valueOf(totalRecord);

        Elements trList = document.select("table.table > tbody > tr");
        for (Iterator iterator = trList.iterator(); iterator.hasNext(); ) {
            Element tr = (Element) iterator.next();
            String ip = tr.selectFirst("td:nth-of-type(1)").ownText().trim();
            String port = tr.selectFirst("td:nth-of-type(3)").ownText();
            proxyList.add(ip + ":" + port);
        }
        int totalPage = totalNum % 50 == 0 ? totalNum / 50 : (totalNum / 50) + 1;
        for (int i = 1; i < totalPage; i++) {
            html = HttpUtil.sendGet("https://proxy.coderbusy.com/classical/anonymous-type/anonymous.aspx?page=" + (i + 1));
            document = Jsoup.parse(html);
            trList = document.select("table.table > tbody > tr");
            for (Iterator iterator = trList.iterator(); iterator.hasNext(); ) {
                Element tr = (Element) iterator.next();
                String ip = tr.selectFirst("td:nth-of-type(1)").ownText().trim();
                String port = tr.selectFirst("td:nth-of-type(3)").ownText();
                proxyList.add(ip + ":" + port);
            }
        }
        logger.info("爬取[码农匿名代理]免费代理结束");
        return proxyList;
    }

    private void start(){

    }

}
