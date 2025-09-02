/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.ca.DelayBuilder;
import com.syan.netonej.http.client.ca.IssueBuilder;
import com.syan.netonej.http.client.ca.ReissueBuilder;
import com.syan.netonej.http.client.ca.RevokeBuilder;
import com.syan.netonej.http.client.tsa.TSACreateBuilder;
import com.syan.netonej.http.client.tsa.TSAVerifyBuilder;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

public class CAClient {
    protected String host;

    protected String port = "9198";

    protected CcgwClient ccgwClient;

    public CAClient(CcgwClient ccgwClient) {
        this.ccgwClient = ccgwClient;
        this.host = ccgwClient.getHost();
        this.port = ccgwClient.getPort();
    }

    public CAClient(String host) {
        this.host = host;
    }
    public CAClient(String host, String port) {
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

    public IssueBuilder issueBuilder(){
        return new IssueBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public ReissueBuilder reissueBuilder(){
        return new ReissueBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public DelayBuilder delayBuilder(){
        return new DelayBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }

    public RevokeBuilder revokeBuilder(){
        return new RevokeBuilder().setHost(host).setPort(port).setCcgwClient(ccgwClient);
    }



}
