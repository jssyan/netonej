package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.engine.DecrtptResponse;

import java.util.HashMap;
import java.util.Map;


public class DecryptBuilder extends BaseClient<DecryptBuilder> {

    private Integer keyId;
    //密文
    private String ciphertext;

    //对称加密算法类型：SM1,SM4,AES
    private String algorithm;

    //加密模式：CBC,ECB,OFB,CFB
    private String algorithmMode;

    //填充模式：PKCS5Padding,NoPadding
    private String paddingMode;

    // 初始化向量（Initialization Vector），用于某些加密模式（如CBC）中增强安全性，确保相同明文加密后产生不同的密文
    private String iv;

    public DecryptBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public DecryptBuilder setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
        return this;
    }

    public DecryptBuilder setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public DecryptBuilder setAlgorithmMode(String algorithmMode) {
        this.algorithmMode = algorithmMode;
        return this;
    }

    public DecryptBuilder setPaddingMode(String paddingMode) {
        this.paddingMode = paddingMode;
        return this;
    }

    public DecryptBuilder setIv(String iv) {
        this.iv = iv;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();

        if (keyId != null) {
            params.put("keyId", String.valueOf(keyId));
        }else {
            throw new NetonejException("keyId not be empty");
        }

        if (ciphertext != null) {
            params.put("ciphertext", ciphertext);
        }else {
            throw new NetonejException("ciphertext not be empty");
        }

        if (algorithm != null) {
            params.put("algorithm", algorithm);
        }

        if (algorithmMode != null) {
            params.put("algorithmMode", algorithmMode);
        }

        if (paddingMode != null) {
            params.put("paddingMode", paddingMode);
        }

        if (iv != null) {
            params.put("iv", iv);
        }

        return params;
    }


    @Override
    public DecrtptResponse build() throws NetonejException {
        return new DecrtptResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_DECRYPT;
    }
}
