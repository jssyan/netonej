package cn.com.syan.netonej;

import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.NetoneSignPKCS7;
import org.junit.Test;
import java.util.Base64;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class PCSClientTest {


    PCSClient client = new PCSClient("192.168.10.215");

    String kid = "afaf4cdb49964c24e172112f2a4b98c9";

    String cn = "J-N-S-P-E";

    String pin = "111111";

    /**
     * 获取可用的PCS中的kid
     * @throws NetonejExcepption
     */
    @Test
    public void testGetKids() throws NetonejExcepption {
        NetoneKeyList keyList = client.getKids();
        System.out.println(keyList.getKeyList());
    }

    /**
     * 根据kid获取证书
     * @throws NetonejExcepption
     */
    @Test
    public void getBase64CertificateById() throws NetonejExcepption {
        NetoneCertificate certificate;
        //根据kid
        //certificate = client.getBase64CertificateById(kid);
        //根据证书主题项的CN
        certificate = client.getBase64CertificateById(cn,IdMagic.SCN);
        System.out.println(certificate.getCertBase64String());
    }

    /**
     * p1签名
     * @throws NetonejExcepption
     */
    @Test
    public void createPKCS1Signature() throws NetonejExcepption {
        String data = Base64.getEncoder().encodeToString("123456".getBytes());
        NetonePCS pcs;

        //pcs = client.createPKCS1Signature(kid,pin,data, DataType.PLAIN, DigestAlgorithm.ECDSASM2WITHSM3);

        pcs = client.createPKCS1Signature(cn,pin,IdMagic.SCN,data,DataType.PLAIN);


        System.out.println(pcs.getRetBase64String());
    }

    /**
     * XML签名
     * @throws NetonejExcepption
     */
    @Test
    public void createXMLSignature() throws NetonejExcepption {
        String data = Base64.getEncoder().encodeToString("<data>123456</data>".getBytes());
        NetonePCS pcs = client.createXMLSignature(kid,pin,data, SignMode.enveloping);
        System.out.println(pcs.getRetBase64String());
    }

    /**
     * p7签名
     * @throws NetonejExcepption
     */
    @Test
    public void createPKCS7Signature() throws NetonejExcepption {
        String data = Base64.getEncoder().encodeToString("123456".getBytes());
        //attach
        NetoneSignPKCS7 pcs;

        //pcs = client.createPKCS7Signature(kid,pin,data);

        //dettch
        //pcs = client.createPKCS7Signature(kid,pin,data,false);

        //使用CN
        //pcs = client.createPKCS7Signature(cn,pin,IdMagic.SCN,data);

        pcs = client.createPKCS7Signature(cn,pin,IdMagic.SCN,data,false);

        System.out.println(pcs.getRetBase64String());
    }

    /**
     * 数字信封  -- 封包
     * @throws NetonejExcepption
     */
    @Test
    public void createEnvelopePacket() throws NetonejExcepption {
        String data = Base64.getEncoder().encodeToString("123456".getBytes());

        System.out.println(data);
        NetonePCS pcs;

        pcs = client.envelopePacket(kid,pin,data, CipherAlgorithm.AES192CBC);

        //使用SCN
        pcs = client.envelopePacket(cn,pin,IdMagic.SCN,data, CipherAlgorithm.AES192CBC);

        System.out.println(pcs.getRetBase64String());
    }

    /**
     * 数字信封  -- 解包
     * @throws NetonejExcepption
     */
    @Test
    public void createEnvelopeUnPacket() throws NetonejExcepption {
        String data = "MIIBeAYKKoEcz1UGAQQCA6CCAWgwggFkAgEBMYIBHjCCARoCAQAwfjBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRQINAPjxdl3mFMf3ySNchjANBgkqgRzPVQGCLQMFAASBhTCBggIhANveuazYmp2po1Hbh7p7ZIYdhSmG6wLzmVfXLnldPNHaAiEA4Qzo2jl3t844nQ3Epb82VqRHK7jyumhZVNiBwxVZAooEIMJK5zgyAqhxoF4t+aJGX4i/CIVcK9PJ/aqWsu26RGtBBBgMlP+wdI4RNl9tQ5q93NKhu2JfcTnY8iAwPQYKKoEcz1UGAQQCATAdBglghkgBZQMEARYEEO5vaacYr3kSJPnn1ahuYieAENGnippyGCBV/SLYLfCSo9o=";
        NetonePCS pcs;

        pcs = client.envelopeUnpack(kid,pin,data);

        //使用SCN
        pcs = client.envelopeUnpack(cn,pin,IdMagic.SCN,data);

        System.out.println(new String(Base64.getDecoder().decode(pcs.getRetBase64String())));
    }



    /**
     * 非对称加解密
     * @throws NetonejExcepption
     */
    @Test
    public void priKeyEncrypt() throws NetonejExcepption {

        String data = Base64.getEncoder().encodeToString("123456".getBytes());
        System.out.println(data);

        //私钥加密
        NetonePCS pcs = client.priKeyEncrypt("daef426db67d7bee2c2dd8938730f262","111111",data);
        System.out.println(pcs.getRetBase64String());
        //公钥解密
        pcs = client.pubKeyDecrypt("daef426db67d7bee2c2dd8938730f262","kid",pcs.getRetBase64String());
        System.out.println(pcs.getRetBase64String());

        //公钥加密
        pcs = client.pubKeyEncrypt("daef426db67d7bee2c2dd8938730f262",data);
        System.out.println(pcs.getRetBase64String());

        //私钥解密
        pcs = client.priKeyDecrypt("daef426db67d7bee2c2dd8938730f262","111111",pcs.getRetBase64String());
        System.out.println(pcs.getRetBase64String());

    }




}
