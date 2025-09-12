package com.syan.netonej.http.client.cosign;


import com.syan.netonej.common.dict.CosignAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.cosign.KeypairResponse;

import java.util.HashMap;
import java.util.Map;


public class GenKeyBuilder extends BaseClient<GenKeyBuilder> {
    private String appid;

    private String transid;

    private String sign;

    private String P1;

    public GenKeyBuilder setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public GenKeyBuilder setTransid(String transid) {
        this.transid = transid;
        return this;
    }

    public GenKeyBuilder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public GenKeyBuilder setP1(String P1) {
        this.P1 = P1;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();
        if(appid!=null){
            params.put("appid",appid);
        }else{
            throw new NetonejException("appid not be empty");
        }

        if(transid!=null){
            params.put("transid",transid);
        }else{
            throw new NetonejException("transid not be empty");
        }

        if(sign!=null){
            params.put("sign",sign);
        }else{
            throw new NetonejException("sign not be empty");
        }

        if(P1!=null){
            params.put("P1",P1);
        }else{
            throw new NetonejException("P1 not be empty");
        }

        return params;
    }


    @Override
    public KeypairResponse build() throws NetonejException {
        return new KeypairResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CosignAction.COSIGN_ACTION_GENKEYPAIR;
    }
}
