/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import com.syan.netonej.http.client.tsa.TSACreateBuilder;
import com.syan.netonej.http.client.tsa.TSAVerifyBuilder;

public class TSAClient {
    protected String host;

    protected String port = "9198";

    public TSAClient() {
    }

    public TSAClient(String host) {
        this.host = host;
    }
    public TSAClient(String host, String port) {
        this.host = host;
        this.port = port;
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

    public TSACreateBuilder tsaCreateBuilder(){
        return new TSACreateBuilder().setHost(host).setPort(port);
    }

    public TSAVerifyBuilder tsaVerifyBuilder(){
        return new TSAVerifyBuilder().setHost(host).setPort(port);
    }
}
