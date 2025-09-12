package com.syan.netonej.http.client.cosign;



import com.syan.netonej.common.dict.CosignAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.cosign.DecryptionResponse;


import java.util.HashMap;
import java.util.Map;


public class DecryptionBuilder extends BaseClient<DecryptionBuilder> {
    private String appid;

    private String transid;

    private String sign;

    private String kid;

    private String T1;

    public DecryptionBuilder setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public DecryptionBuilder setTransid(String transid) {
        this.transid = transid;
        return this;
    }

    public DecryptionBuilder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public DecryptionBuilder setKid(String kid) {
        this.kid = kid;
        return this;
    }

    public DecryptionBuilder setT1(String t1) {
        T1 =t1;
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

        if(T1!=null){
            params.put("T1",T1);
        }else{
            throw new NetonejException("T1 not be empty");
        }

        return params;
    }


    @Override
    public DecryptionResponse build() throws NetonejException {
        return new DecryptionResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CosignAction.COSIGN_ACTION_DECRYPT;
    }
}
