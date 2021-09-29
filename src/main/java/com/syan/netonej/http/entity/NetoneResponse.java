/**
 * 文 件 名:  NetoneResponse.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.entity;

import java.util.Base64;

/**
 * 请求的返回对象
 *
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneResponse {
    /**
     * 状态码值
     */
    private int statusCode;

    /**
     * Netone服务器返回的数据
     */
    private byte[] retBytes;


    public NetoneResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public NetoneResponse(int statusCode, byte[] retBytes) {
        this.statusCode = statusCode;
        this.retBytes = retBytes;
    }

    public byte[] getRetBytes() {
        return retBytes;
    }

    /**
     * @return 状态码值
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return 请求返回的字符串
     */
    public String getRetString() {
        if(retBytes == null){
            return null;
        }
        return new String(retBytes);
    }


    public void setRetString(String str) {
        this.retBytes = Base64.getDecoder().decode(str);
    }

}
