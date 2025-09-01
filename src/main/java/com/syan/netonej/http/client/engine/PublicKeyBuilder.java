package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ccgw.PublicKeyResponse;

import java.util.HashMap;
import java.util.Map;


public class PublicKeyBuilder extends BaseClient<PublicKeyBuilder> {

    private Integer keyId;

    public PublicKeyBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
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

        return params;
    }


    @Override
    public PublicKeyResponse build() throws NetonejException {
        return new PublicKeyResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_PUB;
    }
}
