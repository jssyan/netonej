package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.KeyAlgorithm;
import com.syan.netonej.common.dict.KeyUseage;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetonePCS;

import java.util.HashMap;
import java.util.Map;


public class RandomBuilder extends BaseClient<RandomBuilder> {
    private int length;

    public RandomBuilder setLength(int length) {
        this.length = length;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();
        if(length > 0){
            params.put("length",String.valueOf(length));
            return params;
        }else{
            throw new NetonejException(" 随机数的长度，必须大于0");
        }
    }


    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_RANDON;
    }
}
