package com.syan.netonej.http.client.svs;

import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.ResponseFormat;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneCertList;
import java.util.Map;


public class CertificateListBuilder extends BaseClient<CertificateListBuilder> {

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        return null;
    }

    @Override
    protected String buildUrlPath() {
        return Action.SVS_ACTION_LISTC;
    }

    @Override
    public NetoneCertList build() throws NetonejException {
        setResponseformat("");
        return new NetoneCertList(super.build());
    }
}
