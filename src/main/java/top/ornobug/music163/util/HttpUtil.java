package top.ornobug.music163.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class HttpUtil {

    private static final Log logger = LogFactory.getLog(HttpUtil.class);
    private static final int timeOut = 1000 * 10;
    private static final Random random = new Random();

    //private static final DefaultHttpRequestRetryHandler dhr = new DefaultHttpRequestRetryHandler(3,true);

    private static String[] proxyPoolBakUp = {
            "35.185.145.35:19888",
            "122.152.198.48:19888"
    };
    private static String[] proxyPool = new String[0];

    public static void setProxyPool(String[] proxyPool) {
        HttpUtil.proxyPool = proxyPool;
    }

    public static String[] getProxyPool() {
        if (proxyPool == null || proxyPool.length == 0) {
            proxyPool = proxyPoolBakUp;
        }
        return proxyPool;
    }

    public synchronized static String getProxy() {
        Random random = new Random();
        String[] pool = getProxyPool();
        int band = pool.length;
        String proxy = pool[random.nextInt(band)];
        if (StringUtil.isEmpty(proxy)) {
            proxy = proxyPoolBakUp[0];
        }
        return proxy;
    }

    public static String[] getProxyPoolBakUp() {
        return proxyPoolBakUp;
    }

    /**
     * 发送get请求
     *
     * @param url
     * @return
     */
    public static String sendGet(String url) {
        return sendGet(url, "UTF-8", null);
    }

    public static String sendGet(String url, String charset) {
        return sendGet(url, charset, null);
    }

    /**
     * 发送get请求
     *
     * @param url
     * @param headerMap
     * @return
     */
    public static String sendGet(String url, String charset, Map<String, String> headerMap) {
        long startTime = System.currentTimeMillis();
        String body = null;
        int status = 0;
        logger.info("url:" + url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet method = new HttpGet(url);
        HttpResponse response = null;
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            //method.addHeader("Content-type", "application/json; charset=utf-8");
            //method.setHeader("Accept", "application/json");
            //method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .build();
            method.setConfig(requestConfig);
            if (null != headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    method.addHeader(entry.getKey(), entry.getValue());
                }
            }
            logger.info("开始 execute方法");
            response = httpClient.execute(method);
            logger.info("结束 execute方法");
            status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) {
                body = EntityUtils.toString(response.getEntity(), charset);
            } else {
                Thread.sleep(random.nextInt(5) * 1000);
                body = String.valueOf(status);
            }
        } catch (IOException e) {
            // 网络错误
            logger.error(e.getMessage());
            body = String.valueOf(status);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            //method.releaseConnection();
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            logger.info("调用接口状态：" + status);
        }
        long endTime = System.currentTimeMillis();
        logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
        return body;
    }

    /**
     * @param url
     * @param headerMap
     * @return
     */
    public static String sendGetWithProxy(String url, String charset, Map<String, String> headerMap) {
        String body = null;
        int status = 0;
        long startTime = System.currentTimeMillis();
        logger.info("url:" + url);
        HttpGet method = new HttpGet(url);
        CloseableHttpClient proxyClient = null;
        HttpResponse response = null;
        try {
            String proxyParam = getProxy();
            logger.info("proxy:" + proxyParam);
            String[] param = proxyParam.split(":");
            String ip = "";
            String port = "";
            String userName = "";
            String passwd = "";
            if (param.length == 2) {
                ip = param[0];
                port = param[1];
            } else if (param.length == 4) {
                ip = param[0];
                port = param[1];
                userName = param[2];
                passwd = param[3];
            }
            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setProxy(proxy)
                    .build();
            if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(passwd)) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(ip, Integer.valueOf(port)),  //代理服务器信息
                        new UsernamePasswordCredentials(userName, passwd));

                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultCredentialsProvider(credsProvider)
                        //.setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            } else {
                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        //.setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            }
            method.setConfig(requestConfig);
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            //method.addHeader("Content-type", "application/json; charset=utf-8");
            //method.setHeader("Accept", "*/*");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            if (null != headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    method.addHeader(entry.getKey(), entry.getValue());
                }
            }
            logger.info("开始 execute方法");
            response = proxyClient.execute(method);
            logger.info("结束 execute方法");
            status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) {
                body = EntityUtils.toString(response.getEntity(), charset);
            } else {
                Thread.sleep(random.nextInt(5) * 1000);
                body = String.valueOf(status);
            }
        } catch (IOException e) {
            // 网络错误
            logger.error("httpclient错误：" + e.getMessage());
            body = String.valueOf(status);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (proxyClient != null) {
                try {
                    proxyClient.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            //method.releaseConnection();
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            logger.info("调用接口状态：" + status);
            long endTime = System.currentTimeMillis();
            logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
        }
        return body;
    }

    public static String sendGetWithProxy(String url, String proxyLocal, String charset, Map<String, String> headerMap) {
        String body = null;
        int status = 0;
        long startTime = System.currentTimeMillis();
        logger.info("url:" + url);
        HttpGet method = new HttpGet(url);
        CloseableHttpClient proxyClient = null;
        HttpResponse response = null;
        try {
            String proxyParam = "";
            if (!StringUtil.isEmpty(proxyLocal) && proxyLocal.split(":").length > 2) {
                proxyParam = proxyLocal;
            } else {
                proxyParam = getProxy();
            }
            logger.info("proxy:" + proxyParam);
            String[] param = proxyParam.split(":");
            String ip = "";
            String port = "";
            String userName = "";
            String passwd = "";
            if (param.length == 2) {
                ip = param[0];
                port = param[1];
            } else if (param.length == 4) {
                ip = param[0];
                port = param[1];
                userName = param[2];
                passwd = param[3];
            }
            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            DefaultHttpRequestRetryHandler dhr = new DefaultHttpRequestRetryHandler(3, true);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setProxy(proxy)
                    .build();
            if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(passwd)) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(ip, Integer.valueOf(port)),  //代理服务器信息
                        new UsernamePasswordCredentials(userName, passwd));

                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultCredentialsProvider(credsProvider)
                        .setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            } else {
                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            }
            method.setConfig(requestConfig);
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            //method.addHeader("Content-type", "application/json; charset=utf-8");
            //method.setHeader("Accept", "*/*");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            if (null != headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    method.addHeader(entry.getKey(), entry.getValue());
                }
            }
            logger.info("开始 execute方法");
            response = proxyClient.execute(method);
            logger.info("结束 execute方法");
            status = response.getStatusLine().getStatusCode();

            body = EntityUtils.toString(response.getEntity(), charset);
        } catch (IOException e) {
            // 网络错误
            logger.error("httpclient错误：" + e.getMessage());
            body = String.valueOf(status);
        } catch (Exception e) {
            logger.error("未知错误：" + e.getMessage());
        } finally {
            if (proxyClient != null) {
                try {
                    proxyClient.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            //method.releaseConnection();
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            logger.info("调用接口状态：" + status);
            long endTime = System.currentTimeMillis();
            logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
        }
        return body;
    }

    public static String sendPost(String url) {
        return sendPostWithJson(url, null, "UTF-8", null);
    }

    public static String sendPost(String url, String charset) {
        return sendPostWithJson(url, null, charset, null);
    }

    /**
     * 发送post请求，request payload为json数据
     *
     * @param url
     * @param parameters
     * @return
     */
    public static String sendPostWithJson(String url, String parameters) {
        return sendPostWithJson(url, parameters, "UTF-8", null);
    }

    public static String sendPostWithProxy(String url) {
        return sendPostWithJsonWithProxy(url, null, "UTF-8", null);
    }

    public static String sendPostWithProxy(String url, String charset, Map<String, String> headerMap) {
        return sendPostWithJsonWithProxy(url, null, charset, headerMap);
    }

    public static String sendPostWithJson(String url, String parameters, String charset) {
        return sendPostWithJson(url, parameters, charset, null);
    }

    /**
     * 发送post请求，request payload为json数据
     *
     * @param url
     * @param parameters
     * @return
     */
    public static String sendPostWithJson(String url, String parameters, String charset, Map<String, String> headerMap) {
        String body = null;
        int status = 0;
        long startTime = System.currentTimeMillis();
        ;
        logger.info("parameters:" + parameters);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost method = new HttpPost(url);
        HttpResponse response = null;
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            //method.addHeader("Content-type", "application/json; charset=utf-8");
            //method.setHeader("Accept", "application/json");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .build();
            method.setConfig(requestConfig);
            if (null != headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    method.addHeader(entry.getKey(), entry.getValue());
                }
            }

            if (!StringUtil.isEmpty(parameters)) {
                method.setEntity(new StringEntity(parameters, Charset.forName(charset)));
            }

            logger.info("开始 execute方法");
            response = httpClient.execute(method);
            logger.info("结束 execute方法");
            status = response.getStatusLine().getStatusCode();
            // Read the response body
            if (HttpStatus.SC_OK == status || HttpStatus.SC_CREATED == status) {
                body = EntityUtils.toString(response.getEntity(), charset);
            } else {
                Thread.sleep(random.nextInt(5) * 1000);
                body = String.valueOf(status);
            }
        } catch (IOException e) {
            // 网络错误
            body = String.valueOf(status);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            //method.releaseConnection();
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            logger.info("调用接口状态：" + status);
            long endTime = System.currentTimeMillis();
            logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
        }
        return body;
    }

    public static String sendPostWithJsonWithProxy(String url, String parameters, String charset, Map<String, String> headerMap) {
        String body = null;
        int status = 0;
        long startTime = System.currentTimeMillis();
        logger.info("parameters:" + parameters);
        CloseableHttpClient proxyClient = null;
        HttpPost method = new HttpPost(url);
        HttpResponse response = null;
        try {
            String proxyParam = getProxy();
            logger.info("proxy:" + proxyParam);
            String[] param = proxyParam.split(":");
            String ip = "";
            String port = "";
            String userName = "";
            String passwd = "";
            if (param.length == 2) {
                ip = param[0];
                port = param[1];
            } else if (param.length == 4) {
                ip = param[0];
                port = param[1];
                userName = param[2];
                passwd = param[3];
            }
            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setProxy(proxy)
                    .build();
            if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(passwd)) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(ip, Integer.valueOf(port)),  //代理服务器信息
                        new UsernamePasswordCredentials(userName, passwd));

                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultCredentialsProvider(credsProvider)
                        //.setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            } else {
                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        //.setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            }
            method.setConfig(requestConfig);
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            method.addHeader("Content-type", "application/json; charset=utf-8");
            method.setHeader("Accept", "application/json");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            if (null != headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    method.addHeader(entry.getKey(), entry.getValue());
                }
            }

            if (!StringUtil.isEmpty(parameters)) {
                method.setEntity(new StringEntity(parameters, Charset.forName(charset)));
            }

            logger.info("开始 execute方法");
            response = proxyClient.execute(method);
            logger.info("结束 execute方法");

            status = response.getStatusLine().getStatusCode();
            // Read the response body
            body = EntityUtils.toString(response.getEntity(), charset);
        } catch (IOException e) {
            // 网络错误
            body = String.valueOf(status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (null != proxyClient) {
                    proxyClient.close();
                }
                //method.releaseConnection();
                if (null != response) {
                    EntityUtils.consumeQuietly(response.getEntity());
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            logger.info("调用接口状态：" + status);
            long endTime = System.currentTimeMillis();
            logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
        }
        return body;
    }

    /**
     * 发送put请求
     *
     * @param url
     * @return
     */
    public static String sendPut(String url) {
        return sendPut(url, "UTF-8", null);
    }

    /**
     * 发送put请求
     *
     * @param url
     * @param headerMap
     * @return
     */
    public static String sendPut(String url, String charset, Map<String, String> headerMap) {
        String body = null;
        int status = 0;
        long startTime = System.currentTimeMillis();
        logger.info("url:" + url);
        HttpPut method = new HttpPut(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            method.addHeader("Content-type", "application/json; charset=utf-8");
            method.setHeader("Accept", "application/json");

            //method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .build();
            method.setConfig(requestConfig);
            if (null != headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    method.addHeader(entry.getKey(), entry.getValue());
                }
            }

            logger.info("开始 execute方法");
            response = httpClient.execute(method);
            logger.info("结束 execute方法");
            status = response.getStatusLine().getStatusCode();
            // Read the response body
            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) {
                body = EntityUtils.toString(response.getEntity(), charset);
            } else {
                Thread.sleep(random.nextInt(5) * 1000);
                body = String.valueOf(status);
            }
        } catch (IOException e) {
            // 网络错误
            logger.error(e.getMessage());
            logger.error(e.getMessage());
            body = String.valueOf(status);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            //method.releaseConnection();
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            logger.info("调用接口状态：" + status);
            long endTime = System.currentTimeMillis();
            logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
        }
        return body;
    }

    public static String sendPutWithProxy(String url, String charset) {
        return sendPutWithProxy(url, charset, null);
    }

    public static String sendPutWithProxy(String url, String charset, Map<String, String> headerMap) {
        return sendPutWithProxy(url, null, charset, headerMap);
    }

    public static String sendPutWithProxy(String url, String proxyLocal, String charset, Map<String, String> headerMap) {
        String body = null;
        int status = 0;
        long startTime = System.currentTimeMillis();
        logger.info("url:" + url);
        HttpPut method = new HttpPut(url);
        CloseableHttpClient proxyClient = null;
        HttpResponse response = null;
        try {
            String proxyParam = "";
            if (!StringUtil.isEmpty(proxyLocal) && proxyLocal.split(":").length >= 2) {
                proxyParam = proxyLocal;
            } else {
                proxyParam = getProxy();
            }
            logger.info("proxy:" + proxyParam);
            String[] param = proxyParam.split(":");
            String ip = "";
            String port = "";
            String userName = "";
            String passwd = "";
            if (param.length == 2) {
                ip = param[0];
                port = param[1];
            } else if (param.length == 4) {
                ip = param[0];
                port = param[1];
                userName = param[2];
                passwd = param[3];
            }
            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setProxy(proxy)
                    .build();
            if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(passwd)) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(ip, Integer.valueOf(port)),  //代理服务器信息
                        new UsernamePasswordCredentials(userName, passwd));

                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultCredentialsProvider(credsProvider)
                        //.setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            } else {
                proxyClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        //.setRetryHandler(dhr)
                        .build(); // 创建httpClient实例
            }
            method.setConfig(requestConfig);
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            method.addHeader("Content-type", "application/json; charset=utf-8");
            method.setHeader("Accept", "application/json");

            //method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299");

            if (null != headerMap) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    method.addHeader(entry.getKey(), entry.getValue());
                }
            }
            logger.info("开始 execute方法");
            response = proxyClient.execute(method);
            logger.info("结束 execute方法");
            status = response.getStatusLine().getStatusCode();
            // Read the response body
            body = EntityUtils.toString(response.getEntity(), charset);
        } catch (IOException e) {
            // 网络错误
            logger.error(e.getMessage());
            body = String.valueOf(status);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (null != proxyClient) {
                    proxyClient.close();
                }
                //method.releaseConnection();
                if (response != null) {
                    EntityUtils.consumeQuietly(response.getEntity());
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            logger.info("调用接口状态：" + status);
            long endTime = System.currentTimeMillis();
            logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
        }
        return body;
    }

    public static boolean downloadFile(String url, String filePath, Map<String, String> headers) {

        BufferedReader bufferedReader = null;
        URL httpUrl = null;
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            httpUrl = new URL(url);
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
            //HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection(proxy);
            httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.setReadTimeout(timeOut);
            httpURLConnection.setRequestMethod("GET");
            //设置请求头header
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            //获取内容
            inputStream = httpURLConnection.getInputStream();
            inputStreamToFile(inputStream, new File(filePath));

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
            return false;
        } catch (ProtocolException e) {
            logger.error(e.getMessage());
            return false;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return true;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static boolean testProxy(String title, String url, String ipPort, String charset) {
        logger.info(title + "开始测试代理" + ipPort);
        String body = "";
        int status = 0;
        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String ip = ipPort.split(":")[0];
        String port = ipPort.split(":")[1];
        String userName = "";
        String passwd = "";
        if (ipPort.split(":").length == 4) {
            userName = ipPort.split(":")[2];
            passwd = ipPort.split(":")[3];
        }
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        try {
            if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(passwd)) {
                credsProvider.setCredentials(
                        new AuthScope(ip, Integer.valueOf(port)),  //代理服务器信息
                        new UsernamePasswordCredentials(userName, passwd));
            }
            HttpGet method = new HttpGet(url); // 创建httpget实例
            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut)
                    .setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .setProxy(proxy)
                    .build();
            if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(passwd)) {
                credsProvider.setCredentials(
                        new AuthScope(ip, Integer.valueOf(port)),  //代理服务器信息
                        new UsernamePasswordCredentials(userName, passwd));
                httpClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .setDefaultCredentialsProvider(credsProvider)
                        .build(); // 创建httpClient实例
            } else {
                httpClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .build(); // 创建httpClient实例
            }
            method.setConfig(requestConfig);
            method.setHeader("Accept", "*/*");
            //method.setHeader("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
            //method.setHeader("Host","sellercentral.amazon.com");
            //method.setHeader("Referer","https://sellercentral.amazon.com/hz/fba/profitabilitycalculator/index");
            method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            logger.info("开始 execute方法");
            response = httpClient.execute(method); // 执行http get请求
            logger.info("结束 execute方法");
            status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity(); // 获取返回实体
            body = EntityUtils.toString(entity, charset);
        } catch (Exception e) {
            body = String.valueOf(status);
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
        long endTime = System.currentTimeMillis();
        if (body.startsWith("{")) {
            logger.info(title + "测试" + ipPort + "成功，耗时" + (endTime - startTime));
            return true;
        } else {
            logger.error(title + "测试" + ipPort + "异常，耗时" + (endTime - startTime));
            return false;
        }
    }


    public static void main(String[] args) {

    }

    private static List<String> spiderManoProxy() {

        logger.info("爬取[码农代理]免费代理开始");
        List<String> tempList = new ArrayList<String>();
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
            tempList.add(ip + ":" + port);
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
                tempList.add(ip + ":" + port);
            }
        }
        logger.info("爬取[码农代理]免费代理结束,共" + tempList.size() + "条");
        return tempList;
    }

}
