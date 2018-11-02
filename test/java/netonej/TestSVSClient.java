package netonej;

import com.syan.netonej.http.client.SVSClient;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneCertificate;
import com.syan.netonej.http.entity.NetoneSVS;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.spongycastle.util.encoders.Base64;

import java.io.FileInputStream;
import java.util.List;

/**
 * <p>
 * This class provides...
 * </p>
 *
 * @author Iceberg
 * @version $Revision 2018/5/21
 * @since 1.0
 */
public class TestSVSClient {

    SVSClient svsClient = null;

    private String data;

    private String b64Certificate;

    private String p7;

    private String p1;

    @Before
    public void init() {

        svsClient = new SVSClient("192.168.1.178", "9188");
        data = "123";

        b64Certificate = "MIIDBTCCAm6gAwIBAgINALgHGlkvtHqiyy6cODANBgkqhkiG9w0BAQsFADB5MQsw\n" +
                "CQYDVQQGEwJDTjEQMA4GA1UEBxMHSmlhbmdTdTESMBAGA1UEChMJU1lBTiBUZWNo\n" +
                "MRAwDgYDVQQLEwdTdXBwb3J0MQ8wDQYDVQQDEwZURVNUQ0ExITAfBgNVBC0TGDEy\n" +
                "OTE5NTgyMjVVNEQwMUI3RDE0NzlGQzAiGA8yMDE3MTIyNzE2MDAwMFoYDzIwMjAx\n" +
                "MDE2MTYwMDAwWjBdMQswCQYDVQQGEwJDTjEXMBUGA1UEKhMOMTkyLjE2OC4yMC4x\n" +
                "NjYxDTALBgNVBAoTBFRlc3QxDTALBgNVBAsTBFRlc3QxFzAVBgNVBAMTDjE5Mi4x\n" +
                "NjguMjAuMTY2MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCw6SKMfbwkvZrh\n" +
                "Tt3iavtn1o6GiX4T+atPSnszcwcjBBCeBkk+4lO30n3WBs/RI3Ce6WKM50QrFUSy\n" +
                "Vpet4SmoJUqbEg5ce8iv1v/4bum3JUX62I3SrpDogrMT7vkr4Cs+cyn4S++XgiAo\n" +
                "2bAwgAyPMbKbYJYWLi4amSu+haIp0wIDAQABo4GoMIGlMAkGA1UdEwQCMAAwHQYD\n" +
                "VR0OBBYEFPuJBlwm1QUnWDfruGfzOC1cx6zdMEsGA1UdJQEB/wRBMD8GCCsGAQUF\n" +
                "BwMBBggrBgEFBQcDAgYIKwYBBQUHAwQGCCsGAQUFBwMIBgorBgEEAYI3CgMDBglg\n" +
                "hkgBhvhCBAEwHwYDVR0jBBgwFoAUPimQCdnOOzanjSH8mkStJZ62TXUwCwYDVR0P\n" +
                "BAQDAgTwMA0GCSqGSIb3DQEBCwUAA4GBAIoWQW7Y3gX7CiC6d2Lz5cUwUne8j+Bs\n" +
                "fivXFYN3m1hSy2MSNqEcouhqP8WPV10Vu3rzAAHHrW03cv2v+X0l/IBiBMsU68eg\n" +
                "EIq7lur8RvV9w2JTq9xtwPKTrLfuCUML0fo/syMnn83/p32Kh/TBe64eXR8haeRZ\n" +
                "n2Qjo5JV3y3a";

        p1 = "VgChqbEEHs7/UIPAAQfyVKBO3PHwsTnkrCiswnrxbsXR5hZMl/IlKBuBa1w5Aq4txfJC8ngTk4wu" +
                "5uTtJghOfqriBMO1logryltL5sQhrWApFcj9JC/2IqXR1mBERJQcYPIsN2H/NAUkdA1de96dx5+Y" +
                "a7Qf8bTJwAYXpUu0xeo=";

        p7 = "MIIFaAYJKoZIhvcNAQcCoIIFWTCCBVUCAQExDzANBglghkgBZQMEAgEFADASBgkqhkiG9w0BBwGg\n" +
                "BQQDMTIzoIIDCTCCAwUwggJuoAMCAQICDQC4BxpZL7R6ossunDgwDQYJKoZIhvcNAQELBQAweTEL\n" +
                "MAkGA1UEBhMCQ04xEDAOBgNVBAcTB0ppYW5nU3UxEjAQBgNVBAoTCVNZQU4gVGVjaDEQMA4GA1UE\n" +
                "CxMHU3VwcG9ydDEPMA0GA1UEAxMGVEVTVENBMSEwHwYDVQQtExgxMjkxOTU4MjI1VTREMDFCN0Qx\n" +
                "NDc5RkMwIhgPMjAxNzEyMjcxNjAwMDBaGA8yMDIwMTAxNjE2MDAwMFowXTELMAkGA1UEBhMCQ04x\n" +
                "FzAVBgNVBCoTDjE5Mi4xNjguMjAuMTY2MQ0wCwYDVQQKEwRUZXN0MQ0wCwYDVQQLEwRUZXN0MRcw\n" +
                "FQYDVQQDEw4xOTIuMTY4LjIwLjE2NjCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAsOkijH28\n" +
                "JL2a4U7d4mr7Z9aOhol+E/mrT0p7M3MHIwQQngZJPuJTt9J91gbP0SNwnulijOdEKxVEslaXreEp\n" +
                "qCVKmxIOXHvIr9b/+G7ptyVF+tiN0q6Q6IKzE+75K+ArPnMp+Evvl4IgKNmwMIAMjzGym2CWFi4u\n" +
                "GpkrvoWiKdMCAwEAAaOBqDCBpTAJBgNVHRMEAjAAMB0GA1UdDgQWBBT7iQZcJtUFJ1g367hn8zgt\n" +
                "XMes3TBLBgNVHSUBAf8EQTA/BggrBgEFBQcDAQYIKwYBBQUHAwIGCCsGAQUFBwMEBggrBgEFBQcD\n" +
                "CAYKKwYBBAGCNwoDAwYJYIZIAYb4QgQBMB8GA1UdIwQYMBaAFD4pkAnZzjs2p40h/JpErSWetk11\n" +
                "MAsGA1UdDwQEAwIE8DANBgkqhkiG9w0BAQsFAAOBgQCKFkFu2N4F+wogundi8+XFMFJ3vI/gbH4r\n" +
                "1xWDd5tYUstjEjahHKLoaj/Fj1ddFbt68wABx61tN3L9r/l9JfyAYgTLFOvHoBCKu5bq/Eb1fcNi\n" +
                "U6vcbcDyk6y37glDC9H6P7MjJ5/N/6d9iof0wXuuHl0fIWnkWZ9kI6OSVd8t2jGCAhwwggIYAgEB\n" +
                "MIGKMHkxCzAJBgNVBAYTAkNOMRAwDgYDVQQHEwdKaWFuZ1N1MRIwEAYDVQQKEwlTWUFOIFRlY2gx\n" +
                "EDAOBgNVBAsTB1N1cHBvcnQxDzANBgNVBAMTBlRFU1RDQTEhMB8GA1UELRMYMTI5MTk1ODIyNVU0\n" +
                "RDAxQjdEMTQ3OUZDAg0AuAcaWS+0eqLLLpw4MA0GCWCGSAFlAwQCAQUAoIHkMBgGCSqGSIb3DQEJ\n" +
                "AzELBgkqhkiG9w0BBwEwHAYJKoZIhvcNAQkFMQ8XDTE4MDYxMjA2MzczMlowLwYJKoZIhvcNAQkE\n" +
                "MSIEIKZlpFkgQi+dQX5IZ+/cT7igSh8//x+gfpmOhvf3onrjMHkGCSqGSIb3DQEJDzFsMGowCwYJ\n" +
                "YIZIAWUDBAEqMAsGCWCGSAFlAwQBFjALBglghkgBZQMEAQIwCgYIKoZIhvcNAwcwDgYIKoZIhvcN\n" +
                "AwICAgCAMA0GCCqGSIb3DQMCAgFAMAcGBSsOAwIHMA0GCCqGSIb3DQMCAgEoMA0GCSqGSIb3DQEB\n" +
                "AQUABIGAnf/Xw55/b3EvhS9f1puSoXxVqTbAJ8JxtCIie5HYi0Z0WjmdUdx3LcGuLX/90XkgI/Qg\n" +
                "EfNfoVYsRohgAitpg/gudjEbUCB4bczw4vzbols6COFKLDNX+N1mQS7BeU6i2zSJY2Id/MWLY/lV\n" +
                "lMnXlEM5WsjbD3H4DAi9CBoB1+E=";


    }


