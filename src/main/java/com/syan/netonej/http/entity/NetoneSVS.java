/**
 * 文 件 名:  NetoneSVS.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  SVS服务返回信息
 * 修 改 人:  liyb
 * 修改时间:  2013-04-29
 */
package com.syan.netonej.http.entity;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.exception.NetonejException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;


public class NetoneSVS extends NetoneResponse {
    /**
     *
     */
    private NetoneCertificate certificate;

    /**
     * 根据返回结果构造SVS对象
     * @param response 返回结果
     * @throws NetonejException 异常
     */
    public NetoneSVS(NetoneResponse response) throws NetonejException {
        super(response.getStatusCode(), response.getStatusCodeMessage());
        if(response != null && response.getStatusCode() == 200){
            String result = response.getResult();
//            XmlData xmlData = XMLParser.parserCertList(new ByteArrayInputStream(result.getBytes()));
//            if(xmlData != null && xmlData.getCertificates() != null&& xmlData.getCertificates().size() > 0){
//                certificate = xmlData.getCertificates().get(0);
//            }
//            if(xmlData.getData() != null){
//                setResult(xmlData.getData());
//            }
            xmlParse(result);
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


    private void xmlParse(String xml) throws NetonejException {
        // 1. 创建DOM解析器
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 2. 解析XML（从字符串解析，也可以换成文件）
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            doc.getDocumentElement().normalize();

            // 3. 获取根节点 <svs>
            Element root = doc.getDocumentElement();

            //
            String data = getTextContent(root, "data");

            if(data != null && data.length() > 0){
                setResult(data);
            }

            // 读取 item 下的 certificate
            String crt = "";
            NodeList itemList = root.getElementsByTagName("item");
            if (itemList.getLength() > 0) {
                Element item = (Element) itemList.item(0);
                crt = getTextContent(item, "certificate");
            }
            if (!NetonejUtil.isEmpty(crt)) {
                this.certificate = new NetoneCertificate(crt);
            }
        } catch (Exception e) {
            throw new NetonejException("xml内容解析失败",e);
        }
    }

}
