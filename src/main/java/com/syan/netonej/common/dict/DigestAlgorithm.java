/**
 * 文 件 名: Conventer.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2016-07-26
 */
package com.syan.netonej.common.dict;

/**
 * md5，sha1, sha256, sha384, sha512等）。
 * 如果在request里面没有包含这些数据项，PCS将使用缺省设置,
 * 对于RSA密钥, 缺省摘要算法是sha1, 对于ECC密钥, 缺省摘要算法是ecdsa-sm2-with-sm3
 */
public enum DigestAlgorithm {
	MD5("md5"),
	SHA1("sha1"),
	SHA256("sha256"),
	SHA384("sha384"),
	SHA512("sha512"),
	SM3("sm3"),
	ECDSASM2WITHSM3("ecdsa-sm2-with-sm3"),
	ECDSASM2("ecdsa-sm2");

	private String name;

	public String getName() {
		return name;
	}

	DigestAlgorithm(String name) {
		this.name = name;
	}
}
