package cn.com.syan.netonej.ccgw;



import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.CcgwClient;
import com.syan.netonej.http.client.CosignClient;
import com.syan.netonej.http.client.ca.Extension;
import com.syan.netonej.http.entity.ca.CertificateResponse;
import com.syan.netonej.http.entity.ca.RevokeResponse;
import com.syan.netonej.http.entity.cosign.DecryptionResponse;
import com.syan.netonej.http.entity.cosign.KeypairResponse;
import com.syan.netonej.http.entity.cosign.RandResponse;
import com.syan.netonej.http.entity.cosign.SignatureResponse;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xuyaoyao
 * @Date: 2025/8/29 13:24
 * @Description:
 */
public class CosignTest {

    private CcgwClient client = new CcgwClient("http://192.168.20.135","8028","88b4702c32e81c2d","efc1a098d31f4ea58653ac4d369f6da5");
    CosignClient cosignClient=client.CosignClient();

    String appid="1bfd64f56d1d46bd8f2dcaa068139e69";

    String appSecret="2d1a2eb0";

    String transid="C6CAD49D2F30FD9C8D5A29B995E12F3169DE60AECD707A704DA89DA302CB0DE6";

 //   String sign="wwfmvkSKPEI3z949PCwY5lHq9GDa4WF4qaCRWlcyJJA=";

    String P1="0436234e80be0de00475277942b83690a05b91a5acec162fb589fafbfa10d1cd632473dec8b4e5f72c0eb6aae53b87900423df9f2391e231e3b9379621222e6686";

    String kid="8a2de50ab63e4608a9058d54608a3831";

    String e="4170F359DCB12C9E53D582A5B2FE2911031A148A12271D133BBFF71308B12C70";

    String Q1="043753EFAE4DA9057982FA2E90CF9F70FB7A2F42DC36BCCA6454958F87ABC848D565783A535E3167C803B151BABF6626B46699157FEC3463D88B9B4B8CD3931ECC";

    String T1="04C6BC880C113BC1FCE33A330A802DB39951B6BFE79152395C410629461EC955BD0B0D3EE8EBD488C6006FB820B0FCCB9BF29D520D7F6A6B6858329FC6B6A85834";


    String cn="xuyy";

    private String o="syan";

    private String ou="dev";

    private String c="CN";

    private String s="JS";

    private String l="NJ";

    private String email="123456@syan.com.cn";

    private String certType="1";

    String csr="MIHUMHwCAQAwHDELMAkGA1UEBhMCQ04xDTALBgNVBAMMBHRlc3QwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATuoySVj6zsgRdj2dgXNxw44vh16A5a33YxsTSsWAq+DI+TDd5ZmSxFFcwcAAfKGJGh3Lswy82O0kUhI48WqUs3MAoGCCqBHM9VAYN1A0gAMEUCIQDCZ62dxDPqNXSYGW1vJWCUNDxruIr4lcraul2UzzW5EgIgMIdwuMevOhUn2SA02rRkJvNwIqUdn5uaAbvt7UAco+w=";

    StringBuilder serial=new StringBuilder();

    /**
     * 生成随机数
     * @throws Exception
     */
    @Test
    public void testRand() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("transid",transid);
        params.put("length",32);
        String sign=SignUtil.genSignature(params,appSecret);

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
          Map<String,Object> params = new HashMap<>();
          params.put("appid",appid);
          params.put("transid",transid);
          params.put("P1",P1);
          String sign=SignUtil.genSignature(params,appSecret);

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
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("transid",transid);
        params.put("kid",kid);
        params.put("e",e);
        params.put("Q1",Q1);
        String sign=SignUtil.genSignature(params,appSecret);

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
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("transid",transid);
        params.put("kid",kid);
        params.put("T1",T1);
        String sign=SignUtil.genSignature(params,appSecret);

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


    @Test
    public void test() throws Exception {
        testIssue();
        testDelay();
        testReIssue();
        testRevoke();
    }
    /**
     * 签发证书
     * @throws Exception
     */
   // @Test
    public void testIssue() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("transid",transid);
        params.put("kid",kid);
        params.put("cn",cn);
        params.put("csr",csr);
        params.put("c","CN"); //如果设置了c值 则填c值 如果没设置，填写默认值"CN"
        params.put("certType","1");//如果设置了certType值 则填certType值 如果没设置，填写默认值"1"
        String sign=SignUtil.genSignature(params,appSecret);

