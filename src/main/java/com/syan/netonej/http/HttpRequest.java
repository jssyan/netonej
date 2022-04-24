package com.syan.netonej.http;


import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.okhttp.NetoneJHttpClient;
import okhttp3.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * @Author mmdet
 * @Date 2021-08-16 13:19
 * @Description
 */
public class HttpRequest {
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String url;
    private Request.Builder request;

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private HttpRequest() {
//        if (okHttpClient == null) {
//            synchronized (HttpRequest.class) {
//                if (okHttpClient == null) {
//                    TrustManager[] trustManagers = buildTrustManagers();
//                    okHttpClient = new OkHttpClient.Builder()
//                            .connectTimeout(15, TimeUnit.SECONDS)
//                            .writeTimeout(20, TimeUnit.SECONDS)
//                            .readTimeout(20, TimeUnit.SECONDS)
//                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
//                            .hostnameVerifier((hostName, session) -> true)
//                            .retryOnConnectionFailure(true)
//                            .build();
//                    addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//                }
//            }
//        }
    }

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     * @return
     */
    private static Semaphore getSemaphoreInstance() {
        //只能1个线程同时访问
        synchronized (HttpRequest.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }


    /**
     * 创建OkHttpUtils
     *
     * @return
     */
    public static HttpRequest builder() {
        return new HttpRequest();
    }

    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public HttpRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public HttpRequest addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * 添加参数
     * @param map
     * @return
     */
    public HttpRequest addParam(Map<String,String> map) {
        if(map == null){
            return this;
        }
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        paramMap.putAll(map);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public HttpRequest addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * 添加请求头
     * @return
     */
    public HttpRequest addHeader(Map<String,String> map) {
        if(map == null){
            return this;
        }
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        headerMap.putAll(map);
        return this;
    }

    /**
     * 初始化get方法
     *
     * @return
     */
    public HttpRequest get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(entry.getValue(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    public HttpRequest post() {
        RequestBody requestBody;
        FormBody.Builder formBody = new FormBody.Builder();
        if (paramMap != null) {
            paramMap.forEach(formBody::add);
        }
        requestBody = formBody.build();
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    public HttpRequest postJson(String json) {
        RequestBody requestBody;
        requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    public HttpRequest postBytes(byte[] bytes) {
        RequestBody requestBody;
        requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), bytes);
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    /**
     * 同步请求
     *
     * @return
     */
    public String sync() {
        setHeader(request);
        try {
            OkHttpClient okHttpClient = NetoneJHttpClient.getInstance().getOkHttpClient();
            Response response = okHttpClient.newCall(request.build()).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "请求失败：" + e.getMessage();
        }
    }

    /**
     * 同步请求 -- 结合业务扩展的接口
     *
     * @return
     */
    public NetoneResponse syncBytes() throws NetonejExcepption {
        setHeader(request);
        try {
            OkHttpClient okHttpClient = NetoneJHttpClient.getInstance().getOkHttpClient();
            Response response = okHttpClient.newCall(request.build()).execute();
            if(response.code() == 200){
                return new NetoneResponse(response.code(),response.body().bytes());
            }else{
                return new NetoneResponse(response.code());
            }
        } catch (IOException e) {
            throw new NetonejExcepption("网络请求失败：" + e.getMessage(),e);
        }
    }

    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        OkHttpClient okHttpClient = NetoneJHttpClient.getInstance().getOkHttpClient();
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                buffer.append(response.body().string());
                getSemaphoreInstance().release();
            }
        });
        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        OkHttpClient okHttpClient = NetoneJHttpClient.getInstance().getOkHttpClient();
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccessful(call, response.body().string());
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, String data);

        void onFailure(Call call, String errorMsg);

    }

}
