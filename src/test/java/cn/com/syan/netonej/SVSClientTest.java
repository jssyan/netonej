package cn.com.syan.netonej;

import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.client.SVSClient;
import com.syan.netonej.http.entity.*;
import org.junit.Test;

import java.util.Base64;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class SVSClientTest {

    SVSClient client = new SVSClient("192.168.10.215");


    String kid = "afaf4cdb49964c24e172112f2a4b98c9";

    String cn = "J-N-S-P-E";


    //kid对应的证书
    String cert = "MIICNDCCAdugAwIBAgINAPjxdl3mFMf3ySNchjAKBggqgRzPVQGDdTBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRTAeFw0yMDAxMjAxNjAwMDBaFw0zMDAxMTcxNjAwMDBaMG0xCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJKUzELMAkGA1UEBwwCTkoxDTALBgNVBAoMBFN5YW4xEDAOBgNVBAsMB1Byb2R1Y2UxDzANBgNVBAwMBmVtcGxlZTESMBAGA1UEAwwJSi1OLVMtUC1FMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAETGxel2ar0ttp5IYu9asjRna+hgK8oqUDf7A6E/DSiYZSzGO35IKsNfUd3GVSxsmQeQr9vZyliEwbP9O7+BfrraNgMF4wDAYDVR0TBAUwAwEB/zAdBgNVHQ4EFgQUuFLOe2CMn8t6tYvmOVG6CtXOHGowHwYDVR0jBBgwFoAUuFLOe2CMn8t6tYvmOVG6CtXOHGowDgYDVR0PAQH/BAQDAgGGMAoGCCqBHM9VAYN1A0cAMEQCIBALIzaSMYahxAtkR0x/kd9z9OQ5R7RlCAgjVwiAXo1fAiAZu7rGRfVzozGvm32VcKD7bCp0DZ2bmtwwKuz5FA1tmQ==";

    /**
     * 验证证书的有效性
     * @throws NetonejExcepption
     */
    @Test
    public void testVerifyCertificate() throws NetonejExcepption {
        NetoneSVS svs = client.verifyCertificate(cert);
        System.out.println(svs.getStatusCode());
    }

    /**
     * 枚举服务端证书
     * @throws NetonejExcepption
     */
    @Test
    public void listCertificates() throws NetonejExcepption {
        NetoneCertList list = client.listCertificates();
        System.out.println(list.getCertList().size());
    }


    /**
     * 验证p1签名
     * @throws NetonejExcepption
     */
    @Test
    public void verifyPKCS1() throws NetonejExcepption {
        String data = Base64.getEncoder().encodeToString("123456".getBytes());
        String p1 = "MEUCIQDWh1CKmCnGRlkkdzjqigWakTjhOdp53RKVYKCnzB3OWgIgSH33VLFdhIO/etvDcqRz68Q23nUgbFxV7Y9/0+tJrrk=";

        NetoneSVS svs;

        //使用证书验证
        svs = client.verifyPKCS1(data,p1,DigestAlgorithm.ECDSASM2WITHSM3,DataType.PLAIN,cert);

        //使用证书的CN项验证或者KID
        svs = client.verifyPKCS1(cn,IdMagic.SCN,data,p1,DigestAlgorithm.ECDSASM2WITHSM3,DataType.PLAIN);

        System.out.println(svs.getStatusCode());
    }

    
    @Test
    public void verifyPKCS7() throws NetonejExcepption {
        String data = Base64.getEncoder().encodeToString("123456".getBytes());

        String p7Attach = "MIIDawYKKoEcz1UGAQQCAqCCA1swggNXAgEBMQ4wDAYIKoEcz1UBgxEFADAWBgoqgRzPVQYBBAIBoAgEBjEyMzQ1NqCCAjgwggI0MIIB26ADAgECAg0A+PF2XeYUx/fJI1yGMAoGCCqBHM9VAYN1MG0xCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJKUzELMAkGA1UEBwwCTkoxDTALBgNVBAoMBFN5YW4xEDAOBgNVBAsMB1Byb2R1Y2UxDzANBgNVBAwMBmVtcGxlZTESMBAGA1UEAwwJSi1OLVMtUC1FMB4XDTIwMDEyMDE2MDAwMFoXDTMwMDExNzE2MDAwMFowbTELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkpTMQswCQYDVQQHDAJOSjENMAsGA1UECgwEU3lhbjEQMA4GA1UECwwHUHJvZHVjZTEPMA0GA1UEDAwGZW1wbGVlMRIwEAYDVQQDDAlKLU4tUy1QLUUwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAARMbF6XZqvS22nkhi71qyNGdr6GAryipQN/sDoT8NKJhlLMY7fkgqw19R3cZVLGyZB5Cv29nKWITBs/07v4F+uto2AwXjAMBgNVHRMEBTADAQH/MB0GA1UdDgQWBBS4Us57YIyfy3q1i+Y5UboK1c4cajAfBgNVHSMEGDAWgBS4Us57YIyfy3q1i+Y5UboK1c4cajAOBgNVHQ8BAf8EBAMCAYYwCgYIKoEcz1UBg3UDRwAwRAIgEAsjNpIxhqHEC2RHTH+R33P05DlHtGUICCNXCIBejV8CIBm7usZF9XOjMa+bfZVwoPtsKnQNnZua3DAq7PkUDW2ZMYHtMIHqAgEBMH4wbTELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkpTMQswCQYDVQQHDAJOSjENMAsGA1UECgwEU3lhbjEQMA4GA1UECwwHUHJvZHVjZTEPMA0GA1UEDAwGZW1wbGVlMRIwEAYDVQQDDAlKLU4tUy1QLUUCDQD48XZd5hTH98kjXIYwDAYIKoEcz1UBgxEFADANBgkqgRzPVQGCLQEFAARIMEYCIQCUa79iv4OZ4OgrtlMnq2AZbI5MQoVtCBXtoVWr3yomXQIhAI5Da66VKiNK9FpnlF7fTrdpm/AXIDzTcw5lt5l8Ao+i";

        String p7Dettach = "MIIDYQYKKoEcz1UGAQQCAqCCA1EwggNNAgEBMQ4wDAYIKoEcz1UBgxEFADAMBgoqgRzPVQYBBAIBoIICODCCAjQwggHboAMCAQICDQD48XZd5hTH98kjXIYwCgYIKoEcz1UBg3UwbTELMAkGA1UEBhMCQ04xCzAJBgNVBAgMAkpTMQswCQYDVQQHDAJOSjENMAsGA1UECgwEU3lhbjEQMA4GA1UECwwHUHJvZHVjZTEPMA0GA1UEDAwGZW1wbGVlMRIwEAYDVQQDDAlKLU4tUy1QLUUwHhcNMjAwMTIwMTYwMDAwWhcNMzAwMTE3MTYwMDAwWjBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRTBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABExsXpdmq9LbaeSGLvWrI0Z2voYCvKKlA3+wOhPw0omGUsxjt+SCrDX1HdxlUsbJkHkK/b2cpYhMGz/Tu/gX662jYDBeMAwGA1UdEwQFMAMBAf8wHQYDVR0OBBYEFLhSzntgjJ/LerWL5jlRugrVzhxqMB8GA1UdIwQYMBaAFLhSzntgjJ/LerWL5jlRugrVzhxqMA4GA1UdDwEB/wQEAwIBhjAKBggqgRzPVQGDdQNHADBEAiAQCyM2kjGGocQLZEdMf5Hfc/TkOUe0ZQgII1cIgF6NXwIgGbu6xkX1c6Mxr5t9lXCg+2wqdA2dm5rcMCrs+RQNbZkxge0wgeoCAQEwfjBtMQswCQYDVQQGEwJDTjELMAkGA1UECAwCSlMxCzAJBgNVBAcMAk5KMQ0wCwYDVQQKDARTeWFuMRAwDgYDVQQLDAdQcm9kdWNlMQ8wDQYDVQQMDAZlbXBsZWUxEjAQBgNVBAMMCUotTi1TLVAtRQINAPjxdl3mFMf3ySNchjAMBggqgRzPVQGDEQUAMA0GCSqBHM9VAYItAQUABEgwRgIhAMLlJ1ZJVifKPCAD72lbnXANAG+5SKWsNJEN7CnnJVR9AiEAuvvmVdUT5+IMtR1Ph823s5Fdg+9IzQ15Z8ce9ZzmTGo=";

        NetoneSVS svs;

        svs = client.verifyPKCS7(p7Attach);

        //状态码 200为成功，其他为失败
        System.out.println(svs.getStatusCode());
        //base64编码的签名原文
        System.out.println(svs.getOrginalBase64());
        //签名证书
        NetoneCertificate netoneCertificate = svs.getCertificate();
        System.out.println(netoneCertificate.getSubject());

        svs = client.verifyPKCS7(p7Dettach,data);
        System.out.println(svs.getStatusCode());
        System.out.println(svs.getOrginalBase64());
        netoneCertificate = svs.getCertificate();
        System.out.println(netoneCertificate.getSubject());
    }


}
