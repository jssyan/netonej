package com.syan.netonej.http.client.svs;

import com.syan.netonej.common.dict.Action;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneCertList;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 16:05
 * @Description
 */
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
        return new NetoneCertList(super.build());
    }
}
