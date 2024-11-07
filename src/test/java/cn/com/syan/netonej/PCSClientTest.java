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

    //证书ID
    String sm2kid = "748cd89c206f0561ad6f8b5ee8b7a737";

    String rsaKid = "19e4ee5c5153e19f4bd6ff6349269254";

    //证书的CN项
    String sm2cn = "spark_通讯证书";

    //证书的CN项
    String rsacn = "testssl";

    //证书的私钥保护口令
    String pin = "Syan@9108";


    @Test
    public void testFileSign_filePath() throws Exception{

        String in = "/Users/kisscat/Documents/spark-ebox-syan-1.0.5.apk";
        String out = "/Users/kisscat/Documents/spark-ebox-syan-1.0.5.iapk";
        pcsClient.fileSignBuilder().setId(sm2kid).setIdmagic(IdMagic.KID).setPasswd(pin)
                .setAlgo("SM3").build(in,out);
    }

    /**
     * 文件保护结构
     * @throws Exception
     */
    @Test
    public void testFileSign_fileData() throws Exception{
        //byte[] data= "123".getBytes();

        byte[] data = FileUtil.read("/Users/kisscat/Documents/spark-ebox-syan-1.0.5.apk");

        NetoneResponse response = pcsClient.fileSignBuilder().setId(sm2kid).setIdmagic(IdMagic.KID).setPasswd(pin)
                .setAlgo("SM3").build(data);

        System.out.println(response.getStatusCode());
        System.out.println(Base64.toBase64String(response.getBytesResult()));
    }

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
     * 获取可用的PCS中的kid
     * @throws NetonejException
     */
    @Test
    public void testGenrand() throws NetonejException {

        NetonePCS response =
                pcsClient.randomBuilder()
                        .setLength(32)
                        .build();
        //结果
        System.out.println(response.getStatusCode());
        //错误描述
        System.out.println(response.getResult());
    }


    public String getCN(String subject){
        if(!NetonejUtil.isEmpty(subject)){
            String[] scns = subject.split(",");
            for (String ob :scns){
                String[] cns = ob.split("=");
                if(cns[0].equals("CN")){
                    return cns[1];
                }
            }
        }
        return "";
    }


    /**
     * 获取证书
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
     * 签名
     */
    @Test
    public void createPKCS1Signature() throws NetonejException {
        byte[] data = "0200000000000000041001010000000000000000000000000000000002SN".getBytes();
        NetonePCS pcs = pcsClient.pkcs1Builder()
                .setApplication("abck2")
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2kid) //设置id参数，这里设置的证书cn项
                .setData(data)//签名原文
                .build();
        System.out.println("响应码："+pcs.getStatusCode());
        System.out.println("RAW签名结果 Base64:"+pcs.getResult());
    }

    @Test
    public void createPKCS1SignatureMore() throws NetonejException {

        for(int i= 0;i<1000;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] data = "123".getBytes();
                    try {
                        NetonePCS pcs = pcsClient.pkcs1Builder()
                                .setPasswd(pin)//可选，设置私钥保护口令
                                .setId(sm2kid) //设置id参数，这里设置的证书cn项
                                //.setIdmagic(IdMagic.SNHEX)//指定id的数据类型
                                .setData(data)//签名原文
                                .setDataType(DataType.PLAIN)//可选，默认为原文签名
                                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)//可选,指定签名摘要算法
                                //.setUserId("userid".getBytes())//可选，SM2签发者ID
                                .build();
                        System.out.println(pcs.getStatusCode());
                        System.out.println(pcs.getResult());
                    } catch (NetonejException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

        }

        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        byte[] data = "0200000000000000041001010000000000000000000000000000000002SN".getBytes();
        NetonePCS pcs = pcsClient.pkcs7Builder()
                .setApplication("abck2")
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2kid)
                .setIdmagic(IdMagic.KID)
                .setData(data)
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)
                .setAttach(false)//可选，签名结果中是否包含原始数据
                .setFullchain(false)//可选，签名结果是否嵌入整个证书链
                .setNoattr(false)//可选，签名结果中是否包含签名时间等属性
                .build();
        System.out.println("响应码："+pcs.getStatusCode());
        System.out.println("Detach签名结果 Base64:"+pcs.getResult());
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
        System.out.println(data.length);
        NetonePCS pcs = pcsClient.envelopePacketBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo(CipherAlgorithm.SM4ECB)//可选，设置对称密钥算法
                .setPeer(sm2kid)//可选，设置加密证书
                .setPeerMagic(IdMagic.KID)//可选，指定加密证书的类型
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
        String data = "MIIFGgYKKoEcz1UGAQQCBKCCBQowggUGAgEBMYHEMIHBAgEAMDEwIDELMAkGA1UEBhMCQ04xETAPBgNVBAMMCHNtMl9yb290Ag0AkK1xCLXFaCiIW7nRMA0GCSqBHM9VAYItAwUABHoweAIgSqCp1o+VLWNRAckKPwWRebuvsvICSJuNMqg84tFNbOICIHhulXC9ZaKds4OyuwJZyJb63P1Im74nPz02q/l/5ByTBCAkyUyOdc1Qv3tWvqdO0MVBGnBpxCRdJG8BLRsAFNCYMgQQUjFDbbtw2GbA4LxdkInKVTEOMAwGCCqBHM9VAYMRBQAwKwYKKoEcz1UGAQQCATALBgkqgRzPVQFoAQOAECDvHOULMmpBi8BIPZPz1CSgggNaMIIDVjCCAvygAwIBAgINAJCtcQi1xWgoiFu50TAKBggqgRzPVQGDdTAgMQswCQYDVQQGEwJDTjERMA8GA1UEAwwIc20yX3Jvb3QwHhcNMjExMjA3MjIxNjQ1WhcNMzkxMjA3MTYwMDAwWjBlMQ8wDQYDVQQIDAbnoJTlj5ExEjAQBgNVBAcMCeWNl+S6rOW4gjENMAsGA1UECgwEc3lhbjESMBAGA1UECwwJ56CU5Y+R6YOoMRswGQYDVQQDDBJzcGFya1/pgJrorq/or4HkuaYwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATbf6mIrEzgpIv1RiRVMl7cRNbzTYay9AknqdEjNflw7WQ2Nz8yAQvireaHFVX6mogKwXvfwqceoF/9Woil9ypoo4IB1DCCAdAwCQYDVR0TBAIwADAdBgNVHQ4EFgQUk2lZgdL1LctBlyyLk6YJzTanbuowDgYDVR0PAQH/BAQDAgD/MIGbBgNVHSUBAf8EgZAwgY0GCCsGAQUFBwMBBggrBgEFBQcDAgYIKwYBBQUHAwMGCCsGAQUFBwMEBggrBgEFBQcDCAYKKwYBBAGCNwIBFQYKKwYBBAGCNwIBFgYKKwYBBAGCNwoDAQYKKwYBBAGCNwoDAwYKKwYBBAGCNwoDBAYJYIZIAYb4QgQBBggrBgEFBQcDCQYIKwYBBQUHAwowLgYDVR0fBCcwJTAjoCGgH4YdaHR0cHM6Ly9haWEuc3lhbi5jb20uY24vY3JsL2EwYgYIKwYBBQUHAQEEVjBUMCQGCCsGAQUFBzABhhhodHRwczovL29jc3Auc3lhbi5jb20uY24wLAYIKwYBBQUHMAKGIGh0dHBzOi8vYWlhLnN5YW4uY29tLmNuL2lzc3Vlci9hMB8GA1UdIwQYMBaAFE62EA55ojyyltMkTdh/2Ac4lpUEMEEGA1UdIAQ6MDgwNgYIKoEchvAAZAEwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly9jcHMuc3lhbi5jb20uY24vY3BzMTAKBggqgRzPVQGDdQNIADBFAiEAmKlOSDGvzcD/Z19/5zIItoTtaUa02I8+lJoaz4SSGdoCIExCV6F474ctRWqFt3sEHC9pi1QJuglYSmSdXgMvVTYmMYGeMIGbAgEBMDEwIDELMAkGA1UEBhMCQ04xETAPBgNVBAMMCHNtMl9yb290Ag0AkK1xCLXFaCiIW7nRMAwGCCqBHM9VAYMRBQAwDQYJKoEcz1UBgi0BBQAERjBEAiAdhYQRl7hhn/rjS+PLjisP5evOlTdcdgJBIPlPYrMSqAIgTscVYJiX6eXQL7Fc/BHdKnr7TxZ8SvyLnX6vEvmky1E=";
        NetonePCS pcs = pcsClient.envelopeUnpackBuilder()
                .setId(sm2kid)
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
     * 对称加解密
     */
    @Test
    public void symEncrypt() throws NetonejException {
        String data = "0200000000000000041001010000000000000000000000000000000002SN";
        NetonePCS pcs = pcsClient.symmetricEncryptBuilder()
                .setApplication("abck2")
                .setCipher("SM4-CBC")
                .setData(data)
                .setIv("0000000000000000")
                .build();
        System.out.println("加密结果："+pcs.getResult());
        NetonePCS pcsD = pcsClient.symmetricDecryptBuilder()
                .setApplication("abck2")
                .setCipher("SM4-CBC")
                .setData(data)
                .setIv("0000000000000000")
                .build();
        System.out.println("解密结果："+new String(pcsD.getResult()));
    }


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
