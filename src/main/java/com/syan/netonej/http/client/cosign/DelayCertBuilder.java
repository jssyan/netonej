package com.syan.netonej.http.client.cosign;


import com.syan.netonej.common.dict.CosignAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ca.CertificateResponse;

import java.util.HashMap;
import java.util.Map;


public class DelayCertBuilder extends BaseClient<DelayCertBuilder> {
    private String appid;

    private String transid;

    private String sign;

    private String kid;

    //原签名证书序列号（十六进制）
    private String serial;

    private String duration;

    public DelayCertBuilder setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public DelayCertBuilder setTransid(String transid) {
        this.transid = transid;
        return this;
    }

    public DelayCertBuilder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public DelayCertBuilder setKid(String kid) {
        this.kid = kid;
        return this;
    }

    public DelayCertBuilder setSerial(String serial) {
        this.serial = serial;
        return this;
    }

    public DelayCertBuilder setDuration(String duration) {
        this.duration = duration;
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

        if(duration!=null){
            params.put("duration",duration);
        }else{
            throw new NetonejException("duration not be empty");
        }

        return params;
    }


    @Override
    public CertificateResponse build() throws NetonejException {
        return new CertificateResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CosignAction.COSIGN_ACTION_DELAY;
    }
}
