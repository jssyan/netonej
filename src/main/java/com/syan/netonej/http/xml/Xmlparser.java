/**
 * 文 件 名:  Xmlparser.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  xml分析接口
 * 修 改 人:  liyb
 * 修改时间:  2013-04-29
 */
package com.syan.netonej.http.xml;

import javax.xml.parsers.ParserConfigurationException;

/**xml分析接口
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public interface Xmlparser {
	/**
	 * 返回xml解析结果
	 * @param xmlstr 
	 * @return Object 解析结果
	 * @throws ParserConfigurationException 
	 */
	public Object parse(String xmlstr) throws Exception;
}
