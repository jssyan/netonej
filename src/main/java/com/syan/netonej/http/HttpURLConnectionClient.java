package com.syan.netonej.http;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejException;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022/8/15 14:41
 * @Description
 */
public class HttpURLConnectionClient {

    private final String CHARSET_UTF8 = "UTF-8";

    private String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private String url;

    private String REQUEST_METHOD;

    private int connectTimeout = 60000;

    private int readTimeout=60000;

    private final Map<String,String> headers = new HashMap<String, String>();

    private final Map<String,String> params = new HashMap<String, String>();

    public static HttpURLConnectionClient builder(){
        return new HttpURLConnectionClient();
    }

    public HttpURLConnectionClient url(String url) {
        this.url = url;
        return this;
    }

    public byte[] post() throws NetonejException{
        this.REQUEST_METHOD = "POST";
       return build();
    }
    public byte[] postBytes(byte[] bytes) throws NetonejException{
        this.REQUEST_METHOD = "POST";
        this.CONTENT_TYPE = "application/octet-stream";
        return build(bytes);
    }
    public byte[] postJson(String json) throws NetonejException{
        this.REQUEST_METHOD = "POST";
        this.CONTENT_TYPE = "application/json; charset=utf-8";
        return build(json.getBytes());
    }
    public byte[] postString(String string) throws NetonejException{
        this.REQUEST_METHOD = "POST";
        this.CONTENT_TYPE = "application/text; charset=utf-8";
        return build(string.getBytes());
    }

    public byte[] get() throws NetonejException{
        this.REQUEST_METHOD = "GET";
        return build();
    }

    public HttpURLConnectionClient header(String name,String value){
        if(!NetonejUtil.isEmpty(name) && !NetonejUtil.isEmpty(value)){
            headers.put(name,value);
        }
        return this;
    }

    public HttpURLConnectionClient header(Map<String,String> map){
        if(!map.isEmpty()){
            headers.putAll(map);
        }
        return this;
    }

    public HttpURLConnectionClient param(String name,String value){
        if(!NetonejUtil.isEmpty(name) && !NetonejUtil.isEmpty(value)){
            params.put(name,value);
        }
        return this;
    }

    public HttpURLConnectionClient param(Map<String,String> map){
        if(!map.isEmpty()){
            params.putAll(map);
        }
        return this;
    }

    public HttpURLConnectionClient connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public HttpURLConnectionClient readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    private byte[] build() throws NetonejException{
        byte[] paramsBytes = null;
        if (params != null && params.size() != 0) {
            try {
                paramsBytes = map2url(params);
            } catch (UnsupportedEncodingException e) {
                throw new NetonejException("参数编码失败："+e.getMessage());
            }
        }
        return build(paramsBytes);
    }

    private byte[] build(byte[] paramsBytes) throws NetonejException{
        if(NetonejUtil.isEmpty(url)){
            throw new NetonejException("URL must be set");
        }
        if(url.startsWith("https")){
            return httpsRequest(paramsBytes);
        }
        return httpRequest(paramsBytes);
    }


    private byte[] httpRequest(byte[] paramsBytes) throws NetonejException{
        HttpURLConnection httpConn = null;
        InputStream inputStream = null;
        try {
            URL urlconnect = new URL(url);
            httpConn = (HttpURLConnection) urlconnect.openConnection();
            httpConn.setDoOutput(true);   //需要输出
            httpConn.setDoInput(true);   //需要输入
            httpConn.setUseCaches(false);  //不允许缓存
            httpConn.setRequestMethod(REQUEST_METHOD);   //设置POST方式连接
            httpConn.setInstanceFollowRedirects(true);    //自动执行HTTP重定向
            httpConn.setRequestProperty("Content-Type", CONTENT_TYPE);
            httpConn.setRequestProperty("Charset", CHARSET_UTF8);
            // 设置链接状态
            httpConn.setRequestProperty("connection", "keep-alive");
            if (headers != null && headers.size() != 0) {
                for (Map.Entry<String,String> entry:headers.entrySet()){
                    httpConn.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }
            // 设置超时时间
            httpConn.setConnectTimeout(connectTimeout);
            httpConn.setReadTimeout(readTimeout);
            httpConn.connect();
            //建立输入流，向指向的URL传入参数
            if (paramsBytes != null) {
                OutputStream out = httpConn.getOutputStream();
                out.write(paramsBytes);
                out.flush();
                out.close();
            }
            //获得响应状态
            int resultCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                inputStream = httpConn.getInputStream();
                return inputStream2bytes(inputStream);
            } else {
                throw new NetonejException(resultCode,httpConn.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            throw new NetonejException("无效的URL：" + e.getMessage(), e);
        } catch (IOException e) {
            throw new NetonejException("网络请求失败：" + e.getMessage(), e);
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] httpsRequest(byte[] paramBytes) throws NetonejException{
        HttpsURLConnection httpConn = null;
        InputStream inputStream = null;
        try {
            URL urlconnect = new URL(url);
            httpConn = (HttpsURLConnection) urlconnect.openConnection();
            httpConn.setDoOutput(true);   //需要输出
            httpConn.setDoInput(true);   //需要输入
            httpConn.setUseCaches(false);  //不允许缓存
            httpConn.setRequestMethod(REQUEST_METHOD);   //设置POST方式连接
            httpConn.setInstanceFollowRedirects(true);    //自动执行HTTP重定向
            httpConn.setRequestProperty("Content-Type", CONTENT_TYPE);
            httpConn.setRequestProperty("Charset", CHARSET_UTF8);
            // 设置链接状态
            httpConn.setRequestProperty("connection", "keep-alive");
            if (headers != null && headers.size() != 0) {
                for (Map.Entry<String,String> entry:headers.entrySet()){
                    httpConn.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }
            // 设置超时时间
            httpConn.setConnectTimeout(connectTimeout);
            httpConn.setReadTimeout(readTimeout);

            httpConn.setSSLSocketFactory(createSSLSocketFactory());
            httpConn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            httpConn.connect();
            //建立输入流，向指向的URL传入参数
            if (paramBytes != null) {
                OutputStream out = httpConn.getOutputStream();
                out.write(paramBytes);
                out.flush();
                out.close();
            }
            //获得响应状态
            int resultCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                inputStream = httpConn.getInputStream();
                return inputStream2bytes(inputStream);
            } else {
                throw new NetonejException(resultCode,httpConn.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            throw new NetonejException("无效的URL：" + e.getMessage(), e);
        } catch (IOException e) {
            throw new NetonejException("网络请求失败：" + e.getMessage(), e);
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpConn != null){
                httpConn.disconnect();
            }

        }
    }

    private byte[] inputStream2bytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n;
        while (((n = inputStream.read(buf)) != -1)) {
            dataOutputStream.write(buf, 0, n);
        }
        return dataOutputStream.toByteArray();
    }

    private byte[] map2url(Map<String, String> paramToMap) throws UnsupportedEncodingException {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Map.Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (!NetonejUtil.isEmpty(value)) {
                url.append(URLEncoder.encode(value, CHARSET_UTF8));
            }
        }
        String params = url.toString();
        return params.getBytes(CHARSET_UTF8);
    }


    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }
}
