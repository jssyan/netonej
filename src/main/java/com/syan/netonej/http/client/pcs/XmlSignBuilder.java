package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.common.dict.SignMode;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetonePCS;
import org.bouncycastle.util.encoders.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:32
 * @Description
 */
public class XmlSignBuilder extends BaseClient<XmlSignBuilder> {

    private String id;
    private IdMagic idmagic = IdMagic.KID;
    private String passwd;
    private String data;
    private Integer sigmode;
    private String algo;

    public XmlSignBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public XmlSignBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public XmlSignBuilder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public XmlSignBuilder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }
    public XmlSignBuilder setData(String data) {
        return setData(data.getBytes());
    }
    public XmlSignBuilder setBase64Data(String data) {
        this.data = data;
        return this;
    }

    public XmlSignBuilder setSigmode(SignMode sigmode) {
        this.sigmode = sigmode.ordinal();
        return this;
    }

    public XmlSignBuilder setAlgo(String algo) {
        this.algo = algo;
        return this;
    }

    public XmlSignBuilder setAlgo(DigestAlgorithm digestAlgorithm) {
        this.algo = digestAlgorithm.getName();
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

        if (sigmode != null) {
            params.put("sigmode", String.valueOf(sigmode));
        }

        if (!NetonejUtil.isEmpty(algo)) {
            params.put("algo", algo.trim().toLowerCase());
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_SXS;
    }

    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }
}
