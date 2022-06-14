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
public class CertificateDeleteBuilder extends BaseClient<CertificateDeleteBuilder> {

    private String id;

    private IdMagic idmagic = IdMagic.KID;

    private boolean revoked = false;

    public CertificateDeleteBuilder setRevoked(boolean revoked) {
        this.revoked = revoked;
        return this;
    }

    public CertificateDeleteBuilder setId(String data) {
        this.id = data;
        return this;
    }

    public CertificateDeleteBuilder setIdMagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }


    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String,String> params = new HashMap<String,String>();
        params.put("module", "pki");
        if(revoked){
            params.put("action", "delrevoked");
        }else{
            params.put("action", "deltrusted");
        }
        params.put("idmagic", idmagic.name().toLowerCase());
        params.put("data", id);
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.EAPI_ACTION;
    }
}
