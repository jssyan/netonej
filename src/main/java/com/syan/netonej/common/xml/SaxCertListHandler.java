package com.syan.netonej.common.xml;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.exception.NetonejException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author mmdet
 * @Date 2022-05-06 09:36
 * @Description
 */
public class SaxCertListHandler extends DefaultHandler {

    private List<NetoneCertificate> certificates = new ArrayList<NetoneCertificate>();
    private String data;
    private String qName;

    @Override
    public void startDocument() throws SAXException {
        certificates.clear();
    }

    //遍历xml文件结束标签
    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.qName = qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        this.qName = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);
        if(this.qName.equals("certificate")){
            try {
                this.certificates.add(new NetoneCertificate(value));
            } catch (NetonejException e) {

            }
        }else if(this.qName.equals("data")){
            this.data = value;
        }
    }

    public List<NetoneCertificate> getCertificates() {
        return certificates;
    }

    public String getData() {
        return data;
    }
}
