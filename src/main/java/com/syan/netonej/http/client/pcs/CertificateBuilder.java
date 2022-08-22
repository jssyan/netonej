package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetoneCertificate;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:01
 * @Description
 */
public class CertificateBuilder extends BaseClient<CertificateBuilder> {

    private String id;
    private IdMagic idmagic= IdMagic.KID;
    private String usage="1";

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        if (idmagic != null) {
            params.put("idmagic", idmagic.name().toLowerCase());
        }
        params.put("usage", usage);
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_SG;
    }

    public CertificateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public CertificateBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public CertificateBuilder setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    @Override
    public NetoneCertificate build() throws NetonejException {
        return new NetoneCertificate(super.build());
    }
}
