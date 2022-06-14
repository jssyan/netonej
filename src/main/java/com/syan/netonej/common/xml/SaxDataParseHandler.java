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
public class SaxDataParseHandler extends DefaultHandler {
    private XmlData xmlData;
    private String data;
    private String cert;
    private String qName;

    @Override
    public void startDocument() throws SAXException {
        xmlData = new XmlData();
    }

    //遍历xml文件结束标签
    @Override
    public void endDocument() throws SAXException {
        if(xmlData != null){
            xmlData.setData(data);
            List<NetoneCertificate> certificateList = new ArrayList<NetoneCertificate>(1);
            try {
                if(cert != null){
                    certificateList.add(new NetoneCertificate(cert));
                }
            } catch (NetonejException e) {
                e.printStackTrace();
            }
            xmlData.setCertificates(certificateList);
        }
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
        if(this.qName.equals("data") && this.xmlData != null){
            this.data = value;
        }else if(this.qName.equals("cert") && this.xmlData != null){
            this.cert = value;//p1签名时，xml中会返回<cert>证书</cert>
        }else if(this.qName.equals("status") && this.xmlData != null){
            this.data = value;//修改pin时<status>1</status>
        }else if(this.qName.equals("item") && this.xmlData != null){
            this.cert = value;//数字信封解包时<signer><item>证书</item></signer>
        }
    }

    public XmlData getXmlData() {
        return xmlData;
    }
}
