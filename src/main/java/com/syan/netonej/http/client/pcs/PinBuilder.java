package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetonePCS;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:37
 * @Description
 */
public class PinBuilder extends BaseClient<PinBuilder> {
    private String id;
    private IdMagic idmagic = IdMagic.KID;
    private String oldpwd;
    private String newpwd;
    
    public PinBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PinBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public PinBuilder setOldpwd(String oldpwd) {
        this.oldpwd = oldpwd;
        return this;
    }

    public PinBuilder setNewpwd(String newpwd) {
        this.newpwd = newpwd;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if (NetonejUtil.isEmpty(id)) {
            throw new NetonejException("id not be empty");
        }
        params.put("id", id);
        params.put("oldpwd", oldpwd);
        params.put("newpwd", newpwd);
        if (idmagic != null) {
            params.put("idmagic", idmagic.name().toLowerCase());
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_CHPWD;
    }

    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }
}
