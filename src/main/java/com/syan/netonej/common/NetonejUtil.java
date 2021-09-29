/**
 * 文 件 名:  NetonejUtil.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人:  gejq
 * 修改时间:  2012-6-30
 */
package com.syan.netonej.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.util.encoders.Base64;


/**
 * NetONEJ API 工具类
 *
 * @author gejq
 * @version 2.0.0
 * @since 1.0.0
 */
public class NetonejUtil {

    private static DateFormat dateFormat;

    /**
     * 将字符串变为十六进制的数字字符串
     *
     * @param bytes 原文byte数组
     * @return 十六进制数字字符串
     */
    public static String byte2HexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }

    /**
     * 将字符串变为十六进制的数字字符串
     *
     * @param data 原文字符串
     * @return 十六进制数字字符串
     */
    public static String string2HexString(String data) {
        return byte2HexString(data.getBytes());
    }

    /**
     * 将十六进制的数字字符串还原为字符串
     *
     * @param hex_string 十六进制数字字符串
     * @return 原文字符串
     */
    public static String hexString2String(String hex_string) {
        StringBuffer char_string = new StringBuffer("");
        while (hex_string != null && hex_string.length() >= 2) {
            try {
                char_string.append((char) Byte.parseByte(hex_string.substring(0, 2), 16));
                hex_string = hex_string.substring(2);
            } catch (NumberFormatException e) {
//                log.error("HexString2StringError.NumberFormatException", e);
            }
        }
        return (new String(char_string));
    }

    /**
     * 使用MD5算法将字符串进行摘要（哈希、散列）
     *
     * @param data 原文字符串
     * @return MD5字符串
     */
    public static String md5Encode(String data) {
        return md5Encode(data.getBytes());
    }

    /**
     * 使用MD5算法将字符串进行摘要（哈希、散列）
     *
     * @param data 原文byte[]
     * @return MD5字符串
     */
    public static String md5Encode(byte[] data) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(data);
        } catch (NoSuchAlgorithmException e) {
            //log.error("md5EncodeString.NoSuchAlgorithmException", e);
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toUpperCase();
    }


    /**
     * 使用SHA1算法将字符串进行摘要（哈希、散列）
     *
     * @param data 原文byte[]
     * @return SHA1字符串
     */
    public static String sha1Encode(String data) {
        return sha1Encode(data.getBytes());
    }

    /**
     * 使用SHA1算法将字符串进行摘要（哈希、散列）
     *
     * @param _mess
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] digestBinary(byte[] _mess) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(_mess);
        return md.digest();
    }

    /**
     * 使用SHA1或MD5算法将字符串进行摘要（哈希、散列）
     *
     * @param _mess
     * @param _algorithm 算法
     * @return 二进制摘要
     * @throws NoSuchAlgorithmException
     */
    public static byte[] digestBinary(byte[] _mess, String _algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(_algorithm);
        md.update(_mess);
        return md.digest();
    }

    /**
     * 使用SHA1算法将字符串进行摘要（哈希、散列）
     *
     * @param data 原文字符串
     * @return SHA1字符串
     */
    public static String sha1Encode(byte[] data) {
        String resultString = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            resultString = byte2HexString(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            //log.error("sha1EncodeString.NoSuchAlgorithmException", e);
        }

        return resultString.toUpperCase();
    }
    
    /**
     * 使用SHA1 或 SHA256算法将字符串进行摘要（哈希、散列）
     *
     * @param data 原文字符串
     * @return SHA1字符串
     */
    
    public static String digestString(String data, String _algorithm) throws NoSuchAlgorithmException {
        String resultString = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(_algorithm);
            resultString = byte2HexString(md.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            //log.error("sha1EncodeString.NoSuchAlgorithmException", e);
        }

        return resultString.toUpperCase();
    }
    /**
     * 使用SHA1 或 SHA256算法将字符串进行摘要（哈希、散列）
     *
     * @param data 原文字符串
     * @return SHA1字符串
     */
    
    public static String digestByte(byte[] data, String _algorithm) throws NoSuchAlgorithmException {
        String resultString = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(_algorithm);
            resultString = byte2HexString(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            //log.error("sha1EncodeString.NoSuchAlgorithmException", e);
        }

        return resultString.toUpperCase();
    }

    /**
     * 将byte[]进行Base64编码并返回字符串
     *
     * @param data 待编码的byte[]
     * @return 编码后的字符串
     */
    public static String base64Encode(byte[] data) {
        return new String(Base64.encode(data));
    }


    /**
     * 将Date对象格式化后返回字符串
     *
     * @param date   java.util.Date Object
     * @param format 格式，缺省为：yyyy-MM-dd hh:mm:ss
     * @return 格式化后的时间字符串
     */
    public static String dateFormat(Date date, String format) {
        if (isEmpty(format) || format == null) {
            format = "yyyy-MM-dd hh:mm:ss";
        } else {
            dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        }
        return null;
    }

    /**
     * 字符串非空判断
     *
     * @param string 要判断是否为空的字符串
     * @return 判断结果
     */
    public static boolean isEmpty(String string) {
        if (string == null || "".equals(string) || string.trim().length() == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static Map getMapFromDN(String dn) {
        List strs = split(dn);
        Map map = new HashMap();
        for (int i = 0; i < strs.size(); i++) {
            String item = (String) strs.get(i);
            String[] s = item.split("=");
            map.put(s[0], s[1]);
        }
        return map;
    }

    public static List split(String dn) {
        List items = new ArrayList();
        int index = dn.indexOf(",");
        int start = 0;
        String sub = "";
        while (index < dn.length()) {
            sub = dn.substring(start, index);
            if (sub.endsWith("\\")) {
                index = dn.indexOf(",", index + 1);
                sub = dn.substring(start, index);
            }
            items.add(sub);
            start = index + 1;
            index = dn.indexOf(",", start);
            if (index == -1) break;
        }
        sub = dn.substring(start);
        items.add(sub);
        return items;
    }


    public static String pemToBase64String(String pemString) {
        pemString = pemString.replaceAll("-----BEGIN CERTIFICATE-----", "");
        pemString = pemString.replaceAll("-----END CERTIFICATE-----", "");
        pemString = pemString.replaceAll("\r\n", "");
        pemString = pemString.replaceAll("\n", "");
        return pemString;
    }

    public static String toPemString(String certBase64String) throws IOException {
        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write("-----BEGIN CERTIFICATE-----");
        bw.newLine();
        byte[] bytes = certBase64String.getBytes();
        char[] buf = new char[64];

        for (int i = 0; i < bytes.length; i += buf.length) {
            int index = 0;

            while (index != buf.length) {

                if ((i + index) >= bytes.length) {
                    break;
                }

                buf[index] = (char) bytes[i + index];
                index++;
            }

            bw.write(buf, 0, index);
            bw.newLine();
        }

        bw.write("-----END CERTIFICATE-----");
        bw.newLine();
        bw.close();
        return sw.toString();

    }
    
	 public static X500Name canonicalX500Name(X500Name oldX500Name) {

	        RDN[] newRDNs = transferDERT61String2DERUTF8String(oldX500Name.getRDNs());

	        return new X500Name(newRDNs);
	}

    private static RDN[] transferDERT61String2DERUTF8String(RDN[] oldRDNs) {

        RDN[] newRDNs = new RDN[oldRDNs.length];

        AttributeTypeAndValue attributeTypeAndValue = null;

        ASN1Encodable t61Value = null;
        ASN1Encodable utf8Value = null;
        int index = 0;
        for (RDN old : oldRDNs) {

            attributeTypeAndValue = old.getFirst();

            t61Value = attributeTypeAndValue.getValue();

            if (t61Value instanceof DERT61String) {
                try {
                    utf8Value = new DERUTF8String(new String(((DERT61String) t61Value).getOctets(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                attributeTypeAndValue = new AttributeTypeAndValue(attributeTypeAndValue.getType(), utf8Value);

                newRDNs[index] = new RDN(attributeTypeAndValue);
            } else {
                newRDNs[index] = old;
            }

            index++;
        }

        return newRDNs;
    }



    public static String getCNFromSubject(String subject){
        if(subject == null || !subject.contains("CN=")){
            return "";
        }
        return subject.split("CN=")[1];
    }

}
