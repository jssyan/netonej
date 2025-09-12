package com.syan.netonej.common.dict;


public class CosignAction {

    /**
     * CA - Action 证书申请
     */
    public final static String COSIGN_ACTION_ISSUE = "cosign/api/v1/ca/issue";

    /**
     * COSIGN - Action 证书重签
     */
    public final static String COSIGN_ACTION_REISSUE = "cosign/api/v1/ca/reIssue";

    /**
     * COSIGN - Action 证书延期
     */
    public final static String COSIGN_ACTION_DELAY = "cosign/api/v1/ca/delay";

    /**
     * COSIGN - Action 证书吊销
     */
    public final static String COSIGN_ACTION_REVOKE = "cosign/api/v1/ca/revoke";

    /**
     * COSIGN - Action 证书申请
     */
    public final static String COSIGN_ACTION_RANDOM = "cosign/api/v1/crypto/random";

    /**
     * COSIGN - Action 证书重签
     */
    public final static String COSIGN_ACTION_GENKEYPAIR = "cosign/api/v1/crypto/keypair/gen";

    /**
     * COSIGN - Action 证书延期
     */
    public final static String COSIGN_ACTION_SIGN = "cosign/api/v1/crypto/sign";

    /**
     * COSIGN - Action 证书吊销
     */
    public final static String COSIGN_ACTION_DECRYPT = "cosign/api/v1/crypto/decrypt";

}
