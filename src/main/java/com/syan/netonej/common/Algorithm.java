/**
 * 文 件 名: Conventer.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2016-07-26
 */
package com.syan.netonej.common;
/** 
 * Algorithm 定义数据摘要算法
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class Algorithm {
	/**
	 * md5
	 */
	public static final String MD5="md5";
	/**
	 * sha1 rsa签名默认摘要算法
	 */
	public static final String SHA1="sha1";
	/**
	 * sha256
	 */
	public static final String SHA256="sha256";
	/**
	 * sha384
	 */
	public static final String SHA384="sha384";
	/**
	 * sha512
	 */
	public static final String SHA512="sha512";
	/**
	 * ecdsa-sm2-with-sm3
	 * SM3(2011) (SM2) 
	 * sm2 签名时默认摘要算法
	 */
	public static final String ECDSA_SM2_WITH_SM3="ecdsa-sm2-with-sm3";
	/**
	 * ecdsa-sm2
	 * SM3(2012) (SM2) 
	 */
	public static final String ECDSA_SM2="ecdsa-sm2";
	
    public final static String SM3_ALGORITHM_OID = "1.2.156.10197.1.401";
    
    public final static String SHA256_ALGORITHM_OID = "2.16.840.1.101.3.4.2.1";
    public final static String SHA384_ALGORITHM_OID = "2.16.840.1.101.3.4.2.2";
    public final static String SHA512_ALGORITHM_OID = "2.16.840.1.101.3.4.2.3";

}
