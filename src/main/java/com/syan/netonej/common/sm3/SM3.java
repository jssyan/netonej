/*
 * Project: UniraSDK
 * 
 * @(#) SM3.java   14-6-26 下午3:15
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
 * @version $Revision $Date:14-6-26 下午3:15
 * @since 1.0
 */
class SM3 {
    public static final byte[] iv = new BigInteger("7380166f4914b2b9172442d7da8a0600a96f30bc163138aae38dee4db0fb0e4e", 16).toByteArray();
    public static int[] Tj = new int[64];

    static {
        for (int i = 0; i < 16; i++) {
            Tj[i] = 0x79cc4519;
        }
        for (int i = 16; i < 64; i++) {
            Tj[i] = 0x7a879d8a;
        }
    }

    public static byte[] CF(byte[] V, byte[] B) {
        int[] v, b;
        v = convert(V);
        b = convert(B);
       	return convert(CF(v, b));
    }

    private static int[] convert(byte[] arr) {
        int[] out = new int[arr.length / 4];
        byte[] tmp = new byte[4];
        for (int i = 0; i < arr.length; i += 4) {
            System.arraycopy(arr, i, tmp, 0, 4);
            out[i / 4] = bigEndianByteToInt(tmp);
        }

        return out;
    }

    private static byte[] convert(int[] arr) {
        byte[] out = new byte[arr.length * 4];
        byte[] tmp = null;
        for (int i = 0; i < arr.length; i++) {
            tmp = bigEndianIntToByte(arr[i]);
            System.arraycopy(tmp, 0, out, i * 4, 4);
        }

        return out;
    }

    public static int[] CF(int[] V, int[] B) {
        int a, b, c, d, e, f, g, h;
        int ss1, ss2, tt1, tt2;
        a = V[0];
        b = V[1];
        c = V[2];
        d = V[3];
        e = V[4];
        f = V[5];
        g = V[6];
        h = V[7];

        int[][] arr = expand(B);
        int[] w = arr[0];
        int[] w1 = arr[1];

        for (int j = 0; j < 64; j++) {
            ss1 = (bitCycleLeft(a, 12) + e + bitCycleLeft(Tj[j], j));
            ss1 = bitCycleLeft(ss1, 7);
            ss2 = ss1 ^ bitCycleLeft(a, 12);
            tt1 = FFj(a, b, c, j) + d + ss2 + w1[j];
            tt2 = GGj(e, f, g, j) + h + ss1 + w[j];
            d = c;
            c = bitCycleLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = bitCycleLeft(f, 19);
            f = e;
            e = P0(tt2);
        }

        int[] out = new int[8];
        out[0] = a ^ V[0];
        out[1] = b ^ V[1];
        out[2] = c ^ V[2];
        out[3] = d ^ V[3];
        out[4] = e ^ V[4];
        out[5] = f ^ V[5];
        out[6] = g ^ V[6];
        out[7] = h ^ V[7];

        return out;
    }

    private static int[][] expand(byte[] B) {
        int W[] = new int[68];
        int W1[] = new int[64];
        byte[] tmp = new byte[4];
        for (int i = 0; i < B.length; i += 4) {
            for (int j = 0; j < 4; j++) {
                tmp[j] = B[i + j];
            }
            W[i / 4] = bigEndianByteToInt(tmp);
        }

        for (int i = 16; i < 68; i++) {
            W[i] = P1(W[i - 16] ^ W[i - 9] ^ bitCycleLeft(W[i - 3], 15)) ^ bitCycleLeft(W[i - 13], 7) ^ W[i - 6];
        }

        for (int i = 0; i < 64; i++) {
            W1[i] = W[i] ^ W[i + 4];
        }

        return new int[][]{W, W1};
    }

    private static int[][] expand(int[] B) {
        int W[] = new int[68];
        int W1[] = new int[64];

        System.arraycopy(B, 0, W, 0, B.length);

        for (int i = 16; i < 68; i++) {
            W[i] = P1(W[i - 16] ^ W[i - 9] ^ bitCycleLeft(W[i - 3], 15)) ^ bitCycleLeft(W[i - 13], 7) ^ W[i - 6];
        }

        for (int i = 0; i < 64; i++) {
            W1[i] = W[i] ^ W[i + 4];
        }

        return new int[][]{W, W1};
    }

    private static byte[] bigEndianIntToByte(int num) {
        return back(SM2EllipticCurveParameters.IntToByte(num));
    }

