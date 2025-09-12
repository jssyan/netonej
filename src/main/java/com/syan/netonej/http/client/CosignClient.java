/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import com.syan.netonej.http.client.cosign.DecryptionBuilder;
import com.syan.netonej.http.client.cosign.GenKeyBuilder;
import com.syan.netonej.http.client.cosign.GenRandBuilder;
import com.syan.netonej.http.client.cosign.SignatureBuilder;

public class CosignClient {
    protected String host;

    protected String port = "9198";

    protected CcgwClient ccgwClient;

    public CosignClient(CcgwClient ccgwClient) {
        this.ccgwClient = ccgwClient;
        this.host = ccgwClient.getHost();
        this.port = ccgwClient.getPort();
    }

    public CosignClient(String host) {
        this.host = host;
    }
    public CosignClient(String host, String port) {
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

    public GenRandBuilder genRandBuilder(){
        return new GenRandBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public GenKeyBuilder genKeyBuilder(){
        return new GenKeyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public SignatureBuilder signatureBuilder(){
        return new SignatureBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public DecryptionBuilder decryptionBuilder(){
        return new DecryptionBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }



}
