/**
 * 文 件 名:  NetoneCertificate.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  基类
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.http.entity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;


import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;


import com.syan.netonej.common.NetonejUtil;

/**
 * NetoneCertificate 可获取证书对象、主题项、序列号、DER编码、BASE64编码等等相关信息
 *
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneCertificate extends NetoneBase {

    private static Map<String, String> oidMap = new HashMap<String, String>();

    static {
        oidMap.put("2.5.4.45", "x500uniqueidentifier");
        oidMap.put("2.5.4.45.17", "x500uniqueidentifier");
    }

    /**
     * 计算证书指纹默认算法
     */
    private static String ALGORITHM = "MD5";

    /**
     * X509Certificate
     */
    private X509CertificateHolder certificate;

    /**
     * 证书base64编码
     */
    private String certBase64String;

    /**
     * 根据证书base64编码数据构造NetoneCertificate实例
     *
     * @param certBase64String
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneCertificate(String certBase64String) throws CertificateException, IOException {
        if (!NetonejUtil.isEmpty(certBase64String)) {
            this.certBase64String = certBase64String;
            this.certificate = new X509CertificateHolder(getCertEncoded());
        }
    }

    /**
     * 根据证书编码数据构造NetoneCertificate实例
     *
     * @param cert 证书编码数据
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneCertificate(byte[] cert) throws CertificateException, IOException {
        if (cert != null) {
            this.certBase64String = new String(Base64.getEncoder().encodeToString(cert));
            this.certificate = new X509CertificateHolder(cert);
        }
    }

    /**
     * 根据服务返回状态码构造NetoneCertificate实例
     *
     * @param statusCode
     * @throws CertificateException
     */
    public NetoneCertificate(int statusCode) throws CertificateException {
        super(statusCode);
    }

    /**
     * 根据服务返回NetoneResponse信息构造NetoneCertificate实例
     *
     * @param response
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneCertificate(NetoneResponse response) throws CertificateException, IOException {
        super(response.getStatusCode());
        if (response.getRetBytes() != null) {
            this.certBase64String = response.getRetString();
            this.certificate = new X509CertificateHolder(getCertEncoded());
        }
    }


    /**
     * 获取证书主题项
     *
     * @return 证书主题项, 证书不存在 返回NULL
     * @throws IOException
     */
    public String getSubject() {
        return certificate == null ? null : canonicalX500Name(certificate.getSubject()).toString();
    }

    /**
     * 取得证书签发者
     *
     * @return 证书签发者的主题项 ,证书不存在 返回NULL
     * @throws IOException
     */
    public String getIssuer() {
        return certificate == null ? null : canonicalX500Name(certificate.getIssuer()).toString();
    }

    /**
     * 取得证书的有效开始日期（NotBefore）
     *
     * @return Date类型的日期, 证书不存在 返回NULL
     */
    public Date getNotBefore() {
        return certificate == null ? null : certificate.getNotBefore();
    }

    /**
     * 取得证书的有效结束日期（NotAfter）
     *
     * @return Date类型的日期 ,证书不存在 返回NULL
     */
    public Date getNotAfter() {
        return certificate == null ? null : certificate.getNotAfter();
    }

    /**
     * 取得16进制的证书序列号字符串
     *
     * @return 16进制的序列号字符串, 证书不存在 返回NULL
     */
    public String getHexSerial() {
        return certificate == null ? null : certificate.getSerialNumber().toString(16).toUpperCase();
    }

    /**
     * 取得十进制的证书序列号字符串
     *
     * @return 十进制的序列号字符串, 证书不存在 返回NULL
     */
    public String getDecSerial() {
        return certificate == null ? null : certificate.getSerialNumber().toString();
    }

    public String getVersion() {
        return certificate == null ? null : certificate.getVersionNumber()+"";
    }

    public String getPublicKey() {
        return certificate == null ? null : Base64.getEncoder().encodeToString(certificate.getSubjectPublicKeyInfo().getPublicKeyData().getBytes());
    }

    public Set getCriticalExtensionOIDs() {
        return certificate == null ? null : certificate.getCriticalExtensionOIDs();
    }

    public Set getNonCriticalExtensionOIDs() {
        return certificate == null ? null : certificate.getNonCriticalExtensionOIDs();
    }

    public Extensions getExtensions() {
        if(certificate == null){
            return null;
        }
        return certificate.getExtensions();
    }

    public byte[] getExtension(String identifier ) {
        if(certificate == null){
           return null;
        }
        return certificate.getExtension(new ASN1ObjectIdentifier(identifier)).getExtnValue().getOctets();
    }

    public List<String> getExtensionOIDs() {
        return certificate == null ? null : certificate.getExtensionOIDs();
    }

    /**
     * 获取证书编码数据
     *
     * @return 证书编码数据
     */
    public byte[] getCertEncoded() {
        return certBase64String == null ? null : Base64.getDecoder().decode(certBase64String);
    }

    /**
     * 获取证书base64编码字符串
     *
     * @return base64编码字符串
     */
    public String getCertBase64String() {
        return certBase64String;
    }

    /**
     * 获取证书指纹
     *
     * @param algo 指定算法算法名称：SHA1、MD5
     * @return 指定算法的证书指纹
     * @throws NoSuchAlgorithmException
     */
    public String getFingerprint(String algo) throws NoSuchAlgorithmException {
        String fingerprint = null;
        if (certBase64String != null) {
            if (ALGORITHM.equals(algo)) {
                fingerprint = NetonejUtil.md5Encode(getCertEncoded());
            } else {
                fingerprint = NetonejUtil.sha1Encode(getCertEncoded());
            }
        }
        return fingerprint;
    }

    private X500Name canonicalX500Name(X500Name oldX500Name) {

        RDN[] newRDNs = transferDERT61String2DERUTF8String(oldX500Name.getRDNs());

        return new X500Name(newRDNs);
    }

    private RDN[] transferDERT61String2DERUTF8String(RDN[] oldRDNs) {

        RDN[] newRDNs = new RDN[oldRDNs.length];

        AttributeTypeAndValue attributeTypeAndValue = null;

        ASN1Encodable t61Value = null;
        ASN1Encodable utf8Value = null;
        int index = 0;
        for (RDN old : oldRDNs) {

            attributeTypeAndValue = old.getFirst();

            t61Value = attributeTypeAndValue.getValue();

            if (t61Value instanceof DERT61String) {
                try {
                    utf8Value = new DERUTF8String(new String(((DERT61String) t61Value).getOctets(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                attributeTypeAndValue = new AttributeTypeAndValue(attributeTypeAndValue.getType(), utf8Value);

                newRDNs[index] = new RDN(attributeTypeAndValue);
            } else {
                newRDNs[index] = old;
            }

            index++;
        }

        return newRDNs;
    }

    /**
     * @return
     */
    public String getPublicKeyAlgo() {
        if (certificate == null) {
            return null;
        }

        if ("1.2.840.113549.1.1.1".equals(certificate.getSubjectPublicKeyInfo().getAlgorithmId().getAlgorithm().getId())) {
            return "RSA";
        } else {
            return "SM2";
        }
    }
}
