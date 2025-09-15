package com.syan.netonej.http.client.cosign;


import com.syan.netonej.common.dict.CosignAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ca.CertificateResponse;
import com.syan.netonej.http.entity.ca.RevokeResponse;

import java.util.HashMap;
import java.util.Map;


public class RevokeCertBuilder extends BaseClient<RevokeCertBuilder> {
    private String appid;

    private String transid;

    private String sign;

    private String kid;

    //原签名证书序列号（十六进制）
    private String serial;

    public RevokeCertBuilder setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public RevokeCertBuilder setTransid(String transid) {
        this.transid = transid;
        return this;
    }

    public RevokeCertBuilder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public RevokeCertBuilder setKid(String kid) {
        this.kid = kid;
        return this;
    }

    public RevokeCertBuilder setSerial(String serial) {
        this.serial = serial;
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

        if(serial!=null){
            params.put("serial",serial);
        }else{
            throw new NetonejException("serial not be empty");
        }

        return params;
    }


    @Override
    public RevokeResponse build() throws NetonejException {
        return new RevokeResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CosignAction.COSIGN_ACTION_REVOKE;
    }
}
