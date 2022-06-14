package com.syan.netonej.common.dict;

/**
 * @Author mmdet
 * @Date 2021-08-13 17:04
 * @Description 待签名数据的类型. 0表示是原文, 1表示摘要(摘要数据是binary格式)
 */
public enum DataType {
    PLAIN,//表示是原文数据
    DIGEST,//摘要数据
    TIMESTAMP_REQUEST//der编码格式的时间戳请求,用于签发时间戳
}
