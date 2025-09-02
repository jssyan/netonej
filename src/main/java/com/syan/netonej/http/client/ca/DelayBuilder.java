package com.syan.netonej.http.client.ca;


import com.syan.netonej.common.dict.CaAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ca.CertificateResponse;

import java.util.HashMap;
import java.util.Map;


public class DelayBuilder extends BaseClient<DelayBuilder> {
    private String serial;

    private String duration;

    public DelayBuilder setSerial(String  serial) {
        this.serial =  serial;
        return this;
    }

    public DelayBuilder setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();
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
        return CaAction.CA_ACTION_DELAY;
    }
}
