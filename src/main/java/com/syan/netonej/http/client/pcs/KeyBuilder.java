package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.KeyAlgorithm;
import com.syan.netonej.common.dict.KeyUseage;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneKeyList;
import com.syan.netonej.http.entity.NetoneResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 11:21
 * @Description
 */
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
        return Action.PCS_ACTION_SL;
    }
}
