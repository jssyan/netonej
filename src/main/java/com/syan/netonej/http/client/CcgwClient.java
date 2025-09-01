/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;


import com.syan.netonej.common.dict.ModuleName;

import javax.net.ssl.SSLContext;

public class CcgwClient {
    private String host;
    private String port = "8028";
    private String appId;
    private String appSecret;
    private SSLContext sslContext; // SSL证书配置

    private String moduleName;

    // 构造函数
    public CcgwClient(String host, String port, String appId, String appSecret) {
        this.host = host;
        this.port = port;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public CcgwClient(String host, String port, String appId, String appSecret, SSLContext sslContext) {
        this.host = host;
        this.port = port;
        this.appId = appId;
        this.appSecret = appSecret;
        this.sslContext = sslContext;
    }

    // getter和setter方法
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }
    public SSLContext getSslContext() { return sslContext; }
    public void setSslContext(SSLContext sslContext) { this.sslContext = sslContext; }
    public String getModuleName() {return moduleName;}
    public void setModuleName(String moduleName) {this.moduleName = moduleName;}

    // 创建PCSClient的方法
    public PCSClient PCSClient(){
        this.moduleName= ModuleName.PCS_MODULE;
        return new PCSClient(this);
    }


    // 创建SVSClient的方法
    public SVSClient SVSClient(){
        this.moduleName= ModuleName.SVS_MODULE;
        return new SVSClient(this);
    }

    // 创建TSAClient的方法
    public TSAClient TSAClient(){
        this.moduleName= ModuleName.TSA_MODULE;
        return new TSAClient(this);
    }

    // 创建EngineClient的方法
    public EngineClient EngineClient(){
        this.moduleName= ModuleName.ENGINE_MODULE;
        return new EngineClient(this);
    }

}
