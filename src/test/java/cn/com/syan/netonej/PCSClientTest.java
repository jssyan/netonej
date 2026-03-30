package cn.com.syan.netonej;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.common.entity.FileSignSequence;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.entity.*;
import org.bouncycastle.asn1.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.io.File;
import java.security.Security;
import java.util.List;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class PCSClientTest {

    //设置PCS（私钥密码服务）的服务器IP与端口号
    private PCSClient pcsClient = new PCSClient("192.168.10.89","9178");

    //KID
    String sm2kid = "8baa68513f3823d63ce38b8171d8ef2c";

    //证书的CN项
    String sm2cn = "spark_通讯证书";

    //证书的私钥保护口令
    String pin = "Syan@9108";

    /**
     * 获取可用的PCS中的kid
     * @throws NetonejException
     */
    @Test
    public void testGetKids() throws NetonejException {
        NetoneKeyList response = pcsClient.keyBuilder()
                        .setLimit(10) //可选，返回前n个符合条件的密钥
                        .setKeyUseage(KeyUseage.SIGN)//可选，用于返回特定用法的密钥列表（根据证书对应的密钥用法）
                        .setKeyAlgorithm(KeyAlgorithm.SM2) //可选,用于返回特定算法的密钥
                        .build();
        if (response .getStatusCode() == 200) {
            List<KeyListItem> list =  response.getKeyList();
            for (KeyListItem item : list) {
                System.out.println(item.getId());
                System.out.println(item.getCertificate());
                System.out.println(item.getPrivk());
            }
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());

    }

    /**
     * 创建随机数
     * @throws NetonejException
     */
    @Test
    public void testGenrand() throws NetonejException {
        NetonePCS response = pcsClient.randomBuilder()
                        .setLength(32)
                        .build();
        if (response.getStatusCode() == 200) {
            System.out.println(response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    /**
     * 根据KID获取证书
     * @throws NetonejException
     */
    @Test
    public void getBase64CertificateById() throws Exception {
        NetoneCertificate response = pcsClient.certificateBuilder()
                .setId(sm2kid)
                .setIdmagic(IdMagic.KID)
                //也可通过证书CN项的方式，按照如下两行设置即可：
                //.setId(sm2cn)
                //.setIdmagic(IdMagic.SCN)
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println(response.getBase64String());
            System.out.println(response.getHasPrivkey());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    /**
     * P1签名
     */
    @Test
    public void createPKCS1SignatureByCID() throws NetonejException {
        byte[] data = "SN".getBytes();
        NetonePCS response = pcsClient.pkcs1Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId("1") //设置id参数，这里设置的CID参数
                .setIdmagic(IdMagic.CID)
                .setData(data)//签名原文
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("签名结果 Base64："+response.getResult());
            System.out.println("签名证书："+response.getSingerCert().getSubject());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    @Test
    public void createPKCS1Signature() throws NetonejException {
        byte[] data = "02N".getBytes();
        NetonePCS response = pcsClient.pkcs1Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2kid) //设置id参数，这里设置的证书cn项
                .setData(data)//签名原文
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("签名结果 Base64："+response.getResult());
            System.out.println("签名证书："+response.getSingerCert().getSubject());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    /**
     * XML签名
     * @throws NetonejException
     */
    @Test
    public void createXMLSignature() throws NetonejException {
        String data = "<data>123456</data>";
        NetonePCS response = pcsClient.xmlSignBuilder()
                .setPasswd(pin)
                .setId(sm2kid)
                .setIdmagic(IdMagic.KID)
                .setData(data)
                .setSigmode(SignMode.enveloping)
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("签名结果 Base64："+response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    /**
     * 摘要计算
     * @throws NetonejException
     */
    @Test
    public void digestTest() throws NetonejException{
        String data = "<data>123456</data>";
        NetoneDigest digest = new NetoneDigest("SHA256");
        digest.update(data.getBytes());
        byte[] hsah = digest.digest(data.getBytes());
        String v = Base64.toBase64String(hsah);
        System.out.println(new String(v.getBytes()));
    }

    /**
     * p7签名
     * @throws NetonejException
     */
    @Test
    public void createPKCS7Signature() throws NetonejException {
        byte[] data = "000000000002SN".getBytes();
        NetonePCS response = pcsClient.pkcs7Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2kid)
                .setIdmagic(IdMagic.KID)
                .setData(data)
                .setAttach(true)//可选，签名结果中是否包含原始数据
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("Attach签名结果 Base64："+response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    @Test
    public void createPKCS7SignatureDetach() throws NetonejException {
        byte[] data = "000000000002SN".getBytes();
        NetonePCS response = pcsClient.pkcs7Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2kid)
                .setIdmagic(IdMagic.KID)
                .setData(data)
                .setAttach(false)//可选，签名结果中是否包含原始数据
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("Detach签名结果 Base64："+response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    /**
     * 数字信封  -- 封包
     * @throws NetonejException
     */
    @Test
    public void createEnvelopePacket() throws Exception {
        byte[] data = "02".getBytes();
        NetonePCS response = pcsClient.envelopePacketBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo(CipherAlgorithm.SM4CBC)//可选，设置对称密钥算法
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("结果 Base64："+response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }
    /**
     * 数字信封  -- 封包（带签名）
     * @throws NetonejException
     */
    @Test
    public void createEnvelopePacketAndSign() throws Exception {
        byte[] data = "02".getBytes();
        String crt = "MIICJzCCAcygAwIBAgIGAY9/Z3b+MAwGCCqBHM9VAYN1BQAwSzELMAkGA1UEBhMCQ04xDjAMBgNVBAoTBUdNU1NMMRAwDgYDVQQLEwdQS0kvU00yMRowGAYDVQQDExFNaWRkbGVDQSBmb3IgVGVzdDAiGA8yMDI0MDUxNTE2MDAwMFoYDzIwMjUwNTE1MTYwMDAwWjBXMQswCQYDVQQGEwJDTjETMBEGA1UECAwKdGlhbmppbnNoaTEQMA4GA1UEBwwHaGViZWlxdTEMMAoGA1UECgwDY3QyMRMwEQYDVQQDDApzeWFuX3NtMl8xMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE06iAgLh+gAfJGlrmHzhos24bs07HPxmpNCS8WRKuyHMOeCb4i0g9t9vS3fkeYaYodDyR5SE0prnuYcmj2qLtIKOBiTCBhjAbBgNVHSMEFDASgBD5f1W0J5QzYqZWym/MXRr/MBkGA1UdDgQSBBBnJQgrZE8kPBgx7JafDdhHMDEGCCsGAQUFBwEBBCUwIzAhBggrBgEFBQcwAYYVaHR0cHM6Ly9vY3NwLmdtc3NsLmNuMAkGA1UdEwQCMAAwDgYDVR0PAQH/BAQDAgDAMAwGCCqBHM9VAYN1BQADRwAwRAIgL6rXLyDMEhgpqMfMU+y/gViUrFaD8iimLT4tJqMkg4QCIF7O2hcEq7V8IOlVtHfXylGrAiQftHjg2+mKqzptgM08";
        NetonePCS response = pcsClient.envelopePacketBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo(CipherAlgorithm.SM4CBC)//可选，设置对称密钥算法
                .setPeer(crt)//可选，设置加密证书
                //.setPeerMagic(IdMagic.KID)//可选，指定加密证书的类型
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("结果 Base64："+response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    /**
     * 数字信封  -- 解包
     * @throws NetonejException
     */
    @Test
    public void createEnvelopeUnPacket() throws Exception {
        String data = "MIIBLQYKKoEcz1UGAQQCA6CCAR0wggEZAgEBMYHUMIHRAgEAMEAwLzELMAkGA1UEBhMCQ04xETAPBgNVBAoMCEFCQyBsdGQuMQ0wCwYDVQQDDAR0ZXN0Ag0ApjQrywIoblwG0mevMA0GCSqBHM9VAYItAwUABHsweQIga2bNdeNENfIjz9+P7Y9fNgfT+R0itSKk7gdraEBaKf8CIQC6QkN12T7Gxc/WR4Rj83wo3DLPWBV3UOYRLkn2xgIuaAQgEvD575IDFcH+Icgs9CXsZm8fMixHOCePvqkIrwR7MM4EEBDD6WHzIUR14PoSQmXL45AwPQYKKoEcz1UGAQQCATAdBgkqgRzPVQFoAQQEEPjsvxBTs6GD42W9eawA+NCAEOgQW5nZapUZg+7rdzDqzgc=";
        NetonePCS response = pcsClient.envelopeUnpackBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setBase64Data(data)//设置待解包的数据
                .build();
        if (response.getStatusCode() == 200) {
            System.out.println("结果 Base64："+response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }

    /**
     * 非对称加解密
     * @throws NetonejException
     */
    @Test
    public void priKeyEncrypt() throws NetonejException {
        String data = "0200000000000000041001010000000000000000000000000000000002SN";
        NetonePCS pcs = pcsClient.publicKeyBuilder()
                .setId(sm2kid)
                .setData(data)
                .setEncrypt().build();
        System.out.println("加密结果："+pcs.getResult());
        pcs = pcsClient.privateKeyBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setBase64Data(pcs.getResult())
                .setDecrypt().build();
        System.out.println("解密结果："+new String(org.bouncycastle.util.encoders.Base64.decode(pcs.getResult())));
    }

    /**
     * 修改PIN口令
     * @throws NetonejException
     */
    @Test
    public void testChangepin() throws NetonejException {
        NetonePCS response = pcsClient.pinBuilder()
                .setId(sm2kid)
                .setOldpwd(pin)//设置旧密码
                .setNewpwd("123456")//设置新密码
                .build();
        if (response.getStatusCode() == 200) {
            //0: 成功
            //1: 旧口令验证不通过
            //2: 密钥库操作失败
            //3: 更新新口令失败
            System.out.println("结果 Base64："+response.getResult());
        }
        //错误描述
        System.out.println(response.getStatusCodeMessage());
    }
}
