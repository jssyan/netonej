package com.syan.netonej.http.client.engine;

import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ccgw.SignResponse;

import java.util.HashMap;
import java.util.Map;


public class SignBuilder extends BaseClient<SignBuilder> {

    private Integer keyId;

    //待签名数据
    private String message;

    //数据类型
    private String messageType;

    public SignBuilder setKeyId(Integer keyId) {
        this.keyId = keyId;
        return this;
    }

    public SignBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public SignBuilder setMessageType(String messageType) {
        this.messageType = messageType;
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

        return params;
    }


    @Override
    public SignResponse build() throws NetonejException {
        return new SignResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_SIGN;
    }
}
