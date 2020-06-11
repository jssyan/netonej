/*
 * Project: UniraSDK
 * 
 * @(#) SM2PublicKey.java   14-6-25 上午9:07
 *
 * Copyright 2013 Jiangsu Syan Technology Co.,Ltd. All rights reserved.
 * Jiangsu Syan PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.syan.netonej.common.key;

import org.spongycastle.jce.interfaces.ECPublicKey;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.InvalidKeyException;


/**
 * <p>
 * This class provides...
 * </p>
 *
 * @author Iceberg
 * @version $Revision $Date:14-6-25 上午9:07
 * @since 1.0
 */
public class SM2BCPublicKey implements ECPublicKey {

    private ECPoint ecPointQ = null;
    private boolean withCompression = false;// 是否对公钥进行压缩，默认不压缩，此字段将来可能有用
    private static ECParameterSpec params = null;
    private static ECCurve ecc_curve;

    static {
        BigInteger p = new BigInteger(
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16);// 曲线参数p
        BigInteger a = new BigInteger(
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);// 曲线参数a
        BigInteger b = new BigInteger(
                "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);// 曲线参数b
        BigInteger Gx = new BigInteger(
                "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);// G点x坐标
        BigInteger Gy = new BigInteger(
                "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);// G点y坐标
        BigInteger n = new BigInteger(
                "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);

        final BigInteger h = BigInteger.valueOf(1L);
        //
        ECFieldElement ecc_gx_fieldelement = new ECFieldElement.Fp(p, Gx);
        ECFieldElement ecc_gy_fieldelement = new ECFieldElement.Fp(p, Gy);

        // 构造椭圆曲线
        ecc_curve = new ECCurve.Fp(p, a, b);
        // 构造G点
        ECPoint ecc_point_g = new ECPoint.Fp(ecc_curve, ecc_gx_fieldelement, ecc_gy_fieldelement);
        // 生成ECParameterSpec
        params = new ECParameterSpec(ecc_curve, ecc_point_g, n, h);
    }

    /**
     * 根据公钥点构造公钥
     *
     * @param ecPoint 公钥点
     */
    public SM2BCPublicKey(ECPoint ecPoint) {

        this.ecPointQ = ecPoint;
    }

    /**
     * 根据公钥的encode构造公钥
     *
     * @param publicKey 公钥
     * @throws java.security.InvalidKeyException
     *
     */
    public SM2BCPublicKey(byte[] publicKey) throws InvalidKeyException {
        // 构造公钥点
        byte[] keyBytes = null;

        if (publicKey[0] == 0) {
            keyBytes = new byte[publicKey.length - 1];
            System.arraycopy(publicKey, 1, keyBytes, 0, publicKey.length - 1);
        } else {
            keyBytes = publicKey;
        }

        if (keyBytes.length == 65) {// 如果未压缩，第一位应该是0x04
            this.withCompression = false;
            byte[] X = new byte[32];
            byte[] Y = new byte[32];

            // 将内存中的1到32位存入X点
            System.arraycopy(keyBytes, 1, X, 0, 32);
            // 将内存中的33到64位存入Y点
            System.arraycopy(keyBytes, 33, Y, 0, 32);

            // 将X,Y转换成BigInteger
            BigInteger bigX = new BigInteger(1, X);
            BigInteger bigY = new BigInteger(1, Y);
            // 生成SM2曲线上的公钥点
//            ecPointQ = ecc_curve.createPoint(bigX, bigY);
            BigInteger p = new BigInteger(
                    "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
                    16);// 曲线参数p


            ECFieldElement ex = new ECFieldElement.Fp(p, bigX);
            ECFieldElement ey = new ECFieldElement.Fp(p, bigY);

            ecPointQ = new ECPoint.Fp(ecc_curve, ex, ey);
        } else if (keyBytes.length == 33) {
            this.withCompression = true;
            byte[] X = new byte[32];
            byte[] Y = new byte[1];

            // 将内存中的1到32位存入X点
            System.arraycopy(keyBytes, 1, X, 0, 32);
            System.arraycopy(keyBytes, 0, Y, 0, 1);
            // 将X,Y转换成BigInteger
            BigInteger bigX = new BigInteger(X);
            BigInteger bigY = new BigInteger(Y);

            // 生成SM2曲线上的公钥点
            ecPointQ = ecc_curve.createPoint(bigX, bigY);
        } else {
            throw new InvalidKeyException();
        }
    }

    /**
     * 获取算法
     *
     * @return String 算法
     */
    public String getAlgorithm() {

        return "SM2";
    }

    /**
     * 获取公钥的encode值，将公钥中的bits和X,Y坐标有效的部分拼接成68位的byte
     *
     * @return byte[] 获取公钥的encode值
     */
    public byte[] getEncoded() {

        if (this.withCompression) {
            byte PC;

            if (this.ecPointQ.getAffineYCoord().toBigInteger().testBit(0)) {
                PC = 0x03;
            } else {
                PC = 0x02;
            }

            byte[] X = this.ecPointQ.getAffineXCoord().toBigInteger().toByteArray();
            byte[] PO = new byte[X.length + 1];

            PO[0] = PC;
            System.arraycopy(X, 0, PO, 1, X.length);

            return PO;

        } else {
            // byte[] X = this.ecPointW.getAffineX().toByteArray();
            // byte[] Y = this.ecPointW.getAffineY().toByteArray();
            // 对X和Y做判断，因为BigInteger需要标识是否为负数，所以不能直接使用toByteArray()
            byte[] X = getExactBigIntegerBytes(this.ecPointQ.getAffineXCoord().toBigInteger());
            byte[] Y = getExactBigIntegerBytes(this.ecPointQ.getAffineYCoord().toBigInteger());

            if (X.length == 31) {
                byte[] X1 = new byte[X.length + 1];
                System.arraycopy(X, 0, X1, 1, X.length);
                X = X1;
            }

            if (Y.length == 31) {
                byte[] Y1 = new byte[Y.length + 1];
                System.arraycopy(X, 0, Y1, 1, Y.length);
                Y = Y1;
            }

            byte[] PO = new byte[X.length + Y.length + 1];
            PO[0] = 0x04;
            System.arraycopy(X, 0, PO, 1, X.length);
            System.arraycopy(Y, 0, PO, X.length + 1, Y.length);

            return PO;
        }
    }

    /**
     * 对BigInteger 取真正的值bytes，By Wangjx 2011-10-28 17:40
     *
     * @param big BigInteger
     * @return byte[]
     */
    private byte[] getExactBigIntegerBytes(BigInteger big) {
        byte[] vValue = big.toByteArray();
        byte[] rValue = null;

        // 这里的处理很关键，如果大数的byte不是32，必须在前面补-1，如果是32，则不需要补-1,By Wangjx 2011-10-41
        // 10:00
        if (-1 == big.signum() && vValue.length == 31) {
            rValue = new byte[vValue.length + 1];
            rValue[0] = -1;
            System.arraycopy(vValue, 0, rValue, 1, vValue.length);
        } else {
            rValue = vValue;
        }

        return rValue;
    }

    public String getFormat() {
        return "X.509";
    }

    /**
     * 获取ECC公钥构造的域信息，包含椭圆曲线，G点
     *
     * @return ECParameterSpec ECC的构造域信息
     */
    public ECParameterSpec getParams() {

        return params;
    }


    public ECPoint getQ() {
        return this.ecPointQ;
    }


    public ECParameterSpec getParameters() {
        return params;
    }
}
