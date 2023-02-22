/**
 * 文 件 名:  NetoneCertificate.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:  基类
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25
 */
package com.syan.netonej.common;


import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.entity.DigestAlgorithms;
import org.bouncycastle.jcajce.provider.digest.MD2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NetoneDigest {

    private String oid;
    private MessageDigest messageDigest;

    public NetoneDigest(String hashAlgorithm) throws NetonejException {
        this.oid = DigestAlgorithms.getAllowedDigest(hashAlgorithm);
        if(oid == null){
            throw new NetonejException("不支持的摘要算法:"+hashAlgorithm);
        }
        this.messageDigest = getMessageDigest(hashAlgorithm,oid);
    }

    public void update(byte b){
        messageDigest.update(b);
    }

    public void update(byte[] b){
        messageDigest.update(b);
    }

    public void update(ByteBuffer b){
        messageDigest.update(b);
    }

    public void update(byte[] b, int offset, int len){
        messageDigest.update(b,offset,len);
    }

    public void update(InputStream data) throws IOException {
        byte[] buf = new byte[8192];
        int n;
        while((n = data.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
    }

    public byte[] digest(){
        return messageDigest.digest();
    }

    @Deprecated
    public byte[] digest(byte[] b){
        return messageDigest.digest(b);
    }

    @Deprecated
    public int digest(byte[] b, int offset, int len) throws DigestException {
        return messageDigest.digest(b,offset,len);
    }

    //重置messageDigest
    public void reset(){
        messageDigest.reset();
    }

    //摘要结果长度
    public int getDigestLength(){
        return messageDigest.getDigestLength();
    }

    //摘要算法的OID
    public String getDigestOID(){
        return this.oid;
    }

    private MessageDigest getMessageDigest(String hashAlgorithm,String oid) throws NetonejException {
        byte var4 = -1;
        switch(oid.hashCode()) {
            case -2071451550:
                if (oid.equals("1.2.840.113549.2.2")) {
                    var4 = 0;
                }
                break;
            case -2071451547:
                if (oid.equals("1.2.840.113549.2.5")) {
                    var4 = 1;
                }
                break;
            case -1261045977:
                if (oid.equals("1.3.36.3.2.1")) {
                    var4 = 8;
                }
                break;
            case -1261045976:
                if (oid.equals("1.3.36.3.2.2")) {
                    var4 = 7;
                }
                break;
            case -1261045975:
                if (oid.equals("1.3.36.3.2.3")) {
                    var4 = 9;
                }
                break;
            case -1225949696:
                if (oid.equals("2.16.840.1.101.3.4.2.1")) {
                    var4 = 4;
                }
                break;
            case -1225949695:
                if (oid.equals("2.16.840.1.101.3.4.2.2")) {
                    var4 = 5;
                }
                break;
            case -1225949694:
                if (oid.equals("2.16.840.1.101.3.4.2.3")) {
                    var4 = 6;
                }
                break;
            case -1225949693:
                if (oid.equals("2.16.840.1.101.3.4.2.4")) {
                    var4 = 3;
                }
                break;
            case -308431282:
                if (oid.equals("1.3.14.3.2.26")) {
                    var4 = 2;
                }
                break;
            case 1044166351:
                if (oid.equals("1.2.643.2.2.9")) {
                    var4 = 10;
                }
                break;
            case 523583365:
                if (oid.equals("1.2.156.10197.1.401")) {
                    var4 = 11;
                }
        }

        switch(var4) {
            case 0:
                return new MD2.Digest();
            case 1:
                return new org.bouncycastle.jcajce.provider.digest.MD5.Digest();
            case 2:
                return new org.bouncycastle.jcajce.provider.digest.SHA1.Digest();
            case 3:
                return new org.bouncycastle.jcajce.provider.digest.SHA224.Digest();
            case 4:
                return new org.bouncycastle.jcajce.provider.digest.SHA256.Digest();
            case 5:
                return new org.bouncycastle.jcajce.provider.digest.SHA384.Digest();
            case 6:
                return new org.bouncycastle.jcajce.provider.digest.SHA512.Digest();
            case 7:
                return new org.bouncycastle.jcajce.provider.digest.RIPEMD128.Digest();
            case 8:
                return new org.bouncycastle.jcajce.provider.digest.RIPEMD160.Digest();
            case 9:
                return new org.bouncycastle.jcajce.provider.digest.RIPEMD256.Digest();
            case 10:
                return new org.bouncycastle.jcajce.provider.digest.GOST3411.Digest();
            case 11:
                return new org.bouncycastle.jcajce.provider.digest.SM3.Digest();
            default:
                throw new NetonejException("不支持的摘要算法:"+hashAlgorithm);
        }
    }

    public static void main(String[] args) {
        byte[] ss = "123".getBytes();
        try {
            NetoneDigest digest = new NetoneDigest("SM3");
            digest.update(ss);
            digest.digest();
            System.out.println(digest.getDigestOID());
        } catch (NetonejException e) {
            e.printStackTrace();
        }
    }
}
