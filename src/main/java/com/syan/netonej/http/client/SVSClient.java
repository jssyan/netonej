/**
 * 文 件 名： SVSClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.svs.*;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneSVS;
import org.bouncycastle.util.encoders.Base64;

public class SVSClient {

    protected String host;

    protected String port = "9188";

    protected CcgwClient ccgwClient;

    public SVSClient(CcgwClient ccgwClient) {
        this.ccgwClient = ccgwClient;
        this.host = ccgwClient.getHost();
        this.port = ccgwClient.getPort();
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
        return new CertificateListBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public StampVerifyBuilder stampVerifyBuilder(){
        return new StampVerifyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public CertificateVerifyBuilder certificateVerifyBuilder(){
        return new CertificateVerifyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public PKCS1VerifyBuilder pkcs1VerifyBuilder(){
        return new PKCS1VerifyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public PKCS7VerifyBuilder pkcs7VerifyBuilder(){
        return new PKCS7VerifyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public XMLSignVerifyBuilder xmlSignVerifyBuilder(){
        return new XMLSignVerifyBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }
}
