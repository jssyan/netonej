package cn.com.syan.netonej;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.EapiClient;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneResponse;
import org.junit.Test;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class EapiClientTest {


    private EapiClient eapiClient = new EapiClient("https://192.168.10.215","9108");

    String cert = "MIIELDCCAxSgAwIBAgINANCknqdjofaI1pSEpDANBgkqhkiG9w0BAQsFADAxMQsw\n" +
            "CQYDVQQGEwJDTjERMA8GA1UECgwIQUJDIGx0ZC4xDzANBgNVBAMMBmNhLWdlbjAe\n" +
            "Fw0yMjA0MTUwNzA0NTJaFw00MDA0MTQxNjAwMDBaMCAxCzAJBgNVBAYTAkNOMREw\n" +
            "DwYDVQQDDAh0ZXN0X3RzYTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "ALZY7OA/Adn/QxLkTkzO2D6ZsDYD++lN/HJ23ft0zYyQLI55v1gRQ26bHM5154Al\n" +
            "8Wlw2P9nCNAvMuFrMQHuBZsKK29c8/da0/n04E4/I1RqcDlFDUuWMcXC+W89rTkx\n" +
            "DwWjLJD7acpiyPeqjdO08cR5xKbRrb6eJd2D6vvyIRnCZWe5V/3T8oq+KCvrL8yd\n" +
            "oqeG2mkWEm3F7ez2UCK498euYPfHy/I4dXfY8SW355D2m+aNYlp6V5uqcu2AVTRR\n" +
            "vKB0B4v7mWwBIRO0Z8kJDO4uaq4AciB3fxKhSV3NqN9hC6GV20CE/aRhHSh1rRhP\n" +
            "27wn4OinHwmQMB6IRGAkQP0CAwEAAaOCAVIwggFOMAwGA1UdEwQFMAMBAf8wHQYD\n" +
            "VR0OBBYEFPObj0j+wqfsZGwSVskVSxznG7KyMA8GA1UdDwEB/wQFAwMH/4AwFgYD\n" +
            "VR0lAQH/BAwwCgYIKwYBBQUHAwgwLgYDVR0fBCcwJTAjoCGgH4YdaHR0cHM6Ly9h\n" +
            "aWEuc3lhbi5jb20uY24vY3JsL2EwYgYIKwYBBQUHAQEEVjBUMCQGCCsGAQUFBzAB\n" +
            "hhhodHRwczovL29jc3Auc3lhbi5jb20uY24wLAYIKwYBBQUHMAKGIGh0dHBzOi8v\n" +
            "YWlhLnN5YW4uY29tLmNuL2lzc3Vlci9hMB8GA1UdIwQYMBaAFFFFsTRKV80jaqDY\n" +
            "72Rjnw8oj8U3MEEGA1UdIAQ6MDgwNgYIKoEchvAAZAEwKjAoBggrBgEFBQcCARYc\n" +
            "aHR0cHM6Ly9jcHMuc3lhbi5jb20uY24vY3BzMTANBgkqhkiG9w0BAQsFAAOCAQEA\n" +
            "LyK1sYwFKE9MRnlzWHDN2t95VXg9PplpomFuXIQOoYp09cCiNGOkYNZHkWoZkOYF\n" +
            "K326cDeIpg9yGzHH1GI6ierhm2Bgjxqi6EHb64EFZIfA6NmT5pr4s37NN9OCDo/o\n" +
            "E1yzYcFc02Xdb6QuW7DdHVH50ioUQff5UKRBzzxZBMkoheUyMO5sDTF5hlB/X5dG\n" +
            "4NtDbZAiYEhl6rk51YkoxE9gN5fKvSXVd1dwFNOdqgKfRa2tK6fMTkHzSQy1BdAL\n" +
            "FV4OhUunIIauPpbaN+d+M2HLw3qYWbk3pMAAsNBGOuu6gwSCDTbg+6P9RzGWoaG8\n" +
            "4fz/4GQNkvwCkauqGj1O1A==";


    /**
     * 证书列表
     * @throws NetonejException
     */
    @Test
    public void listCertificates() throws NetonejException {
        NetoneCertList list = eapiClient
                .certificateListBuilder()
                .setRevoked(false)//可选，是否获取不可信证书
                .build();
        System.out.println(list.getStatusCode());
        System.out.println(list.getCertList().size());

        //解析出证书的CN项

        if(list.getCertList().size() > 0){
            String subject = list.getCertList().get(5).getSubject();
            System.out.println(NetonejUtil.getCNFromSubject(subject));
        }

    }

    /**
     * 上传证书
     * @throws Exception
     */
    @Test
    public void uploadCertificates() throws Exception {
        NetoneResponse response = eapiClient
                .certificateUploadBuilder()
                .setRevoked(true)//可选，是否上传到不可信列表
                .setCert(cert)//要上传的证书
                .build();
        System.out.println(response.getStatusCode());
    }

    /**
     * 删除证书
     * @throws Exception
     */
    @Test
    public void deleteCertificates() throws Exception {
        NetoneResponse response = eapiClient.certificateDeleteBuilder()
                .setId("黄")
                .setIdMagic(IdMagic.SCN)//设置要删除证书的CN
                .build();
        System.out.println(response.getStatusCode());
    }

}
