/**
 * 文 件 名:  XmlparserFacotry3_4_3.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**svs vc p1 p7 vx 3.4.3版本xml分析
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
class Xmlparservx_3_4_9 implements Parser {

    @Override
    public Object parse(String xmlstr) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xmlstr.getBytes());
        org.w3c.dom.Document doc = parser.parse(is);
        is.close();

        //获得根节点元素
        Node record = doc.getFirstChild();
        NodeList nl = record.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if ("item".equals(nl.item(i).getNodeName())) {
                return nl.item(i).getFirstChild().getTextContent();
            }

        }

        return null;
    }

    @Override
    public Object parseData(String xmlstr) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