    @Test
    public void testVerifyPKCS7Detached() throws Exception {
        //PKCS7签名，detach模式
        String p7Data = "MIIDqAYKKoEcz1UGAQQCAqCCA5gwggOUAgEBMQ4wDAYIKoEcz1UBgxEFADAMBgoqgRzPVQYBBAIBoIICmTCCApUwggI5oAMCAQICBRA0FoGAMAwGCCqBHM9VAYN1BQAwXDELMAkGA1UEBhMCQ04xMDAuBgNVBAoMJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEbMBkGA1UEAwwSQ0ZDQSBURVNUIFNNMiBPQ0ExMB4XDTE4MTEwMTA2MzgyOVoXDTIzMTEwMTA2MzgyOVowgYYxCzAJBgNVBAYTAmNuMRAwDgYDVQQKDAdPQ0ExUlNBMRAwDgYDVQQLDAdDQU1MTUFDMRkwFwYDVQQLDBBPcmdhbml6YXRpb25hbC0zMTgwNgYDVQQDDC8wNDFAWkMwMDAwMDAwMDAwMDAxQEMwMDAwMDAwMDAwMDAxXzAwMUAwMDAwMDAwMjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABAvtHGgtNNAniIZt37BtFPQ4ZTNQtYSKfjJe7LV8RQvdETB8s0di3EdqXMtcchjU2TWYyYMiCZgy3LPobLRzZ16jgbowgbcwHwYDVR0jBBgwFoAUa/4Y2o9COqa4bbMuiIM6NKLBMOEwDAYDVR0TAQH/BAIwADA4BgNVHR8EMTAvMC2gK6AphidodHRwOi8vdWNybC5jZmNhLmNvbS5jbi9TTTIvY3JsMzk0MS5jcmwwDgYDVR0PAQH/BAQDAgbAMB0GA1UdDgQWBBTtM9EiJGDhFtxkVN+Gf5Odb3THRzAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQwDAYIKoEcz1UBg3UFAANIADBFAiEAqmVZxNN7yWuMHrplkE+CS8KpPE2lN6btBn4QGTHzmboCIDRkFJ1+Au0H6idLONQxP4nw742xBrp+DHQ2Az8fVsn1MYHTMIHQAgEBMGUwXDELMAkGA1UEBhMCQ04xMDAuBgNVBAoMJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEbMBkGA1UEAwwSQ0ZDQSBURVNUIFNNMiBPQ0ExAgUQNBaBgDAMBggqgRzPVQGDEQUAMA0GCSqBHM9VAYItAQUABEcwRQIhAJ8ctFtAAsHn8NbXUV6ULmQUjdS5DbHkAE+wjQl6Nm96AiBlGTW9plaws0zRN58am04omhAcUzfoVFbIBDEdvkJu0A==";

        //PKCS7签名的文件
        String dataFile = "/Users/Iceberg/Desktop/CFCA/NIHF1002811000001-20181028-00000004.ZIP";

        byte[] dataBinary = IOUtils.toByteArray(new FileInputStream(dataFile));

        NetoneSVS result = this.svsClient.verifyPKCS7(p7Data, Base64.toBase64String(dataBinary), true);
        assert result.getStatusCode() == 200;
    }

