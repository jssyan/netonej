/**
 * 文 件 名： PCSClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.HttpPostProcesserFactory;
import com.syan.netonej.http.HttpStatus;
import com.syan.netonej.http.entity.NetoneBase;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneEnvelope;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.entity.NetoneSignPKCS7;


/**
 * NetONE PCS API
 *
 * @author liyb
 * @version 2.0.0
 * @since 1.0.0
 */
public class PCSClient extends BaseClient {
    private static final Log log = LogFactory.getLog(PCSClient.class);
    /**
     * PCS - Action 列出可用的Key ID
     */
    private final static String PCS_ACTION_SL = "sl.svr";
    /**
     * PCS - Action 获取与Key ID对应的X.509证书
     */
    private final static String PCS_ACTION_SG = "sg.svr";
    /**
     * PCS - Action 生成PKCS#1 数字签名
     */
    private final static String PCS_ACTION_SMP1 = "smp1.svr";
    /**
     * PCS - Action 生成PKCS#7 数字签名
     */
    private final static String PCS_ACTION_SMP7 = "smp7.svr";
    /**
     * PCS - Action 私钥加密
     */
    private final static String PCS_ACTION_SPE = "spe.svr";
    /**
     * PCS - Action 私钥解密
     */
    private final static String PCS_ACTION_SPD = "spd.svr";
    /**
     * PCS - Action 公钥加密
     */
    private final static String PCS_ACTION_PPE = "ppe.svr";
    /**
     * PCS - Action 公钥解密
     */
    private final static String PCS_ACTION_PPD = "ppd.svr";
    /**
     * PCS - Action  生成XML数字签名
     */
    private final static String PCS_ACTION_SXS = "sxs.svr";
    /**
     * PCS - Action 数字信封封包
     */
    private final static String PCS_ACTION_ENVSEAL = "envseal.svr";
    /**
     * PCS - Action 数字信封解包
     */
    private final static String PCS_ACTION_ENVOPEN = "envopen.svr";

    /**
     * PCS - Action 修改密钥访问口令
     */
    private final static String PCS_ACTION_CHPWD = "chpwd.svr";

    /**
     * Parameters - ID类型键值  "idmagic"
     */
    private final static String PARAME_IDMAGIC = "idmagic";
    /**
     * Parameters - ID键值  "id"
     */
    private final static String PARAME_ID = "id";

    /**
     * Parameters - 加密算法  "cipher"
     *
     * @since 3.0.10
     */
    private final static String PARAME_CIPHER = "cipher";
    /**
     * Parameters - PCS口令键值  "passwd"
     */
    private final static String PARAME_PASSWD = "passwd";

    /**
     * Parameters - 证书键值(数字信封专用)  "peer"
     */
    private final static String PARAME_PEER = "peer";

    /**
     * Parameters - 原文数据值类型  "datatype"
     */
    private final static String DATA_TYPE = "datatype";


    /**
     * Parameters - p7签名是否包含整个证书链键值  "fullchain"
     */
    private final static String PARAME_FULLCHAIN = "fullchain";
    /**
     * 是否包含原文
     */
    public final static String PARAME_ATTACH = "attach";

    /**
     * Parameters - 签名算法键值  "algo"
     */
    private final static String PARAME_ALGO = "algo";

    /**
     * 是否包含证书链
     */
    private boolean containsCertChain = false;

    /**
     * PCS 服务IP
     */
    private String _host;
    /**
     * PCS 服务PORT
     */
    private String _port;

    /**
     * 根据服务器IP 端口 构造PCSClient
     *
     * @param host 服务器IP
     * @param port 服务端口
     */
    public PCSClient(String host, String port) {
        super(HttpPostProcesserFactory.createPostProcesser());
        this._host = host;
        this._port = port;
    }

    /**
     * 设置可选参数
     *
     * @return 设置参数的map
     */
    private Map<String, String> prepareParameter() {

        Map<String, String> params = new HashMap<String, String>();
        params.put(RESPONSE_FORMAT_KEY, responseformat);
        //设置是否农行k宝二代应用
        if (!NetonejUtil.isEmpty(application)) {
            params.put(PARAME_APPLICATION, PARAME_APPLICATION_VALUE);
        }


        return params;
    }

