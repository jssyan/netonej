/*
 * Project: UniraSDK
 * 
 * @(#) SM2EllipticCurveParameters.java   14-6-26 下午3:14
 *
 * Copyright 2013 Jiangsu Syan Technology Co.,Ltd. All rights reserved.
 * Jiangsu Syan PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.syan.netonej.common.sm3;

import java.math.BigInteger;

/**
 * <p>
 * This class provides...
 * </p>
 *
 * @author Iceberg
 * @version $Revision $Date:14-6-26 下午3:14
 * @since 1.0
 */
public class SM2EllipticCurveParameters {

    private static BigInteger p = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16);
    private static BigInteger a = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);
    private static BigInteger b = new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);
    private static BigInteger n = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);
    private static BigInteger Gx = new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    private static BigInteger Gy = new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);

    public static byte[] getP() {
        return asUnsigned32ByteArray(p);
    }

    public static byte[] getA() {
        return asUnsigned32ByteArray(a);
    }

    public static byte[] getB() {
        return asUnsigned32ByteArray(b);
    }

    public static byte[] getN() {
        return asUnsigned32ByteArray(n);
    }

    public static byte[] getGx() {
        return asUnsigned32ByteArray(Gx);
    }

    public static byte[] getGy() {
        return asUnsigned32ByteArray(Gy);
    }

    /**
     * 整形转换成网络传输的字节流（字节数组）型数据
     *
     * @param num 一个整型数据
     * @return 4个字节的自己数组
     */
    public static byte[] IntToByte(int num) {
        byte[] bytes = new byte[4];

        bytes[0] = (byte) (0xff & (num >> 0));
        bytes[1] = (byte) (0xff & (num >> 8));
        bytes[2] = (byte) (0xff & (num >> 16));
        bytes[3] = (byte) (0xff & (num >> 24));

        return bytes;
    }

    /**
     * 四个字节的字节数据转换成一个整形数据
     *
     * @param bytes 4个字节的字节数组
     * @return 一个整型数据
     */
    public static int ByteToInt(byte[] bytes) {
        int num = 0;
        int temp;
        temp = (0x000000ff & (bytes[0])) << 0;
        num = num | temp;
        temp = (0x000000ff & (bytes[1])) << 8;
        num = num | temp;
        temp = (0x000000ff & (bytes[2])) << 16;
        num = num | temp;
        temp = (0x000000ff & (bytes[3])) << 24;
        num = num | temp;

        return num;
    }

    public static byte[] LongToByte(long num) {
        byte[] bytes = new byte[8];

        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (0xff & (num >> (i * 8)));
        }

        return bytes;
    }


    public static byte[] asUnsigned32ByteArray(BigInteger n) {
        return asUnsignedNByteArray(n, 32);
    }

    public static byte[] asUnsignedNByteArray(BigInteger x, int length) {
        if (x == null) {
            return null;
        }

        byte[] tmp = new byte[length];
        int len = x.toByteArray().length;
        if (len > length + 1) {
            return null;
        }

        if (len == length + 1) {
            if (x.toByteArray()[0] != 0) {
                return null;
            } else {
                System.arraycopy(x.toByteArray(), 1, tmp, 0, length);
                return tmp;
            }
        } else {
            System.arraycopy(x.toByteArray(), 0, tmp, length - len, len);
            return tmp;
        }
    }
}
