package com.syan.netonej.http.client.svs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneSVS;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 15:54
 * @Description
 */
public class CertificateVerifyBuilder extends BaseClient<CertificateVerifyBuilder> {
    private String id;
    private IdMagic idmagic = IdMagic.KID;
    private String cert;//所需要验证的证书(B64编码). 或者使用(id/idmagic组合)
    private String signts;

    public CertificateVerifyBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public CertificateVerifyBuilder setIdMagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public CertificateVerifyBuilder setCert(String cert) {
        this.cert = cert;
        return this;
    }

    public CertificateVerifyBuilder setSignts(String signts) {
        this.signts = signts;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if (!NetonejUtil.isEmpty(cert)) {
            params.put("cert", cert);
        }else{
            if (!NetonejUtil.isEmpty(id)) {
                params.put("id", id);
                params.put("idmagic", idmagic.name().toLowerCase());
            }
        }
        if(!NetonejUtil.isEmpty(signts)){
            params.put("signts", signts);
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.SVS_ACTION_VC;
    }

    @Override
    public NetoneSVS build() throws NetonejException {
        return new NetoneSVS(super.build());
    }
}
