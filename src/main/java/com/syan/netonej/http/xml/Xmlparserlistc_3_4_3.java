/**
 * 文 件 名:  Xmlparserlistc_3_4_3.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  listc 分析实现类
 * 修 改 人:  liyb
 * 修改时间:  2013-04-29
 */
package com.syan.netonej.http.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.syan.netonej.http.entity.NetoneCertificate;

/**listc 3.4.3版本xml分析
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
class Xmlparserlistc_3_4_3 implements Parser {

    @Override
    public Object parse(String xmlstr) throws Exception {

        java.util.List<NetoneCertificate> listc = new ArrayList<NetoneCertificate>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xmlstr.getBytes());
        org.w3c.dom.Document doc = parser.parse(is);
        is.close();
        //获得根节点元素
        Node record = doc.getFirstChild();
        NodeList items = null;
        NodeList nl = record.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if ("trusted".equals(nl.item(i).getNodeName())) {
                items = nl.item(i).getChildNodes();
                for (int j = 0; j < items.getLength(); j++) {
                    try {
                        listc.add(new NetoneCertificate(items.item(j).getFirstChild().getTextContent()));
                    } catch (Exception e) {
                        throw new Exception("Xmlparserlistc_3_4_3 XML 解析中 构造证书失败" + e + "::" + items.item(j).getFirstChild().getTextContent());
                    }
                }
            }
        }
        return listc;
    }

    @Override
    public Object parseData(String xmlstr) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
