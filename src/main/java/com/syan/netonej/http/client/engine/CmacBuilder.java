package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ccgw.CmacResponse;

import java.util.HashMap;
import java.util.Map;


public class CmacBuilder extends BaseClient<CmacBuilder> {

    private Integer keyId;
    //明文
    private String plaintext;

    // 初始化向量（Initialization Vector），用于某些加密模式（如CBC）中增强安全性，确保相同明文加密后产生不同的密文
    private String iv;

    public CmacBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public CmacBuilder setPlaintext(String plaintext) {
        this.plaintext = plaintext;
        return this;
    }

    public CmacBuilder setIv(String iv) {
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

        if (plaintext != null) {
            params.put("plaintext", plaintext);
        }else {
            throw new NetonejException("plaintext not be empty");
        }

        if (iv != null) {
            params.put("iv", iv);
        }

        return params;
    }


    @Override
    public CmacResponse build() throws NetonejException {
        return new CmacResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_CMAC;
    }
}
