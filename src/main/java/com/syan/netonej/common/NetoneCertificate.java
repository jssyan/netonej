/**
 * 文 件 名:  NetoneCertificate.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  基类
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.common;

import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.KeyListItem;
import com.syan.netonej.http.entity.NetoneResponse;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * NetoneCertificate 可获取证书对象、主题项、序列号、DER编码、BASE64编码等等相关信息
 *
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneCertificate extends NetoneResponse {

    /**
     * X509Certificate
     */
    private X509Certificate certificate;

    private String hasPrivkey = "0";

    /**
     * 根据证书base64编码数据构造NetoneCertificate实例
     * @param certBase64String
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneCertificate(String certBase64String) throws NetonejException {
        parseCertificate(certBase64String,"0");
    }

    public void parseCertificate(String certBase64String,String privkry) throws NetonejException {
        if (NetonejUtil.isEmpty(certBase64String)) {
            throw new NetonejException("证书内容不能为空");
        }
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(certBase64String));
            CertificateFactory factory = new CertificateFactory();
            certificate = (X509Certificate) factory.engineGenerateCertificate(in);
            this.hasPrivkey = privkry;
            setStatusCode(200);
        } catch (Exception e) {
            setStatusCode(-1);
            throw new NetonejException("证书解析失败");
        }
    }

    /**
     * 根据证书编码数据构造NetoneCertificate实例
     * @param cert 证书编码数据
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneCertificate(byte[] cert) throws NetonejException {
        parseCertificate(Base64.toBase64String(cert),"0");
    }

    /**
     * 根据服务返回NetoneResponse信息构造NetoneCertificate实例
     *
     * @param response
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneCertificate(NetoneResponse response) throws NetonejException {
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            if(response.getFormat() == ResponseFormat.TEXT){
                parseCertificate(result,"0");
            }else{
                KeyListItem item = XMLParser.parserCert(new ByteArrayInputStream(result.getBytes()));
                if(item == null){
                    throw new NetonejException("证书解析失败");
                }
                parseCertificate(item.getCertificate(),item.getPrivk());
            }
        }
    }


    public String getHasPrivkey() {
        return hasPrivkey;
    }

    /**
     * 获取证书主题项
     *
     * @return 证书主题项, 证书不存在 返回NULL
     * @throws IOException
     */
    public String getSubject() {
        return certificate.getSubjectDN().toString();
    }

    /**
     * 取得证书签发者
     *
     * @return 证书签发者的主题项 ,证书不存在 返回NULL
     * @throws IOException
     */
    public String getIssuer() {
        return certificate.getIssuerDN().toString();
    }

    /**
     * 取得证书的有效开始日期（NotBefore）
     *
     * @return Date类型的日期, 证书不存在 返回NULL
     */
    public Date getNotBefore() {
        return certificate.getNotBefore();
    }

    public String getNotBefore(String format) {
        return NetonejUtil.dateFormat(getNotBefore(),format);
    }

    /**
     * 取得证书的有效结束日期（NotAfter）
     *
     * @return Date类型的日期 ,证书不存在 返回NULL
     */
    public Date getNotAfter() {
        return certificate == null ? null : certificate.getNotAfter();
    }

    public String getNotAfter(String format) {
        return NetonejUtil.dateFormat(getNotAfter(),format);
    }

    /**
     * 取得16进制的证书序列号字符串
     * @return 16进制的序列号字符串, 证书不存在 返回NULL
     */
    public String getHexSerial() {
        return certificate.getSerialNumber().toString(16).toUpperCase();
    }

    /**
     * 取得十进制的证书序列号字符串
     *
     * @return 十进制的序列号字符串, 证书不存在 返回NULL
     */
    public String getDecSerial() {
        return certificate.getSerialNumber().toString();
    }

    public int getVersion() {
        return certificate.getVersion();
    }

    public String getPublicKey() {
        return Base64.toBase64String(certificate.getPublicKey().getEncoded());
    }

    public Set<String> getCriticalExtensionOIDs() {
        return certificate.getCriticalExtensionOIDs();
    }

    public Set<String> getNonCriticalExtensionOIDs() {
        return certificate.getNonCriticalExtensionOIDs();
    }

    public byte[] getExtension(String identifier) {
        return certificate.getExtensionValue(identifier);
    }

    public List<String> getExtendedKeyUsage() throws NetonejException{
        try {
            return certificate.getExtendedKeyUsage();
        } catch (CertificateParsingException e) {
            throw new NetonejException("证书解析失败",e);
        }
    }

    /**
     * 获取证书编码数据
     *
     * @return 证书编码数据
     */
    public byte[] getEncoded() throws NetonejException {
        try {
            return certificate.getEncoded();
        } catch (CertificateEncodingException e) {
            throw new NetonejException("证书编码失败",e);
        }
    }

    /**
     * 获取证书base64编码字符串
     *
     * @return base64编码字符串
     */
    public String getBase64String() throws NetonejException{
        return Base64.toBase64String(getEncoded());
    }

    public String getPEMString() throws NetonejException{
        try {
            return NetonejUtil.base64StringtoPem(getBase64String());
        } catch (IOException e) {
            throw new NetonejException("转换为PEM格式失败",e);
        }
    }

    /**
     * 获取证书指纹()SHA1
     * @return 指定算法的证书指纹
     * @throws NoSuchAlgorithmException
     */
    public String getFingerprint() throws NetonejException {
        NetoneDigest digest = new NetoneDigest("SHA1");
        return NetonejUtil.byte2HexString(digest.digest(getEncoded()));
    }

    public String getPublicKeyAlgo() {
        return certificate.getPublicKey().getAlgorithm();
    }

    public X509Certificate getX509Certificate() {
        return certificate;
    }
}
