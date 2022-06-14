/**
 * 文 件 名:  NetoneTSA.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-05-02
 */
package com.syan.netonej.http.entity;

import java.io.IOException;

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

import com.syan.netonej.exception.NetonejException;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.util.encoders.Base64;

public class NetoneTSA extends NetoneResponse {

//    private static Map<String, String> algoMap = new HashMap<String, String>();
//
//    static {
//        algoMap.put("1.3.14.3.2.26", "sha1");
//        algoMap.put("1.2.840.113549.2.5", "md5");
//        algoMap.put("2.16.840.1.101.3.4.2.1", "sha256");
//        algoMap.put("1.2.156.10197.1.401", "sm3");
//    }


    /**
     * 由于TSA接口返回的是二进制数据，所以这里getResult的返回值要返回base64编码的，而不是父类getResult的new string形式
     * @return
     */
    @Override
    public String getResult() {
        return Base64.toBase64String(getBytesResult());
    }

    /**
     * @param response
     * @throws TSPException
     * @throws IOException
     */
    public NetoneTSA(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response.getResult() != null){
            setResult(response.getBytesResult());
        }
//        if (response.getResult() != null) {
//            try {
//                //todo
//                byte[] stamp = response.getBytesResult();
//                TimeStampResponse resp = new TimeStampResponse(stamp);
//                tsTokeninfo = resp.getTimeStampToken().getTimeStampInfo();
//                CMSSignedDataUtil cms = new CMSSignedDataUtil(resp.getTimeStampToken().getEncoded());
//                List<IssuerAndSerialNumber> issuerAndSerialNumbers = cms.getSignerIssuerAndSerialNumber();
//                if (issuerAndSerialNumbers.size() > 0) {
//                    this.serial = issuerAndSerialNumbers.get(0).getSerialNumber().getValue().toString(16);
//                }
//                X500Name x500name = X500Name.getInstance(tsTokeninfo.getTsa().getName());
//                //x500name = NetonejUtil.canonicalX500Name(x500name);
//                this.subject = x500name.toString();
//                if(tsTokeninfo.getNonce() != null){
//                    this.nonce = tsTokeninfo.getNonce().toString(16).toUpperCase();
//                }
//
//                this.imprint = NetonejUtil.byte2HexString(tsTokeninfo.getMessageImprintDigest());
//                this.timestamp = tsTokeninfo.getGenTime();
//                String algoName = algoMap.get(tsTokeninfo.getMessageImprintAlgOID().toString());
//                this.algo = algoName == null ? tsTokeninfo.getMessageImprintAlgOID().toString() : algoName;
//
//                this.timestampbase64 = Base64.getEncoder().encodeToString(stamp);
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                throw new NetonejException(e.getMessage(), e);
//            }
//
//        }
    }

//    /**
//     * 证书序列号
//     */
//    private String serial;
//    /**
//     * 随机数
//     */
//    private String nonce;
//    /**
//     * 原文摘要
//     */
//    private String imprint;
//    /**
//     * 摘要算法
//     */
//    private String algo;
//    /**
//     * 时间戳签发时间
//     */
//    private Date timestamp;
//
//    private String subject;
//
//

}
