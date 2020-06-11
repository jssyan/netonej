/**
 * 文 件 名： SVSClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.math.ec.ECPoint;


import com.syan.netonej.common.FileByteArrayReader;
import com.syan.netonej.common.NetonejIdMagic;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.key.SM2BCPublicKey;
import com.syan.netonej.common.sm3.SM3Digest;
import com.syan.netonej.common.x509.SM2X509Certificate;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.HttpPostProcesserFactory;
import com.syan.netonej.http.HttpStatus;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.entity.NetoneSVS;
import com.syan.netonej.http.xml.Parser;
import com.syan.netonej.http.xml.XmlparserFacotry;

/**
 * NetONE SVS API
 *
 * @author gejq
 * @version 2.0.0
 * @since 1.0.0
 */
public class SVSClient extends BaseClient {

    private static final Log log = LogFactory.getLog(SVSClient.class);

    /**
     * SVS - Action 签名证书验证
     */
    private final static String SVS_ACTION_VC = "vc.svr";
    /**
     * SVS - Action PKCS#1签名验证
     */
    private final static String SVS_ACTION_VP1 = "vp1.svr";
    /**
     * SVS - Action PKCS#7签名验证
     */
    private final static String SVS_ACTION_VP7 = "vp7.svr";
    /**
     * SVS - Action XML签名验证
     */
    private final static String SVS_ACTION_VX = "vx.svr";
    /**
     * SVS - Action 获取证书列表
     */
    private final static String SVS_ACTION_LISTC = "listc.svr";

    /**
     * Parameters - 签名数据键值  "signature"
     */
    private final static String PARAME_SIGNATURE = "signature";
    /**
     * Parameters - p7签名数据键值  "p7data"
     */
    private final static String PARAME_P7DATA = "p7data";
    /**
     * Parameters - p7签名原文键值  "p7odat"
     */
    private final static String PARAME_P7ODAT = "p7odat";

    /**
     * Parameters - 签名算法键值  "algo"
     */
    private final static String PARAME_ALGO = "algo";
    /**
     * Parameters - 原文数据类型键值  "datt"
     */
    private final static String PARAME_DATT = "datt";

    /**
     * Parameters - 绿色通道键值  "greenpass"
     */
    private final static String PARAME_GREENPASS = "greenpass";
    /**
     * Parameters - 绿色通道键值  "greenpass" 默认值
     */
    private final static String PARAME_GREENPASS_VALUE = "0";

    /**
     * Parameters - ID类型键值  "idmagic"
     */
    private final static String PARAME_IDMAGIC = "idmagic";
    /**
     * Parameters - ID键值  "id"
     */
    private final static String PARAME_ID = "id";
    /**
     * PCS 服务IP
     */
    private String _host;

    /**
     * PCS 服务PORT
     */
    private String _port;

    /**
     * 证书验证方法
     */
    private final static String PARAME_VFLAG = "vflag";

    /**
     * Values - 摘要算法类型(SVS专用)  "RSA-MD5"
     */
    public final static String SVS_ALOG_MD5 = "RSA-MD5";
    /**
     * Values - 摘要算法类型(SVS专用)  "RSA-SHA1"
     */
    public final static String SVS_ALOG_SHA1 = "RSA-SHA1";
    /**
     * Values - 摘要算法类型(SVS专用)  "ECDSA-SM2-WITH-SM3"
     */
    public final static String SVS_ALOG_SM3 = "ECDSA-SM2-WITH-SM3";

    /**
     * 绿色通道 在服务端设置启用greenpass的情况下，客户端可以通过设置0禁用绿色通道
     */
    private boolean greenpass = false;

    /**
     * 证书验证方法. 0:不验证证书状态 0x02:验证证书链 0x04:CRL验证 0x08:CRL全验证,包括验证发行者证书. 如果既验证证书链,又验证CRL,则flag值为:(0x02|0x04)=0x06.
     */
    private String vflag = null;

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

    /**
     * SVSClient 初始化
     *
     * @param host 服务器IP
     * @param port 服务端口
     */
    public SVSClient(String host, String port) {
        super(HttpPostProcesserFactory.createPostProcesser());
        this._host = host;
        this._port = port;
    }

