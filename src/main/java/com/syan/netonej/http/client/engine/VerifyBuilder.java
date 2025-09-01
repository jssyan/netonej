package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.engine.VerifyResponse;

import java.util.HashMap;
import java.util.Map;


public class VerifyBuilder extends BaseClient<VerifyBuilder> {

    private Integer keyId;

    //待签名数据
    private String message;

    //数据类型
    private String messageType;

    //签名
    private String signature;

    public VerifyBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public VerifyBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public VerifyBuilder setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public VerifyBuilder setSignature(String signature) {
        this.signature = signature;
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

        if (message != null) {
            params.put("message", message);
        }else {
            throw new NetonejException("message not be empty");
        }

        if (messageType != null) {
            params.put("messageType", messageType);
        }

        if (signature != null) {
            params.put("signature", signature);
        }else {
            throw new NetonejException("signature not be empty");
        }

        return params;
    }


    @Override
    public VerifyResponse build() throws NetonejException {
        return new VerifyResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_VERIFY;
    }
}
