/**
 * 文 件 名: Conventer.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2016-07-26
 */
package com.syan.netonej.common.dict;


public enum CipherAlgorithm {
	SM4CBC("SM4-CBC"),
	AES192CBC("AES-192-CBC"),
	AES256CBC("AES-256-CBC");

	private String name;

	public String getName() {
		return name;
	}

	CipherAlgorithm(String name) {
		this.name = name;
	}
}
