package cn.com.syan.netonej;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejExcepption;
import com.syan.netonej.http.client.EapiClient;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneResponse;
import org.junit.Test;

import java.net.URLEncoder;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class EapiClientTest {

    EapiClient client = new EapiClient("https://192.168.10.215");

    String cert = "MIIB+zCCAaGgAwIBAgINALhK8zi+O2r42GdEKzAKBggqgRzPVQGDdTA1MQswCQYD\n" +
            "VQQGEwJDTjESMBAGA1UEChQJMTg3Y2Ffc20yMRIwEAYDVQQDFAkxODdjYV9zbTIw\n" +
            "IhgPMjAxOTExMDExNjAwMDBaGA8yMDIwMTExOTE2MDAwMFowHjELMAkGA1UEBhMC\n" +
            "Q04xDzANBgNVBAMTBjEwLjE1NTBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABD1x\n" +
            "02TNl9XRpn3/34Vq8ooVw+ZjgYhf7pDh2D7/oL8arvPnJgUa1p4LxmXXZlenM2ym\n" +
            "fNDPW2EMpsemgQIpr2SjgagwgaUwCQYDVR0TBAIwADAdBgNVHQ4EFgQUWuc1fSpW\n" +
            "XXwbFQNftr/gfYIqe8AwSwYDVR0lAQH/BEEwPwYIKwYBBQUHAwEGCCsGAQUFBwMC\n" +
            "BggrBgEFBQcDBAYIKwYBBQUHAwgGCisGAQQBgjcKAwMGCWCGSAGG+EIEATAfBgNV\n" +
            "HSMEGDAWgBTwH02c7KiHtmZf/eXy48KoGytFlzALBgNVHQ8EBAMCA/gwCgYIKoEc\n" +
            "z1UBg3UDSAAwRQIhAPvgx6bcx+neM9lLfdRrnPxyH1eAfW3NbauoF3zmruI3AiBs\n" +
            "THdAZaUWroXagq+yf+TUcR83DeTBglhn22q7G2RzEA==";


    /**
     * 可信证书列表
     * @throws NetonejExcepption
     */
    @Test
    public void listCertificates() throws NetonejExcepption {
        NetoneCertList list = client.listCertificates();
        System.out.println(list.getStatusCode());
        System.out.println(list.getCertList().size());

        //解析出证书的CN项

        if(list.getCertList().size() > 0){
            String subject = list.getCertList().get(0).getSubject();
            System.out.println(NetonejUtil.getCNFromSubject(subject));
        }

    }

    /**
     * 不可信证书列表
     * @throws NetonejExcepption
     */
    @Test
    public void listCertificates2() throws NetonejExcepption {
        NetoneCertList list = client.listCertificates(true);
        System.out.println(list.getStatusCode());
        System.out.println(list.getCertList().size());
    }

    /**
     * 向可信证书列表上传证书
     * @throws Exception
     */
    @Test
    public void uploadCertificates() throws Exception {
        NetoneResponse response = client.uploadCert(cert);
        System.out.println(response.getStatusCode());
    }

    /**
     * 向不可信证书列表上传证书
     * @throws Exception
     */
    @Test
    public void uploadCertificates2() throws Exception {
        NetoneResponse response = client.uploadCert(cert,true);
        System.out.println(response.getStatusCode());

    }

    /**
     * 删除可信证书列表中的证书
     * @throws Exception
     */
    @Test
    public void deleteCertificates() throws Exception {
        NetoneResponse response = client.deleteCert("0000000", IdMagic.SCN);
        System.out.println(response.getStatusCode());
    }

    /**
     * 删除不可信证书列表中的证书
     * @throws Exception
     */
    @Test
    public void deleteCertificates2() throws Exception {
        NetoneResponse response = client.deleteCert("10.155", IdMagic.SCN,true);
        System.out.println(response.getStatusCode());
    }
}