    /**
     * 设置可选参数
     *
     * @return 预置参数的MAP
     */
    private Map<String, String> prepareParameter() {

        Map<String, String> params = new HashMap<String, String>();
        params.put(RESPONSE_FORMAT_KEY, responseformat);

        //设置禁用绿色通道
        if (!this.greenpass) {
            params.put(PARAME_GREENPASS, PARAME_GREENPASS_VALUE);
        }

        //设置是否农行k宝二代应用
        if (!NetonejUtil.isEmpty(application)) {
            params.put(PARAME_APPLICATION, PARAME_APPLICATION_VALUE);
        }

        //证书验证方法
        if (!NetonejUtil.isEmpty(vflag)) {
            params.put(PARAME_VFLAG, vflag);
        }

        return params;
    }

    /**
     * 验证数字证书有效性
     *
     * @param Base64Certificate
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyCertificate(String Base64Certificate) throws NetonejExcepption {

        log.debug("-SVS（vc）Base64编码公钥证书：" + Base64Certificate);

        NetoneSVS svs = null;
        Map<String, String> params = prepareParameter();
        params.put(PARAME_CERT, Base64Certificate);
        params.put(RESPONSE_FORMAT_KEY, "2");

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(SVS_ACTION_VC), params);
            log.debug("netone response=" + response.toString());
            svs = new NetoneSVS(response);
        } catch (Exception e) {
            log.error("-SVS（vc）证书验证失败", e);
            throw new NetonejExcepption("-SVS（vc）证书验证失败" + e, e);
        }

        return svs;
    }

    /**
     * 验证数字证书有效性
     *
     * @param certDn
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyCertificateByDn(String certDn) throws NetonejExcepption {

        log.debug("-SVS（vc）DN：" + certDn);

        NetoneSVS svs = null;
        Map<String, String> params = prepareParameter();
        params.put("id", (String) NetonejUtil.getMapFromDN(certDn).get("CN"));
        params.put(PARAME_IDMAGIC, NetonejIdMagic.SCN);
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(SVS_ACTION_VC), params);
            log.debug("netone response=" + response.toString());
            svs = new NetoneSVS(response);
        } catch (Exception e) {
            log.error("-SVS（vc）证书验证失败", e);
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
    public NetoneSVS verifyPKCS1(byte[] plainText, String signature, String certDN) throws NetonejExcepption {

        log.debug("-SVS（vp1）data：" + NetonejUtil.base64Encode(plainText) + " signature：" + signature + " certDn：" + certDN);


        NetoneSVS svs = null;
        Map<String, String> params = prepareParameter();
        params.put(PARAME_DATA, NetonejUtil.base64Encode(plainText));
        params.put(PARAME_DATT, "0");
        params.put(PARAME_SIGNATURE, signature);
        params.put(PARAME_IDMAGIC, "scn");
        params.put(PARAME_ID, (String) NetonejUtil.getMapFromDN(certDN).get("CN"));
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(SVS_ACTION_VP1), params);
            log.debug("netone response=" + response.toString());
            svs = new NetoneSVS(response);
        } catch (Exception e) {
            log.error("-SVS(vp1)数字签名验证失败", e);
            throw new NetonejExcepption("-SVS(vp1)数字签名验证失败" + e, e);
        }

        return svs;
    }

    /**
     * 验证PKCS#1数字签名
     *
     * @param data              签名时的原文数据
     * @param signature         PKCS#1签名结果
     * @param algo              签名时选择的摘要算法 - RSA-MD5 = md5算法（RSA），RSA-SHA1=sha1算法（RSA），ECDSA-SM2-WITH-SM3 = sm3算法（ECC）
     * @param datt              签名时选择的原文类型 - 0 = 原文 ，1 = 原文摘要
     * @param Base64Certificate 签名时所用证书（Base64编码）
     * @param dataB64           true = 原文数据是Base64编码格式，false = 原文数据是普通字符串格式。
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyPKCS1(String data, String signature, String algo, String datt, String Base64Certificate, boolean dataB64) throws NetonejExcepption {

        log.debug("-SVS（vp1）data：" + data + " signature：" + signature + " algo：" + algo + " datt：" + datt);

        NetoneSVS svs = null;
        try {
            Map<String, String> params = prepareParameter();
            if (dataB64) {
                params.put(PARAME_DATA, data);
            } else {
                params.put(PARAME_DATA, NetonejUtil.base64Encode(data.getBytes("utf-8")));
            }
            params.put(PARAME_SIGNATURE, signature);
            params.put(PARAME_DATT, datt);
            params.put(PARAME_CERT, Base64Certificate);
            if (!NetonejUtil.isEmpty(algo)) {
                params.put(PARAME_ALGO, algo);
            }

            NetoneResponse response = processer.doPost(getServiceUrl(SVS_ACTION_VP1), params);
            log.debug("netone response=" + response.toString());
            svs = new NetoneSVS(response);
        } catch (Exception e) {
            log.error("-SVS(vp1)数字签名验证失败", e);
            throw new NetonejExcepption("-SVS(vp1)数字签名验证失败" + e, e);
        }

        return svs;
    }


    /**
     * 验证PKCS#1数字签名（使用以下缺省值：algo = RSA-SHA1）
     *
     * @param data              签名时的原文数据
     * @param signature         PKCS#1签名结果
     * @param datt              签名时选择的原文类型 - 0 = 原文，1 = 原文摘要
     * @param Base64Certificate 签名时所用证书（Base64编码）
     * @param dataB64           true = 原文数据是Base64编码格式，false = 原文数据是普通字符串格式。
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyPKCS1(String data, String signature, String datt, String Base64Certificate, boolean dataB64) throws NetonejExcepption {
        return verifyPKCS1(data, signature, "", datt, Base64Certificate, dataB64);
    }

    /**
     * 验证PKCS#1数字签名（使用以下缺省值：algo = RSA-SHA1，datt = 0）
     *
     * @param data              签名时的原文数据
     * @param signature         PKCS#1签名结果
     * @param Base64Certificate Base64Certificate 签名时所用证书（Base64编码）
     * @param dataB64           true = 原文数据是Base64编码格式，false = 原文数据是普通字符串格式。
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyPKCS1(String data, String signature, String Base64Certificate, boolean dataB64) throws NetonejExcepption {
        return verifyPKCS1(data, signature, "", "0", Base64Certificate, dataB64);
    }

    /**
     * Detached（原文分离）模式验证PKCS#7数字签名
     *
     * @param p7data  PKCS#7签名结果
     * @param p7odat  签名时的原文数据
     * @param dataB64 true = 原文数据是Base64编码格式，false = 原文数据是普通字符串格式。
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneSVS verifyPKCS7(String p7data, String p7odat, boolean dataB64) throws NetonejExcepption {

        log.debug("-SVS（vp7）p7data：" + p7data + " p7odat：" + p7odat + " dataB64：" + dataB64);

        NetoneSVS svs = null;
        try {
            Map<String, String> params = prepareParameter();
            params.put(PARAME_P7DATA, p7data);
            if (!NetonejUtil.isEmpty(p7odat)) {
                if (dataB64) {
                    params.put(PARAME_P7ODAT, p7odat);
                } else {
                    params.put(PARAME_P7ODAT, NetonejUtil.base64Encode(p7odat.getBytes("utf-8")));

                }
            }
            NetoneResponse response = processer.doPost(getServiceUrl(SVS_ACTION_VP7), params);
            log.debug("netone response=" + response.toString());
            svs = new NetoneSVS(response);
        } catch (Exception e) {
            log.error("-SVS（vp7）数字签名验证失败", e);
            throw new NetonejExcepption("-SVS（vp7）数字签名验证失败" + e, e);
        }

        return svs;
    }

    /**
     * Attached（携带原文）模式验证PKCS#7数字签名
     *
     * @param p7data PKCS#7签名结果
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption
     */
    public NetoneSVS verifyPKCS7(String p7data) throws NetonejExcepption {
        return verifyPKCS7(p7data, "", false);
    }

