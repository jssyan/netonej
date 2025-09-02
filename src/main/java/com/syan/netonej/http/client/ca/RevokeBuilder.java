package com.syan.netonej.http.client.ca;


import com.syan.netonej.common.dict.CaAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ca.RevokeResponse;

import java.util.HashMap;
import java.util.Map;


public class RevokeBuilder extends BaseClient<RevokeBuilder> {
    private String serial;

    public RevokeBuilder setSerial(String  serial) {
        this.serial =  serial;
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

        return params;
    }


    @Override
    public RevokeResponse build() throws NetonejException {
        return new RevokeResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return CaAction.CA_ACTION_REVOKE;
    }
}
