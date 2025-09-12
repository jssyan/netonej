package com.syan.netonej.http.client.cosign;


import com.syan.netonej.common.dict.CosignAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.cosign.RandResponse;

import java.util.HashMap;
import java.util.Map;


public class GenRandBuilder extends BaseClient<GenRandBuilder> {
    private String appid;

    private String transid;

    private String sign;

    private Integer length;

    public GenRandBuilder setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public GenRandBuilder setTransid(String transid) {
        this.transid = transid;
        return this;
    }

    public GenRandBuilder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public GenRandBuilder setLength(Integer length) {
        this.length = length;
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

        if(length!=null){
            params.put("length",String.valueOf(length));
        }else{
            throw new NetonejException("length not be empty");
        }

        return params;
    }


    @Override
    public RandResponse build() throws NetonejException {
        return new RandResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CosignAction.COSIGN_ACTION_RANDOM;
    }
}
