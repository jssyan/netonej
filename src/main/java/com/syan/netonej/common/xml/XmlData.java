package com.syan.netonej.common.xml;

import com.syan.netonej.common.NetoneCertificate;
import java.util.List;

/**
 * @Author mmdet
 * @Date 2022-05-06 14:51
 * @Description
 */
public class XmlData {

    private String data;

    private List<NetoneCertificate> certificates;

    public XmlData() {
    }

    public XmlData(String data, List<NetoneCertificate> certificates) {
        this.data = data;
        this.certificates = certificates;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<NetoneCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<NetoneCertificate> certificates) {
        this.certificates = certificates;
    }
}