    /**
     * 对文件验证PKCS#1数字签名
     *
     * @param fileName          签名的原文件
     * @param signature         PKCS#1签名结果
     * @param algo              签名时选择的摘要算法 - RSA-MD5 = md5算法（RSA），RSA-SHA1=sha1算法（RSA），ECDSA-SM2-WITH-SM3 = sm3算法（ECC）
     * @param base64Certificate 签名时所用证书（Base64编码）
     * @return 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption API全局异常类
     * @author wangjx 2015-4-18
     */
    public NetoneSVS verifyPKCS1File(String fileName, String signature, String algo, String base64Certificate) throws NetonejExcepption {

        log.debug("-SVS（vp1）fileName：" + fileName + " signature：" + signature + " algo：" + algo);

        NetoneSVS netoneSVS = null;
        try {
            byte[] digest = null;
            byte[] fileBinay = FileByteArrayReader.read(fileName);

            if ("RSA-MD5".equalsIgnoreCase(algo)) {
                digest = NetonejUtil.digestBinary(fileBinay, "MD5");
            } else if ("RSA-SHA1".equalsIgnoreCase(algo)) {
                digest = NetonejUtil.digestBinary(fileBinay, "SHA-1");
            } else if ("ECDSA-SM2-WITH-SM3".equalsIgnoreCase(algo)) {
                digest = sm3Digest(fileBinay, base64Certificate);
            }
            String sm3Digest = NetonejUtil.base64Encode(digest);
            netoneSVS = this.verifyPKCS1(sm3Digest, signature, algo, "1", base64Certificate, true);
        } catch (Exception e) {
            log.error("-SVS（vp1）数字签名验证失败", e);
            throw new NetonejExcepption("-SVS（vp1）数字签名验证失败" + e, e);
        }


        return netoneSVS;
    }