        CertificateResponse response =
                cosignClient.issueCertBuilder()
                        //必填
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setKid(kid)
                        .setCn(cn)
                        .setCsr(csr) //证书签发请求
                        //选填
          /*              .setO(o) //证书 O 项,证书所属组织
                        .setOu(ou) //证书 OU 项,证书所属组织单元
                        .setC(c) //证书 C 项,国家,默认 CN（缺省值，默认为CN）
                        .setS(s) //证书 S 项,证书所属省
                        .setL(l) //证书 L 项,证书所属市
                        .setE(email) //证书 E 项,电子邮箱
                        .setCertType(certType) //签发双证(1) , 签发单证（0), 默认1*/
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println("签名证书:"+response.getSignatureCert());
        serial.append(parseCertificate(response.getSignatureCert()).getSerialNumber().toString(16));
        System.out.println("证书序列号:"+serial.toString());
        System.out.println("加密证书:"+response.getEncryptionCert());
        System.out.println("加密私钥:"+response.getEncryptionKey());

    }

    /**
     * 重签证书
     * @throws Exception
     */
  //  @Test
    public void testReIssue() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("transid",transid);
        params.put("kid",kid);
        params.put("serial",serial.toString());
        params.put("cn",cn+"2");
        params.put("csr",csr);
        params.put("c","CN"); //如果设置了c值 则填c值 如果没设置，填写默认值"CN"
        params.put("certType","1");//如果设置了certType值 则填certType值 如果没设置，填写默认值"1"
        String sign=SignUtil.genSignature(params,appSecret);

        CertificateResponse response =
                cosignClient.reIssueCertBuilder()
                        //必填
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setKid(kid)
                        .setSerial(serial.toString())
                        .setCn(cn+"2")
                        .setCsr(csr) //证书签发请求
                        //选填
                   /*     .setO(o+"2") //证书 O 项,证书所属组织
                        .setOu(ou+"2") //证书 OU 项,证书所属组织单元
                        .setC(c) //证书 C 项,国家,默认 CN（缺省值，默认为CN）
                        .setS(s+"2") //证书 S 项,证书所属省
                        .setL(l+"2") //证书 L 项,证书所属市
                        .setE(email) //证书 E 项,电子邮箱
                        .setCertType(certType) //签发双证(1) , 签发单证（0), 默认1*/
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println("签名证书:"+response.getSignatureCert());
        serial.replace(0,24,parseCertificate(response.getSignatureCert()).getSerialNumber().toString(16));
        System.out.println("证书序列号:"+serial.toString());
        System.out.println("加密证书:"+response.getEncryptionCert());
        System.out.println("加密私钥:"+response.getEncryptionKey());

    }


    /**
     * 延期证书
     * @throws Exception
     */
//    @Test
    public void testDelay() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("transid",transid);
        params.put("kid",kid);
        params.put("serial",serial.toString());
        params.put("duration","1y");
        String sign=SignUtil.genSignature(params,appSecret);

        CertificateResponse response =
                cosignClient.delayCertBuilder()
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setKid(kid)
                        .setSerial(serial.toString())
                        .setDuration("1y")
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println("签名证书:"+response.getSignatureCert());
        serial.replace(0,24,parseCertificate(response.getSignatureCert()).getSerialNumber().toString(16));
        System.out.println("证书序列号:"+serial.toString());
        System.out.println("加密证书:"+response.getEncryptionCert());
        System.out.println("加密私钥:"+response.getEncryptionKey());

    }

    /**
     * 注销证书
     * @throws Exception
     */
 //   @Test
    public void testRevoke() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("transid",transid);
        params.put("kid",kid);
        params.put("serial",serial.toString());
        String sign=SignUtil.genSignature(params,appSecret);

        RevokeResponse response =
                cosignClient.revokeCertBuilder()
                        .setAppid(appid)
                        .setTransid(transid)
                        .setSign(sign)
                        .setKid(kid)
                        .setSerial(serial.toString())
                        .build();
        //状态码
        System.out.println(response.getStatusCode());
        //结果
        System.out.println(response.getResult());

    }






    /**
     * 解析证书对象
     * @param certBase64String base64证书内容
     * @throws NetonejException 异常
     */
    public X509Certificate parseCertificate(String certBase64String) throws NetonejException {
        if (NetonejUtil.isEmpty(certBase64String)) {
            throw new NetonejException("证书内容不能为空");
        }
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(certBase64String));
            CertificateFactory factory = new CertificateFactory();
            X509Certificate certificate = (X509Certificate) factory.engineGenerateCertificate(in);
            return certificate;
        } catch (Exception e) {
            throw new NetonejException("证书解析失败");
        }
    }


}
