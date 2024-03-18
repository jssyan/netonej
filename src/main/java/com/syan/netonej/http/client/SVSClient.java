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

    public StampVerifyBuilder stampVerifyBuilder(){
        return new StampVerifyBuilder().setHost(host).setPort(port);
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

    @Deprecated
    public NetoneSVS verifyCertificate(String base64Certificate, String signts) throws NetonejException {
        return certificateVerifyBuilder().setCert(base64Certificate).setSignts(signts).build();
    }

    @Deprecated
    public NetoneSVS verifyCertificate(String Base64Certificate) throws NetonejException {
        return verifyCertificate(Base64Certificate,"");
    }

    @Deprecated
    public NetoneSVS verifyCertificate(String id, IdMagic idMagic) throws NetonejException {
        return verifyCertificate(id,idMagic,"");
    }

    @Deprecated
    public NetoneSVS verifyCertificate(String id,IdMagic idMagic,String signts) throws NetonejException {
        return certificateVerifyBuilder().setId(id).setIdMagic(idMagic).setSignts(signts).build();
    }

    @Deprecated
    public NetoneSVS verifyCertificateByDn(String certDn) throws NetonejException {
        return verifyCertificate(certDn,IdMagic.SCN,"");
    }

    @Deprecated
    public NetoneCertList listCertificates() throws NetonejException {
        return certificateListBuilder().build();
    }

    @Deprecated
    public NetoneSVS verifyXML(String data) throws NetonejException {
        return xmlSignVerifyBuilder().setBase64Data(data).build();
    }

    @Deprecated
    public NetoneSVS verifyPKCS7(String p7data) throws NetonejException {
        return verifyPKCS7(p7data,null);
    }

    @Deprecated
    public NetoneSVS verifyPKCS7(String p7data, String p7odat) throws NetonejException {
        return pkcs7VerifyBuilder().setBase64Data(p7odat).setBase64Pkcs7(p7data).build();
    }

    @Deprecated
    public NetoneSVS verifyPKCS7(String p7data, String p7odat, boolean dataB64) throws NetonejException {
        PKCS7VerifyBuilder builder = pkcs7VerifyBuilder().setBase64Pkcs7(p7data);
        if(dataB64){
            builder.setBase64Data(p7odat);
        }else{
            builder.setData(p7odat);
        }
        return builder.build();
    }

    @Deprecated
    public NetoneSVS verifyPKCS1(byte[] plainText, String signature, String certDN) throws NetonejException {
        return pkcs1VerifyBuilder()
                .setBase64Signature(signature)
                .setData(plainText)
                .setId(certDN)
                .setIdmagic(IdMagic.SCN)
                .build();
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, String algo, String datt, String certificate, boolean dataB64) throws NetonejException {
        PKCS1VerifyBuilder builder = pkcs1VerifyBuilder().setBase64Signature(signature);
        if(!dataB64){
            data = Base64.toBase64String(data.getBytes());
        }
        builder.setBase64Data(data).setAlgo(algo).setCert(certificate);
        if(datt.equals("0")){
            builder.setDataType(DataType.PLAIN);
        }else{
            builder.setDataType(DataType.DIGEST);
        }
        return builder.build();
    }

    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, String datt, String Base64Certificate, boolean dataB64) throws NetonejException {
        return verifyPKCS1(data, signature, null,datt, Base64Certificate, dataB64);
    }

    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, String base64Certificate, boolean dataB64) throws NetonejException {
        if(!dataB64){
            data = Base64.toBase64String(data.getBytes());
        }
        return pkcs1VerifyBuilder()
                .setBase64Signature(signature)
                .setBase64Data(data)
                .setCert(base64Certificate)
                .setDataType(DataType.PLAIN)
                .build();
    }

    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, DigestAlgorithm algo, DataType datt, String certificate) throws NetonejException {
        return pkcs1VerifyBuilder()
                .setBase64Signature(signature)
                .setBase64Data(data)
                .setAlgo(algo)
                .setCert(certificate).setDataType(datt).build();
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, DataType datt, String certificate) throws NetonejException {
        return verifyPKCS1(data,signature,null,datt,certificate);
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, DigestAlgorithm algo, String certificate) throws NetonejException {
        return verifyPKCS1(data,signature,algo,null,certificate);
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String data, String signature, String certificate) throws NetonejException {
        return verifyPKCS1(data,signature,null,null,certificate);
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DigestAlgorithm algo, DataType datt) throws NetonejException {
        return pkcs1VerifyBuilder()
                .setBase64Signature(signature)
                .setBase64Data(data)
                .setAlgo(algo)
                .setId(id)
                .setIdmagic(idMagic)
                .setDataType(datt)
                .build();
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DataType datt) throws NetonejException {
        return verifyPKCS1(id,idMagic,data,signature,null,datt);
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String id,IdMagic idMagic,String data, String signature, DigestAlgorithm algo) throws NetonejException {
        return verifyPKCS1(id,idMagic,data,signature,algo,null);
    }
    @Deprecated
    public NetoneSVS verifyPKCS1(String id, IdMagic idMagic,String data,String signature) throws NetonejException {
        return verifyPKCS1(id,idMagic,data,signature,null,null);
    }
}