    /**
     * 验证XML签名
     *
     * @param data XML数据(Base64编码).
     * @return NetoneSVS 验证结果：200 = 正常，其他均为失败
     * @throws NetonejExcepption
     */
    public NetoneSVS verifyXML(String data) throws NetonejExcepption {

        log.debug("-SVS（vx）data：" + data);

        NetoneSVS svs = null;
        Map<String, String> params = prepareParameter();
        params.put(PARAME_DATA, data);
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(SVS_ACTION_VX), params);
            log.debug("netone response=" + response.toString());
            svs = new NetoneSVS(response);
        } catch (Exception e) {
            log.error("-SVS（vx）验证XML签名失败", e);
            throw new NetonejExcepption("-SVS（vx）验证XML签名失败" + e, e);
        }
        return svs;
    }


    /**
     * 枚举服务端证书列表
     *
     * @return NetoneCertList  服务端证书列表
     * @throws NetonejExcepption
     */
    public NetoneCertList listCertificates() throws NetonejExcepption {

        log.debug("-SVS（listc）");

        NetoneCertList list = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put(RESPONSE_FORMAT_KEY, responseformat);
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(SVS_ACTION_LISTC), params);
            log.debug("netone response=" + response.toString());
            list = new NetoneCertList(response.getStatusCode());
            if (response.getStatusCode() == HttpStatus.SC_OK) {
                list.setCertList((List<NetoneCertificate>) XmlparserFacotry.parseXmlString(response.getRetString()));
            }
        } catch (Exception e) {

            log.error("-SVS（listc）枚举服务端的证书", e);

            throw new NetonejExcepption("-SVS（listc）枚举服务端的证书" + e, e);
        }
        return list;
    }


    /**
     * 获取证书验证方法取值
     *
     * @return 证书验证方法. 0:不验证证书状态 0x02:验证证书链 0x04:CRL验证 0x08:CRL全验证,包括验证发行者证书. 如果既验证证书链,又验证CRL,则flag值为:(0x02|0x04)=0x06.
     */
    public String getVflag() {
        return vflag;
    }


    /**
     * 设置证书验证方法
     *
     * @param vflag 证书验证方法. 0:不验证证书状态 0x02:验证证书链 0x04:CRL验证 0x08:CRL全验证,包括验证发行者证书. 如果既验证证书链,又验证CRL,则flag值为:(0x02|0x04)=0x06.
     */
    public void setVflag(String vflag) {
        this.vflag = vflag;
    }


    /* (non-Javadoc)
     * @see com.syan.netonej.http.client.BaseClient#getHostIp()
     */
    @Override
    public String getHostIp() {
        return this._host;
    }

    /* (non-Javadoc)
     * @see com.syan.netonej.http.client.BaseClient#getPort()
     */
    @Override
    public String getPort() {
        return this._port;
    }

    /**
     * 根据SM2证书，对数据计算SM3摘要
     *
     * @param b64Certificate Base64编码的数字证书C
     * @param data           待计算只要的原文数据
     * @return sm3摘要
     * @author wangjx 2015-4-18
     */
    private byte[] sm3Digest(byte[] data, String b64Certificate) throws IOException, CertificateParsingException {
        byte[] digest = new byte[32];

        X509CertificateHolder holder = new X509CertificateHolder(Base64.decodeBase64(b64Certificate));
        SM2X509Certificate sm2X509Certificate = new SM2X509Certificate(holder.toASN1Structure());
        SM3Digest sm3Digest = new SM3Digest();

        SM2BCPublicKey publicKey = (SM2BCPublicKey) sm2X509Certificate.getPublicKey();
        ECPoint ecPoint = publicKey.getQ();
        sm3Digest.addId(ecPoint.getAffineXCoord().toBigInteger(), ecPoint.getAffineYCoord().toBigInteger());

        sm3Digest.update(data, 0, data.length);
        sm3Digest.doFinal(digest, 0);

        return digest;
    }

}
