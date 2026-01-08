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
    String sm2kid = "c633fa077422e9f20ccbfe697b20695e";

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

        NetoneKeyList response =
                pcsClient.keyBuilder()
                        .setLimit(10) //可选，返回前n个符合条件的密钥
                        .setKeyUseage(KeyUseage.SIGN)//可选，用于返回特定用法的密钥列表（根据证书对应的密钥用法）
                        .setKeyAlgorithm(KeyAlgorithm.SM2) //可选,用于返回特定算法的密钥
                        .build();
        //结果
        System.out.println(response.getStatusCode());
        //错误描述
        System.out.println(response.getStatusCodeMessage());
        List<KeyListItem> list =  response.getKeyList();
        System.out.println(list.size());
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
        //结果
        System.out.println(response.getStatusCode());
        //错误描述
        System.out.println(response.getResult());
    }

    /**
     * 根据KID获取证书
     * @throws NetonejException
     */
    @Test
    public void getBase64CertificateById() throws Exception {
        NetoneCertificate certificate = pcsClient.certificateBuilder()
                .setId(sm2kid)
                .setIdmagic(IdMagic.KID)
                //也可通过证书CN项的方式，按照如下两行设置即可：
                //.setId(sm2cn)
                //.setIdmagic(IdMagic.SCN)
                .build();
        System.out.println(certificate.getSubject());
        System.out.println(certificate.getHasPrivkey());
    }

    /**
     * P1签名
     */
    @Test
    public void createPKCS1SignatureByCID() throws NetonejException {
        byte[] data = "SN".getBytes();
        NetonePCS pcs = pcsClient.pkcs1Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId("1") //设置id参数，这里设置的证书cn项
                .setIdmagic(IdMagic.CID)
                .setData(data)//签名原文
                .build();
        System.out.println("响应码："+pcs.getStatusCode());
        System.out.println("RAW签名结果 Base64:"+pcs.getResult());
    }

    @Test
    public void createPKCS1Signature() throws NetonejException {
        byte[] data = "0200000000000000041001010000000000000000000000000000000002SN".getBytes();
        NetonePCS pcs = pcsClient.pkcs1Builder()
                .setApplication("abck2")
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2kid) //设置id参数，这里设置的证书cn项
                .setData(data)//签名原文
                .setAlgo(DigestAlgorithm.SM3)
                .build();
        System.out.println("响应码："+pcs.getStatusCode());
        System.out.println("RAW签名结果 Base64:"+pcs.getResult());
    }

    /**
     * XML签名
     * @throws NetonejException
     */
    @Test
    public void createXMLSignature() throws NetonejException {
        String data = "<data>123456</data>";
        NetonePCS pcs = pcsClient.xmlSignBuilder()
                .setPasswd(pin)
                .setId(sm2cn)
                .setIdmagic(IdMagic.SCN)
                .setData(data)
                .setSigmode(SignMode.enveloping)
                .setAlgo(DigestAlgorithm.SHA1)
                .build();
        System.out.println(pcs.getResult());

        System.out.println(new String(Base64.decode(pcs.getResult())));
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
        NetonePCS pcs = pcsClient.pkcs7Builder()
                .setPasswd("Syan@9108")//可选，设置私钥保护口令
                .setId("8047398788918a8a21c49b151bafc7ca")
                .setIdmagic(IdMagic.KID)
                .setData(data)
                .setAttach(true)//可选，签名结果中是否包含原始数据
                .setFullchain(false)//可选，签名结果是否嵌入整个证书链
                .setNoattr(false)//可选，签名结果中是否包含签名时间等属性
                .build();
        System.out.println("响应码："+pcs.getStatusCode());
        System.out.println("Detach签名结果 Base64:\n"+pcs.getResult());
    }

    /**
     * 数字信封  -- 封包
     * @throws NetonejException
     */
    @Test
    public void createEnvelopePacket() throws Exception {
        byte[] data = "02".getBytes();
        NetonePCS pcs = pcsClient.envelopePacketBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo(CipherAlgorithm.SM4CBC)//可选，设置对称密钥算法
                .build();
        System.out.println("响应码："+pcs.getStatusCode());
        System.out.println("信封结果 Base64:"+pcs.getResult());
    }
    /**
     * 数字信封  -- 封包（带签名）
     * @throws NetonejException
     */
    @Test
    public void createEnvelopePacketAndSign() throws Exception {
        byte[] data = "02".getBytes();
        String crt = "MIICJzCCAcygAwIBAgIGAY9/Z3b+MAwGCCqBHM9VAYN1BQAwSzELMAkGA1UEBhMCQ04xDjAMBgNVBAoTBUdNU1NMMRAwDgYDVQQLEwdQS0kvU00yMRowGAYDVQQDExFNaWRkbGVDQSBmb3IgVGVzdDAiGA8yMDI0MDUxNTE2MDAwMFoYDzIwMjUwNTE1MTYwMDAwWjBXMQswCQYDVQQGEwJDTjETMBEGA1UECAwKdGlhbmppbnNoaTEQMA4GA1UEBwwHaGViZWlxdTEMMAoGA1UECgwDY3QyMRMwEQYDVQQDDApzeWFuX3NtMl8xMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE06iAgLh+gAfJGlrmHzhos24bs07HPxmpNCS8WRKuyHMOeCb4i0g9t9vS3fkeYaYodDyR5SE0prnuYcmj2qLtIKOBiTCBhjAbBgNVHSMEFDASgBD5f1W0J5QzYqZWym/MXRr/MBkGA1UdDgQSBBBnJQgrZE8kPBgx7JafDdhHMDEGCCsGAQUFBwEBBCUwIzAhBggrBgEFBQcwAYYVaHR0cHM6Ly9vY3NwLmdtc3NsLmNuMAkGA1UdEwQCMAAwDgYDVR0PAQH/BAQDAgDAMAwGCCqBHM9VAYN1BQADRwAwRAIgL6rXLyDMEhgpqMfMU+y/gViUrFaD8iimLT4tJqMkg4QCIF7O2hcEq7V8IOlVtHfXylGrAiQftHjg2+mKqzptgM08";
        System.out.println(data.length);
        NetonePCS pcs = pcsClient.envelopePacketBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo(CipherAlgorithm.SM4CBC)//可选，设置对称密钥算法
                .setPeer(crt)//可选，设置加密证书
                //.setPeerMagic(IdMagic.KID)//可选，指定加密证书的类型
                .build();
        System.out.println("响应码："+pcs.getStatusCode());
        System.out.println("信封结果 Base64:"+pcs.getResult());
    }

    /**
     * 数字信封  -- 解包
     * @throws NetonejException
     */
    @Test
    public void createEnvelopeUnPacket() throws Exception {
        //String data = "MIIFGgYKKoEcz1UGAQQCBKCCBQowggUGAgEBMYHEMIHBAgEAMDEwIDELMAkGA1UEBhMCQ04xETAPBgNVBAMMCHNtMl9yb290Ag0AkK1xCLXFaCiIW7nRMA0GCSqBHM9VAYItAwUABHoweAIgSqCp1o+VLWNRAckKPwWRebuvsvICSJuNMqg84tFNbOICIHhulXC9ZaKds4OyuwJZyJb63P1Im74nPz02q/l/5ByTBCAkyUyOdc1Qv3tWvqdO0MVBGnBpxCRdJG8BLRsAFNCYMgQQUjFDbbtw2GbA4LxdkInKVTEOMAwGCCqBHM9VAYMRBQAwKwYKKoEcz1UGAQQCATALBgkqgRzPVQFoAQOAECDvHOULMmpBi8BIPZPz1CSgggNaMIIDVjCCAvygAwIBAgINAJCtcQi1xWgoiFu50TAKBggqgRzPVQGDdTAgMQswCQYDVQQGEwJDTjERMA8GA1UEAwwIc20yX3Jvb3QwHhcNMjExMjA3MjIxNjQ1WhcNMzkxMjA3MTYwMDAwWjBlMQ8wDQYDVQQIDAbnoJTlj5ExEjAQBgNVBAcMCeWNl+S6rOW4gjENMAsGA1UECgwEc3lhbjESMBAGA1UECwwJ56CU5Y+R6YOoMRswGQYDVQQDDBJzcGFya1/pgJrorq/or4HkuaYwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATbf6mIrEzgpIv1RiRVMl7cRNbzTYay9AknqdEjNflw7WQ2Nz8yAQvireaHFVX6mogKwXvfwqceoF/9Woil9ypoo4IB1DCCAdAwCQYDVR0TBAIwADAdBgNVHQ4EFgQUk2lZgdL1LctBlyyLk6YJzTanbuowDgYDVR0PAQH/BAQDAgD/MIGbBgNVHSUBAf8EgZAwgY0GCCsGAQUFBwMBBggrBgEFBQcDAgYIKwYBBQUHAwMGCCsGAQUFBwMEBggrBgEFBQcDCAYKKwYBBAGCNwIBFQYKKwYBBAGCNwIBFgYKKwYBBAGCNwoDAQYKKwYBBAGCNwoDAwYKKwYBBAGCNwoDBAYJYIZIAYb4QgQBBggrBgEFBQcDCQYIKwYBBQUHAwowLgYDVR0fBCcwJTAjoCGgH4YdaHR0cHM6Ly9haWEuc3lhbi5jb20uY24vY3JsL2EwYgYIKwYBBQUHAQEEVjBUMCQGCCsGAQUFBzABhhhodHRwczovL29jc3Auc3lhbi5jb20uY24wLAYIKwYBBQUHMAKGIGh0dHBzOi8vYWlhLnN5YW4uY29tLmNuL2lzc3Vlci9hMB8GA1UdIwQYMBaAFE62EA55ojyyltMkTdh/2Ac4lpUEMEEGA1UdIAQ6MDgwNgYIKoEchvAAZAEwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly9jcHMuc3lhbi5jb20uY24vY3BzMTAKBggqgRzPVQGDdQNIADBFAiEAmKlOSDGvzcD/Z19/5zIItoTtaUa02I8+lJoaz4SSGdoCIExCV6F474ctRWqFt3sEHC9pi1QJuglYSmSdXgMvVTYmMYGeMIGbAgEBMDEwIDELMAkGA1UEBhMCQ04xETAPBgNVBAMMCHNtMl9yb290Ag0AkK1xCLXFaCiIW7nRMAwGCCqBHM9VAYMRBQAwDQYJKoEcz1UBgi0BBQAERjBEAiAdhYQRl7hhn/rjS+PLjisP5evOlTdcdgJBIPlPYrMSqAIgTscVYJiX6eXQL7Fc/BHdKnr7TxZ8SvyLnX6vEvmky1E=";
        String data = "MIIBFAYKKoEcz1UGAQQCA6CCAQQwggEAAgEBMYG8MIG5AgEAMCowGjELMAkGA1UEBhMCQ04xCzAJBgNVBAMMAjMzAgwUnT/1UCfO1U53zucwCwYJKoEcz1UBgi0DBHsweQIhAJyeYpFFuH0xAX+I7TVru/6S0b+5c1WK0s7/LNzXWpDYAiAGvE9NPJ2ainxDIHlsz+a01Ve1i/QuJbPQ34+4zq8zNAQgA7UDQOCjfL6gAoMTy1LjW82CdwtLgPXbvKWs3tC1cVoEEPHpf1gAuSLnY6iqVUbwnTgwPAYKKoEcz1UGAQQCATAcBggqgRzPVQFoAgQQfLJ2NTW9TxM9+TEXuGEzMIAQgV0dy5g3P5e6KfgpxYR0pg==";
        NetonePCS pcs = pcsClient.envelopeUnpackBuilder()
                .setId("9845c5a9a7abc07a9f5c13597ab190ce")
                .setPasswd(pin)
                .setBase64Data(data)//设置待解包的数据
                .build();
        System.out.println(pcs.getResult());
    }

    /**
     * 非对称加解密
     * @throws NetonejException
     */
    @Test
    public void priKeyEncrypt() throws NetonejException {
        String data = "0200000000000000041001010000000000000000000000000000000002SN";
        NetonePCS pcs = pcsClient.publicKeyBuilder()
                .setApplication("abck2")
                .setId(sm2kid)
                .setData(data)
                .setEncrypt().build();
        System.out.println("加密结果："+pcs.getResult());
        pcs = pcsClient.privateKeyBuilder()
                .setApplication("abck2")
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
        //0: 成功
        //1: 旧口令验证不通过
        //2: 密钥库操作失败
        //3: 更新新口令失败
        System.out.println(response.getResult());
    }
}
