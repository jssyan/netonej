package com.syan.netonej.common;


import java.io.IOException;
import java.math.BigInteger;

import org.spongycastle.crypto.digests.GeneralDigest;
import org.spongycastle.util.Memoable;
import org.spongycastle.util.Pack;
import org.spongycastle.util.encoders.Base64;

import com.syan.netonej.common.sm3.SM2EllipticCurveParameters;




/**
 * implementation of SM3.
 *
 * <pre>
 *      block  word  digest
 * SM3  512    32    256
 * </pre>
 */
public class SM3Digest
extends GeneralDigest
{
private static final int    DIGEST_LENGTH = 32;

private int     H1, H2, H3, H4, H5, H6, H7, H8;

private int[]   X = new int[64];
private int     xOff;

//扩展后的消息分组
private int[]   W = new int[68];
private int[]   W1 = new int[64];

private byte[] idKeyHash; //用保存公钥hash
/**
 * Standard constructor
 */
public SM3Digest()
{
    reset();
}

/**
 * Copy constructor.  This will copy the state of the provided
 * message digest.
 */
public SM3Digest(SM3Digest t)
{
    super(t);

    H1 = t.H1;
    H2 = t.H2;
    H3 = t.H3;
    H4 = t.H4;
    H5 = t.H5;
    H6 = t.H6;
    H7 = t.H7;
    H8 = t.H8;

    System.arraycopy(t.X, 0, X, 0, t.X.length);
    xOff = t.xOff;
}

public String getAlgorithmName()
{
    return "SM3";
}

public int getDigestSize()
{
    return DIGEST_LENGTH;
}

protected void processWord(
    byte[]  in,
    int     inOff)
{
    // Note: Inlined for performance
//    X[xOff] = Pack.bigEndianToInt(in, inOff);
    int n = in[inOff] << 24;
    n |= (in[++inOff] & 0xff) << 16;
    n |= (in[++inOff] & 0xff) << 8;
    n |= (in[++inOff] & 0xff);
    X[xOff] = n;

    if (++xOff == 16)
    {
        processBlock();
    }
}

protected void processLength(
    long    bitLength)
{
    if (xOff > 14)
    {
        processBlock();
    }

    X[14] = (int)(bitLength >>> 32);
    X[15] = (int)(bitLength & 0xffffffff);
}

public int doFinal(
    byte[]  out,
    int     outOff)
{
    finish();

    Pack.intToBigEndian(H1, out, outOff);
    Pack.intToBigEndian(H2, out, outOff + 4);
    Pack.intToBigEndian(H3, out, outOff + 8);
    Pack.intToBigEndian(H4, out, outOff + 12);
    Pack.intToBigEndian(H5, out, outOff + 16);
    Pack.intToBigEndian(H6, out, outOff + 20);
    Pack.intToBigEndian(H7, out, outOff + 24);
    Pack.intToBigEndian(H8, out, outOff + 28);

    reset();

    return DIGEST_LENGTH;
}

/**
 * reset the chaining variables
 */
public void reset()
{
    super.reset();

    /* SM3 initial hash value
     * The first 32 bits of the fractional parts of the square roots
     * of the first eight prime numbers
     */

    H1 = 0x7380166f;
    H2 = 0x4914b2b9;
    H3 = 0x172442d7;
    H4 = 0xda8a0600;
    H5 = 0xa96f30bc;
    H6 = 0x163138aa;
    H7 = 0xe38dee4d;
    H8 = 0xb0fb0e4e;

    xOff = 0;
    for (int i = 0; i != X.length; i++)
    {
        X[i] = 0;
    }
}

protected void processBlock()
{
	//扩展函数
	int j=0;
	for(j=0;j<16;j++)
		W[j] = X[j];
    for(j=16;j<=67;j++)
    {
    	W[j] = P1(W[j-16] ^ W[j-9] ^ LShift(W[j-3] , 15))
    			^LShift(W[j-13],7)
    			^W[j-6];
    }
    for(j=0;j<=63;j++)
    {
    	W1[j] = W[j] ^ W[j+4];
    }

    //
    // set up working variables.
    //
    int     a = H1;
    int     b = H2;
    int     c = H3;
    int     d = H4;
    int     e = H5;
    int     f = H6;
    int     g = H7;
    int     h = H8;

//    System.out.printf("    %X\n",a);
    //压缩函数
    int SS1,SS2,TT1,TT2;
    int Tj = 0x79cc4519; //0 =< j <= 15
    for(j = 0; j < 64; j ++)
    {
    	if(j >= 16) Tj = 0x7a879d8a;
    	
        // SS1 <- ((A<<<12) + E + (Tj<<<j))<<<7
    	
    	SS1 = LShift(a,12) + e + LShift(Tj,j);
    	SS1 = LShift(SS1,7);
    	
    	//SS2 <- SS1 ^ (A<<<12)
    	SS2 = SS1 ^ LShift(a,12);
    	
    	//TT1 <- FFj(A,B,C) + D + SS2 +W′j
    	TT1 = FF(a,b,c,j) + d + SS2 + W1[j];

    	//TT2 <- GGj(E,F,G) + H + SS1 +Wj
    	TT2 = GG(e,f,g,j) + h + SS1 + W[j];
    	
    	d = c;
    	c = LShift(b,9);
    	b = a;
    	a = TT1;
    	h = g;
    	g = LShift(f,19);
    	f = e;
    	e = P0(TT2);

//    	System.out.printf("j=%d %X\n",j,a);
    }

    H1 ^= a;
    H2 ^= b;
    H3 ^= c;
    H4 ^= d;
    H5 ^= e;
    H6 ^= f;
    H7 ^= g;
    H8 ^= h;

    //
    // reset the offset and clean out the word buffer.
    //
    xOff = 0;
    for (int i = 0; i < 16; i++)
    {
        X[i] = 0;
    }
}

/* SM3 functions */
//<<<
private int LShift(int x,int bit)
{
	return x<<bit|x>>>(32-bit);
}

private int P0(int x)
{//P0(X) = X ^ (X<<<9) ^ (X<<<17)
	return x ^ (x<<9|x>>>23) ^ (x<<17|x>>>15);
}

private int P1(int x)
{//P1(X) = X ^ (X<<<15) ^ (X<<<23)
	return x ^ (x<<15|x>>>17) ^ (x<<23|x>>>9);
}

private int FF(int x,int y,int z,int j)
{
	if(j <= 15)
		return x ^ y ^ z;
	else
		return (x&y)|(x&z)|(y&z);
}
private int GG(int x,int y,int z,int j)
{
	if(j <= 15)
		return x ^ y ^ z;
	else
		return (x&y)|(~x&z);
}
    
    public void addIdAndPubKey(byte[] pkey,byte[] id){    	

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
          System.arraycopy(pkey, 0, buffer, dPos, 64);


          SM3Digest digest = new SM3Digest();
          digest.update(buffer, 0, buffer.length);     
      	  idKeyHash = new byte[32];
          digest.doFinal(idKeyHash, 0);
    
    }
    
	public void update(byte[] in, int inOff, int len) {
		if(idKeyHash!=null){
			byte[] totalData=new byte[in.length+idKeyHash.length];
			System.arraycopy(idKeyHash, 0, totalData, 0, 32);
			System.arraycopy(in, 0, totalData, 32, in.length);			
			super.update(totalData, 0, totalData.length);
			idKeyHash=null;
		}else{
			super.update(in, inOff, len);
		}
	}


	public Memoable copy() {	
		return null;
	}

	@Override
	public void reset(Memoable arg0) {
		// TODO Auto-generated method stub
		
	}

}

