/**
 * 文 件 名： SVSClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import java.util.*;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.xml.XmlparserFacotry;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.entity.NetoneSVS;

/**
 * NetONE SVS API
 *
 * @author gejq
 * @version 2.0.0
 * @since 1.0.0
 */
public class SVSClient extends BaseClient {

    /**
     * 绿色通道 在服务端设置启用greenpass的情况下，客户端可以通过设置0禁用绿色通道
     */
    private boolean greenpass = false;


    /**
     * 启用绿色通道
     */
    public void enabledGreenpass() {
        this.greenpass = true;
    }

    /**
     * 禁用绿色通道
     */
    public void disabledGreepass() {
        this.greenpass = false;
    }

    public SVSClient(String host, String port, String application) {
        super(host, port, application);
    }

    public SVSClient(String host, String port) {
       super(host,port);
    }

    public SVSClient(String host) {
        super(host,"9188");
    }

    @Override
    protected Map<String, String> prepareParameter() {
        Map<String, String> map = super.prepareParameter();
        if(!this.greenpass){
            map.put("greenpass", "0");
        }
        return map;
    }

    /**
     * 验证数字证书有效性
     *
     * @param Base64Certificate
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyCertificate(String Base64Certificate,String signts) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("cert", Base64Certificate);
        if(!NetonejUtil.isEmpty(signts)){
            params.put("signts", signts);
        }
        try {
            NetoneResponse response = doHttpPost(Action.SVS_ACTION_VC, params);
            return new NetoneSVS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-SVS（vc）证书验证失败" + e, e);
        }
    }

    public NetoneSVS verifyCertificate(String Base64Certificate) throws NetonejExcepption {
        return verifyCertificate(Base64Certificate,"");
    }

    public NetoneSVS verifyCertificate(String id,IdMagic idMagic) throws NetonejExcepption {
        return verifyCertificate(id,idMagic,null);
    }


    public NetoneSVS verifyCertificate(String id,IdMagic idMagic,String signts) throws NetonejExcepption {
        NetoneSVS svs = null;
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        params.put("idmagic", idMagic.name().toLowerCase());
        if(!NetonejUtil.isEmpty(signts)){
            params.put("signts", signts);
        }
        try {
            NetoneResponse response = doHttpPost(Action.SVS_ACTION_VC, params);

            svs = new NetoneSVS(response);
        } catch (Exception e) {

            e.printStackTrace();
            throw new NetonejExcepption("-SVS（vc）证书验证失败" + e, e);
        }

        return svs;
    }

    /**
     * 验证PKCS#1数字签名
     *
     * @param plainText 签名时的原文数据
     * @param signature PKCS#1签名结果
     * @param certDN    证书DN
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    @Deprecated
    public NetoneSVS verifyPKCS1(byte[] plainText, String signature, String certDN) throws NetonejExcepption {


        NetoneSVS svs = null;
        Map<String, String> params = prepareParameter();
        params.put("data", NetonejUtil.base64Encode(plainText));
        params.put("datt", "0");
        params.put("singature", signature);
        params.put("idmagic", "scn");
        params.put("id", (String) NetonejUtil.getMapFromDN(certDN).get("CN"));
        try {
            NetoneResponse response =doHttpPost(Action.SVS_ACTION_VP1, params);

            svs = new NetoneSVS(response);
        } catch (Exception e) {

            throw new NetonejExcepption("-SVS(vp1)数字签名验证失败" + e, e);
        }

        return svs;
    }

    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, String algo, String datt, String certificate, boolean dataB64) throws NetonejExcepption {
        if (dataB64) {
            return verifyPKCS1Action(data,signature,algo,Integer.parseInt(datt),certificate);
        } else {
            return verifyPKCS1Action(NetonejUtil.base64Encode(data.getBytes()),signature,algo,Integer.parseInt(datt),certificate);
        }
    }

    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, String datt, String Base64Certificate, boolean dataB64) throws NetonejExcepption {
        return verifyPKCS1(data, signature, "", datt, Base64Certificate, dataB64);
    }

    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, String Base64Certificate, boolean dataB64) throws NetonejExcepption {
        return verifyPKCS1(data, signature, "", "0", Base64Certificate, dataB64);
    }

    public NetoneSVS verifyPKCS1(String data, String signature, DigestAlgorithm algo, DataType datt, String certificate) throws NetonejExcepption {
        return verifyPKCS1Action(data,signature,algo.getName(),datt.ordinal(),certificate);
    }

    public NetoneSVS verifyPKCS1(String data, String signature, DataType datt, String certificate) throws NetonejExcepption {
        return verifyPKCS1Action(data,signature,null,datt.ordinal(),certificate);
    }

    public NetoneSVS verifyPKCS1(String data, String signature, DigestAlgorithm algo, String certificate) throws NetonejExcepption {
        return verifyPKCS1Action(data,signature,algo.getName(),DataType.PLAIN.ordinal(),certificate);
    }

    public NetoneSVS verifyPKCS1(String data, String signature, String certificate) throws NetonejExcepption {
        return verifyPKCS1Action(data,signature,null,DataType.PLAIN.ordinal(),certificate);
    }

    /**
     *  验证PKCS#1数字签名
     * @param data 签名时的原文数据(b64编码)
     * @param signature  PKCS#1签名结果(b64编码)
     * @param algo 签名时选择的摘要算法 - RSA-MD5 = md5算法（RSA），RSA-SHA1=sha1算法（RSA），ECDSA-SM2-WITH-SM3 = sm3算法（ECC）
     * @param datt 签名时选择的原文类型 - 0 = 原文 ，1 = 原文摘要
     * @param Base64Certificate 签名时所用证书（Base64编码）
     * @return
     * @throws NetonejExcepption
     */
    private NetoneSVS verifyPKCS1Action(String data, String signature, String algo, int datt, String Base64Certificate) throws NetonejExcepption {
        try {
            Map<String, String> params = prepareParameter();
            params.put("data", data);
            params.put("signature", signature);
            params.put("datt", datt+"");
            params.put("cert", Base64Certificate);
            if (!NetonejUtil.isEmpty(algo)) {
                params.put("algo", algo);
            }
            NetoneResponse response = doHttpPost(Action.SVS_ACTION_VP1, params);
            return new NetoneSVS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-SVS(vp1)数字签名验证失败" + e, e);
        }
    }

