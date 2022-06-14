package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetonePCS;
import org.bouncycastle.util.encoders.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:16
 * @Description
 */

public class PrivateKeyBuilder extends BaseClient<PrivateKeyBuilder> {
    private String id;
    private IdMagic idmagic= IdMagic.KID;
    private String passwd;
    private String data;
    private boolean decytpt=false;

    public PrivateKeyBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PrivateKeyBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public PrivateKeyBuilder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public PrivateKeyBuilder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }

    public PrivateKeyBuilder setData(String data) {
        return setData(data.getBytes());
    }

    public PrivateKeyBuilder setBase64Data(String data) {
        this.data = data;
        return this;
    }

    public PrivateKeyBuilder setDecrypt() {
        this.decytpt = true;
        return this;
    }
    public PrivateKeyBuilder setEncrypt() {
        this.decytpt = false;
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
        params.put("data",data);

        if (!NetonejUtil.isEmpty(passwd)) {
            params.put("passwd", passwd);
        }
        if (idmagic != null) {
            params.put("idmagic", idmagic.name().toLowerCase());
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return decytpt? Action.PCS_ACTION_SPD:Action.PCS_ACTION_SPE;
    }

    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }
}
