package com.syan.netonej.http.client.eapi;

import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneCertList;
import com.syan.netonej.http.entity.NetoneResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 16:05
 * @Description
 */
public class CertificateListBuilder extends BaseClient<CertificateListBuilder> {

    private boolean revoked = false;

    public CertificateListBuilder setRevoked(boolean revoked) {
        this.revoked = revoked;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("module", "pki");
        if(revoked){
            params.put("action", "listrevoked");
        }else{
            params.put("action", "listtrusted");
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.EAPI_ACTION;
    }

    @Override
    public NetoneCertList build() throws NetonejException {
        NetoneResponse response = super.build();
        response.setFormat(ResponseFormat.JSON);
        return new NetoneCertList(response);
    }
}
