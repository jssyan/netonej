package cn.com.syan.netonej;

import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.http.client.TSAClient;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class TSAClientTest {
    private TSAClient tsaClient = new TSAClient("http://demo.syan.com.cn","9198");


    /**
     * 根据原文内容签发时间戳
     * @throws Exception
     */
    @Test
    public void testGetTimestamp() throws Exception {
        //Base64编码的原文
        String data = "nEsVI+uBQwPyQYwUHpKh+iz1hHWtwS+Z3a0hXv3+Ntw+Hyqo6zkcQjDNCP5Kw6XGvR6PQJAl3CAga5HdSqGSHQ==";

        //签署时间戳
        NetoneTSA netoneTSA = tsaClient.tsaCreateBuilder()
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)//可选。设置摘要算法
                .setData(Base64.decode(data))//设置签署原文
                .build();
        System.out.println(netoneTSA.getStatusCode());
        //时间戳签名
        System.out.println(netoneTSA.getResult());
        //签名算法
        System.out.println(netoneTSA.getAlgo());
        //签名证书的DN
        System.out.println(netoneTSA.getSubject());
        //原文摘要值
        System.out.println(netoneTSA.getImprint());
        //签名时间
        System.out.println(netoneTSA.getTimestamp());

        //验证时间戳
        NetoneTSA netoneTSAVerify = tsaClient.tsaVerifyBuilder()
                .setData(Base64.decode(data))//设置签名原文
                .setBase64Timestamp(netoneTSA.getResult())//设置base64格式的时间戳
                .build();
        System.out.println("验证响应码:"+netoneTSAVerify.getStatusCode());

        netoneTSAVerify = tsaClient.verifyTimestamp(netoneTSA.getResult(),data);
        System.out.println(netoneTSAVerify.getStatusCode());
        //时间戳
        System.out.println(netoneTSAVerify.getResult());
        //签名算法
        System.out.println(netoneTSAVerify.getAlgo());
        //签名证书的DN
        System.out.println(netoneTSAVerify.getSubject());
        //原文摘要值
        System.out.println(netoneTSAVerify.getImprint());
        //签名时间
        System.out.println(netoneTSAVerify.getTimestamp());
    }

    /**
     * 构造时间戳请求签发时间戳
     * @throws Exception
     */
    @Test
    public void test() throws Exception{
        //原文摘要计算
        byte[] digest = new NetoneDigest("SM3").digest("123".getBytes());
        //构造时间戳请求
        TimeStampRequestGenerator tsReqGen = new TimeStampRequestGenerator();
        tsReqGen.setCertReq(false);
        TimeStampRequest tsReq = tsReqGen.generate(GMObjectIdentifiers.sm3, digest);

        FileUtil.save(tsReq.getEncoded(),"/Users/momocat/Documents/temp","tsa_req_sm3");
        //申请时间戳
        NetoneTSA netoneTSA = tsaClient.tsaCreateBuilder()
                .setData(tsReq.getEncoded())
                .setDataType(DataType.TIMESTAMP_REQUEST) //数据类型设置为时间戳请求类型
                .build();


        System.out.println("签发成功："+tsaClient.getHost()+":"+tsaClient.getPort());
        System.out.println("签发成功："+netoneTSA.getResult());

//        //验证
//        NetoneTSA netoneTSAVerify = tsaClient.tsaVerifyBuilder()
//                .setData(digest)
//                .setDataType(DataType.DIGEST)
//                .setBase64Timestamp(netoneTSA.getResult())
//                .build();
//        System.out.println(netoneTSAVerify.getStatusCode());
//
//        netoneTSAVerify = tsaClient.verifyTimestamp(netoneTSA.getResult(),Base64.toBase64String("123".getBytes()),DataType.PLAIN);
//        System.out.println(netoneTSAVerify.getStatusCode());
    }

    /**
     * 签发时间戳的其他写法（主要兼容3.0.18以下版本的写法）
     * @throws Exception
     */
    @Test
    public void testOther() throws Exception{

        //Base64编码的原文
        String data = "nEsVI+uBQwPyQYwUHpKh+iz1hHWtwS+Z3a0hXv3+Ntw+Hyqo6zkcQjDNCP5Kw6XGvR6PQJAl3CAga5HdSqGSHQ==";

        //根据原文与指定的摘要算法申请时间戳:
        NetoneTSA netoneTSA = tsaClient.createTimestamp(data,"sm3");
        System.out.println(netoneTSA.getResult());

        //验证
        NetoneTSA netoneTSAVerify = tsaClient.verifyTimestamp(netoneTSA.getResult());
        System.out.println(netoneTSAVerify.getStatusCode());

        netoneTSAVerify = tsaClient.verifyTimestamp(netoneTSA.getResult(),data);
        System.out.println(netoneTSAVerify.getStatusCode());


        //根据原文、指定的摘要算法申请时间戳:
        netoneTSA = tsaClient.createTimestamp(Base64.decode(data),"sm3",0);
        System.out.println(netoneTSA.getResult());

        //根据原文的摘要值、指定的摘要算法申请时间戳:
        //1.摘要计算
        byte[] digest = new NetoneDigest("sm3").digest("123".getBytes());
        netoneTSA = tsaClient.createTimestamp(digest,"sm3",1);
        System.out.println(netoneTSA.getResult());

        //根据原文与指定的摘要算法申请时间戳(枚举类型的参数设置示例):
        netoneTSA = tsaClient.createTimestamp(data,DataType.PLAIN,DigestAlgorithm.SHA1);
        System.out.println(netoneTSA.getResult());

        //根据原文的摘要值、指定的摘要算法申请时间戳(枚举类型的参数设置示例):
        //1.摘要计算
        digest = new NetoneDigest("sm3").digest("123".getBytes());
        netoneTSA = tsaClient.createTimestamp(Hex.toHexString(digest),DataType.DIGEST,DigestAlgorithm.SM3);
        System.out.println(netoneTSA.getResult());

        //验证
        netoneTSAVerify = tsaClient.verifyTimestamp(netoneTSA.getResult(),Hex.toHexString(digest),DataType.DIGEST);
        System.out.println(netoneTSAVerify.getStatusCode());

    }
}
