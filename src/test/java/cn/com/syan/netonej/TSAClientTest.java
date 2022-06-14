package cn.com.syan.netonej;

import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.SVSClient;
import com.syan.netonej.http.client.TSAClient;
import com.syan.netonej.http.client.tsa.TSACreateBuilder;
import com.syan.netonej.http.client.tsa.TSAVerifyBuilder;
import com.syan.netonej.http.entity.NetoneTSA;
import com.syan.netonej.http.okhttp.NetoneJHttpClient;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.junit.Test;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class TSAClientTest {

    private TSAClient tsaClient = new TSAClient("192.168.20.223","9198");
    /**
     * 根据原文数据 签发时间戳
     * @throws NetonejException
     */

    @Test
    public void testGetTimestamp() throws NetonejException {
        String data = "123456";
        //签署
        NetoneTSA netoneTSA = tsaClient.tsaCreateBuilder()
                .setAlgo(DigestAlgorithm.SHA1)//可选。设置签名摘要算法
                .setData(data.getBytes())//设置签署原文
                .build();
        System.out.println(netoneTSA.getResult());
        //验证
        netoneTSA = tsaClient.tsaVerifyBuilder()
                .setData(data.getBytes())//设置签名原文
                .setBase64Timestamp(netoneTSA.getResult())//设置base64格式的时间戳
                .build();
        System.out.println(netoneTSA.getStatusCode());
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