    private static int bigEndianByteToInt(byte[] bytes) {
        return SM2EllipticCurveParameters.ByteToInt(back(bytes));
    }

    private static int FFj(int X, int Y, int Z, int j) {
        if (j >= 0 && j <= 15) {
            return FF1j(X, Y, Z);
        } else {
            return FF2j(X, Y, Z);
        }
    }

    private static int GGj(int X, int Y, int Z, int j) {
        if (j >= 0 && j <= 15) {
            return GG1j(X, Y, Z);
        } else {
            return GG2j(X, Y, Z);
        }
    }

    /**
     * *******************************************
     */
    // 逻辑位运算函数
    private static int FF1j(int X, int Y, int Z) {

        return X ^ Y ^ Z;
    }

    private static int FF2j(int X, int Y, int Z) {

        return ((X & Y) | (X & Z) | (Y & Z));
    }

    private static int GG1j(int X, int Y, int Z) {

        return X ^ Y ^ Z;
    }

    private static int GG2j(int X, int Y, int Z) {

        return (X & Y) | (~X & Z);
    }

    private static int P0(int X) {
//        int y = rotateLeft(X, 9);
        int y = bitCycleLeft(X, 9);
//        int z = rotateLeft(X, 17);
        int z = bitCycleLeft(X, 17);

        return X ^ y ^ z;
    }

    private static int P1(int X) {

        return X ^ bitCycleLeft(X, 15) ^ bitCycleLeft(X, 23);
    }

    /**
     * 对最后一个分组字节数据padding
     *
     * @param in
     * @param bLen 分组个数
     * @return
     */
    public static byte[] padding(byte[] in, int bLen) {
        //第一bit为1 所以长度=8 * in.length+1 k为所补的bit k+1/8 为需要补的字节
        int k = 448 - (8 * in.length + 1) % 512;
        if (k < 0) {
            k = 960 - (8 * in.length + 1) % 512;
        }
        k += 1;
        byte[] padd = new byte[k / 8];
        padd[0] = (byte) 0x80;
        long n = in.length * 8 + bLen * 512;
        //64/8 字节 长度
        //k/8 字节padding
        byte[] out = new byte[in.length + k / 8 + 64 / 8];
        int pos = 0;
        System.arraycopy(in, 0, out, 0, in.length);
        pos += in.length;
        System.arraycopy(padd, 0, out, pos, padd.length);
        pos += padd.length;
        byte[] tmp = back(SM2EllipticCurveParameters.LongToByte(n));
        System.arraycopy(tmp, 0, out, pos, tmp.length);

        return out;
    }

    /**
     * 字节数组逆序
     *
     * @param in byte[]
     * @return byte[]
     */
    private static byte[] back(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[out.length - i - 1];
        }

        return out;
    }


    public static int rotateLeft(int x, int n) {
        return (x << n) | (x >> (32 - n));
    }

    private static int bitCycleLeft(int n, int bitLen) {
//        bitLen %= 32;
//        byte[] tmp = bigEndianIntToByte(n);
//        int byteLen = bitLen / 8;
//        int len = bitLen % 8;
//        if (byteLen > 0) {
//            tmp = byteCycleLeft(tmp, byteLen);
//        }
//
//        if (len > 0) {
//            tmp = bitSmall8CycleLeft(tmp, len);
//        }
//
//        return bigEndianByteToInt(tmp);
    	
        //以上代码性能较差用以下代码替换
      return   ((n << bitLen) | (n >>> (32 - bitLen)));
    }

    private static byte[] bitSmall8CycleLeft(byte[] in, int len) {
        byte[] tmp = new byte[in.length];
        int t1, t2, t3;
        for (int i = 0; i < tmp.length; i++) {
            t1 = (byte) ((in[i] & 0x000000ff) << len);
            t2 = (byte) ((in[(i + 1) % tmp.length] & 0x000000ff) >> (8 - len));
            t3 = (byte) (t1 | t2);
            tmp[i] = (byte) t3;
        }


        return tmp;
    }

    private static byte[] byteCycleLeft(byte[] in, int byteLen) {
        byte[] tmp = new byte[in.length];
        System.arraycopy(in, byteLen, tmp, 0, in.length - byteLen);
        System.arraycopy(in, 0, tmp, in.length - byteLen, byteLen);

        return tmp;
    }
}
