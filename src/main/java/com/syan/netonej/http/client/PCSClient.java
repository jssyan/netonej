/**
 * 文 件 名： PCSClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import java.util.HashMap;
import java.util.Map;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneEnvelope;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.entity.NetoneSignPKCS7;


public class PCSClient extends BaseClient {

    public PCSClient(String host, String port, String application) {
        super(host, port, application);
    }

    public PCSClient(String host, String port) {
        super(host, port);
    }

    public PCSClient(String host) {
        super(host,"9178");
    }

    //========================1.获取PCS 密钥库中所有密钥id========================

    @Deprecated
    public NetoneKeyList getPcsIds() throws NetonejExcepption {
        return this.getKids();
    }

    public NetoneKeyList getKids() throws NetonejExcepption {
        return this.getKids(null,null,null);
    }

    public NetoneKeyList getKids(int limit) throws NetonejExcepption {
        return this.getKids(limit,null,null);
    }

    public NetoneKeyList getKids(KeyAlgorithm keyAlgorithm) throws NetonejExcepption {
        return this.getKids(null,keyAlgorithm.getValue(),null);
    }

    public NetoneKeyList getKids(KeyUseage keyUseage) throws NetonejExcepption {
        return this.getKids(null,null,keyUseage.getValue());
    }

    public NetoneKeyList getKids(int limit,KeyAlgorithm keyAlgorithm) throws NetonejExcepption {
        return this.getKids(limit,keyAlgorithm.getValue(),null);
    }

    public NetoneKeyList getKids(int limit,KeyUseage keyUseage) throws NetonejExcepption {
        return this.getKids(limit,null,keyUseage.getValue());
    }

    public NetoneKeyList getKids(KeyAlgorithm keyAlgorithm,KeyUseage keyUseage) throws NetonejExcepption {
        return this.getKids(null,keyAlgorithm.getValue(),keyUseage.getValue());
    }

    public NetoneKeyList getKids(int limit, KeyAlgorithm keyAlgorithm,KeyUseage keyUseage) throws NetonejExcepption {
        return this.getKids(limit,keyAlgorithm.getValue(),keyUseage.getValue());
    }

    /**
     * 获取可用的密钥ID，返回可用的密钥ID列表。如果有多个可用的密钥ID，这些ID之间通过换行符分割
     * @param limit  <n> 返回前n个符合条件的密钥,<=0返回所有的
     * @param algo   用于返回特定算法 1代表sm2, 2代表rsa，3代表ecc, 4代表sk，不填返回所有非对称密钥
     * @param useage 用于返回特定用法的密钥列表（根据证书对应的密钥用法）， 1是签名用法，2是加密用法
     * @return {@link com.syan.netonej.http.entity.NetoneKeyList}
     * @throws NetonejExcepption
     */
    private NetoneKeyList getKids(Integer limit,Integer algo,Integer useage) throws NetonejExcepption{
        Map<String, String> params = prepareParameter();
        if(limit != null){
            params.put("limit",String.valueOf(limit));
        }
        if(algo != null){
            params.put("algo",String.valueOf(algo));
        }
        if(useage != null){
            params.put("useage",String.valueOf(useage));
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_SL, params);
            return new NetoneKeyList(response);
        } catch (Exception e) {
            throw new NetonejExcepption("PCS（sl）密钥id获取失败:" + e, e);
        }
    }


    //========================2.使用密钥id获取对应的Base64编码公钥证书========================

    @Deprecated
    public NetoneCertificate getBase64CertificateById(String id, String idMagic) throws NetonejExcepption {
        return this.getCertById(id,idMagic);
    }

    public NetoneCertificate getBase64CertificateById(String id) throws NetonejExcepption {
        return this.getCertById(id,null);
    }

    public NetoneCertificate getBase64CertificateById(String id, IdMagic idMagic) throws NetonejExcepption {
        return this.getCertById(id,idMagic.name());
    }
    /**
     * 使用密钥id获取对应的Base64编码公钥证书
     * @param id      密钥id
     * @param idMagic id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @return {@link com.syan.netonej.http.entity.NetoneCertificate} 证书信息
     * @throws NetonejExcepption API全局异常类
     */
    private NetoneCertificate getCertById(String id, String idMagic) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_SG, params);
            return new NetoneCertificate(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（sg） 获取Base64证书失败" + e, e);
        }
    }


    //========================3.PKCS#1格式数字签名========================

    @Deprecated
    public NetonePCS createPKCS1Signature(byte[] plainText, String certDN, String pwd, String algo) throws NetonejExcepption {
        return this.createPKCS1Sign(
                (String) NetonejUtil.getMapFromDN(certDN).get("CN"),pwd,IdMagic.SCN.name(),NetonejUtil.base64Encode(plainText),DataType.PLAIN.ordinal(),algo);
    }

    /**
     * 使用密钥id生成PKCS#1格式数字签名
     *
     * @param id       密钥id
     * @param pwd      密钥访问口令
     * @param idMagic  {@link com.syan.netonej.common.dict.IdMagic} id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data     待签名原文数据
     * @param datatype 待签名数据的类型. 0表示是原文, 1表示摘要(摘要数据是binary格式)
     * @param algo     摘要算法 请参看{@link com.syan.netonej.common.dict.DigestAlgorithm} 不确定算法可设置null
     * @return {@link com.syan.netonej.http.entity.NetonePCS} PKCS#1数字签名结果
     * @throws NetonejExcepption API全局异常类
     */
    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd, String idMagic, byte[] data, String datatype, String algo) throws NetonejExcepption {

        NetonePCS signpkcs1 = createPKCS1Signature(id, pwd, idMagic, NetonejUtil.base64Encode(data), datatype, algo);

        return signpkcs1;
    }

    @Deprecated
    public NetonePCS createPKCS1Signature(String id, String pwd, String idMagic, String data, String datatype, String algo) throws NetonejExcepption {
        return this.createPKCS1Sign(id,pwd,idMagic,data,Integer.valueOf(datatype),algo);
    }

    public NetonePCS createPKCS1Signature(String id, String pwd,IdMagic idMagic, String data, DataType datatype, DigestAlgorithm algo) throws NetonejExcepption {
        return this.createPKCS1Sign(id,pwd,idMagic.name(),data,datatype.ordinal(),algo.getName());
    }
    public NetonePCS createPKCS1Signature(String id, String pwd,IdMagic idMagic, String data, DataType datatype) throws NetonejExcepption {
        return this.createPKCS1Sign(id,pwd,idMagic.name(),data,datatype.ordinal(),null);
    }
    public NetonePCS createPKCS1Signature(String id, String pwd, String data, DataType datatype, DigestAlgorithm algo) throws NetonejExcepption {
        return this.createPKCS1Sign(id,pwd,null,data,datatype.ordinal(),algo.getName());
    }
    public NetonePCS createPKCS1Signature(String id, String pwd, String data, DataType datatype) throws NetonejExcepption {
        return this.createPKCS1Sign(id,pwd,null,data,datatype.ordinal(),null);
    }
    /**
     * 使用密钥id生成PKCS#1格式数字签名
     * @param id       密钥id
     * @param pwd      密钥访问口令
     * @param idMagic  {@link com.syan.netonej.common.dict.IdMagic} id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data     待签名原文数据(base64编码)
     * @param datatype 待签名数据的类型. 0表示是原文, 1表示摘要(摘要数据是binary格式), 2表示摘要(摘要数据是hex string格式) hex string格式主要是为了配合SignerX控件使用, 因为SignerX摘要的返回是hex string
     * @param algo     摘要算法 请参看{@link com.syan.netonej.common.dict.DigestAlgorithm}
     * @return {@link com.syan.netonej.http.entity.NetonePCS} PKCS#1数字签名结果
     * @throws NetonejExcepption API全局异常类
     */
    private NetonePCS createPKCS1Sign(String id, String pwd, String idMagic, String data, int datatype, String algo) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        if (!NetonejUtil.isEmpty(pwd)) {
            params.put("passwd", pwd);
        }
        params.put("data", data);
        params.put("datatype", String.valueOf(datatype));
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        if (!NetonejUtil.isEmpty(algo)) {
            params.put("algo", algo.trim().toLowerCase());
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_SMP1, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（smp1）创建PKCS#1数字签名失败" + e, e);
        }
    }


    //========================4.使用密钥id生成PKCS#7格式数字签名========================

    @Deprecated
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, String idMagic, String data, boolean attach, String algo) throws NetonejExcepption {
        return createP7Signature(id,pwd,idMagic,data,attach,false,algo);
    }

    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, boolean attach, DigestAlgorithm algo) throws NetonejExcepption {
        return createP7Signature(id,pwd,idMagic.name(),data,attach,false,algo.getName());
    }
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, DigestAlgorithm algo) throws NetonejExcepption {
        return createP7Signature(id,pwd,idMagic.name(),data,true,false,algo.getName());
    }
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data,boolean attach) throws NetonejExcepption {
        return createP7Signature(id,pwd,idMagic.name(),data,attach,false,null);
    }
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data) throws NetonejExcepption {
        return createP7Signature(id,pwd,idMagic.name(),data,true,false,null);
    }
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data) throws NetonejExcepption {
        return createP7Signature(id,pwd,null,data,true,false,null);
    }
    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, DigestAlgorithm algo) throws NetonejExcepption {
        return createP7Signature(id,pwd,null,data,attach,false,algo.getName());
    }

    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach) throws NetonejExcepption {
        return createP7Signature(id,pwd,null,data,attach,false,null);
    }

    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, boolean fullchain,DigestAlgorithm algo) throws NetonejExcepption {
        return createP7Signature(id,pwd,null,data,attach,fullchain,algo.getName());
    }

    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd,String data, boolean attach, boolean fullchain) throws NetonejExcepption {
        return createP7Signature(id,pwd,null,data,attach,fullchain,null);
    }

    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, IdMagic idMagic, String data, boolean attach, boolean fullchain,DigestAlgorithm algo) throws NetonejExcepption {
        return createP7Signature(id,pwd,idMagic.name(),data,attach,fullchain,algo.getName());
    }

    /**
     * 使用密钥id生成PKCS#7格式数字签名
     *
     * @param id      密钥id
     * @param pwd     密钥访问口令
     * @param idMagic {@link com.syan.netonej.common.dict.IdMagic} id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data    待签名原文数据(base64编码)
     * @param attach  是否在签名结果里面包含原文数据 'true' or 'false'
     * @param algo    摘要算法 请参看{@link com.syan.netonej.common.dict.DigestAlgorithm}
     * @return {@link com.syan.netonej.http.entity.NetoneSignPKCS7} PKCS#7数字签名结果
     * @throws NetonejExcepption API全局异常类
     */

    private NetoneSignPKCS7 createP7Signature(String id, String pwd, String idMagic, String data, boolean attach, boolean fullchain,String algo) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        if (pwd != null && !"".equals(pwd)) {
            params.put("passwd", pwd);
        }
        params.put("data", data);
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        if (!attach) {
            params.put("attach", "0");
        }
        if (fullchain) {
            params.put("fullchain", "1");
        }
        if (!NetonejUtil.isEmpty(algo)) {
            params.put("algo", algo.trim().toLowerCase());
        }
        params.put("noattr", "1");
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_SMP7, params);
            return new NetoneSignPKCS7(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（smp7）创建PKCS#7数字签名失败" + e, e);
        }
    }

    //========================5.使用密钥id对应的密钥进行数字信封封包========================

    public NetoneEnvelope envelopePacket(String id, String pwd, String idMagic, String data, String base64Certificate) throws NetonejExcepption {

        return this.envelopePacket(id, pwd, idMagic, data, base64Certificate, null);
    }


    public NetoneEnvelope envelopePacket(String id, String pwd, String idMagic, String data, String base64Certificate, String cipher) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,idMagic,data,base64Certificate,cipher);
    }


    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic, String data, String base64Certificate, CipherAlgorithm cipher) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,idMagic.name(),data,base64Certificate,cipher.getName());
    }

    public NetoneEnvelope envelopePacket(String id, String pwd, String data, String base64Certificate, CipherAlgorithm cipher) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,null,data,base64Certificate,cipher.getName());
    }

    public NetoneEnvelope envelopePacket(String id, String pwd, String data, CipherAlgorithm cipher) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,null,data,null,cipher.getName());
    }

    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data, CipherAlgorithm cipher) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,idMagic.name(),data,null,cipher.getName());
    }

    public NetoneEnvelope envelopePacket(String id, String pwd, String data, String base64Certificate) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,null,data,base64Certificate,null);
    }

    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data, String base64Certificate) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,idMagic.name(),data,base64Certificate,null);
    }

    public NetoneEnvelope envelopePacket(String id, String pwd, String data) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,null,data,null,null);
    }

    public NetoneEnvelope envelopePacket(String id, String pwd, IdMagic idMagic,String data) throws NetonejExcepption {
        return envelopePacketAction(id,pwd,idMagic.name(),data,null,null);
    }

    /**
     * 使用密钥id对应的密钥进行数字信封封包
     *
     * @param id                密钥id
     * @param pwd               密钥访问口令
     * @param idMagic           {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data              待封包数据，长度不限
     * @param base64Certificate Base64编码接收方公钥证书
     * @return {@link com.syan.netonej.http.entity.NetoneEnvelope} 数字信封封包结果
     * @throws NetonejExcepption API全局异常类
     * @since 3.0.10
     */
    private NetoneEnvelope envelopePacketAction(String id, String pwd, String idMagic, String data, String base64Certificate, String cipher) throws NetonejExcepption {

        Map<String, String> params = prepareParameter();

        params.put("id", id);
        if (pwd != null && !"".equals(pwd)) {
            params.put("passwd", pwd);
        }
        params.put("data", data);

        if(!NetonejUtil.isEmpty(base64Certificate)){
            params.put("peer", base64Certificate);
        }

        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        if (!NetonejUtil.isEmpty(cipher)) {
            params.put("cipher", cipher);
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_ENVSEAL, params);
            return new NetoneEnvelope(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（envseal）数字信封封包失败" + e, e);
        }
    }


    //========================6.使用密钥id对应的密钥进行数字信封解包========================
    /**
     * 使用密钥id对应的密钥进行数字信封解包
     *
     * @param id       密钥id
     * @param pwd      密钥访问口令
     * @param idMagic  {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param envelope 待解包数据（Base64编码）
     * @return {@link com.syan.netonej.http.entity.NetonePCS}  数字信封解包后的数据（原文）
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS envelopeUnpack(String id, String pwd, String idMagic, String envelope) throws NetonejExcepption {
        return envelopeUnpackAction(id,pwd,idMagic,envelope);
    }

    public NetonePCS envelopeUnpack(String id, String pwd, IdMagic idMagic, String envelope) throws NetonejExcepption {
        return envelopeUnpackAction(id,pwd,idMagic.name(),envelope);
    }

    public NetonePCS envelopeUnpack(String id, String pwd, String envelope) throws NetonejExcepption {
        return envelopeUnpackAction(id,pwd,null,envelope);
    }

    private NetonePCS envelopeUnpackAction(String id, String pwd, String idMagic, String envelope) throws NetonejExcepption {

        Map<String, String> params = prepareParameter();

        params.put("id", id);
        if (pwd != null && !"".equals(pwd)) {
            params.put("passwd", pwd);
        }
        params.put("data", envelope);

        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }

        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_ENVOPEN, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（envopen）数字信封解包失败" + e, e);
        }
    }


    //========================7.使用密钥id对应的证书私钥进行加密========================

    public NetonePCS priKeyEncrypt(String id, String pwd, String idMagic, String data) throws NetonejExcepption {
        return this.privateKeyEncrypt(id,pwd,idMagic,data);
    }

    public NetonePCS priKeyEncrypt(String id, String pwd, String data) throws NetonejExcepption {
        return this.privateKeyEncrypt(id,pwd,null,data);
    }

    public NetonePCS priKeyEncrypt(String id, String pwd,IdMagic idMagic, String data) throws NetonejExcepption {
        return this.privateKeyEncrypt(id,pwd,idMagic.name(),data);
    }

    /**
     * 使用密钥id对应的证书私钥进行加密 （注意：RSA私钥加密的明文最大长度不能超过私钥长度的2倍，SM2不支持私钥加密）
     * @param id      密钥id
     * @param pwd     密钥访问口令
     * @param idMagic {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data    待加密数据（Base64编码）
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 私钥加密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    private NetonePCS privateKeyEncrypt(String id, String pwd, String idMagic, String data) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        if (!NetonejUtil.isEmpty(pwd)) {
            params.put("passwd", pwd);
        }
        params.put("data", data);
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_SPE, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（spe）私钥加密失败" + e, e);
        }
    }


    //========================8.使用密钥id对应的证书私钥进解密========================

    public NetonePCS priKeyDecrypt(String id, String pwd, String idMagic, String encryptData) throws NetonejExcepption {
        return privateKeyDecrypt(id,pwd,idMagic,encryptData);
    }

    public NetonePCS priKeyDecrypt(String id, String pwd, IdMagic idMagic, String encryptData) throws NetonejExcepption {
        return privateKeyDecrypt(id,pwd,idMagic.name(),encryptData);
    }

    public NetonePCS priKeyDecrypt(String id, String pwd, String encryptData) throws NetonejExcepption {
        return privateKeyDecrypt(id,pwd,null,encryptData);
    }

    /**
     * 使用密钥id对应的证书私钥进解密 （注意：RSA私钥解密的明文最大长度不能超过私钥长度的2倍，SM2私钥无此限制）
     *
     * @param id          密钥id
     * @param pwd         密钥访问口令
     * @param idMagic     {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param encryptData 待解密数据（Base64编码）
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 私钥解密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    private NetonePCS privateKeyDecrypt(String id, String pwd, String idMagic, String encryptData) throws NetonejExcepption {

        Map<String, String> params = prepareParameter();

        params.put("id", id);
        if (!NetonejUtil.isEmpty(pwd)) {
            params.put("passwd", pwd);
        }
        params.put("data", encryptData);
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_SPD, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（spd）私钥解密失败" + e, e);
        }
    }


    //========================9.使用密钥id对应的证书进行公钥加密========================

    public NetonePCS pubKeyEncrypt(String id, String idMagic, String data) throws NetonejExcepption {
        return publicKeyEncrypt(id,idMagic,data);
    }

    public NetonePCS pubKeyEncrypt(String id, IdMagic idMagic, String data) throws NetonejExcepption {
        return publicKeyEncrypt(id,idMagic.name(),data);
    }

    public NetonePCS pubKeyEncrypt(String id, String data) throws NetonejExcepption {
        return publicKeyEncrypt(id,null,data);
    }

    /**
     * 使用密钥id对应的证书进行公钥加密 （注意：RSA公钥加密的明文最大长度不能超过私钥长度的2倍，SM2公钥无此限制）
     *
     * @param id      密钥id
     * @param idMagic {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data    Base64编码的待加密数据
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 公钥加密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    private NetonePCS publicKeyEncrypt(String id, String idMagic, String data) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        params.put("data", data);
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_PPE, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（ppe） 公钥加密失败" + e, e);
        }
    }


    //========================10.使用密钥id对应的证书进行公钥解密========================

    public NetonePCS pubKeyDecrypt(String id, String idMagic, String encryptData) throws NetonejExcepption {
        return publicKeyDecrypt(id,idMagic,encryptData);
    }

    public NetonePCS pubKeyDecrypt(String id, IdMagic idMagic, String encryptData) throws NetonejExcepption {
        return publicKeyDecrypt(id,idMagic.name(),encryptData);
    }

    public NetonePCS pubKeyDecrypt(String id, String encryptData) throws NetonejExcepption {
        return publicKeyDecrypt(id,null,encryptData);
    }

    /**
     * 使用密钥id对应的证书进行公钥解密 （注意，RSA公钥解密的明文最大长度不能超过私钥长度的2倍，SM2不支持公钥解密）
     *
     * @param id          密钥id
     * @param idMagic     {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param encryptData Base64编码的待解密数据
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 公钥解密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    private NetonePCS publicKeyDecrypt(String id, String idMagic, String encryptData) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        params.put("data", encryptData);
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_PPD, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（ppd）公钥解密失败" + e, e);
        }
    }


    //========================11.使用密钥id对应的证书创建XML签名========================

    public NetonePCS createXMLSignature(String id, String pwd, String idMagic, String database64) throws NetonejExcepption {
        return createXMLSign(id,pwd,idMagic,database64,SignMode.enveloped.ordinal());
    }

    public NetonePCS createXMLSignature(String id, String pwd, String database64) throws NetonejExcepption {
        return createXMLSign(id,pwd,null,database64,SignMode.enveloped.ordinal());
    }

    public NetonePCS createXMLSignature(String id, String pwd, String database64,SignMode signMode) throws NetonejExcepption {
        return createXMLSign(id,pwd,null,database64,signMode.ordinal());
    }

    public NetonePCS createXMLSignature(String id, String pwd, IdMagic idMagic,String database64) throws NetonejExcepption {
        return createXMLSign(id,pwd,idMagic.name(),database64,SignMode.enveloped.ordinal());
    }

    public NetonePCS createXMLSignature(String id, String pwd, IdMagic idMagic,String database64,SignMode signMode) throws NetonejExcepption {
        return createXMLSign(id,pwd,idMagic.name(),database64,signMode.ordinal());
    }
    @Deprecated
    public NetonePCS createXMLSignature(String id, String pwd, String idMagic, String database64,String sigmode) throws NetonejExcepption {
        return createXMLSign(id,pwd,idMagic,database64,Integer.parseInt(sigmode));
    }

    /**
     * 使用密钥id对应的证书创建XML签名
     *
     * @param id         密钥id
     * @param pwd        密钥访问口令
     * @param idMagic    {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param database64 BASE64编码的待签名数据，数据长度不限
     * @return {@link com.syan.netonej.http.entity.NetonePCS} xml签名数据
     * @throws NetonejExcepption
     */
    private NetonePCS createXMLSign(String id, String pwd, String idMagic, String database64,int sigmode) throws NetonejExcepption {
        Map<String, String> params = prepareParameter();
        params.put("id", id);
        if (pwd != null && !"".equals(pwd)) {
            params.put("passwd", pwd);
        }
        params.put("data", database64);
        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        params.put("sigmode", String.valueOf(sigmode));
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_SXS, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（sxs）创建PKCS#7数字签名失败" + e, e);
        }
    }

    //========================12.修改密钥访问口令========================
    /**
     * 修改密钥访问口令
     * @param id      密钥id
     * @param idMagic {@link com.syan.netonej.common.dict.IdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param oldpwd  原密码口令
     * @param newpwd  新密码口令
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 修改结果
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS changePassword(String id, String idMagic, String oldpwd,String newpwd) throws NetonejExcepption {
        return changePasswordAction(id,idMagic,oldpwd,newpwd);
    }

    public NetonePCS changePassword(String id, IdMagic idMagic, String oldpwd,String newpwd) throws NetonejExcepption {
        return changePasswordAction(id,idMagic.name(),oldpwd,newpwd);
    }

    public NetonePCS changePassword(String id, String oldpwd,String newpwd) throws NetonejExcepption {
        return changePasswordAction(id,null,oldpwd,newpwd);
    }

    private NetonePCS changePasswordAction(String id, String idMagic, String oldpwd,String newpwd) throws NetonejExcepption {

        Map<String, String> params = new HashMap<String, String>();

        params.put("id", id);

        params.put("oldpwd", oldpwd);

        params.put("newpwd", newpwd);

        if (!NetonejUtil.isEmpty(idMagic)) {
            params.put("idmagic", idMagic.trim().toLowerCase());
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_CHPWD, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（chpwd） 密钥访问口令失败" + e, e);
        }
    }

    //========================16.CMAC密钥派生函数========================

    public NetonePCS cmacKeyDerivation(String id, String passwd, String factor,CipherAlgorithm cipher,String pubk  ) throws NetonejExcepption{
        return cmacKeyDerivationFunc(id,passwd,factor,cipher.getName(),pubk);
    }

    public NetonePCS cmacKeyDerivation(String id, String passwd, String factor,CipherAlgorithm cipher) throws NetonejExcepption{
        return cmacKeyDerivationFunc(id,passwd,factor,cipher.getName(),null);
    }

    /**CMAC密钥派生函数
     *
     * @param id      密钥ID
     * @param passwd  口令
     * @param factor  密钥派生因子(BASE64编码）
     * @param cipher   密钥派生算法（比如： sm4, aes128, aes256)等
     * @param pubk    如果设置此项，传入base64格式的客户端的公钥/证书，则服务会用此公钥加密返回密钥派生密钥
     * @return        {@link com.syan.netonej.http.entity.NetonePCS} BASE64编码的密钥派生密钥（当客户端传入公钥/证书时，返回BASE编码的被公钥加密的密钥派生密钥）
     * @throws NetonejExcepption
     */
    private NetonePCS cmacKeyDerivationFunc(String id, String passwd, String factor,String cipher,String pubk  ) throws NetonejExcepption{

        Map<String, String> params = new HashMap<String, String>();

        params.put("id", id);

        params.put("passwd", passwd);

        params.put("factor", factor);

        params.put("cipher",cipher);

        if (!NetonejUtil.isEmpty(pubk)) {
            params.put("pubk", pubk);
        }
        try {
            NetoneResponse response = doHttpPost(Action.PCS_ACTION_CMACKDF, params);
            return new NetonePCS(response);
        } catch (Exception e) {
            throw new NetonejExcepption("-PCS（cmac） 密钥派生失败" + e, e);
        }
    }

}
