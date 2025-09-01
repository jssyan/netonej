package com.syan.netonej.http.client.engine;


import com.syan.netonej.common.dict.EngineAction;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.ccgw.RandomResponse;

import java.util.HashMap;
import java.util.Map;


public class RandBuilder extends BaseClient<RandBuilder> {
    private int len;

    public RandBuilder setLength(int length) {
        this.len = length;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();
        if(len > 0){
            params.put("len",String.valueOf(len));
            return params;
        }else{
            throw new NetonejException(" 随机数的长度，必须大于0");
        }
    }


    @Override
    public RandomResponse build() throws NetonejException {
        return new RandomResponse(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return EngineAction.ENGINE_ACTION_RANDOM;
    }
}