    public NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DigestAlgorithm algo, DataType datt) throws NetonejExcepption {
        return verifyPKCS1ActionById(data,signature,algo.getName(),datt.ordinal(),id,idMagic.name());
    }

    public NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DataType datt) throws NetonejExcepption {
        return verifyPKCS1ActionById(data,signature,null,datt.ordinal(),id,idMagic.name());
    }

    public NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DigestAlgorithm algo) throws NetonejExcepption {
        return verifyPKCS1ActionById(data,signature,algo.getName(),DataType.PLAIN.ordinal(),id,idMagic.name());
    }

    public NetoneSVS verifyPKCS1(String id, IdMagic idMagic,String data,String signature) throws NetonejExcepption {
        return verifyPKCS1ActionById(data,signature,null,DataType.PLAIN.ordinal(),id,idMagic.name());
    }


    private NetoneSVS verifyPKCS1ActionById(String data, String signature, String algo, int datt, String id,String idMagic) throws NetonejExcepption {
        try {
            Map<String, String> params = prepareParameter();
            params.put("data", data);
            params.put("signature", signature);
            params.put("datt", datt+"");
            params.put("id", id);
            if (!NetonejUtil.isEmpty(idMagic)) {
                params.put("idmagic", idMagic.trim().toLowerCase());
            }
            if (!NetonejUtil.isEmpty(algo)) {
                params.put("algo", algo);
            }
            NetoneResponse response = doHttpPost(Action.SVS_ACTION_VP1, params);
            return new NetoneSVS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-SVS(vp1)数字签名验证失败" + e, e);
        }
    }

    /**
     * Detached（原文分离）模式验证PKCS#7数字签名
     * @param p7data  PKCS#7签名结果
     * @param p7odat  签名时的原文数据
     * @param dataB64 true = 原文数据是Base64编码格式，false = 原文数据是普通字符串格式。
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyPKCS7(String p7data, String p7odat, boolean dataB64) throws NetonejExcepption {
        try {
            Map<String, String> params = prepareParameter();
            params.put("p7data", p7data);
            if (!NetonejUtil.isEmpty(p7odat)) {
                if (dataB64) {
                    params.put("p7odat", p7odat);
                } else {
                    params.put("p7odat", NetonejUtil.base64Encode(p7odat.getBytes("utf-8")));
                }
            }
            NetoneResponse response = doHttpPost(Action.SVS_ACTION_VP7, params);
            return new NetoneSVS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-SVS（vp7）数字签名验证失败" + e, e);
        }
    }

    public NetoneSVS verifyPKCS7(String p7data, String p7odat) throws NetonejExcepption {
        return verifyPKCS7(p7data, p7odat, true);
    }

    /**
     * Attached（携带原文）模式验证PKCS#7数字签名
     *
     * @param p7data PKCS#7签名结果
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption
     */
    public NetoneSVS verifyPKCS7(String p7data) throws NetonejExcepption {
        return verifyPKCS7(p7data, "", true);
    }

    /**
     * 对文件验证PKCS#1数字签名
     * @param fileName          签名的原文件
     * @param signature         PKCS#1签名结果
     * @param algo              签名时选择的摘要算法 - RSA-MD5 = md5算法（RSA），RSA-SHA1=sha1算法（RSA），ECDSA-SM2-WITH-SM3 = sm3算法（ECC）
     * @param base64Certificate 签名时所用证书（Base64编码）
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     * @author wangjx 2015-4-18
     */
    //@Deprecated
