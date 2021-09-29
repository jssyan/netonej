package com.syan.netonej.common.dict;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:12
 * @Description
 */
public enum  KeyUseage {

    SIGN(1),
    ENCRYPT(2);

    private int value;

    KeyUseage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
