/**
 * 文 件 名:  NetonejIdMagic.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  NetonejIdMagic 定义IDMagic 取值
 * 修 改 人:  liyb
 * 修改时间:  2013-05-04 
 */
package com.syan.netonej.common;

/** NetonejIdMagic 定义IDMagic 取值
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetonejIdMagic {
	/**
	 * 密钥ID, (大小写无关). 这是缺省参数
	 */
	public final static  String KEYID ="kid";
	/**
	 * 证书序列号的16进制表示, (大小写无关) 
	 */
	public final static  String SNHEX ="snhex";
	/**
	 * 证书序列号的10进制表示, (大小写无关)
	 */
	public final static  String SNDEC ="sndec";
	/**
	 * 证书指纹(SHA1), 大小写无关
	 */
	public final static  String TNSHA1 ="tnsha1";
	/**
	 * 证书主题项中的CN, 大小写敏感
	 */
	public final static  String SCN ="scn";
	/**
	 * id里面包含是base64编码的私钥(DER或者PEM格式)
	 */
	public final static  String EXTPRI ="extpri";
	/**
	 *  id里面包含是base64编码的公钥(DER或者PEM格式)
	 */
	public final static  String EXTPUB ="extpub";
	
}
