package com.syan.netonej.common.dict;

/**
 * @Author mmdet
 * @Date 2021-08-13 17:04
 * @Description 待签名数据的类型. 0表示是原文, 1表示摘要(摘要数据是binary格式),
 * 2表示摘要(摘要数据是hex string格式) hex string格式主要是为了配合SignerX控件使用, 因为SignerX摘要的返回是hex string
 */
public enum DataType {
    PLAIN,
    DIGEST,
    DIGESTHEX
}
