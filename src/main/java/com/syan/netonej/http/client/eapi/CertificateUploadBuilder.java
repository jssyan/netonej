package com.syan.netonej.http.client.eapi;

import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 16:05
 * @Description
 */
public class CertificateUploadBuilder extends BaseClient<CertificateUploadBuilder> {

    private String cert;

    private boolean revoked = false;

    public CertificateUploadBuilder setRevoked(boolean revoked) {
        this.revoked = revoked;
        return this;
    }

    public CertificateUploadBuilder setCert(String cert) {
        this.cert = cert;
        return this;
    }


    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String,String> params = new HashMap<String,String>();
        params.put("module", "pki");
        if(revoked){
            params.put("action", "addrevoked");
        }else{
            params.put("action", "addtrusted");
        }
        params.put("data", cert);
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.EAPI_ACTION;
    }
}