    @Test
    public void testVerifyPKCS1() throws Exception {

        NetoneSVS result = this.svsClient.verifyPKCS1(data, p1, b64Certificate, false);
        assert result.getStatusCode() == 200;
    }


    @Test
    public void verifyPKCS7() throws Exception {

        NetoneSVS result = this.svsClient.verifyPKCS7(p7);

        System.out.println(result.getOrginalBase64());
        assert result.getStatusCode() == 200;
    }


    @Test
    public void testListCertificate() throws Exception {
        NetoneCertList list = this.svsClient.listCertificates();
        List<NetoneCertificate> ncs = list.getCertList();
        for (NetoneCertificate c : ncs) {
            System.out.println(c.getSubject());
        }
    }

    @Test
    public void testVerifyXML() throws Exception {

        String xmlData = "PD94bWwgdmVyc2lvbj0iMS4wIj8+CjxkYXRhPmhlbGxvPFNpZ25hdHVyZSB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+CjxTaWduZWRJbmZvPgo8Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8U2lnbmF0dXJlTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxkc2lnLW1vcmUjcnNhLXNoYTI1NiIvPgo8UmVmZXJlbmNlPgo8VHJhbnNmb3Jtcz4KPFRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNlbnZlbG9wZWQtc2lnbmF0dXJlIi8+CjwvVHJhbnNmb3Jtcz4KPERpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3NoYTI1NiIvPgo8RGlnZXN0VmFsdWU+SmlQd2RhbU9CYXk2ckdGSVB6aXlOejlOaDZleXVWbEpMS3N2WTE5QVp5MD08L0RpZ2VzdFZhbHVlPgo8L1JlZmVyZW5jZT4KPC9TaWduZWRJbmZvPgo8U2lnbmF0dXJlVmFsdWU+Sy90UVlVbXR2Sk5QendTVGVwc3MxVEpxRnZxaEdlakl2cUtKVVRqbDRCZVBkQy8yMWRZOXBxb0crUmRtWlE2KwpLbmpEVkFldWVVWHRwL2ZWcDhXZ01pRDQ5MlJwSkFLa3BMMWY2VFRvZ0VFclEwL1ZTdzNjaEs4MStrS3RVeGVWClJ2ci9xdjZxWU1lalBialc1SitJSmhYbFdCOE51SnREQzJxN0NESTVLb0E9PC9TaWduYXR1cmVWYWx1ZT4KPEtleUluZm8+CjxYNTA5RGF0YT4KPFg1MDlDZXJ0aWZpY2F0ZT5NSUlEQlRDQ0FtNmdBd0lCQWdJTkFMZ0hHbGt2dEhxaXl5NmNPREFOQmdrcWhraUc5dzBCQVFzRkFEQjVNUXN3CkNRWURWUVFHRXdKRFRqRVFNQTRHQTFVRUJ4TUhTbWxoYm1kVGRURVNNQkFHQTFVRUNoTUpVMWxCVGlCVVpXTm8KTVJBd0RnWURWUVFMRXdkVGRYQndiM0owTVE4d0RRWURWUVFERXdaVVJWTlVRMEV4SVRBZkJnTlZCQzBUR0RFeQpPVEU1TlRneU1qVlZORVF3TVVJM1JERTBOemxHUXpBaUdBOHlNREUzTVRJeU56RTJNREF3TUZvWUR6SXdNakF4Ck1ERTJNVFl3TURBd1dqQmRNUXN3Q1FZRFZRUUdFd0pEVGpFWE1CVUdBMVVFS2hNT01Ua3lMakUyT0M0eU1DNHgKTmpZeERUQUxCZ05WQkFvVEJGUmxjM1F4RFRBTEJnTlZCQXNUQkZSbGMzUXhGekFWQmdOVkJBTVREakU1TWk0eApOamd1TWpBdU1UWTJNSUdmTUEwR0NTcUdTSWIzRFFFQkFRVUFBNEdOQURDQmlRS0JnUUN3NlNLTWZid2t2WnJoClR0M2lhdnRuMW82R2lYNFQrYXRQU25zemN3Y2pCQkNlQmtrKzRsTzMwbjNXQnMvUkkzQ2U2V0tNNTBRckZVU3kKVnBldDRTbW9KVXFiRWc1Y2U4aXYxdi80YnVtM0pVWDYySTNTcnBEb2dyTVQ3dmtyNENzK2N5bjRTKytYZ2lBbwoyYkF3Z0F5UE1iS2JZSllXTGk0YW1TdStoYUlwMHdJREFRQUJvNEdvTUlHbE1Ba0dBMVVkRXdRQ01BQXdIUVlEClZSME9CQllFRlB1SkJsd20xUVVuV0RmcnVHZnpPQzFjeDZ6ZE1Fc0dBMVVkSlFFQi93UkJNRDhHQ0NzR0FRVUYKQndNQkJnZ3JCZ0VGQlFjREFnWUlLd1lCQlFVSEF3UUdDQ3NHQVFVRkJ3TUlCZ29yQmdFRUFZSTNDZ01EQmdsZwpoa2dCaHZoQ0JBRXdId1lEVlIwakJCZ3dGb0FVUGltUUNkbk9PemFualNIOG1rU3RKWjYyVFhVd0N3WURWUjBQCkJBUURBZ1R3TUEwR0NTcUdTSWIzRFFFQkN3VUFBNEdCQUlvV1FXN1kzZ1g3Q2lDNmQyTHo1Y1V3VW5lOGorQnMKZml2WEZZTjNtMWhTeTJNU05xRWNvdWhxUDhXUFYxMFZ1M3J6QUFISHJXMDNjdjJ2K1gwbC9JQmlCTXNVNjhlZwpFSXE3bHVyOFJ2Vjl3MkpUcTl4dHdQS1RyTGZ1Q1VNTDBmby9zeU1ubjgzL3AzMktoL1RCZTY0ZVhSOGhhZVJaCm4yUWpvNUpWM3kzYTwvWDUwOUNlcnRpZmljYXRlPgo8L1g1MDlEYXRhPgo8L0tleUluZm8+CjwvU2lnbmF0dXJlPjwvZGF0YT4K";
        NetoneSVS result = this.svsClient.verifyXML(xmlData);
        assert result.getStatusCode() == 200;
    }

    @Test
    public void testVC() throws Exception {
        NetoneSVS result = svsClient.verifyCertificate(b64Certificate);

        System.out.println(result.getOrginalBase64());

        assert result.getStatusCode() == 200;
    }
}
