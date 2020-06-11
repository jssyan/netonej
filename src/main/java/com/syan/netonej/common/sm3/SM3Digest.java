package com.syan.netonej.common.sm3;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import com.syan.netonej.common.Conventer;

public class SM3Digest {
    private static final int BYTE_LENGTH = 32;
    /**
     * SM3值的长度
     */
    private static final int BLOCK_LENGTH = 64;
    /**
     * SM3分组长度
     */
    private static final int BUFFER_LENGTH = BLOCK_LENGTH * 1;
    /**
     * 缓冲区长度
     */
    private byte[] xBuf = new byte[BUFFER_LENGTH];
    /**
     * 缓冲区
     */
    private int xBufOff;
    /**
     * 缓冲区偏移量
     */
    private byte[] V = (byte[]) SM3.iv.clone();
    /**
     * 初始向量
     */
    private int cntBlock = 0;

    private byte[] userID = {0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38};

    public SM3Digest() {

    }

    /**
     * SM3结果输出
     *
     * @param out    保存SM3结构的缓冲区
     * @param outOff 缓冲区偏移量
     * @return
     */
    public int doFinal(byte[] out, int outOff) {
        byte[] tmp = doFinal();
        System.arraycopy(tmp, 0, out, 0, tmp.length);

        return BYTE_LENGTH;
    }


    public void reset() {
        xBufOff = 0;
        cntBlock = 0;
        V = (byte[]) SM3.iv.clone();
    }

    /**
     * 明文输入
     *
     * @param in    明文输入缓冲区
     * @param inOff 缓冲区偏移量
     * @param len   明文长度
     */
    public void update(byte[] in, int inOff, int len) {
        int partLen = BUFFER_LENGTH - xBufOff;
        int inputLen = len;
        int dPos = inOff;
        if (partLen < inputLen) {
            System.arraycopy(in, dPos, xBuf, xBufOff, partLen);
            inputLen -= partLen;
            dPos += partLen;
            doUpdate();
            long time2=System.currentTimeMillis();
            while (inputLen > BUFFER_LENGTH) {
                System.arraycopy(in, dPos, xBuf, 0, BUFFER_LENGTH);
                inputLen -= BUFFER_LENGTH;
                dPos += BUFFER_LENGTH;        
                doUpdate();              
            }
   
        }

        System.arraycopy(in, dPos, xBuf, xBufOff, inputLen);
        xBufOff += inputLen;
    }

    private void doUpdate() {
        byte[] B = new byte[BLOCK_LENGTH];
        for (int i = 0; i < BUFFER_LENGTH; i += BLOCK_LENGTH) {
            System.arraycopy(xBuf, i, B, 0, B.length);
            doHash(B);
        }
        xBufOff = 0;
    }

    private void doHash(byte[] B) {
    
        byte[] tmp = SM3.CF(V, B);
      
        System.arraycopy(tmp, 0, V, 0, V.length);
        cntBlock++;
   
    }

    private byte[] doFinal() {
        byte[] B = new byte[BLOCK_LENGTH];
        byte[] buffer = new byte[xBufOff];
        System.arraycopy(xBuf, 0, buffer, 0, buffer.length);
        byte[] tmp = SM3.padding(buffer, cntBlock);
        for (int i = 0; i < tmp.length; i += BLOCK_LENGTH) {
            System.arraycopy(tmp, i, B, 0, B.length);
            doHash(B);
        }

        return V;
    }

    private byte[] getSM2Za(byte[] x, byte[] y, byte[] id) {
        byte[] tmp = SM2EllipticCurveParameters.IntToByte(id.length * 8);
        byte[] buffer = new byte[32 * 6 + 2 + id.length];
        buffer[0] = tmp[1];
        buffer[1] = tmp[0];
        byte[] a = SM2EllipticCurveParameters.getA();
        byte[] b = SM2EllipticCurveParameters.getB();
        byte[] gx = SM2EllipticCurveParameters.getGx();
        byte[] gy = SM2EllipticCurveParameters.getGy();
        int dPos = 2;
        System.arraycopy(id, 0, buffer, dPos, id.length);
        dPos += id.length;
        System.arraycopy(a, 0, buffer, dPos, 32);
        dPos += 32;
        System.arraycopy(b, 0, buffer, dPos, 32);
        dPos += 32;
        System.arraycopy(gx, 0, buffer, dPos, 32);
        dPos += 32;
        System.arraycopy(gy, 0, buffer, dPos, 32);
        dPos += 32;
        System.arraycopy(x, 0, buffer, dPos, 32);
        dPos += 32;
        System.arraycopy(y, 0, buffer, dPos, 32);
        dPos += 32;

        SM3Digest digest = new SM3Digest();
        digest.update(buffer, 0, buffer.length);
        byte[] out = new byte[32];
        digest.doFinal(out, 0);

        return out;
    }

    /**
     * 设置userID
     *
     * @param affineX SM2公钥仿射坐标X
     * @param affineY SM2公钥仿射坐标Y
     */
    public void addId(BigInteger affineX, BigInteger affineY) {
        addId(affineX, affineY, this.userID);
    }

    /**
     * 设置userID
     *
     * @param affineX SM2公钥仿射坐标X
     * @param affineY SM2公钥仿射坐标Y
     * @param userID  userID
     */
    public void addId(BigInteger affineX, BigInteger affineY, byte[] userID) {
        this.userID = userID;
        byte[] x = SM2EllipticCurveParameters.asUnsigned32ByteArray(affineX);
        byte[] y = SM2EllipticCurveParameters.asUnsigned32ByteArray(affineY);
        byte[] tmp = getSM2Za(x, y, this.userID);

        reset();
        System.arraycopy(tmp, 0, xBuf, xBufOff, 32);
        xBufOff = 32;
    }

    public String getAlgorithmName() {
        return "SM3";
    }

    public int getDigestSize() {
        return BYTE_LENGTH;
    }


    public void update(byte in) {
        byte[] buffer = new byte[]{in};
        update(buffer, 0, 1);
    }
}

