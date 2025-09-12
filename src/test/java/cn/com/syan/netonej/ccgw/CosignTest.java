package cn.com.syan.netonej.ccgw;



import com.syan.netonej.http.client.CcgwClient;
import com.syan.netonej.http.client.CosignClient;
import com.syan.netonej.http.entity.cosign.DecryptionResponse;
import com.syan.netonej.http.entity.cosign.KeypairResponse;
import com.syan.netonej.http.entity.cosign.RandResponse;
import com.syan.netonej.http.entity.cosign.SignatureResponse;
import org.junit.Test;

/**
 * @Author: xuyaoyao
 * @Date: 2025/8/29 13:24
 * @Description:
 */
public class CosignTest {

    private CcgwClient client = new CcgwClient("http://192.168.10.89","8028","88b4702c32e81c2d","efc1a098d31f4ea58653ac4d369f6da5");
    CosignClient cosignClient=client.CosignClient();

    String appid="ca23182701d44f16bbb28754b4fe042a";

    String transid="C6CAD49D2F30FD9C8D5A29B995E12F3169DE60AECD707A704DA89DA302CB0DE6";

    String sign="wwfmvkSKPEI3z949PCwY5lHq9GDa4WF4qaCRWlcyJJA=";

    String P1="0436234e80be0de00475277942b83690a05b91a5acec162fb589fafbfa10d1cd632473dec8b4e5f72c0eb6aae53b87900423df9f2391e231e3b9379621222e6686";

    String kid="718e57e55f5544b6a1e1442bfb64c394";

    String e="4170F359DCB12C9E53D582A5B2FE2911031A148A12271D133BBFF71308B12C70";

    String Q1="043753EFAE4DA9057982FA2E90CF9F70FB7A2F42DC36BCCA6454958F87ABC848D565783A535E3167C803B151BABF6626B46699157FEC3463D88B9B4B8CD3931ECC";

    String T1="04C6BC880C113BC1FCE33A330A802DB39951B6BFE79152395C410629461EC955BD0B0D3EE8EBD488C6006FB820B0FCCB9BF29D520D7F6A6B6858329FC6B6A85834";


    /**
     * 生成随机数
     * @throws Exception
     */
    @Test
    public void testRand() throws Exception {
        RandResponse response =
                cosignClient.genRandBuilder()
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setLength(32)
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println("随机数："+response.getResult());

    }

    /**
     * 生成密钥对
     * @throws Exception
     */
      @Test
    public void testKey() throws Exception {
        KeypairResponse response =
                cosignClient.genKeyBuilder()
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setP1(P1)
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println("密钥ID:"+response.getKid());
        System.out.println("服务端公钥:"+response.getP());

    }

    /**
     * 协同签名
     * @throws Exception
     */
    @Test
    public void testSign() throws Exception {
        SignatureResponse response =
                cosignClient.signatureBuilder()
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setKid(kid)
                        .setE(e)
                        .setQ1(Q1)
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println("r："+response.getR());
        System.out.println("s2："+response.getS2());
        System.out.println("s3："+response.getS3());

    }

    /**
     * 协同解密
     * @throws Exception
     */
    @Test
    public void testDec() throws Exception {
        DecryptionResponse response =
                cosignClient.decryptionBuilder()
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setKid(kid)
                        .setT1(T1)
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println("T2:"+response.getT2());

    }

}