    /**
     * 获取PCS 密钥库中所有密钥id
     *
     * @return {@link com.syan.netonej.http.entity.NetoneKeyList} key列表
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneKeyList getPcsIds() throws NetonejExcepption {

        log.debug("-PCS（sl）");

        NetoneKeyList keylist = null;
        Map<String, String> params = prepareParameter();

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SL), params);
            keylist = new NetoneKeyList(response);
        } catch (Exception e) {

            log.error("-PCS（sl）密钥id获取失败", e);

            throw new NetonejExcepption("PCS（sl）密钥id获取失败:" + e, e);
        }
        return keylist;
    }

    /**
     * 使用密钥id获取对应的Base64编码公钥证书
     *
     * @param id      密钥id
     * @param idMagic id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @return {@link com.syan.netonej.http.entity.NetoneCertificate} 证书信息
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneCertificate getBase64CertificateById(String id, String idMagic) throws NetonejExcepption {

        log.debug("-PCS（sg）id：" + id + "idMagic：" + idMagic);

        NetoneCertificate netonecert = null;
        Map<String, String> params = prepareParameter();
        params.put(PARAME_ID, id);

        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }


        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SG), params);
            netonecert = new NetoneCertificate(response);
        } catch (Exception e) {

            log.error("-PCS（sg） 获取Base64证书失败", e);

            throw new NetonejExcepption("-PCS（sg） 获取Base64证书失败" + e, e);
        }

        return netonecert;
    }

    /**
     * 使用密钥id生成PKCS#1格式数字签名
     *
     * @param plainText 待签名原文数据
     * @param certDN    证书主题
     * @param pwd       密钥访问口令
     * @param algo      摘要算法 请参看{@link com.syan.netonej.common.Algorithm}
     * @return {@link com.syan.netonej.http.entity.NetonePCS} PKCS#1数字签名结果
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS createPKCS1Signature(byte[] plainText, String certDN, String pwd, String algo) throws NetonejExcepption {

        log.debug("-PCS（smp1）certDN：" + certDN);

        NetonePCS signpkcs1 = null;

        Map<String, String> params = prepareParameter();
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }
        params.put(PARAME_DATA, NetonejUtil.base64Encode(plainText));
        params.put(DATA_TYPE, "0");
        params.put(PARAME_IDMAGIC, "scn");
        params.put(PARAME_ID, (String) NetonejUtil.getMapFromDN(certDN).get("CN"));

        if (algo != null) {
            params.put(PARAME_ALGO, algo);
        }
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SMP1), params);
            signpkcs1 = new NetonePCS(response);
        } catch (Exception e) {

            log.error("-PCS（smp1）创建PKCS#1数字签名失败", e);

            throw new NetonejExcepption("-PCS（smp1）创建PKCS#1数字签名失败" + e, e);

        }

        return signpkcs1;
    }

    /**
     * 使用密钥id生成PKCS#1格式数字签名
     *
     * @param id       密钥id
     * @param pwd      密钥访问口令
     * @param idMagic  {@link com.syan.netonej.common.NetonejIdMagic} id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data     待签名原文数据
     * @param datatype 待签名数据的类型. 0表示是原文, 1表示摘要(摘要数据是binary格式)
     * @param algo     摘要算法 请参看{@link com.syan.netonej.common.Algorithm} 不确定算法可设置null
     * @return {@link com.syan.netonej.http.entity.NetonePCS} PKCS#1数字签名结果
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS createPKCS1Signature(String id, String pwd, String idMagic, byte[] data, String datatype, String algo) throws NetonejExcepption {

        log.debug("-PCS（smp1）id：" + id + " idMagic：" + idMagic + " pwd：" + pwd + " data：" + data + " datatype：" + datatype);

        NetonePCS signpkcs1 = createPKCS1Signature(id, pwd, idMagic, NetonejUtil.base64Encode(data), datatype, algo);

        return signpkcs1;
    }

    /**
     * 使用密钥id生成PKCS#1格式数字签名
     *
     * @param id       密钥id
     * @param pwd      密钥访问口令
     * @param idMagic  {@link com.syan.netonej.common.NetonejIdMagic} id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data     待签名原文数据(base64编码)
     * @param datatype 待签名数据的类型. 0表示是原文, 1表示摘要(摘要数据是binary格式), 2表示摘要(摘要数据是hex string格式) hex string格式主要是为了配合SignerX控件使用, 因为SignerX摘要的返回是hex string
     * @param algo     摘要算法 请参看{@link com.syan.netonej.common.Algorithm}
     * @return {@link com.syan.netonej.http.entity.NetonePCS} PKCS#1数字签名结果
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS createPKCS1Signature(String id, String pwd, String idMagic, String data, String datatype, String algo) throws NetonejExcepption {

        log.debug("-PCS（smp1）id：" + id + " idMagic：" + idMagic + " pwd：" + pwd + " data：" + data + " datatype：" + datatype);

        NetonePCS signpkcs1 = null;

        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }
        params.put(PARAME_DATA, data);
        params.put(DATA_TYPE, datatype);
        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }
        if (algo != null) {
            params.put(PARAME_ALGO, algo);
        }
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SMP1), params);
            log.debug("netone response=" + response.toString());
            signpkcs1 = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（smp1）创建PKCS#1数字签名失败", e);

            throw new NetonejExcepption("-PCS（smp1）创建PKCS#1数字签名失败" + e, e);
        }

        return signpkcs1;
    }

    /**
     * 使用密钥id生成PKCS#7格式数字签名
     *
     * @param id      密钥id
     * @param pwd     密钥访问口令
     * @param idMagic {@link com.syan.netonej.common.NetonejIdMagic} id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data    待签名原文数据(base64编码)
     * @param attach  是否在签名结果里面包含原文数据 'true' or 'false'
     * @param algo    摘要算法 请参看{@link com.syan.netonej.common.Algorithm}
     * @return {@link com.syan.netonej.http.entity.NetoneSignPKCS7} PKCS#7数字签名结果
     * @throws NetonejExcepption API全局异常类
     */

