package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetonePCS;
import org.bouncycastle.util.encoders.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:11
 * @Description
 */
public class EnvelopeUnpackBuilder extends BaseClient<EnvelopeUnpackBuilder> {

    private String id;
    private IdMagic idmagic;
    private String passwd;
    private String data;
    private String digestAlgo;
    

    public EnvelopeUnpackBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public EnvelopeUnpackBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public EnvelopeUnpackBuilder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public EnvelopeUnpackBuilder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }

    public EnvelopeUnpackBuilder setData(String data) {
        return setData(data.getBytes());
    }

    public EnvelopeUnpackBuilder setBase64Data(String data) {
        this.data = data;
        return this;
    }

    public EnvelopeUnpackBuilder setDigestAlgo(String digestAlgo) {
        this.digestAlgo = digestAlgo;
        return this;
    }

    public EnvelopeUnpackBuilder setDigestAlgo(DigestAlgorithm digestAlgo) {
        this.digestAlgo = digestAlgo.getName();
        return this;
    }
    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if (NetonejUtil.isEmpty(id)) {
            throw new NetonejException("id not be empty");
        }
        params.put("id", id);
        if (data == null) {
            throw new NetonejException("data not be empty");
        }
        params.put("data", data);

        if (!NetonejUtil.isEmpty(passwd)) {
            params.put("passwd", passwd);
        }
        if (idmagic != null) {
            params.put("idmagic", idmagic.name().toLowerCase());
        }
        if (!NetonejUtil.isEmpty(digestAlgo)) {
            params.put("algo", digestAlgo);
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_ENVOPEN;
    }

    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }
}

