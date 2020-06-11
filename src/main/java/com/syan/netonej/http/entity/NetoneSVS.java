/**
 * 文 件 名:  NetoneSVS.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  SVS服务返回信息
 * 修 改 人:  liyb
 * 修改时间:  2013-04-29
 */
package com.syan.netonej.http.entity;

import java.io.IOException;
import java.security.cert.CertificateException;

import com.syan.netonej.http.HttpStatus;
import com.syan.netonej.http.xml.XmlparserFacotry;

/**
 * SVS服务返回信息
 * <p>
 * update by wangjx 2018-5-22
 *
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneSVS extends NetoneBase {
    /**
     *
     */
    private NetoneCertificate certificate;

    //原文
    private String orginalBase64;

    /**
     * 根据返回结果构造SVS对象
     *
     * @param response
     * @throws CertificateException
     * @throws IOException
     */
    public NetoneSVS(NetoneResponse response) throws CertificateException, IOException {
        super(response.getStatusCode());
        try {
            if (response.getStatusCode() == HttpStatus.SC_OK) {
                String orginal = XmlparserFacotry.parseXmlString4Data(response.getRetString()).toString();
                Object obj = XmlparserFacotry.parseXmlString(response.getRetString());

                if (obj != null) {
                    certificate = new NetoneCertificate(obj.toString());
                }

                this.setOrginalBase64(orginal);
            }
        } catch (Exception e) {
            throw new CertificateException(e.getMessage(), e);
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

    public String getOrginalBase64() {
        return orginalBase64;
    }

    public void setOrginalBase64(String orginalBase64) {
        this.orginalBase64 = orginalBase64;
    }


}
