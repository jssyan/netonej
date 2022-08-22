package cn.com.syan.netonej;

import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.http.client.TSAClient;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class TSAClientTest {

    private TSAClient tsaClient = new TSAClient("http://demo.syan.com.cn","9198");


    @Test
    public void testGetTimestamp2() throws Exception {
        //原文
        String data = "nEsVI+uBQwPyQYwUHpKh+iz1hHWtwS+Z3a0hXv3+Ntw+Hyqo6zkcQjDNCP5Kw6XGvR6PQJAl3CAga5HdSqGSHQ==";

        //签署
        NetoneTSA netoneTSA = tsaClient.tsaCreateBuilder()
                .setAlgo(DigestAlgorithm.ECDSASM2WITHSM3)//可选。设置摘要算法
                .setData(Base64.decode(data))//设置签署原文
                .build();
        System.out.println(netoneTSA.getResult());

        //验证
        netoneTSA = tsaClient.tsaVerifyBuilder()
                .setData(Base64.decode(data))//设置签名原文
                .setBase64Timestamp(netoneTSA.getResult())//设置base64格式的时间戳
                .build();
        System.out.println("验证响应码:"+netoneTSA.getStatusCode());
    }

    /**
     * 根据时间戳请求签发时间戳
     * @throws Exception
     */
    @Test
    public void test() throws Exception{
        //签发
        byte[] digest = new NetoneDigest("SHA1").digest("123".getBytes());
        TimeStampRequestGenerator tsReqGen = new TimeStampRequestGenerator();
        tsReqGen.setCertReq(false);
        TimeStampRequest tsReq = tsReqGen.generate(CMSAlgorithm.SHA1, digest);

        NetoneTSA netoneTSA = tsaClient.tsaCreateBuilder()
                .setData(tsReq.getEncoded())
                .setDataType(DataType.TIMESTAMP_REQUEST) //数据类型设置为时间戳请求类型
                .build();


        System.out.println(netoneTSA.getResult());

        //验证
        netoneTSA = tsaClient.tsaVerifyBuilder()
                .setData(digest)
                .setDataType(DataType.DIGEST)
                .setBase64Timestamp(netoneTSA.getResult())
                .build();
        System.out.println(netoneTSA.getStatusCode());
    }

}
