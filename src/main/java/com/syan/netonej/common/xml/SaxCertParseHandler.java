package com.syan.netonej.common.xml;

import com.syan.netonej.http.entity.KeyListItem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * @Author mmdet
 * @Date 2022-05-06 09:36
 * @Description
 */
public class SaxCertParseHandler extends DefaultHandler {

    private KeyListItem keyListItem;
    private String qName;

    @Override
    public void startDocument() throws SAXException {
    }

    //遍历xml文件结束标签
    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.qName = qName;
        if (this.qName.equals("data")) {
            keyListItem = new KeyListItem();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        this.qName = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);
        if(this.qName.equals("id") && this.keyListItem != null){
            this.keyListItem.setId(value);
        }else if(this.qName.equals("certificate") && this.keyListItem != null){
            this.keyListItem.setCertificate(value);
        }else if(this.qName.equals("privk") && this.keyListItem != null){
            this.keyListItem.setPrivk(value);
        }
    }

    public KeyListItem getKeyListItem() {
        return keyListItem;
    }
}
