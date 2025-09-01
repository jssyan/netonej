package cn.com.syan.netonej.ccgw;


import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.CcgwClient;
import com.syan.netonej.http.client.EngineClient;
import com.syan.netonej.http.entity.engine.*;
import org.junit.Test;

/**
 * @Author: xuyaoyao
 * @Date: 2025/9/1 10:32
 * @Description:
 */
public class EngineTest {
    private CcgwClient client = new CcgwClient("http://192.168.10.89","8028","bbb622eea8faf4a9"," 936b55c4e8c94cdbb00e1aadc4887b8d");
    EngineClient engineClient=client.EngineClient();

    //明文
    String plaintext = "YWUyNDljYzU0MzlmMTUwNA==";

    //对称解密 密文
    String ciphertext="Nemn1n1P0T/rTSZGAAC32NUquTgfgYWFSIXombHBdKM=";

    //非对称解密 密文
    String ciphertext2="BDZ9i3iwwpOuWC1o54s2L3QPJhyRBcxhdxi5sIqo6beHG2/dC5FlVJdcuBRdfKWndF55MPqpuBaXnsyuA/RhtWIBuy/KMnaoypUVEJsFgk7EvkCalU/IY6cVl11qt6TzoeF2ndDNSnu74xpQJrGsu2U=";

    //签名
    String signature = "MEYCIQC/DZnQkyP0Kz90ZMaqaIwlnTAoYF0/qbZb2tE5gA+oLgIhAMQNHG7PbZ2uBgJI+SVzLqRylYa7if8lQToUlN/sKNUI";


    /**
     * 创建随机数
     * @throws NetonejException
     */
    @Test
    public void testGenrand() throws NetonejException {

        RandomResponse response = engineClient.randBuilder()
                .setLength(32) //随机数长度
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * 对称加密
     * @throws NetonejException
     */
    @Test
    public void testEnc() throws NetonejException {

        EncrtptResponse response = engineClient.encryptBuilder()
                //必填参数
                .setKeyId(1)
                .setPlaintext(plaintext) //明文
                //选填
                .setAlgorithm("SM4") //对称加密算法类型：SM1,SM4,AES
                .setAlgorithmMode("ECB") //加密模式：CBC,ECB,OFB,CFB
                .setPaddingMode("PKCS5Padding") //填充模式：PKCS5Padding,NoPadding
                .setIv("777f7477417943b6") //初始化向量
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * 对称解密
     * @throws NetonejException
     */
    @Test
    public void testDec() throws NetonejException {

        DecrtptResponse response = engineClient.decryptBuilder()
                //必填
                .setKeyId(1)
                .setCiphertext(ciphertext) //密文
                //选填
                .setAlgorithm("SM4") //对称加密算法类型：SM1,SM4,AES
                .setAlgorithmMode("ECB") //加密模式：CBC,ECB,OFB,CFB
                .setPaddingMode("PKCS5Padding") //填充模式：PKCS5Padding,NoPadding
                .setIv("777f7477417943b6") //初始化向量
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * hmac
     * @throws NetonejException
     */
    @Test
    public void testHmac() throws NetonejException {

        HmacResponse response = engineClient.hmacBuilder()
                .setKeyId(1)
                .setPlaintext(plaintext)
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * cmac
     * @throws NetonejException
     */
    @Test
    public void testCmac() throws NetonejException {

        CmacResponse response = engineClient.cmacBuilder()
                //必填参数
                .setKeyId(1)
                .setPlaintext(plaintext)
                //选填
                .setIv("a5ab73f5028dc95a")
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * 签名
     * @throws NetonejException
     */
    @Test
    public void testSign() throws NetonejException {

        SignResponse response = engineClient.signBuilder()
                //必填参数
                .setKeyId(1)
                .setMessage(plaintext) //待签名数据
                //选填
                .setMessageType("RAW") //数据类型
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }


    /**
     * 签名
     * @throws NetonejException
     */
    @Test
    public void testVerify() throws NetonejException {

        VerifyResponse response = engineClient.verifyBuilder()
                //必填参数
                .setKeyId(1)
                .setMessage(plaintext) //待签名数据
                .setSignature(signature) //签名值
                //选填
                .setMessageType("RAW")
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * 非对称加密
     * @throws NetonejException
     */
    @Test
    public void testAsyEnc() throws NetonejException {

        AsyEncryptResponse response = engineClient.asyEncryptBuilder()
                .setKeyId(1)
                .setPlaintext(plaintext)
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * 非对称解密
     * @throws NetonejException
     */
    @Test
    public void testAsyDec() throws NetonejException {

        AsyDecryptResponse response = engineClient.asyDecryptBuilder()
                .setKeyId(1)
                .setCiphertext(ciphertext2)
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());
    }

    /**
     * 导出公钥
     * @throws NetonejException
     */
    @Test
    public void testPub() throws NetonejException {

        PublicKeyResponse response = engineClient.publicKeyBuilder()
                .setKeyId(1)
                .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getSignPublicKey());
        System.out.println(response.getEncPublicKey());
    }

}
