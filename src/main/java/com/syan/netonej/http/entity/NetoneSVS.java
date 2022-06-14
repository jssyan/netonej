/**
 * 文 件 名:  NetoneSVS.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  SVS服务返回信息
 * 修 改 人:  liyb
 * 修改时间:  2013-04-29
 */
package com.syan.netonej.http.entity;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.xml.XMLParser;
import com.syan.netonej.common.xml.XmlData;
import com.syan.netonej.exception.NetonejException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;


public class NetoneSVS extends NetoneResponse {
    /**
     *
     */
    private NetoneCertificate certificate;

    /**
     * 根据返回结果构造SVS对象
     *
     * @param response
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneSVS(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
            XmlData xmlData = XMLParser.parserCertList(new ByteArrayInputStream(result.getBytes()));
            certificate = xmlData.getCertificates().get(0);
            if(xmlData.getData() != null){
                setResult(xmlData.getData());
            }
        }
    }

    /**
     * @return 证书对象
     */
    public NetoneCertificate getCertificate() {
        return certificate;
    }

    /**
     * @param certificate 证书对象
     */
    public void setCertificate(NetoneCertificate certificate) {
        this.certificate = certificate;
    }

}
