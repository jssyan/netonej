package com.syan.netonej.http.okhttp;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @Author mmdet
 * @Date 2022-04-24 11:47
 * @Description
 */
public class NetoneJHttpClient {

    /**
     * 全局保持一个okHttpClient对象，
     * 每个OkHttpClient对象都有自己的连接池和线程池，创建多个会导致大量的线程堆积，从而可能会导致程序崩溃。
     */
    private OkHttpClient okHttpClient;

    private static class SingletonHolder{
        private static final NetoneJHttpClient INSTANCE = new NetoneJHttpClient();
    }

    private NetoneJHttpClient(){
        TrustManager[] trustManagers = buildTrustManagers();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostName, SSLSession sslSession) {
                return true;
            }
        });
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }

    public static NetoneJHttpClient getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public OkHttpClient getOkHttpClient() {
       return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public void connectTimeout(long timeout, TimeUnit unit){
        okHttpClient = okHttpClient.newBuilder().connectTimeout(timeout, unit).build();
    }
    public void writeTimeout(long timeout, TimeUnit unit){
        okHttpClient = okHttpClient.newBuilder().writeTimeout(timeout, unit).build();
    }
    public void readTimeout(long timeout, TimeUnit unit){
        okHttpClient = okHttpClient.newBuilder().readTimeout(timeout, unit).build();
    }

    /**
     * 设置连接池大小
     * @param maxPoolSize
     */
    public void setConnectPool(int maxPoolSize){
        ConnectionPool connectionPool = new ConnectionPool(maxPoolSize,1L,TimeUnit.MINUTES);
        okHttpClient = okHttpClient.newBuilder().connectionPool(connectionPool).build();
    }

    /**
     * 设置连接池大小与空闲连接存活时长
     * @param maxPoolSize
     */
    public void setConnectPool(int maxPoolSize,long keepAliveDuration, TimeUnit timeUnit){
        ConnectionPool connectionPool = new ConnectionPool(maxPoolSize,keepAliveDuration,timeUnit);
        okHttpClient = okHttpClient.newBuilder().connectionPool(connectionPool).build();
    }

    public void closeConnectPool(){
        getOkHttpClient().connectionPool().evictAll();
    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
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

    private TrustManager[] buildTrustManagers() {
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

}
