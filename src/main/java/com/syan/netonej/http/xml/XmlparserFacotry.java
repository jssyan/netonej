/**
 * 文 件 名:  XmlparserFacotry.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  xml分析器管理类
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.syan.netonej.exception.NetonejExcepption;

/**
 * xml分析器管理类
 *
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class XmlparserFacotry {
    /**
     * 解析器文件名前缀
     */
    private final static String fix_name = "Xmlparser";
    /**
     * 解析器包路径
     */
    private final static String package_path = "com.syan.netonej.http.xml.";

    /**
     * Action
     */
    private static String action;

    /**
     * 解析xml返回解析结果
     *
     * @param xmlstring
     * @return 解析结果
     * @throws Exception
     */
    public static Object parseXmlString(String xmlstring) throws NetonejExcepption {

        try {
            if (xmlstring.startsWith("{") && xmlstring.endsWith("}")) {
                Parser parser = (Parser) Class.forName(package_path + "JSONParser").newInstance();
                return parser.parse(xmlstring);
            } else {
                //XML
                String actionAndVersion = getActionVersion(xmlstring);
                Parser parser = (Parser) Class.forName(package_path + fix_name + actionAndVersion).newInstance();
                return parser.parse(xmlstring);
            }
        } catch (Exception e) {
            throw new NetonejExcepption(e.getMessage(), e);
        }
    }

    /**
     * 解析xml返回签名原文
     *
     * @param xmlstring
     * @return 解析结果
     * @throws Exception
     */
    public static Object parseXmlString4Data(String xmlstring) throws NetonejExcepption {
        try {
            if (xmlstring.startsWith("{") && xmlstring.endsWith("}")) {
                Parser parser = (Parser) Class.forName(package_path + "JSONParser").newInstance();
                return parser.parseData(xmlstring);
            } else {

                String actionAndVersion = getActionVersion(xmlstring);
                Parser parser = (Parser) Class.forName(package_path + fix_name + actionAndVersion).newInstance();
                return "vp7".equals(action) ? parser.parseData(xmlstring) : parser.parse(xmlstring);

            }
        } catch (Exception e) {
            throw new NetonejExcepption(e.getMessage(), e);
        }
    }

    /**
     * @param xmlContent
     * @return String action名称_版本号字符串
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private static String getActionVersion(String xmlContent) throws IOException, SAXException, ParserConfigurationException {
        final String listcAction = "listc";
        String version = quickGetItem("version", xmlContent);
        action = quickGetItem("action", xmlContent);


        String retValue = "";
        //3.4.3版除了listc外 其他xml结构一样 用vx 解析器获取证书数据

        File file = new File(package_path + fix_name + version.replace('.', '_') + ".class");
        if (file.exists()) {
            retValue = action + "_" + version.replace('.', '_');
        } else {
            retValue = listcAction.equals(action) ? "listc_3_4_3" : "vx_3_4_3";
        }

        return retValue;
    }


    private static String quickGetItem(String nodeName, String xmlContent) throws IOException {

        String leftPart = nodeName + ">";
        String beginNode = "<" + leftPart;
        String endNode = "</" + leftPart;

        int beginModeIndex = xmlContent.indexOf(beginNode);
        int endModeIndex = xmlContent.indexOf(endNode);

        if (-1 == beginModeIndex || -1 == endModeIndex) {
            throw new IOException("未找到解析XML节点：" + nodeName + ",请检查XML数据格式");
        }

        return xmlContent.substring(beginModeIndex + beginNode.length(), endModeIndex);
    }
}
