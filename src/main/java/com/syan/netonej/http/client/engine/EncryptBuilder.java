package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.engine.EncrtptResponse;

import java.util.HashMap;
import java.util.Map;


public class EncryptBuilder extends BaseClient<EncryptBuilder> {

    private Integer keyId;
    //明文
    private String plaintext;

    //对称加密算法类型：SM1,SM4,AES
    private String algorithm;

    //加密模式：CBC,ECB,OFB,CFB,GCM
    private String algorithmMode;

    //填充模式：PKCS5Padding,NoPadding
    private String paddingMode;

    // 初始化向量（Initialization Vector），用于某些加密模式（如CBC）中增强安全性，确保相同明文加密后产生不同的密文
    private String iv;

    //GCM 模式额外的认证参数，（Base64 编码）
    private String aad;

    //GCM 模式生成校验码的长度，默认16字节
    private Integer tlen;

    public EncryptBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public EncryptBuilder setPlaintext(String plaintext) {
        this.plaintext = plaintext;
        return this;
    }

    public EncryptBuilder setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public EncryptBuilder setAlgorithmMode(String algorithmMode) {
        this.algorithmMode = algorithmMode;
        return this;
    }

    public EncryptBuilder setPaddingMode(String paddingMode) {
        this.paddingMode = paddingMode;
        return this;
    }

    public EncryptBuilder setIv(String iv) {
        this.iv = iv;
        return this;
    }

    public EncryptBuilder setAad(String aad) {
        this.aad = aad;
        return this;
    }


    public EncryptBuilder setTlen(Integer tlen) {
        this.tlen = tlen;
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

        if (plaintext != null) {
            params.put("plaintext", plaintext);
        }else {
            throw new NetonejException("plaintext not be empty");
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

        if (aad != null) {
            params.put("aad", aad);
        }

        if (tlen != null) {
            params.put("tlen", String.valueOf(tlen));
        }

        return params;
    }


    @Override
    public EncrtptResponse build() throws NetonejException {
        return new EncrtptResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_ENCRYPT;
    }
}
