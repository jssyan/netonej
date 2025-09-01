package com.syan.netonej.common.dict;


public class EngineAction {

    /**
     * ENGINE - Action 随机数
     */
    public final static String ENGINE_ACTION_RANDOM = "random";

    /**
     * ENGINE - Action 对称加密
     */
    public final static String ENGINE_ACTION_ENCRYPT = "encrypt";

    /**
     * ENGINE - Action 对称解密
     */
    public final static String ENGINE_ACTION_DECRYPT = "decrypt";

    /**
     * ENGINE - Action
     */
    public final static String ENGINE_ACTION_HMAC = "hmac";

    /**
     * ENGINE - Action 密钥派生
     */
    public final static String ENGINE_ACTION_CMAC = "cmac";

    /**
     * ENGINE - Action 签名
     */
    public final static String ENGINE_ACTION_SIGN = "sign";
    /**
     * ENGINE - Action 验签
     */
    public final static String ENGINE_ACTION_VERIFY = "verify";

    /**
     * ENGINE - Action 非对称加密
     */
    public final static String ENGINE_ACTION_ASYENC = "asymmetricEncrypt";
    /**
     * ENGINE - Action 非对称解密
     */
    public final static String ENGINE_ACTION_ASYDEC = "asymmetricDecrypt";

    /**
     * ENGINE - Action  导出公钥
     */
    public final static String ENGINE_ACTION_PUB = "getPublickey";

}
