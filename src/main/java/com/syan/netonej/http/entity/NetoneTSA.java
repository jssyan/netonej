/**
 * 文 件 名:  NetoneTSA.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-05-02
 */
package com.syan.netonej.http.entity;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import org.spongycastle.asn1.cms.IssuerAndSerialNumber;
//import org.spongycastle.asn1.x500.X500Name;
//import org.spongycastle.asn1.x509.GeneralName;
//import org.spongycastle.cert.X509CertificateHolder;
//import org.spongycastle.cms.CMSException;
//import org.spongycastle.cms.CMSSignedData;
//import org.spongycastle.cms.SignerInformation;
//import org.spongycastle.tsp.TSPException;
//import org.spongycastle.tsp.TimeStampResponse;
//import org.spongycastle.tsp.TimeStampTokenInfo;
//import org.spongycastle.util.encoders.Base64;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampTokenInfo;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;

public class NetoneTSA extends NetoneResponse {

    private byte[] originResult;

    /**
     * 由于TSA接口返回的是二进制数据，所以这里getResult的返回值要返回base64编码的，而不是父类getResult的new string形式
     * @return
     */
    @Override
    public String getResult() {
        return originResult == null?Base64.toBase64String(getBytesResult()):Base64.toBase64String(originResult);
    }

    /**
     * @param response
     * @throws TSPException
     * @throws IOException
     */
    public NetoneTSA(byte[] originResult,NetoneResponse response) throws NetonejException {
        super(response.getStatusCode(), response.getBytesResult(),response.getStatusCodeMessage());
        this.originResult = originResult;
        if (response.getStatusCode() == 200&&response.getBytesResult() != null) {
            try {
                byte[] stamp = originResult==null?response.getBytesResult():originResult;
                TimeStampResponse resp = new TimeStampResponse(stamp);
                TimeStampTokenInfo tsTokeninfo = resp.getTimeStampToken().getTimeStampInfo();
                X500Name x500name = X500Name.getInstance(tsTokeninfo.getTsa().getName());
                this.subject = x500name.toString();
                if(tsTokeninfo.getNonce() != null){
                    this.nonce = tsTokeninfo.getNonce().toString(16).toUpperCase();
                }
                this.imprint = NetonejUtil.byte2HexString(tsTokeninfo.getMessageImprintDigest());
                this.timestamp = tsTokeninfo.getGenTime();
                this.algo = DigestAlgorithms.getDigest(tsTokeninfo.getMessageImprintAlgOID().toString());
            } catch (Exception e) {
                throw new NetonejException(e.getMessage(), e);
            }
        }
    }

    /**
     * 随机数
     */
    private String nonce;
    /**
     * 原文摘要
     */
    private String imprint;
    /**
     * 摘要算法
     */
    private String algo;
    /**
     * 时间戳签发时间
     */
    private Date timestamp;

    private String subject;


    public String getNonce() {
        return nonce;
    }

    public String getImprint() {
        return imprint;
    }

    public String getAlgo() {
        return algo;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSubject() {
        return subject;
    }
}
