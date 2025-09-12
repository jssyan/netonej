package com.syan.netonej.http.client.cosign;


import com.syan.netonej.common.dict.CosignAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.cosign.SignatureResponse;

import java.util.HashMap;
import java.util.Map;


public class SignatureBuilder extends BaseClient<SignatureBuilder> {
    private String appid;

    private String transid;

    private String sign;

    private String kid;

    private String e;

    private String Q1;

    public SignatureBuilder setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public SignatureBuilder setTransid(String transid) {
        this.transid = transid;
        return this;
    }

    public SignatureBuilder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public SignatureBuilder setKid(String kid) {
        this.kid = kid;
        return this;
    }

    public SignatureBuilder setE(String e) {
        this.e = e;
        return this;
    }

    public SignatureBuilder setQ1(String q1) {
        Q1 = q1;
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

        if(kid!=null){
            params.put("kid",kid);
        }else{
            throw new NetonejException("kid not be empty");
        }

        if(e!=null){
            params.put("e",e);
        }else{
            throw new NetonejException("e not be empty");
        }

        if(Q1!=null){
            params.put("Q1",Q1);
        }else{
            throw new NetonejException("Q1 not be empty");
        }

        return params;
    }


    @Override
    public SignatureResponse build() throws NetonejException {
        return new SignatureResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CosignAction.COSIGN_ACTION_SIGN;
    }
}