    public NetoneSignPKCS7 createPKCS7Signature(String id, String pwd, String idMagic, String data, boolean attach, String algo) throws NetonejExcepption {

        log.debug("-PCS（smp7）id：" + id + " idMagic：" + idMagic + " pwd：" + pwd + " data：" + data);

        NetoneSignPKCS7 signpkcs7 = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }

        params.put(PARAME_DATA, data);

        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }
        if (!attach) {
            params.put(PARAME_ATTACH, "0");
        }
        if (containsCertChain) {
            params.put(PARAME_FULLCHAIN, "1");
        }

        if (algo != null) {
            params.put(PARAME_ALGO, algo);
        }

        log.debug("-PCS（smp7）params:" + params);
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SMP7), params);
            log.debug("netone response=" + response.toString());
            signpkcs7 = new NetoneSignPKCS7(response);
        } catch (Exception e) {
            log.error("-PCS（smp7）创建PKCS#7数字签名失败", e);

            throw new NetonejExcepption("-PCS（smp7）创建PKCS#7数字签名失败" + e, e);
        }

        return signpkcs7;
    }

    /**
     * 使用密钥id对应的密钥进行数字信封封包
     *
     * @param id                密钥id
     * @param pwd               密钥访问口令
     * @param idMagic           {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data              待封包数据，长度不限
     * @param base64Certificate Base64编码接收方公钥证书
     * @return {@link com.syan.netonej.http.entity.NetoneEnvelope} 数字信封封包结果
     * @throws NetonejExcepption API全局异常类
     */
    public NetoneEnvelope envelopePacket(String id, String pwd, String idMagic, String data, String base64Certificate) throws NetonejExcepption {

        return this.envelopePacket(id, pwd, idMagic, data, base64Certificate, null);
    }

    /**
     * 使用密钥id对应的密钥进行数字信封封包
     *
     * @param id                密钥id
     * @param pwd               密钥访问口令
     * @param idMagic           {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data              待封包数据，长度不限
     * @param base64Certificate Base64编码接收方公钥证书
     * @return {@link com.syan.netonej.http.entity.NetoneEnvelope} 数字信封封包结果
     * @throws NetonejExcepption API全局异常类
     * @since 3.0.10
     */
    public NetoneEnvelope envelopePacket(String id, String pwd, String idMagic, String data, String base64Certificate, String cipher) throws NetonejExcepption {

        log.debug("-PCS（envseal）id：" + id + " idMagic：" + idMagic + " pwd：" + pwd + " data：" + data + " Base64Certificate：" + base64Certificate);

        NetoneEnvelope envelope = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }

        params.put(PARAME_DATA, NetonejUtil.base64Encode(data.getBytes()));
        params.put(PARAME_PEER, base64Certificate);

        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }

        if (!NetonejUtil.isEmpty(cipher)) {
            params.put(PARAME_CIPHER, cipher);
        }

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_ENVSEAL), params);
            log.debug("netone response=" + response.toString());
            envelope = new NetoneEnvelope(response);
        } catch (Exception e) {
            log.error("-PCS（envseal）数字信封封包失败", e);

            throw new NetonejExcepption("-PCS（envseal）数字信封封包失败" + e, e);
        }
        return envelope;
    }

    /**
     * 使用密钥id对应的密钥进行数字信封解包
     *
     * @param id       密钥id
     * @param pwd      密钥访问口令
     * @param idMagic  {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param envelope 待解包数据（Base64编码）
     * @return {@link com.syan.netonej.http.entity.NetonePCS}  数字信封解包后的数据（原文）
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS envelopeUnpack(String id, String pwd, String idMagic, String envelope) throws NetonejExcepption {

        log.debug("-PCS（envopen）id：" + id + " idMagic：" + idMagic + " pwd：" + pwd + " envelope：" + envelope);

        NetonePCS retobj = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }
        params.put(PARAME_DATA, envelope);
        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_ENVOPEN), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（envopen）数字信封解包失败", e);
            throw new NetonejExcepption("-PCS（envopen）数字信封解包失败" + e, e);
        }

        return retobj;
    }


    /**
     * 使用密钥id对应的证书私钥进行加密 （注意：RSA私钥加密的明文最大长度不能超过私钥长度的2倍，SM2不支持私钥加密）
     *
     * @param id      密钥id
     * @param pwd     密钥访问口令
     * @param idMagic {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data    待加密数据（Base64编码）
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 私钥加密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS priKeyEncrypt(String id, String pwd, String idMagic, String data) throws NetonejExcepption {

        log.debug("-PCS（spe）id：" + id + " idMagic：" + idMagic + " pwd：" + pwd + " data：" + data);

        NetonePCS retobj = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }
        params.put(PARAME_DATA, data);
        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SPE), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（spe）私钥加密失败", e);

            throw new NetonejExcepption("-PCS（spe）私钥加密失败" + e, e);
        }

        return retobj;
    }

    /**
     * 使用密钥id对应的证书私钥进解密 （注意：RSA私钥解密的明文最大长度不能超过私钥长度的2倍，SM2私钥无此限制）
     *
     * @param id          密钥id
     * @param pwd         密钥访问口令
     * @param idMagic     {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param encryptData 待解密数据（Base64编码）
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 私钥解密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS priKeyDecrypt(String id, String pwd, String idMagic, String encryptData) throws NetonejExcepption {

        log.debug("-PCS（spd）id：" + id + " idMagic：" + idMagic + " pwd：" + pwd + " encryptData：" + encryptData);

        NetonePCS retobj = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }
        params.put(PARAME_DATA, encryptData);
        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SPD), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（spd）私钥解密失败", e);

            throw new NetonejExcepption("-PCS（spd）私钥解密失败" + e, e);
        }

        return retobj;
    }


    /**
     * 使用密钥id对应的证书进行公钥加密 （注意：RSA公钥加密的明文最大长度不能超过私钥长度的2倍，SM2公钥无此限制）
     *
     * @param id      密钥id
     * @param idMagic {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param data    Base64编码的待加密数据
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 公钥加密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS pubKeyEncrypt(String id, String idMagic, String data) throws NetonejExcepption {

        log.debug("-PCS（ppe） id：" + id + "idMagic：" + idMagic + "data：" + data);

        NetonePCS retobj = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);

        params.put(PARAME_DATA, data);

        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_PPE), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（ppe） 公钥加密失败", e);

            throw new NetonejExcepption("-PCS（ppe） 公钥加密失败" + e, e);
        }

        return retobj;
    }


    /**
     * 使用密钥id对应的证书进行公钥解密 （注意，RSA公钥解密的明文最大长度不能超过私钥长度的2倍，SM2不支持公钥解密）
     *
     * @param id          密钥id
     * @param idMagic     {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param encryptData Base64编码的待解密数据
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 公钥解密后的数据
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS pubKeyDecrypt(String id, String idMagic, String encryptData) throws NetonejExcepption {
        log.debug("-PCS（ppd）id：" + id + "idMagic：" + idMagic + " encryptData：" + encryptData);

        NetonePCS retobj = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);

        params.put(PARAME_DATA, encryptData);
        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_PPD), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（ppd）公钥解密失败", e);

            throw new NetonejExcepption("-PCS（ppd）公钥解密失败" + e, e);
        }

        return retobj;
    }

    /**
     * 使用密钥id对应的证书创建XML签名
     *
     * @param id         密钥id
     * @param pwd        密钥访问口令
     * @param idMagic    {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param database64 BASE64编码的待签名数据，数据长度不限
     * @return {@link com.syan.netonej.http.entity.NetonePCS} xml签名数据
     * @throws NetonejExcepption
     */
    public NetonePCS createXMLSignature(String id, String pwd, String idMagic, String database64) throws NetonejExcepption {

        log.debug("-PCS（xsx）id：" + id + "idMagic：" + idMagic + " database64：" + database64);

        NetonePCS retobj = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }
        params.put(PARAME_DATA, database64);
        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SXS), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（sxs）创建PKCS#7数字签名失败", e);

            throw new NetonejExcepption("-PCS（sxs）创建PKCS#7数字签名失败" + e, e);
        }

        return retobj;
    }


    public NetonePCS createXMLSignature(String id, String pwd, String idMagic, String database64,String sigmode) throws NetonejExcepption {

        log.debug("-PCS（xsx）id：" + id + "idMagic：" + idMagic + " database64：" + database64);

        NetonePCS retobj = null;
        Map<String, String> params = prepareParameter();

        params.put(PARAME_ID, id);
        if (pwd != null && !"".equals(pwd)) {
            params.put(PARAME_PASSWD, pwd);
        }
        params.put(PARAME_DATA, database64);
        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }
        params.put("sigmode", sigmode);
        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_SXS), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（sxs）创建PKCS#7数字签名失败", e);

            throw new NetonejExcepption("-PCS（sxs）创建PKCS#7数字签名失败" + e, e);
        }

        return retobj;
    }

    /**
     * 修改密钥访问口令
     *
     * @param id      密钥id
     * @param idMagic {@link com.syan.netonej.common.NetonejIdMagic}id类型 - kid=密钥id，scn=证书CN主题项，snhex=十六进制格式证书序列号，sndec=十进制格式证书序列号，tnsha1=SHA1格式证书指纹
     * @param oldpwd  原密码口令
     * @param newpwd  新密码口令
     * @return {@link com.syan.netonej.http.entity.NetonePCS} 修改结果
     * @throws NetonejExcepption API全局异常类
     */
    public NetonePCS changePassword(String id, String idMagic, String oldpwd,String newpwd) throws NetonejExcepption {

        log.debug("-PCS（chpwd） id：" + id + "idMagic：" + idMagic);

        NetonePCS retobj = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put(PARAME_ID, id);

        params.put("oldpwd", oldpwd);

        params.put("newpwd", newpwd);

        if (NetonejUtil.isEmpty(idMagic)) {
            params.put(PARAME_IDMAGIC, "kid");
        } else {
            params.put(PARAME_IDMAGIC, idMagic);
        }

        try {
            NetoneResponse response = processer.doPost(getServiceUrl(PCS_ACTION_CHPWD), params);
            log.debug("netone response=" + response.toString());
            retobj = new NetonePCS(response);
        } catch (Exception e) {
            log.error("-PCS（chpwd） 密钥访问口令失败", e);

            throw new NetonejExcepption("-PCS（chpwd） 密钥访问口令失败" + e, e);
        }

        return retobj;
    }


    /*
     * 获取PCS服务IP
     * @return PCS服务IP
     */
    @Override
    public String getHostIp() {
        return this._host;
    }

    /*
     * 获取PCS服务端口
     * @return PCS服务端口
     */
    @Override
    public String getPort() {
        return this._port;
    }

    public boolean isContainsCertChain() {
        return containsCertChain;
    }

    /**
     * 设置包含证书链
     *
     * @param containsCertChain 设置包含证书链
     */
    public void setContainsCertChain(boolean containsCertChain) {
        this.containsCertChain = containsCertChain;
    }

}
