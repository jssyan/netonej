package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.engine.AsyDecryptResponse;

import java.util.HashMap;
import java.util.Map;


public class AsyDecryptBuilder extends BaseClient<AsyDecryptBuilder> {

    private Integer keyId;
    //明文
    private String ciphertext;

    public AsyDecryptBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public AsyDecryptBuilder setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
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

        return params;
    }


    @Override
    public AsyDecryptResponse build() throws NetonejException {
        return new AsyDecryptResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_ASYDEC;
    }
}
