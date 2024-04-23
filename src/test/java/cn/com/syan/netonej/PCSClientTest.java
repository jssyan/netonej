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
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class PCSClientTest {

    //设置PCS（私钥密码服务）的服务器IP与端口号
    private PCSClient pcsClient = new PCSClient("192.168.20.223","9178");

    //证书ID
    String sm2kid = "593e2e00fdfefced7428be967d6d5b72";

    String rsaKid = "03ac7fd59857b8f850826b7f95f9c188";

    //证书的CN项
    String sm2cn = "sm2";

    //证书的私钥保护口令
    String pin = "111111";


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
     * p1签名
     * @throws NetonejException
     */
    @Test
    public void createPKCS1Signature() throws NetonejException {
        byte[] data = "123".getBytes();
        NetonePCS pcs = pcsClient.pkcs1Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2kid) //设置id参数，这里设置的证书cn项
                //.setIdmagic(IdMagic.SNHEX)//指定id的数据类型
                .setData(data)//签名原文
                .setDataType(DataType.PLAIN)//可选，默认为原文签名
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)//可选,指定签名摘要算法
                //.setUserId("userid".getBytes())//可选，SM2签发者ID
                .build();
        System.out.println(pcs.getResult());
        //System.out.println(pcs.getSingerCert().getSubject());
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
        String data = "hello";
        NetonePCS pcs = pcsClient.pkcs7Builder()
                .setPasswd(pin)//可选，设置私钥保护口令
                .setId(sm2cn)
                .setIdmagic(IdMagic.SCN)
                .setData(data)
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)
                .setAttach(true)//可选，签名结果中是否包含原始数据
                .setFullchain(false)//可选，签名结果是否嵌入整个证书链
                .setNoattr(false)//可选，签名结果中是否包含签名时间等属性
                .build();
        System.out.println(pcs.getResult());

    }

    /**
     * 数字信封  -- 封包
     * @throws NetonejException
     */
    @Test
    public void createEnvelopePacket() throws Exception {
        String data = "123456";
        NetonePCS pcs = pcsClient.envelopePacketBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo(CipherAlgorithm.DES)//可选，设置对称密钥算法
                .setPeer(sm2kid)//可选，设置加密证书
                .setPeerMagic(IdMagic.KID)//可选，指定加密证书的类型
                .setDigestAlgo("ecdsa-sm2-with-sm3")//可选
                .build();
        System.out.println(pcs.getStatusCode());
        System.out.println("Base64:"+pcs.getResult());
        System.out.println("PEM:"+NetonejUtil.base64StringtoPem(pcs.getResult()));
    }

    /**
     * 数字信封  -- 解包
     * @throws NetonejException
     */
    @Test
    public void createEnvelopeUnPacket() throws Exception {
        String data = "MIIDswYKKoEcz1UGAQQCBKCCA6MwggOfAgEBMYHcMIHZAgEAMD8wLjELMAkGA1UEBhMCQ04xETAPBgNVBAoeCFFIW4l50WKAMQwwCgYDVQQDEwNzbTICDQC9ErluFqJv4/DOafkwDQYJKoEcz1UBgi0DBQAEgYMwgYACID0X0xw3SH8Niv1tWZz/jBppYO3c4V/wV1upxEhOvZgyAiA2VD2QHQRoU3r1hP664PJY9JYklNqQgA1M+ijD1SrFNAQgAAIwpnwFp2WwLarLQHUCWuHovLhSY6EHhaftemrduqAEGO2ti2dOBiVeETYHQuY2mOo4+Hqs60rGWTEOMAwGCCqBHM9VAYMRBQAwPQYKKoEcz1UGAQQCATAdBglghkgBZQMEARYEECGc0j7SLBB3HkaOPUGWfX2AEJ7ZRlge3GLGZOT3uP7i7higggG8MIIBuDCCAV6gAwIBAgINAL0SuW4Wom/j8M5p+TAKBggqgRzPVQGDdTAuMQswCQYDVQQGEwJDTjERMA8GA1UECh4IUUhbiXnRYoAxDDAKBgNVBAMTA3NtMjAiGA8yMDIwMDgwNzAwMDAwMFoYDzIwMzAwODA1MDAwMDAwWjAuMQswCQYDVQQGEwJDTjERMA8GA1UECh4IUUhbiXnRYoAxDDAKBgNVBAMTA3NtMjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABCDd/1wVqreFFn/Fhw2f3fMuYnzEB3r7RCfTnuIXcEH9YuEUgf3NZil/Wf+KHkYHid0r8MIqTxfC9u63k/Ss9PSjXTBbMAwGA1UdEwQFMAMBAf8wHQYDVR0OBBYEFMA0kmswVj5KCWI3kywDuKVC+GJeMB8GA1UdIwQYMBaAFMA0kmswVj5KCWI3kywDuKVC+GJeMAsGA1UdDwQEAwIBhjAKBggqgRzPVQGDdQNIADBFAiEAhc8Cf8dkDfLDgNKG9S7+0DV1qun+BSxy1q/vku5gi9gCIEK9K5kET/KGLEE7G01UF3EGQerVWLEj1WCeKekF/g25MYGrMIGoAgEBMD8wLjELMAkGA1UEBhMCQ04xETAPBgNVBAoeCFFIW4l50WKAMQwwCgYDVQQDEwNzbTICDQC9ErluFqJv4/DOafkwDAYIKoEcz1UBgxEFADAMBggqgRzPVQGDdQUAMEYCIQCGtisADMeyIVDor1MNWJjS8OQgZyb1h0OvlZvvDChLAgIhAPF5Br4XHjs6hwx6i+4BDnWF9VcTS7Cjs1dWCEvSS2DH";
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

        String data = "123456";

//        //私钥加密,ECC算法不支持私钥加密
//        NetonePCS pcs = pcsClient.privateKeyBuilder()
//                .setId(sm2kid)
//                .setPasswd(pin)
//                .setData(data)
//                .setEncrypt()//加密
//                .build();
//        System.out.println(pcs.getResult());
//        //公钥解密,ECC算法不支持公钥解密
//        pcs = pcsClient.publicKeyBuilder()
//                .setId(sm2kid)
//                .setBase64Data(pcs.getResult())
//                .setDecrypt()
//                .build();
//        System.out.println(pcs.getResult());
//
        //公钥加密
        NetonePCS pcs = pcsClient.publicKeyBuilder()
                .setId(sm2kid)
                .setData(data)
                .setEncrypt()
                .build();
        System.out.println(pcs.getResult());
//
        //私钥解密
        pcs = pcsClient.privateKeyBuilder()
                .setId(sm2kid)
                .setPasswd(pin)
                .setBase64Data(pcs.getResult())
                .setDecrypt()//解密
                .build();
        System.out.println(new String(org.bouncycastle.util.encoders.Base64.decode(pcs.getResult())));

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
