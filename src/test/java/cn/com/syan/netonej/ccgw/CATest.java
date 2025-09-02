package cn.com.syan.netonej.ccgw;


import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetoneDigest;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.CAClient;
import com.syan.netonej.http.client.CcgwClient;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.client.ca.Extension;
import com.syan.netonej.http.entity.KeyListItem;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetonePCS;
import com.syan.netonej.http.entity.ca.CertificateResponse;
import com.syan.netonej.http.entity.ca.RevokeResponse;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @Author: xuyaoyao
 * @Date: 2025/8/29 13:24
 * @Description:
 */
public class CATest {

    private CcgwClient client = new CcgwClient("http://192.168.10.89","8028","bbb622eea8faf4a9","936b55c4e8c94cdbb00e1aadc4887b8d");
    CAClient caClient = client.CAClient();

    String cn="xuyy";

    private String o="syan";

    private String ou="dev";

    private String c="CN";

    private String s="JS";

    private String l="NJ";

    private String e="123456@syan.com.cn";

    private Extension[] extension;

    private String certType="0";

    String csr="MIICvjCCAaYCAQAwLjELMAkGA1UEBhMCQ04xETAPBgNVBAoMCEFCQyBsdGQuMQwwCgYDVQQDDAN4eXkwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDU1JZZLlaIFkC0cBX6hIvOffFl0uPVN2fcwDPpuaySQ49jcdbsOLaW+DZAMX5DbKEpASoA0ZmS04tRP8Jj/o7oRF0Vcb5ngdIJ0kfD+Vo/vW1c7C7mFvKrV4KsHyhZJx7eLRWzIWIdQzRWLXz465wxryYcroEsRNVXG6VD2JoV7BHbz02NZnS21VIeHWArAEu8W9acSeY6XUxuquJ7kVh2m7xlaHIJWN3OtQ+J1StSRITF+j60hxbrWgX88//Ic5hnB6gVKW49kEFkRr+1/UNrtfuRs8pwLB80rfXgPvYV9TXK0OTIUzk5Oo9jRzdt/KTOVcf7F/BSJadhVdkAs62BAgMBAAGgSzBJBgkqhkiG9w0BCQ4xPDA6MAkGA1UdEwQCMAAwHQYDVR0OBBYEFLDpyMIeM+tfjKtduUF7SVKPYjp+MA4GA1UdDwEB/wQEAwID+DANBgkqhkiG9w0BAQsFAAOCAQEADkHcbRoNN+lcQIRRLmYfQvOX3uv8dkEn+nhI3YbLcZKUBBB8IuLiuByQl6YDy5PF3/Jr70C5Uq0giLg6uzf1M3AavL1d00HlkEpiovqgUFLNYbfmsvYxR1U0XVVYuUMhWDqPJe6gnpoQvq41sClPXTKJauvEIcuulh/qqkKNz1zmN9X+oBCj9Vy6nDUfXAnzVqwAJVLJDaPf24Ln6ooaK6iB6KiTkZmjgXdFIxOq9QT5y/mHX+A4hwETu2wrtBjEMToOtM+WfkCQitoc583/29QcLMu2hZkvXtrNbZ5u+2fW0XcPDHHKEjlKMpPakJheitI2wlT6ORkbBuiWYX8xow==";

    StringBuilder serial=new StringBuilder();

    @Test
    public void test() throws Exception {
        testIssue();
        testReIssue();
        testDelay();
        testRevoke();
    }

    /**
     * 签发证书
     * @throws Exception
     */
    @Test
    public void testIssue() throws Exception {
        CertificateResponse response =
                caClient.issueBuilder()
                        //必填
                        .setCn(cn)
                        .setCsr(csr)
                        //选填
                        .setO(o) //证书 O 项,证书所属组织
                        .setOu(ou) //证书 OU 项,证书所属组织单元
                        .setC(c) //证书 C 项,国家,默认 CN（缺省值，默认为CN）
                        .setS(s) //证书 S 项,证书所属省
                        .setL(l) //证书 L 项,证书所属市
                        .setE(e) //证书 E 项,电子邮箱
                        .setExtension(genExt("test")) //证书扩展项，是个对象数组
                        .setCertType(certType) //签发双证(1) , 签发单证（0), 默认1
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
    @Test
    public void testReIssue() throws Exception {
        CertificateResponse response =
                caClient.reissueBuilder()
                        //必填
                        .setSerial(serial.toString())
                        .setCn(cn+"2")
                        .setCsr(csr)
                        //选填
                        .setO(o+"2") //证书 O 项,证书所属组织
                        .setOu(ou+"2") //证书 OU 项,证书所属组织单元
                        .setC(c) //证书 C 项,国家,默认 CN（缺省值，默认为CN）
                        .setS(s+"2") //证书 S 项,证书所属省
                        .setL(l+"2") //证书 L 项,证书所属市
                        .setE(e) //证书 E 项,电子邮箱
                        .setExtension(genExt("test"+"2")) //证书扩展项，是个对象数组
                        .setCertType(certType) //签发双证(1) , 签发单证（0), 默认1
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
    @Test
    public void testDelay() throws Exception {
        CertificateResponse response =
                caClient.delayBuilder()
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
    @Test
    public void testRevoke() throws Exception {
        RevokeResponse response =
                caClient.revokeBuilder()
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

    public Extension[] genExt(String value){
        Extension[] extensions = new Extension[1];
        Extension ext=new Extension();
        ext.setOid("2.5.4.9");
        ext.setName("address");
        ext.setValue(value);
        extensions[0]=ext;
        return extensions;
    }

}
