package com.syan.netonej.common.xml;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.KeyListItem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author mmdet
 * @Date 2022-05-06 09:29
 * @Description
 */
public class XMLParser {

    public static List<KeyListItem> parserKeyList(InputStream in) throws NetonejException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxHandler handler = new SaxHandler();
            parser.parse(in,handler);
            return handler.getKeys();
        } catch (Exception e) {
            throw new NetonejException("xml解析失败",e);
        }
    }

    public static KeyListItem parserCert(InputStream in) throws NetonejException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxCertParseHandler handler = new SaxCertParseHandler();
            parser.parse(in,handler);
            return handler.getKeyListItem();
        } catch (Exception e) {
            throw new NetonejException("xml解析失败",e);
        }
    }

    public static XmlData parserXmlData(InputStream in) throws NetonejException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxDataParseHandler handler = new SaxDataParseHandler();
            parser.parse(in,handler);
            return handler.getXmlData();
        } catch (Exception e) {
            throw new NetonejException("xml解析失败",e);
        }
    }

    public static XmlData parserCertList(InputStream in) throws NetonejException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxCertListHandler handler = new SaxCertListHandler();
            parser.parse(in,handler);
            return new XmlData(handler.getData(),handler.getCertificates());
        } catch (Exception e) {
            throw new NetonejException("xml解析失败",e);
        }
    }


}
