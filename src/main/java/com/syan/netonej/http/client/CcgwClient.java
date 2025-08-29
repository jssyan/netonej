/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;


import com.syan.netonej.common.dict.KeyAlgorithm;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.ccgw.pcs.KeyBuilder;
import com.syan.netonej.http.entity.NetoneKeyList;

public class CcgwClient {
    protected String host;

    protected String port = "8028";

    protected String appId;

    protected String appSecret;

    public CcgwClient(String host) {
        this.host = host;
    }
    public CcgwClient(String host, String port,String appId, String appSecret) {
        this.host = host;
        this.port = port;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppSecret(String secret) {
        this.appSecret = secret;
    }

    public KeyBuilder keyBuilder(){
        return new KeyBuilder().setHost(host).setPort(port).setAppId(appId).setAppSecret(appSecret);
    }
}
