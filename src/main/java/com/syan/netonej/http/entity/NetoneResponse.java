/**
 * 文 件 名:  NetoneResponse.java
 * 版    权:  Jiangsu Syan Technology Co.,Ltd Copyright 2013 All Right Reserved
 * 描    述:
 * 修 改 人:  liyb
 * 修改时间:  2013-04-25 Apr 29, 2013
 */
package com.syan.netonej.http.entity;

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
     * 请求返回的字符串
     */
    private String retString;


    /**
     * @return 状态码值
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode 状态码值
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return 请求返回的字符串
     */
    public String getRetString() {
        return retString;
    }

    /**
     * @param retString 请求返回的字符串
     */
    public void setRetString(String retString) {
        this.retString = retString;
    }

    /**
     * toString
     *
     * @return
     * @since 3.0.10
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("NetoneResponse{");
        builder.append("statusCode=").append(statusCode);
        builder.append(",retString=").append(retString).append("}");

        return builder.toString();
    }
}
