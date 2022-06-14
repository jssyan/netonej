package com.syan.netonej.http.client.svs;

import com.syan.netonej.common.dict.Action;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.SVSClient;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetoneResponse;
import com.syan.netonej.http.entity.NetoneSVS;
import org.bouncycastle.util.encoders.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-06 16:56
 * @Description
 */
public class XMLSignVerifyBuilder extends BaseClient<XMLSignVerifyBuilder> {

    private String data;
    

    public XMLSignVerifyBuilder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }
    public XMLSignVerifyBuilder setBase64Data(String data) {
        this.data = data;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("data", data);
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.SVS_ACTION_VX;
    }

    @Override
    public NetoneSVS build() throws NetonejException {
        return new NetoneSVS(super.build());
    }
}
