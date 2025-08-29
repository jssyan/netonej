package com.syan.netonej.http.client.ccgw.pcs;

import com.syan.netonej.common.dict.*;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.ccgw.base.BaseClient;
import com.syan.netonej.http.entity.NetoneKeyList;

import java.util.HashMap;
import java.util.Map;


public class KeyBuilder extends BaseClient<KeyBuilder> {
    private Integer limit;
    private KeyAlgorithm keyAlgorithm;
    private KeyUseage keyUseage;

    public KeyBuilder setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public KeyBuilder setKeyAlgorithm(KeyAlgorithm keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
        return this;
    }

    public KeyBuilder setKeyUseage(KeyUseage keyUseage) {
        this.keyUseage = keyUseage;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();
        if(limit != null){
            params.put("limit",String.valueOf(limit));
        }
        if(keyAlgorithm != null){
            params.put("algo",String.valueOf(keyAlgorithm.getValue()));
        }
        if(keyUseage != null){
            params.put("useage",String.valueOf(keyUseage.getValue()));
        }
        return params;
    }


    @Override
    public NetoneKeyList build() throws NetonejException {
        return new NetoneKeyList(super.build());
    }

    @Override
    protected String buildUrlPath() {
        return "ccgw/api/"+ ApiVersion.VERSION+"/"+ ModuleName.PCS_MODULE+"/"+Action.PCS_ACTION_SL;
    }
}
