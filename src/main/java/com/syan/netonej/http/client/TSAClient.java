/**
 * 文 件 名： TSAClient.java
 * 版    权：Jiangsu Syan Technology Co.,Ltd Copyright 2012 All Right Reserved
 * 修 改 人：gejq
 * 最后更新时间：2012-11-13
 */
package com.syan.netonej.http.client;

import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.tsa.TSACreateBuilder;
import com.syan.netonej.http.client.tsa.TSAVerifyBuilder;
import com.syan.netonej.http.entity.NetoneTSA;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

public class TSAClient {
    protected String host;

    protected String port = "9198";

    public TSAClient(String host) {
        this.host = host;
    }
    public TSAClient(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public TSACreateBuilder tsaCreateBuilder(){
        return new TSACreateBuilder().setHost(host).setPort(port);
    }

    public TSAVerifyBuilder tsaVerifyBuilder(){
        return new TSAVerifyBuilder().setHost(host).setPort(port);
    }

    @Deprecated
    public NetoneTSA createTimestamp(byte[] data, String algo, int datatype) throws NetonejException {
        TSACreateBuilder builder = tsaCreateBuilder();
        if(datatype==0){
            builder.setDataType(DataType.PLAIN);
        }else{
            builder.setDataType(DataType.DIGEST);
        }
        builder.setData(data);
        builder.setAlgo(algo);
        return builder.build();
    }

    @Deprecated
    public NetoneTSA createTimestamp(String data,String algo) throws NetonejException{
        return createTimestamp(Base64.decode(data),algo,DataType.PLAIN.ordinal());
    }
    @Deprecated
    public NetoneTSA createTimestamp(String data, DataType dataType, DigestAlgorithm algo) throws NetonejException{
        return createTimestamp(data,algo.getName(),dataType.ordinal());
    }

    @Deprecated
    public NetoneTSA createTimestamp(String data, String algo, int datatype) throws NetonejException {
        TSACreateBuilder builder = tsaCreateBuilder();
        if(datatype==0){
            builder.setDataType(DataType.PLAIN);
            builder.setData(Base64.decode(data));
        }else{
            builder.setDataType(DataType.DIGEST);
            builder.setData(Hex.decode(data));
        }
        builder.setAlgo(algo);
        return builder.build();
    }

    @Deprecated
    public NetoneTSA createTimestampTsrs(byte[] data) throws NetonejException{
        return tsaCreateBuilder()
                .setData(data)
                .setDataType(DataType.TIMESTAMP_REQUEST) //数据类型设置为时间戳请求类型
                .build();
    }

    /**
     * 验证时间戳签名
     * @param timestamp 时间戳签名数据
     * @return TSR实体对象类 {@link com.syan.netonej.http.entity.NetoneTSA}
     * @throws NetonejException API全局异常类
     */
    @Deprecated
    public NetoneTSA verifyTimestamp(String timestamp) throws NetonejException{
        return tsaVerifyBuilder()
                .setBase64Timestamp(timestamp)
                .build();
    }
    @Deprecated
    public NetoneTSA verifyTimestamp(String timestamp,String data) throws NetonejException{
        return tsaVerifyBuilder()
                .setData(Base64.decode(data))
                .setDataType(DataType.PLAIN)
                .setBase64Timestamp(timestamp)
                .build();
    }
    @Deprecated
    public NetoneTSA verifyTimestamp(String timestamp,String data,DataType dataType) throws NetonejException{
        TSAVerifyBuilder builder = tsaVerifyBuilder().setBase64Timestamp(timestamp);
        if(dataType == DataType.PLAIN){
            builder.setDataType(DataType.PLAIN);
            builder.setData(Base64.decode(data));
        }else if(dataType == DataType.DIGEST){
            builder.setDataType(DataType.DIGEST);
            builder.setData(Hex.decode(data));
        }else{
            throw new NetonejException("不支持的数据类型");
        }
        return builder.build();
    }
}