//    public NetoneSVS verifyPKCS1File(String fileName, String signature, String algo, String base64Certificate) throws NetonejExcepption {
//        NetoneSVS netoneSVS = null;
//        try {
//            byte[] digest = null;
//            byte[] fileBinay = FileByteArrayReader.read(fileName);
//
//            if ("RSA-MD5".equalsIgnoreCase(algo)) {
//                digest = NetonejUtil.digestBinary(fileBinay, "MD5");
//            } else if ("RSA-SHA1".equalsIgnoreCase(algo)) {
//                digest = NetonejUtil.digestBinary(fileBinay, "SHA-1");
//            } else if ("ECDSA-SM2-WITH-SM3".equalsIgnoreCase(algo)) {
//                digest = sm3Digest(fileBinay, base64Certificate);
//            }
//            String sm3Digest = NetonejUtil.base64Encode(digest);
//            netoneSVS = this.verifyPKCS1(sm3Digest, signature, algo, "1", base64Certificate, true);
//        } catch (Exception e) {
//            throw new NetonejExcepption("-SVS（vp1）数字签名验证失败" + e, e);
//        }
//
//
//        return netoneSVS;
//    }

    /**
     * 验证XML签名
     *
     * @param data XML数据(Base64编码).
     * @return NetoneSVS 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption
     */
    public NetoneSVS verifyXML(String data) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("data", data);
        try {
            NetoneResponse response = doHttpPost(Action.SVS_ACTION_VX, params);
            return new NetoneSVS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-SVS（vx）验证XML签名失败" + e, e);
        }
    }


    /**
     * 枚举服务端证书列表
     * @return NetoneCertList  服务端证书列表
     * @throws NetonejExcepption
     */
    public NetoneCertList listCertificates() throws NetonejExcepption {
        try {
            NetoneResponse response = doHttpPost(Action.SVS_ACTION_LISTC, null);
            NetoneCertList list = new NetoneCertList(response.getStatusCode());
            if (response.getStatusCode() == 200) {
                list.setCertList((List<NetoneCertificate>) XmlparserFacotry.parseXmlString(response.getRetString()));
            }
            return list;
        } catch (Exception e) {
            throw new NetonejExcepption("-SVS（listc）枚举服务端的证书" + e, e);
        }
    }

    /**
     * 根据SM2证书，对数据计算SM3摘要
     *
     * @param b64Certificate Base64编码的数字证书C
     * @param data           待计算只要的原文数据
     * @return sm3摘要
     * @author wangjx 2015-4-18
     */
//    private byte[] sm3Digest(byte[] data, String b64Certificate) throws IOException, CertificateParsingException {
//        byte[] digest = new byte[32];
//
//        X509CertificateHolder holder = new X509CertificateHolder(Base64.getDecoder().decode(b64Certificate));
//        SM2X509Certificate sm2X509Certificate = new SM2X509Certificate(holder.toASN1Structure());
//        SM3Digest sm3Digest = new SM3Digest();
//
//        SM2BCPublicKey publicKey = (SM2BCPublicKey) sm2X509Certificate.getPublicKey();
//        ECPoint ecPoint = publicKey.getQ();
//        sm3Digest.addId(ecPoint.getAffineXCoord().toBigInteger(), ecPoint.getAffineYCoord().toBigInteger());
//
//        sm3Digest.update(data, 0, data.length);
//        sm3Digest.doFinal(digest, 0);
//
//        return digest;
//    }
}
