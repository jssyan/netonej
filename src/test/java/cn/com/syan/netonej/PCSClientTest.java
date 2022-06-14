package cn.com.syan.netonej;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.entity.*;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.util.List;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class PCSClientTest {

    private PCSClient pcsClient = new PCSClient("192.168.10.215","9178");


    String kid = "afaf4cdb49964c24e172112f2a4b98c9";
    String sm2kid = "afaf4cdb49964c24e172112f2a4b98c9";

    String cn = "J-N-S-P-E";

    String cert = "MIICNDCCAdugAwIBAgINAPjxdl3mFMf3ySNchjAKBggqgRzPVQGDdTBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRTAeFw0yMDAxMjAxNjAwMDBaFw0zMDAxMTcxNjAwMDBaMG0xCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJKUzELMAkGA1UEBwwCTkoxDTALBgNVBAoMBFN5YW4xEDAOBgNVBAsMB1Byb2R1Y2UxDzANBgNVBAwMBmVtcGxlZTESMBAGA1UEAwwJSi1OLVMtUC1FMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAETGxel2ar0ttp5IYu9asjRna+hgK8oqUDf7A6E/DSiYZSzGO35IKsNfUd3GVSxsmQeQr9vZyliEwbP9O7+BfrraNgMF4wDAYDVR0TBAUwAwEB/zAdBgNVHQ4EFgQUuFLOe2CMn8t6tYvmOVG6CtXOHGowHwYDVR0jBBgwFoAUuFLOe2CMn8t6tYvmOVG6CtXOHGowDgYDVR0PAQH/BAQDAgGGMAoGCCqBHM9VAYN1A0cAMEQCIBALIzaSMYahxAtkR0x/kd9z9OQ5R7RlCAgjVwiAXo1fAiAZu7rGRfVzozGvm32VcKD7bCp0DZ2bmtwwKuz5FA1tmQ==";

    String pin = "111111";

    String rsakid = "03ac7fd59857b8f850826b7f95f9c188";
    String rsacn = "黄健";
    String pemcert = "MIIEKjCCAxKgAwIBAgINAPfmrCYwyD95JvHzfDANBgkqhkiG9w0BAQsFADAxMQsw\n" +
            "CQYDVQQGEwJDTjERMA8GA1UECgwIQUJDIGx0ZC4xDzANBgNVBAMMBmNhLWdlbjAe\n" +
            "Fw0yMjA0MTUwNjI2NTJaFw00MDA0MTQxNjAwMDBaMB4xCzAJBgNVBAYTAkNOMQ8w\n" +
            "DQYDVQQDDAbpu4TlgaUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCw\n" +
            "Z2PoCOj06dDO1aRL3lX31LQMmvl6HvawPguUnyIN9K1mMod91WRjdn9wmJhhXOyS\n" +
            "2UJpynv5q8XCqL68dZ0JL421/AebbjmxjiECs/ledo6x4anh24C1+gXby0QVMNOD\n" +
            "6AGW4l8wA0b8Jy700mO1XA4iqm+39Pi4jkq6hNk4RASETI36SDC1Ipwu4uxKO/3X\n" +
            "dBmBhsh7MsVDlCzz2qXvUAdUCRCkiMO0NFBid0EHxLkTPghprrpL6kf6UJwObdWS\n" +
            "458ZVKFhbbS5qpCKaM+ZuvSjDvOeIrLVMTgwG7fp9F/TJF+Qzawl6F36ILhI0g8I\n" +
            "xipUr4cElEJ6x8XOaE4RAgMBAAGjggFSMIIBTjAMBgNVHRMEBTADAQH/MB0GA1Ud\n" +
            "DgQWBBTWAMD6bpcRp+2K9D3a0PqJVypCqDAPBgNVHQ8BAf8EBQMDB/+AMBYGA1Ud\n" +
            "JQEB/wQMMAoGCCsGAQUFBwMIMC4GA1UdHwQnMCUwI6AhoB+GHWh0dHBzOi8vYWlh\n" +
            "LnN5YW4uY29tLmNuL2NybC9hMGIGCCsGAQUFBwEBBFYwVDAkBggrBgEFBQcwAYYY\n" +
            "aHR0cHM6Ly9vY3NwLnN5YW4uY29tLmNuMCwGCCsGAQUFBzAChiBodHRwczovL2Fp\n" +
            "YS5zeWFuLmNvbS5jbi9pc3N1ZXIvYTAfBgNVHSMEGDAWgBRRRbE0SlfNI2qg2O9k\n" +
            "Y58PKI/FNzBBBgNVHSAEOjA4MDYGCCqBHIbwAGQBMCowKAYIKwYBBQUHAgEWHGh0\n" +
            "dHBzOi8vY3BzLnN5YW4uY29tLmNuL2NwczEwDQYJKoZIhvcNAQELBQADggEBACZz\n" +
            "0ZG5nb+4+yoCz8HXeIctB1z53KdaY93zBY7YA4G1jzI47128EKId8wN3hg1eBkpj\n" +
            "2rbFp3M53BfAl1bMo0AnM22ivesCF0GmplDErBRH9WQzR7nIVaZPkrceEVPlBE7R\n" +
            "0MLFs0UtX700op1OZat05OFN+gV2OFf/BztZ8fvP+xLWYEePKfG474m4iRz2xY6s\n" +
            "8ASF0nEfYQbic6N5VnXzMf3m6SLc5mWmCpSZlUm7SbX8ouNwuCJzua9lgLbJgEVB\n" +
            "xUhRhhAbVTj/A1uyM7B8I7uOmoAkQuDPrAJ7N0wLIoJhpVG/XVU/gniW/S2tezBV\n" +
            "cQnsQd2PSo+LpUXA5AQ=";

    String rsacert = pemcert.replaceAll("\n","");

    /**
     * 获取可用的PCS中的kid
     * @throws NetonejException
     */
    @Test
    public void testGetKids() throws NetonejException {

        System.out.println("");
        NetoneKeyList response =
                pcsClient.keyBuilder()
                        .setLimit(10) //可选，返回前n个符合条件的密钥
                        .setKeyUseage(KeyUseage.SIGN)//可选，用于返回特定用法的密钥列表（根据证书对应的密钥用法）
                        .setKeyAlgorithm(KeyAlgorithm.SM2) //可选,用于返回特定算法的密钥
                        .build();
        List<KeyListItem> list =  response.getKeyList();
        System.out.println(response.getStatusCode());
        System.out.println(response.getStatusCodeMessage());
        System.out.println(list.size());
    }

    /**
     * 获取证书
     * @throws NetonejException
     */
    @Test
    public void getBase64CertificateById() throws Exception {
        NetoneCertificate certificate = pcsClient.certificateBuilder()
                .setId(kid)
                .setIdmagic(IdMagic.KID)
                .build();
        System.out.println(certificate.getPEMString());
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
                .setId(cn) //设置id参数，这里设置的证书cn项
                .setIdmagic(IdMagic.SCN)//指定id的数据类型
                .setData(data)//签名原文
                .setDataType(DataType.PLAIN)//可选，默认为原文签名
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)//可选,指定签名摘要算法
                .setUserId("userid".getBytes())//可选，SM2签发者ID
                .build();
        System.out.println(pcs.getResult());
        System.out.println(pcs.getSingerCert());
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
                .setId(rsakid)
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
                .setId(kid)
                .setIdmagic(IdMagic.CID)
                .setData(data.getBytes())
                .setAlgo(DigestAlgorithm.ECDSASM2)
                .setAttach(false)//可选，签名结果中是否包含原始数据
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
                .setId(kid)
                .setPasswd(pin)
                .setData(data)
                .setCipherAlgo("sm1-cbc")//可选，设置对称密钥算法
                .setPeer(rsacert)//可选，设置加密证书
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
        String data = "MIIE3gYKKoEcz1UGAQQCBKCCBM4wggTKAgEBMYIBXjCCAVoCAQAwQjAxMQswCQYDVQQGEwJDTjERMA8GA1UECgwIQUJDIGx0ZC4xDzANBgNVBAMMBmNhLWdlbgINAPfmrCYwyD95JvHzfDANBgkqhkiG9w0BAQEFAASCAQCFrrCvyxHfvzdjiPW8jvf606pDLJ2ihzH/9iROhqzpNbOppQbgo550XciW4OEq/NLg4YxLzyU+V0BMdjZUt3FzLTbeojvJ3pLfG7xXpQQMnqEgALdP5tY3BE9lAZD7bgipvtkqL0HewKICmDFt292LSays5fo2SbDh4ZvF4ALcvW4ubDaEvWsZkLd4C9pbdOvTj/5+fdsHRRvKgQfrB4c8oYXOlvhXUnBXv0qA07jOUW41W/USoCISBJu8onkp7AgMhnHw5FzPE4LWK0pU6L2kuLmvzSInD/FmaeKlg3PibNnSZsZdZ79h90LhTtbGc+kNZcU3GHS9trFzoaz+NqLZMQ4wDAYIKoEcz1UBgxEFADAsBgoqgRzPVQYBBAIBMBQGCCqGSIb3DQMHBAgHSCBbvxB3u4AId9L0RnutxsygggI4MIICNDCCAdugAwIBAgINAPjxdl3mFMf3ySNchjAKBggqgRzPVQGDdTBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRTAeFw0yMDAxMjAxNjAwMDBaFw0zMDAxMTcxNjAwMDBaMG0xCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJKUzELMAkGA1UEBwwCTkoxDTALBgNVBAoMBFN5YW4xEDAOBgNVBAsMB1Byb2R1Y2UxDzANBgNVBAwMBmVtcGxlZTESMBAGA1UEAwwJSi1OLVMtUC1FMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAETGxel2ar0ttp5IYu9asjRna+hgK8oqUDf7A6E/DSiYZSzGO35IKsNfUd3GVSxsmQeQr9vZyliEwbP9O7+BfrraNgMF4wDAYDVR0TBAUwAwEB/zAdBgNVHQ4EFgQUuFLOe2CMn8t6tYvmOVG6CtXOHGowHwYDVR0jBBgwFoAUuFLOe2CMn8t6tYvmOVG6CtXOHGowDgYDVR0PAQH/BAQDAgGGMAoGCCqBHM9VAYN1A0cAMEQCIBALIzaSMYahxAtkR0x/kd9z9OQ5R7RlCAgjVwiAXo1fAiAZu7rGRfVzozGvm32VcKD7bCp0DZ2bmtwwKuz5FA1tmTGB6DCB5QIBATB+MG0xCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJKUzELMAkGA1UEBwwCTkoxDTALBgNVBAoMBFN5YW4xEDAOBgNVBAsMB1Byb2R1Y2UxDzANBgNVBAwMBmVtcGxlZTESMBAGA1UEAwwJSi1OLVMtUC1FAg0A+PF2XeYUx/fJI1yGMAwGCCqBHM9VAYMRBQAwDAYIKoEcz1UBg3UFADBEAiBG90Zi53iJMDIF0bn/zZr4s0HD9uLRQQfFzq+ifjpuvAIgXv16XMK7gVG/0wcCt0p1XTmGWnIKAoRTT3HEgg9HYJY=";
        NetonePCS pcs = pcsClient.envelopeUnpackBuilder()
                .setId(rsakid)
                .setPasswd(pin)
                .setBase64Data(data)//设置待解包的数据
                .build();
        System.out.println(pcs.getSingerCert().getSubject());
    }

    /**
     * 非对称加解密
     * @throws NetonejException
     */
    @Test
    public void priKeyEncrypt() throws NetonejException {

        String data = "123456";

        //私钥加密,ECC算法不支持私钥加密
        NetonePCS pcs = pcsClient.privateKeyBuilder()
                .setId(rsakid)
                .setPasswd(pin)
                .setData(data)
                .setEncrypt()//加密
                .build();
        //System.out.println(pcs.getResult());
        //公钥解密,ECC算法不支持公钥解密
        pcs = pcsClient.publicKeyBuilder()
                .setId(kid)
                .setBase64Data(pcs.getResult())
                .setDecrypt()
                .build();
        //System.out.println(pcs.getResult());
//
        //公钥加密
        pcs = pcsClient.publicKeyBuilder()
                .setId(kid)
                .setData(data)
                .setEncrypt()
                .build();
        System.out.println(pcs.getResult());
//
        //私钥解密
        pcs = pcsClient.privateKeyBuilder()
                .setId(kid)
                .setPasswd(pin)
                .setBase64Data(pcs.getResult())
                .setDecrypt()//解密
                .build();
        System.out.println(new String(org.bouncycastle.util.encoders.Base64.decode(pcs.getResult())));

    }


    @Test
    public void testChangepin() throws NetonejException {
        NetonePCS response = pcsClient.pinBuilder()
                .setId(kid)
                .setOldpwd(pin)//设置旧密码
                .setNewpwd("123456")//设置新密码
                .build();
        System.out.println(response.getResult());
    }
}
