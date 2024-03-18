
package com.syan.netonej.http.client;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.pcs.*;
import com.syan.netonej.http.entity.*;
import org.bouncycastle.util.encoders.Base64;

public class PCSClient{

    protected String host;

    protected String port = "9178";

    public PCSClient(String host) {
        this.host = host;
    }

    public PCSClient(String host, String port) {
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

    public KeyBuilder keyBuilder(){
        return new KeyBuilder().setHost(host).setPort(port);
    }

    public CertificateBuilder certificateBuilder(){
        return new CertificateBuilder().setHost(host).setPort(port);
    }

    public EnvelopePacketBuilder envelopePacketBuilder(){
        return new EnvelopePacketBuilder().setHost(host).setPort(port);
    }

    public EnvelopeUnpackBuilder envelopeUnpackBuilder(){
        return new EnvelopeUnpackBuilder().setHost(host).setPort(port);
    }

    public PinBuilder pinBuilder(){
        return new PinBuilder().setHost(host).setPort(port);
    }

    public PKCS1Builder pkcs1Builder(){
        return new PKCS1Builder().setHost(host).setPort(port);
    }

    public PKCS7Builder pkcs7Builder(){
        return new PKCS7Builder().setHost(host).setPort(port);
    }

    public PrivateKeyBuilder privateKeyBuilder(){
        return new PrivateKeyBuilder().setHost(host).setPort(port);
    }

    public PublicKeyBuilder publicKeyBuilder(){
        return new PublicKeyBuilder().setHost(host).setPort(port);
    }

    public XmlSignBuilder xmlSignBuilder(){
        return new XmlSignBuilder().setHost(host).setPort(port);
    }

    public FileSignBuilder fileSignBuilder(){
        return new FileSignBuilder().setHost(host).setPort(port);
    }

    @Deprecated
    public NetoneKeyList getPcsIds() throws NetonejException {
        return keyBuilder().build();
    }
    @Deprecated
    public NetoneKeyList getKids() throws NetonejException {
        return keyBuilder().build();
    }
    @Deprecated
    public NetoneKeyList getKids(int limit) throws NetonejException {
        return keyBuilder().setLimit(limit).build();
    }
    @Deprecated
    public NetoneKeyList getKids(KeyAlgorithm keyAlgorithm) throws NetonejException {
        return keyBuilder().setKeyAlgorithm(keyAlgorithm).build();
    }

    @Deprecated
    public NetoneKeyList getKids(KeyUseage keyUseage) throws NetonejException {
        return keyBuilder().setKeyUseage(keyUseage).build();
    }

    @Deprecated
    public NetoneKeyList getKids(int limit,KeyAlgorithm keyAlgorithm) throws NetonejException {
        return keyBuilder().setLimit(limit).setKeyAlgorithm(keyAlgorithm).build();
    }

    @Deprecated
    public NetoneKeyList getKids(int limit,KeyUseage keyUseage) throws NetonejException {
        return keyBuilder().setLimit(limit).setKeyUseage(keyUseage).build();
    }

    @Deprecated
    public NetoneKeyList getKids(KeyAlgorithm keyAlgorithm,KeyUseage keyUseage) throws NetonejException {
        return keyBuilder().setKeyAlgorithm(keyAlgorithm).setKeyUseage(keyUseage).build();
    }

    @Deprecated
    public NetoneKeyList getKids(int limit, KeyAlgorithm keyAlgorithm,KeyUseage keyUseage) throws NetonejException {
        return keyBuilder().setLimit(limit).setKeyAlgorithm(keyAlgorithm).setKeyUseage(keyUseage).build();
    }

    @Deprecated
    public NetoneCertificate getBase64CertificateById(String id, String idMagic) throws NetonejException {
        return certificateBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase())).build();
    }

    @Deprecated
    public NetoneCertificate getBase64CertificateById(String id) throws NetonejException {
        return getBase64CertificateById(id,IdMagic.KID.name());
    }

    @Deprecated
    public NetoneCertificate getBase64CertificateById(String id, IdMagic idMagic) throws NetonejException {
        return certificateBuilder().setId(id).setIdmagic(idMagic).build();
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(byte[] plainText, String certDN, String pwd, String algo) throws NetonejException {
        return pkcs1Builder().setId(certDN).setData(plainText).setPasswd(pwd).setAlgo(algo).build();
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd, String idMagic, byte[] data, String datt, String algo) throws NetonejException {
        PKCS1Builder builder = pkcs1Builder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setBase64Data(Base64.toBase64String(data))
                .setPasswd(pwd)
                .setAlgo(algo);
        if(datt.equals("0")){
            builder.setDataType(DataType.PLAIN);
        }else{
            builder.setDataType(DataType.DIGEST);
        }
        return builder.build();
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd, String idMagic, String data, String datt, String algo,String signerId) throws NetonejException {
        PKCS1Builder builder = pkcs1Builder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setPasswd(pwd)
                .setAlgo(algo);
        if(datt.equals("0")){
            builder.setDataType(DataType.PLAIN);
            builder.setBase64Data(Base64.toBase64String(data.getBytes()));
        }else{
            builder.setDataType(DataType.DIGEST);
            builder.setBase64Data(data);
        }
        builder.setUserId(signerId.getBytes());
        return builder.build();
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd, IdMagic idMagic, String data, DataType datatype, DigestAlgorithm algo, String signerId) throws NetonejException {
        return pkcs1Builder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setAlgo(algo)
                .setBase64Data(data)
                .setDataType(datatype)
                .setUserId(signerId.getBytes()).build();
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd,IdMagic idMagic, String data, DataType datatype) throws NetonejException {
        return pkcs1Builder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setDataType(datatype).build();
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd, String data, DataType datatype, DigestAlgorithm algo) throws NetonejException {
        return pkcs1Builder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAlgo(algo)
                .setDataType(datatype).build();
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd, String data, DataType datatype) throws NetonejException {
        return pkcs1Builder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setDataType(datatype).build();
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, String idMagic, String data, boolean attach, String algo) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .setAlgo(algo)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, boolean attach, DigestAlgorithm algo) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .setAlgo(algo)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, DigestAlgorithm algo) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAlgo(algo)
                .build();
        return new NetoneSignPKCS7(pcs);
    }
    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data,boolean attach) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .build();
        return new NetoneSignPKCS7(pcs);
    }
    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .build();
        return new NetoneSignPKCS7(pcs);
    }
    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, DigestAlgorithm algo) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .setAlgo(algo)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, boolean fullchain,DigestAlgorithm algo) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .setFullchain(fullchain)
                .setAlgo(algo)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, boolean fullchain) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .setFullchain(fullchain)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, boolean attach, boolean fullchain,DigestAlgorithm algo) throws NetonejException {
        NetoneResponse pcs = pkcs7Builder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setAttach(attach)
                .setFullchain(fullchain)
                .setAlgo(algo)
                .build();
        return new NetoneSignPKCS7(pcs);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, String idMagic, String data, String base64Certificate) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id)
                .setPasswd(pwd).setIdmagic(IdMagic.valueOf(idMagic))
                .setBase64Data(data).setPeer(base64Certificate).build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, String idMagic, String data, String base64Certificate, String cipher) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id)
                .setPasswd(pwd).setIdmagic(IdMagic.valueOf(idMagic))
                .setBase64Data(data)
                .setPeer(base64Certificate)
                .setCipherAlgo(cipher)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic, String data, String base64Certificate, CipherAlgorithm cipher) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id)
                .setPasswd(pwd).setIdmagic(idMagic)
                .setBase64Data(data)
                .setPeer(base64Certificate)
                .setCipherAlgo(cipher)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, String data, String base64Certificate, CipherAlgorithm cipher) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setPeer(base64Certificate)
                .setCipherAlgo(cipher)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, String data, CipherAlgorithm cipher) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setCipherAlgo(cipher)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data, CipherAlgorithm cipher) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setCipherAlgo(cipher)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, String data, String base64Certificate) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setPeer(base64Certificate)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data, String base64Certificate) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .setPeer(base64Certificate)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, String data) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id)
                .setPasswd(pwd)
                .setBase64Data(data)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data) throws NetonejException {
        NetoneResponse res = envelopePacketBuilder().setId(id).setIdmagic(idMagic)
                .setPasswd(pwd)
                .setBase64Data(data)
                .build();
        return new NetoneEnvelope(res);
    }

    @Deprecated
    public NetonePCS envelopeUnpack(String id, String pwd, String idMagic, String envelope) throws NetonejException {
        return envelopeUnpackBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase())).setPasswd(pwd).setBase64Data(envelope).build();
    }

    @Deprecated
    public NetonePCS envelopeUnpack(String id, String pwd, IdMagic idMagic, String envelope) throws NetonejException {
        return envelopeUnpackBuilder().setId(id).setIdmagic(idMagic).setPasswd(pwd)
                .setBase64Data(envelope).build();
    }

    @Deprecated
    public NetonePCS envelopeUnpack(String id, String pwd, String envelope) throws NetonejException {
        return envelopeUnpackBuilder().setId(id).setPasswd(pwd)
                .setBase64Data(envelope).build();
    }

    @Deprecated
    public NetonePCS priKeyEncrypt(String id, String pwd, String idMagic, String data) throws NetonejException {
        return privateKeyBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setBase64Data(data).setPasswd(pwd).setEncrypt().build();
    }

    @Deprecated
    public NetonePCS priKeyEncrypt(String id, String pwd, String data) throws NetonejException {
        return privateKeyBuilder().setId(id)
                .setBase64Data(data).setPasswd(pwd).setEncrypt().build();
    }

    @Deprecated
    public NetonePCS priKeyEncrypt(String id, String pwd,IdMagic idMagic, String data) throws NetonejException {
        return privateKeyBuilder().setId(id).setIdmagic(idMagic).setEncrypt()
                .setBase64Data(data).setPasswd(pwd).build();
    }

    @Deprecated
    public NetonePCS priKeyDecrypt(String id, String pwd, String idMagic, String encryptData) throws NetonejException {
        return privateKeyBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setBase64Data(encryptData).setPasswd(pwd).setDecrypt().build();
    }

    @Deprecated
    public NetonePCS priKeyDecrypt(String id, String pwd, IdMagic idMagic, String encryptData) throws NetonejException {
        return privateKeyBuilder().setId(id).setIdmagic(idMagic)
                .setBase64Data(encryptData).setPasswd(pwd).setDecrypt().build();
    }

    @Deprecated
    public NetonePCS priKeyDecrypt(String id, String pwd, String encryptData) throws NetonejException {
        return privateKeyBuilder().setId(id)
                .setBase64Data(encryptData).setPasswd(pwd).setDecrypt().build();
    }

    @Deprecated
    public NetonePCS pubKeyEncrypt(String id, String idMagic, String data) throws NetonejException {
        return publicKeyBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setBase64Data(data).setEncrypt().build();
    }

    @Deprecated
    public NetonePCS pubKeyEncrypt(String id, IdMagic idMagic, String data) throws NetonejException {
        return publicKeyBuilder().setId(id).setIdmagic(idMagic).setBase64Data(data).setEncrypt().build();
    }

    @Deprecated
    public NetonePCS pubKeyEncrypt(String id, String data) throws NetonejException {
        return publicKeyBuilder().setId(id).setBase64Data(data).setEncrypt().build();
    }

    @Deprecated
    public NetonePCS pubKeyDecrypt(String id, String idMagic, String encryptData) throws NetonejException {
        return publicKeyBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setBase64Data(encryptData).setDecrypt().build();
    }

    @Deprecated
    public NetonePCS pubKeyDecrypt(String id, IdMagic idMagic, String encryptData) throws NetonejException {
        return publicKeyBuilder().setId(id).setIdmagic(idMagic)
                .setBase64Data(encryptData).setDecrypt().build();
    }

    @Deprecated
    public NetonePCS pubKeyDecrypt(String id, String encryptData) throws NetonejException {
        return publicKeyBuilder().setId(id)
                .setBase64Data(encryptData).setDecrypt().build();
    }

    @Deprecated
    public NetonePCS createXMLSignature(String id, String pwd, String idMagic, String database64) throws NetonejException {
        return xmlSignBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase())).setPasswd(pwd)
                .setBase64Data(database64).build();
    }

    @Deprecated
    public NetonePCS createXMLSignature(String id, String pwd, String database64) throws NetonejException {
        return xmlSignBuilder().setId(id).setPasswd(pwd)
                .setBase64Data(database64).build();
    }

    @Deprecated
    public NetonePCS createXMLSignature(String id, String pwd, String database64,SignMode signMode) throws NetonejException {
        return xmlSignBuilder().setId(id).setPasswd(pwd)
                .setBase64Data(database64).setSigmode(signMode).build();
    }

    @Deprecated
    public NetonePCS createXMLSignature(String id, String pwd, IdMagic idMagic,String database64) throws NetonejException {
        return xmlSignBuilder().setId(id).setPasswd(pwd).setIdmagic(idMagic)
                .setBase64Data(database64).build();
    }

    @Deprecated
    public NetonePCS createXMLSignature(String id, String pwd, IdMagic idMagic,String database64,SignMode signMode) throws NetonejException {
        return xmlSignBuilder().setId(id).setPasswd(pwd).setIdmagic(idMagic)
                .setBase64Data(database64).setSigmode(signMode).build();
    }
    @Deprecated
    public NetonePCS createXMLSignature(String id, String pwd, String idMagic, String database64,String sigmode) throws NetonejException {
        return xmlSignBuilder().setId(id).setPasswd(pwd).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase()))
                .setBase64Data(database64).setSigmode(SignMode.valueOf(sigmode.toUpperCase())).build();
    }

    @Deprecated
    public NetonePCS changePassword(String id, String idMagic, String oldpwd,String newpwd) throws NetonejException {
        return pinBuilder().setId(id).setIdmagic(IdMagic.valueOf(idMagic.toUpperCase())).setOldpwd(oldpwd).setNewpwd(newpwd).build();
    }
    @Deprecated
    public NetonePCS changePassword(String id, IdMagic idMagic, String oldpwd,String newpwd) throws NetonejException {
        return pinBuilder().setId(id).setIdmagic(idMagic)
                .setOldpwd(oldpwd).setNewpwd(newpwd).build();
    }

    @Deprecated
    public NetonePCS changePassword(String id, String oldpwd,String newpwd) throws NetonejException {
        return pinBuilder().setId(id)
                .setOldpwd(oldpwd).setNewpwd(newpwd).build();
    }

}
