/**
 * 文 件 名:  NetoneResponse.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.entity;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.exception.ErrorCode;

/**
 * 请求的返回对象
 *
 * @author liyb
 * @version 2.0.0
 * @since 2.0.0
 */
public class NetoneResponse {

    private ResponseFormat format = ResponseFormat.TEXT;
    /**
     * 状态码值
     */
    private int statusCode;

    /**
     * code非200的情况下，可能返回的消息,默认为空
     */
    private String message = "";

    /**
     * Netone服务器返回的数据
     */
    private byte[] result;

    public NetoneResponse() { }

    public NetoneResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public NetoneResponse(int statusCode, byte[] result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public NetoneResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public NetoneResponse(int statusCode, byte[] result,String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getBytesResult() {
        return result;
    }

    public String getResult() {
        if(result != null){
            return new String(result);
        }
        return null;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public void setResult(String result) {
        this.result = result.getBytes();
    }

    public ResponseFormat getFormat() {
        return format;
    }

    public void setFormat(ResponseFormat format) {
        this.format = format;
    }

    public String getStatusCodeMessage() {
        String emsg = ErrorCode.getStatusCodeMessage(statusCode);
        if (NetonejUtil.isEmpty(emsg)){
            return message;
        }
        return emsg;
    }
}
