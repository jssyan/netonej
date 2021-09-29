/**
 * 文 件 名: Conventer.java 
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  
 * 修 改 人:  liyb
 * 修改时间:  2016-07-26
 */
package com.syan.netonej.common.dict;

/**
 * 1代表sm2, 2代表rsa，3代表ecc, 4代表sk
 */
public enum KeyAlgorithm {
	SM2(1),
	RSA(2),
	ECC(3),
	SK(4);

	private int value;

	KeyAlgorithm(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
