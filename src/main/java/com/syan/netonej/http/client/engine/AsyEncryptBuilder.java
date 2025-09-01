package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ccgw.AsyEncryptResponse;

import java.util.HashMap;
import java.util.Map;


public class AsyEncryptBuilder extends BaseClient<AsyEncryptBuilder> {

    private Integer keyId;
    //明文
    private String plaintext;

    public AsyEncryptBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public AsyEncryptBuilder setPlaintext(String plaintext) {
        this.plaintext = plaintext;
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

        return params;
    }


    @Override
    public AsyEncryptResponse build() throws NetonejException {
        return new AsyEncryptResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_ASYENC;
    }
}
