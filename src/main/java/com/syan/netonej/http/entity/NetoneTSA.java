/**
 * 文 件 名:  NetoneTSA.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-05-02
 */
package com.syan.netonej.http.entity;

import java.io.IOException;
import java.util.*;

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

import com.syan.netonej.common.CMSSignedDataUtil;
import com.syan.netonej.common.NetonejUtil;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampTokenInfo;

public class NetoneTSA extends NetoneBase {

    private static Map<String, String> algoMap = new HashMap<String, String>();

    static {
        algoMap.put("1.3.14.3.2.26", "sha1");
        algoMap.put("1.2.840.113549.2.5", "md5");
        algoMap.put("2.16.840.1.101.3.4.2.1", "sha256");
        algoMap.put("1.2.156.10197.1.401", "sm3");
    }

    /**
     * DER编码格式的时间戳base64编码字符串
     */
    private String timestampbase64;

    /**
     * 时间戳信息
     */
    private TimeStampTokenInfo tsTokeninfo;

    /**
     * @param response
     * @throws TSPException
     * @throws IOException
     */
    public NetoneTSA(NetoneResponse response) throws TSPException, IOException {
        super(response.getStatusCode());
        if (response.getRetBytes() != null) {
            try {
                byte[] stamp = response.getRetBytes();
                TimeStampResponse resp = new TimeStampResponse(stamp);
                tsTokeninfo = resp.getTimeStampToken().getTimeStampInfo();
                CMSSignedDataUtil cms = new CMSSignedDataUtil(resp.getTimeStampToken().getEncoded());
                List<IssuerAndSerialNumber> issuerAndSerialNumbers = cms.getSignerIssuerAndSerialNumber();
                if (issuerAndSerialNumbers.size() > 0) {
                    this.serial = issuerAndSerialNumbers.get(0).getSerialNumber().getValue().toString(16);
                }
                X500Name x500name = X500Name.getInstance(tsTokeninfo.getTsa().getName());
                x500name = NetonejUtil.canonicalX500Name(x500name);
                this.subject = x500name.toString();
                if(tsTokeninfo.getNonce() != null){
                    this.nonce = tsTokeninfo.getNonce().toString(16).toUpperCase();
                }

                this.imprint = NetonejUtil.byte2HexString(tsTokeninfo.getMessageImprintDigest());
                this.timestamp = tsTokeninfo.getGenTime();
                String algoName = algoMap.get(tsTokeninfo.getMessageImprintAlgOID().toString());
                this.algo = algoName == null ? tsTokeninfo.getMessageImprintAlgOID().toString() : algoName;

                this.timestampbase64 = Base64.getEncoder().encodeToString(stamp);
            } catch (CMSException e) {
                // TODO Auto-generated catch block
                throw new TSPException(e.getMessage(), e);
            }

        }
    }

    /**
     * 证书序列号
     */
    private String serial;
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

    /**
     * 获取证书序列号
     *
     * @return 证书序列号
     */
    public String getSerial() {
        return serial;
    }

    /**
     * 获取随机数
     *
     * @return 随机数
     */
    public String getNonce() {
        return nonce;
    }


    /**
     * 获取时间戳签名的摘要信息
     *
     * @return 证书指纹
     */
    public String getImprint() {
        return imprint;
    }


    /**
     * 获取签名摘要算法
     *
     * @return 摘要算法（md5 or sha1）
     */
    public String getAlgo() {
        return algo;
    }


    /**
     * 获取时间戳（java.util.Date Object）
     *
     * @return 时间戳（java.util.Date Object）
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * 获取证书主题项
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Serial：" + getSerial());
        stringBuffer.append("#Subject：" + getSubject());
        stringBuffer.append("#Nonce：" + getNonce());
        stringBuffer.append("#Algo：" + getAlgo());
        stringBuffer.append("#Imprint：" + getImprint());
        stringBuffer.append("#DateTime：" + getTimestamp());
        return stringBuffer.toString();
    }

    /**
     * @return 时间戳数据base64编码字符串
     */
    public String getTimestampbase64() {
        return timestampbase64;
    }

    /**
     * @return 时间戳数据
     */
    public byte[] getEncoded() {
        return timestampbase64 == null ? null : Base64.getDecoder().decode(timestampbase64);
    }

    public TimeStampTokenInfo getTsTokeninfo() {
        return tsTokeninfo;
    }


}
