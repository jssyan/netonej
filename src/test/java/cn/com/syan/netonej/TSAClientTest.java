package cn.com.syan.netonej;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.client.TSAClient;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.junit.Test;

import java.util.Base64;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class TSAClientTest {


    TSAClient client = new TSAClient("202.100.108.30","9198");


    /**
     * 根据原文数据 签发时间戳
     * @throws NetonejExcepption
     */

    @Test
    public void testGetTimestamp() throws NetonejExcepption {
        String data = Base64.getEncoder().encodeToString("123456".getBytes());
        NetoneTSA netoneTSA = client.createTimestamp(data,DataType.PLAIN,DigestAlgorithm.SHA1);
        System.out.println(netoneTSA.getTimestampbase64());

        netoneTSA = client.verifyTimestamp(netoneTSA.getTimestampbase64(),data,DataType.PLAIN);
        System.out.println(netoneTSA.getStatusCode());
    }

    /**
     * 根据时间戳请求签发时间戳
     * @throws Exception
     */
    @Test
    public void test() throws Exception{
        byte[] digest = NetonejUtil.digestBinary("123".getBytes());
        TimeStampRequestGenerator tsReqGen = new TimeStampRequestGenerator();
        tsReqGen.setCertReq(true);
        TimeStampRequest tsReq = tsReqGen.generate(CMSAlgorithm.SHA1, digest);

        NetoneTSA netoneTSA = client.createTimestamp(tsReq.getEncoded());

        System.out.println(netoneTSA.getTimestampbase64());

        netoneTSA = client.verifyTimestamp(netoneTSA.getTimestampbase64());
        System.out.println(netoneTSA.getStatusCode());
    }


}
