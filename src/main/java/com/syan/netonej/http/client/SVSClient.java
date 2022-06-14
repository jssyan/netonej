/**
 * 文 件 名： SVSClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import com.syan.netonej.http.client.svs.*;

/**
 * NetONE SVS API
 *
 * @author gejq
 * @version 2.0.0
 * @since 1.0.0
 */
public class SVSClient {

    protected String host;

    protected String port = "9188";

    public SVSClient() {
    }

    public SVSClient(String host) {
        this.host = host;
    }
    public SVSClient(String host, String port) {
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

    public CertificateListBuilder certificateListBuilder(){
        return new CertificateListBuilder().setHost(host).setPort(port);
    }

    public CertificateVerifyBuilder certificateVerifyBuilder(){
        return new CertificateVerifyBuilder().setHost(host).setPort(port);
    }

    public PKCS1VerifyBuilder pkcs1VerifyBuilder(){
        return new PKCS1VerifyBuilder().setHost(host).setPort(port);
    }

    public PKCS7VerifyBuilder pkcs7VerifyBuilder(){
        return new PKCS7VerifyBuilder().setHost(host).setPort(port);
    }

    public XMLSignVerifyBuilder xmlSignVerifyBuilder(){
        return new XMLSignVerifyBuilder().setHost(host).setPort(port);
    }
}
