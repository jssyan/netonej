package cn.com.syan.netonej;

import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.SVSClient;
import com.syan.netonej.http.entity.*;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class SVSClientTest {


    private SVSClient svsClient = new SVSClient("192.168.20.223","9188");

    String kid = "4517532d2ce6ce9a94c131b685c40c72";

    String cn = "pcs_sm2";

    //kid对应的证书
    String cert = "MIICNDCCAdugAwIBAgINAPjxdl3mFMf3ySNchjAKBggqgRzPVQGDdTBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRTAeFw0yMDAxMjAxNjAwMDBaFw0zMDAxMTcxNjAwMDBaMG0xCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJKUzELMAkGA1UEBwwCTkoxDTALBgNVBAoMBFN5YW4xEDAOBgNVBAsMB1Byb2R1Y2UxDzANBgNVBAwMBmVtcGxlZTESMBAGA1UEAwwJSi1OLVMtUC1FMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAETGxel2ar0ttp5IYu9asjRna+hgK8oqUDf7A6E/DSiYZSzGO35IKsNfUd3GVSxsmQeQr9vZyliEwbP9O7+BfrraNgMF4wDAYDVR0TBAUwAwEB/zAdBgNVHQ4EFgQUuFLOe2CMn8t6tYvmOVG6CtXOHGowHwYDVR0jBBgwFoAUuFLOe2CMn8t6tYvmOVG6CtXOHGowDgYDVR0PAQH/BAQDAgGGMAoGCCqBHM9VAYN1A0cAMEQCIBALIzaSMYahxAtkR0x/kd9z9OQ5R7RlCAgjVwiAXo1fAiAZu7rGRfVzozGvm32VcKD7bCp0DZ2bmtwwKuz5FA1tmQ==";

    /**
     * 验证证书的有效性
     * @throws NetonejException
     */
    @Test
    public void testVerifyCertificate() throws NetonejException {
        NetoneResponse svs = svsClient.certificateVerifyBuilder()
                .setCert(cert)//设置证书
                //.setId(cn).setIdMagic(IdMagic.SCN)//可选，使用IdMagic形式
                .build();
        System.out.println(svs.getStatusCode());
        System.out.println(svs.getStatusCodeMessage());
    }

    /**
     * 枚举服务端证书
     * @throws NetonejException
     */
    @Test
    public void listCertificates() throws NetonejException {
        NetoneCertList list = svsClient
                .certificateListBuilder()
                .setResponseformat(ResponseFormat.XML)
                .build();
        System.out.println(list.getCertList());
    }


    /**
     * 验证p1签名
     * @throws NetonejException
     */
    @Test
    public void verifyPKCS1() throws NetonejException {
        String data = "123";
        String pkcs1 = "MEUCIBx0ZTlQLS3tzfVxJARTA773pGQDq+pvLWNcwLUJqKwnAiEAtVdWtxfuGsFXvEPeNexcjkJy6b97cO+k/trx/4SrkdI=";

        NetoneSVS svs = svsClient.pkcs1VerifyBuilder()
                //.setCert("MIICQDCCAeOgAwIBAgIGANAZOXKeMAwGCCqBHM9VAYN1BQAwQzELMAkGA1UEBhMCY24xEDAOBgNVBAoMB2luZm9zZWMxDzANBgNVBAsMBnN5c3RlbTERMA8GA1UEAwwIY2E2Ml9zbTIwHhcNMjExMDI3MDEzNzA3WhcNMjIxMDI3MDEzNzA3WjBBMQswCQYDVQQGEwJDTjEPMA0GA1UECgwGSFhCQU5LMQ8wDQYDVQQLDAZIWEJBTksxEDAOBgNVBAMMB0hYQlRlc3QwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAAQWlm3OamvHpodq4cRRusRpwYXHIIrMXzqvyu+7HbjrSh9LNsyPkNEkAVLGxC6GELD/rd2S5BnYxbwzPEDObifLo4HCMIG/MB8GA1UdIwQYMBaAFNB8xx7P/5V38v9ciERtpJNOUA+aMAkGA1UdEwQCMAAwYgYDVR0fBFswWTBXoFWgU6RRME8xDzANBgNVBAMMBmNybDY5NzEMMAoGA1UECwwDY3JsMQ8wDQYDVQQLDAZzeXN0ZW0xEDAOBgNVBAoMB2luZm9zZWMxCzAJBgNVBAYTAmNuMA4GA1UdDwEB/wQEAwIGwDAdBgNVHQ4EFgQUH0olTrKCneLVDzY9SApQQe8W21IwDAYIKoEcz1UBg3UFAANJADBGAiEA/kNOo7Wy0vw2OMqU7kYjr4PDD5dp2FwCKaJaSxv16kUCIQDkiN8NGYzUpu/k/bHzmuZg/DA8EU/ctdYKlmHR3InXvQ==") //使用证书验证
                .setId("BD12B96E16A26FE3F0CE69F9").setIdmagic(IdMagic.SNHEX)//可选，使用id形式
                .setBase64Signature(pkcs1)//base64格式的签名
                .setData(data)//原文
                .setAlgo(DigestAlgorithm.ECDSASM2)//可选，签名摘要算法
                .build();
        System.out.println(svs.getStatusCode());
        System.out.println(svs.getCertificate().getSubject());
    }

    /**
     * 单元测试：PKCS7签名结果验证
     * @throws NetonejException
     */
    @Test
    public void verifyPKCS7() throws Exception {
        String data = Base64.toBase64String("Hello".getBytes());

        //替换为您的签名值
        String p7Attach = "MIIDeAYKKoEcz1UGAQQCAqCCA2gwggNkAgEBMQ4wDAYIKoEcz1UBgxEFADAUBgoqgRzPVQYBBAIBoAYEBHRlc3SgggGoMIIBpDCCAUmgAwIBAgINAJ/XuXXCakPej2+2gDAKBggqgRzPVQGDdTAkMQswCQYDVQQGEwJDTjEVMBMGA1UEAwwMcGNzXzIwMjIwNzEzMB4XDTIyMDcxMjE2MDAwMFoXDTMyMDcxMjE2MDAwMFowJDELMAkGA1UEBhMCQ04xFTATBgNVBAMMDHBjc18yMDIyMDcxMzBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABBEu3/ZZ41WykasJBlQtkyChCzow8TAJDvDcRLhWeLbuYe2VdWh5HWYvz37D42Y5JT2V1/TMpmefX0NsF1ysNsujYDBeMAwGA1UdEwQFMAMBAf8wHQYDVR0OBBYEFIn45CNWVhsoscjcpm6OcOVTWDiwMB8GA1UdIwQYMBaAFIn45CNWVhsoscjcpm6OcOVTWDiwMA4GA1UdDwEB/wQEAwIBhjAKBggqgRzPVQGDdQNJADBGAiEA5hv9ND8IQZeuvMnmoFzEYITs+riqaba8xSruvy2UijsCIQDkF8j6q+dxzLPafQ/JqEuxb4Jlrd5VRAkHMbDEemxYmTGCAYswggGHAgEBMDUwJDELMAkGA1UEBhMCQ04xFTATBgNVBAMMDHBjc18yMDIyMDcxMwINAJ/XuXXCakPej2+2gDAMBggqgRzPVQGDEQUAoIHkMBgGCSqGSIb3DQEJAzELBgkqhkiG9w0BBwEwHAYJKoZIhvcNAQkFMQ8XDTIyMDcxMzA3NDA1NlowLwYJKoZIhvcNAQkEMSIEIFXhLpFlDS/sVux04dPk3b/OLvOmWJDCoZ7PiKMH52ojMHkGCSqGSIb3DQEJDzFsMGowCwYJYIZIAWUDBAEqMAsGCWCGSAFlAwQBFjALBglghkgBZQMEAQIwCgYIKoZIhvcNAwcwDgYIKoZIhvcNAwICAgCAMA0GCCqGSIb3DQMCAgFAMAcGBSsOAwIHMA0GCCqGSIb3DQMCAgEoMA0GCSqBHM9VAYItAQUABEcwRQIhAO1MQp0JII5ykxT/myV+fFQ+2Boce0Ti0tKJgubzywzwAiAnH87eArTqpQqtlwb2hLHComLM30qSYABDKgF9tcwupw==";
        String p7Dettach = "MIIESQYKKoEcz1UGAQQCAqCCBDkwggQ1AgEBMQ4wDAYIKoEcz1UBgxEFADAMBgoqgRzPVQYBBAIBoIICODCCAjQwggHboAMCAQICDQD48XZd5hTH98kjXIYwCgYIKoEcz1UBg3UwbTELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkpTMQswCQYDVQQHDAJOSjENMAsGA1UECgwEU3lhbjEQMA4GA1UECwwHUHJvZHVjZTEPMA0GA1UEDAwGZW1wbGVlMRIwEAYDVQQDDAlKLU4tUy1QLUUwHhcNMjAwMTIwMTYwMDAwWhcNMzAwMTE3MTYwMDAwWjBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRTBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABExsXpdmq9LbaeSGLvWrI0Z2voYCvKKlA3+wOhPw0omGUsxjt+SCrDX1HdxlUsbJkHkK/b2cpYhMGz/Tu/gX662jYDBeMAwGA1UdEwQFMAMBAf8wHQYDVR0OBBYEFLhSzntgjJ/LerWL5jlRugrVzhxqMB8GA1UdIwQYMBaAFLhSzntgjJ/LerWL5jlRugrVzhxqMA4GA1UdDwEB/wQEAwIBhjAKBggqgRzPVQGDdQNHADBEAiAQCyM2kjGGocQLZEdMf5Hfc/TkOUe0ZQgII1cIgF6NXwIgGbu6xkX1c6Mxr5t9lXCg+2wqdA2dm5rcMCrs+RQNbZkxggHUMIIB0AIBATB+MG0xCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJKUzELMAkGA1UEBwwCTkoxDTALBgNVBAoMBFN5YW4xEDAOBgNVBAsMB1Byb2R1Y2UxDzANBgNVBAwMBmVtcGxlZTESMBAGA1UEAwwJSi1OLVMtUC1FAg0A+PF2XeYUx/fJI1yGMAwGCCqBHM9VAYMRBQCggeQwGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMjIwNDI1MDYwNDI4WjAvBgkqhkiG9w0BCQQxIgQgIHz0EFMvkqR97iRc6bEf9x9Xjr12PrO76kTr0EPQGPsweQYJKoZIhvcNAQkPMWwwajALBglghkgBZQMEASowCwYJYIZIAWUDBAEWMAsGCWCGSAFlAwQBAjAKBggqhkiG9w0DBzAOBggqhkiG9w0DAgICAIAwDQYIKoZIhvcNAwICAUAwBwYFKw4DAgcwDQYIKoZIhvcNAwICASgwDQYJKoEcz1UBgi0BBQAERzBFAiEAx135YUBqGub/8KsmlKojOMTYD8GmPEu8fgam4xGVDo8CIFiDfiuBQaaqIph7SXi2ikfHQhOIViCOu3QebDaonG+b";

        //验证一：签名带原文的情况验证，业务处理应对返回的NetoneSVS对象做状态码、NULL值等失败情况的判断
//
        NetoneSVS svs = svsClient.pkcs7VerifyBuilder()
                .setBase64Pkcs7(p7Attach)
                .setGreenpass("0")
                .build();
        if(svs.getStatusCode() == 200){
            System.out.println(new String(Base64.decode(svs.getResult())));
            System.out.println(svs.getCertificate().getSubject());
        }else{
            System.out.println(svs.getStatusCodeMessage());
        }



//         svs = svsClient.pkcs7VerifyBuilder()
//                .setBase64Pkcs7(p7Dettach)//P7签名
//                .setBase64Data(data)//可选，原文，Attach模式下不需要设置
//                .build();
//        System.out.println(svs.getResult());
//        System.out.println(svs.getCertificate().getSubject());

    }

    @Test
    public void verifyXMLSign() throws NetonejException {
        String p1 = "PD94bWwgdmVyc2lvbj0iMS4wIj8+CjxTaWduYXR1cmUgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPgo8U2lnbmVkSW5mbz4KPENhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz4KPFNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZHNpZy1tb3JlI3JzYS1zaGEyNTYiLz4KPFJlZmVyZW5jZSBVUkk9IiNkYXRhIj4KPERpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNzaGExIi8+CjxEaWdlc3RWYWx1ZT40THl0MjdnaVJGQ1JoWTFtZitJVXRodXZnWDQ9PC9EaWdlc3RWYWx1ZT4KPC9SZWZlcmVuY2U+CjwvU2lnbmVkSW5mbz4KPFNpZ25hdHVyZVZhbHVlPmkzK0szcFNRdkUzR3FCai84c3QvdFRiaC9sRVpua3Q5ZlFvV296Y2dOSGZOUU5xbUtRYlBFUG95NEFTZWp4MkEKVWplc0FtallZTjhGOVVTbUpPdU5mUmxTZ1dqVll3YzY4ZXlVT2xKUGh6WnNFYXZoRjM0Nm1VY2ppU0o2SnZ4bgpGc2dRemJVZmU1YldVYkhLQW1WSWNEWEtQelRyV21UR2N4a29FWkNEQjFmS1VERXU1Y3NUYW1qaDA1T21SRWZ3CjdtQWZuUFFFNW9NL1dGM1p6QWpXR0tBM2JwcGo0cXpEeWtNRnZjOFZLM094T1R0NWxsa2duTXVaWnFVS0hmbkEKZHdLbSt4c1cxZDd0QW9BRVIxS1hRWUdUVnRxSys4dldGZi95aW5NdUJCOXo0WWtZcXdUNXAyWnZnME1tYkUycwp1N1dKV3lEM0t5cVUweWJtMWpEZjlBPT08L1NpZ25hdHVyZVZhbHVlPgo8S2V5SW5mbz4KPFg1MDlEYXRhPgo8WDUwOUNlcnRpZmljYXRlPk1JSUVLakNDQXhLZ0F3SUJBZ0lOQVBmbXJDWXd5RDk1SnZIemZEQU5CZ2txaGtpRzl3MEJBUXNGQURBeE1Rc3cKQ1FZRFZRUUdFd0pEVGpFUk1BOEdBMVVFQ2d3SVFVSkRJR3gwWkM0eER6QU5CZ05WQkFNTUJtTmhMV2RsYmpBZQpGdzB5TWpBME1UVXdOakkyTlRKYUZ3MDBNREEwTVRReE5qQXdNREJhTUI0eEN6QUpCZ05WQkFZVEFrTk9NUTh3CkRRWURWUVFEREFicHU0VGxnYVV3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQ3cKWjJQb0NPajA2ZERPMWFSTDNsWDMxTFFNbXZsNkh2YXdQZ3VVbnlJTjlLMW1Nb2Q5MVdSamRuOXdtSmhoWE95UwoyVUpweW52NXE4WENxTDY4ZFowSkw0MjEvQWViYmpteGppRUNzL2xlZG82eDRhbmgyNEMxK2dYYnkwUVZNTk9ECjZBR1c0bDh3QTBiOEp5NzAwbU8xWEE0aXFtKzM5UGk0amtxNmhOazRSQVNFVEkzNlNEQzFJcHd1NHV4S08vM1gKZEJtQmhzaDdNc1ZEbEN6ejJxWHZVQWRVQ1JDa2lNTzBORkJpZDBFSHhMa1RQZ2hwcnJwTDZrZjZVSndPYmRXUwo0NThaVktGaGJiUzVxcENLYU0rWnV2U2pEdk9lSXJMVk1UZ3dHN2ZwOUYvVEpGK1F6YXdsNkYzNklMaEkwZzhJCnhpcFVyNGNFbEVKNng4WE9hRTRSQWdNQkFBR2pnZ0ZTTUlJQlRqQU1CZ05WSFJNRUJUQURBUUgvTUIwR0ExVWQKRGdRV0JCVFdBTUQ2YnBjUnArMks5RDNhMFBxSlZ5cENxREFQQmdOVkhROEJBZjhFQlFNREIvK0FNQllHQTFVZApKUUVCL3dRTU1Bb0dDQ3NHQVFVRkJ3TUlNQzRHQTFVZEh3UW5NQ1V3STZBaG9CK0dIV2gwZEhCek9pOHZZV2xoCkxuTjVZVzR1WTI5dExtTnVMMk55YkM5aE1HSUdDQ3NHQVFVRkJ3RUJCRll3VkRBa0JnZ3JCZ0VGQlFjd0FZWVkKYUhSMGNITTZMeTl2WTNOd0xuTjVZVzR1WTI5dExtTnVNQ3dHQ0NzR0FRVUZCekFDaGlCb2RIUndjem92TDJGcApZUzV6ZVdGdUxtTnZiUzVqYmk5cGMzTjFaWEl2WVRBZkJnTlZIU01FR0RBV2dCUlJSYkUwU2xmTkkycWcyTzlrClk1OFBLSS9GTnpCQkJnTlZIU0FFT2pBNE1EWUdDQ3FCSElid0FHUUJNQ293S0FZSUt3WUJCUVVIQWdFV0hHaDAKZEhCek9pOHZZM0J6TG5ONVlXNHVZMjl0TG1OdUwyTndjekV3RFFZSktvWklodmNOQVFFTEJRQURnZ0VCQUNaegowWkc1bmIrNCt5b0N6OEhYZUljdEIxejUzS2RhWTkzekJZN1lBNEcxanpJNDcxMjhFS0lkOHdOM2hnMWVCa3BqCjJyYkZwM001M0JmQWwxYk1vMEFuTTIyaXZlc0NGMEdtcGxERXJCUkg5V1F6UjduSVZhWlBrcmNlRVZQbEJFN1IKME1MRnMwVXRYNzAwb3AxT1phdDA1T0ZOK2dWMk9GZi9CenRaOGZ2UCt4TFdZRWVQS2ZHNDc0bTRpUnoyeFk2cwo4QVNGMG5FZllRYmljNk41Vm5Yek1mM202U0xjNW1XbUNwU1psVW03U2JYOG91Tnd1Q0p6dWE5bGdMYkpnRVZCCnhVaFJoaEFiVlRqL0ExdXlNN0I4STd1T21vQWtRdURQckFKN04wd0xJb0pocFZHL1hWVS9nbmlXL1MydGV6QlYKY1Fuc1FkMlBTbytMcFVYQTVBUT08L1g1MDlDZXJ0aWZpY2F0ZT4KPC9YNTA5RGF0YT4KPC9LZXlJbmZvPgo8T2JqZWN0IElkPSJkYXRhIj48ZGF0YT4xMjM0NTY8L2RhdGE+PC9PYmplY3Q+CjwvU2lnbmF0dXJlPgo=";

        NetoneSVS svs;
        svs = svsClient.xmlSignVerifyBuilder()
                .setBase64Data(p1)
                .build();

        System.out.println(svs.getStatusCode());
        System.out.println(svs.getCertificate().getSubject());
    }


}
