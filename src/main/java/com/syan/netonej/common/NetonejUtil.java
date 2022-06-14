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
     * 将Date对象格式化后返回字符串
     *
     * @param date   java.util.Date Object
     * @param format 格式，缺省为：yyyy-MM-dd hh:mm:ss
     * @return 格式化后的时间字符串
     */
    public static String dateFormat(Date date, String format) {
        SimpleDateFormat dateFormat;
        if (isEmpty(format)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat(format);
        }
        return dateFormat.format(date);
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

    public static String pemStringToBase64(String pemString) {
        pemString = pemString.replaceAll("-----BEGIN CERTIFICATE-----", "");
        pemString = pemString.replaceAll("-----END CERTIFICATE-----", "");
        pemString = pemString.replaceAll("\r\n", "");
        pemString = pemString.replaceAll("\n", "");
        return pemString;
    }

    public static String base64StringtoPem(String certBase64String) throws IOException{
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

    public static String getCNFromSubject(String subject){
        if(subject == null || !subject.contains("CN=")){
            return "";
        }
        return subject.split("CN=")[1];
    }

}
