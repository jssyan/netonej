package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.DataType;
import com.syan.netonej.common.dict.DigestAlgorithm;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import com.syan.netonej.http.entity.NetonePCS;
import org.bouncycastle.util.encoders.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 15:39
 * @Description
 */
public class PKCS1Builder extends BaseClient<PKCS1Builder> {
    private String id;
    private IdMagic idmagic= IdMagic.KID;
    private String passwd;
    private String data;
    private DataType dataType = DataType.PLAIN;
    private String algo;
    private byte[] signerid;

    @Override
    protected Map<String, String> buildParams() throws NetonejException{
        Map<String, String> params = new HashMap<String, String>();
        if (NetonejUtil.isEmpty(id)) {
            throw new NetonejException("id not be empty");
        }
        params.put("id", id);
        if (data == null) {
            throw new NetonejException("data not be empty");
        }
        params.put("data", data);
        params.put("datatype", String.valueOf(dataType.ordinal()));

        if (!NetonejUtil.isEmpty(passwd)) {
            params.put("passwd", passwd);
        }
        if (idmagic != null) {
            params.put("idmagic", idmagic.name().toLowerCase());
        }
        if (!NetonejUtil.isEmpty(algo)) {
            params.put("algo", algo.trim().toLowerCase());
        }
        if (signerid != null) {
            params.put("signerid", Base64.toBase64String(signerid));
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_SMP1;
    }

    public PKCS1Builder setId(String id) {
        this.id = id;
        return this;
    }

    public PKCS1Builder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public PKCS1Builder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public PKCS1Builder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }

    public PKCS1Builder setData(String data) {
        return setData(data.getBytes());
    }

    public PKCS1Builder setBase64Data(String data) {
        this.data = data;
        return this;
    }

    public PKCS1Builder setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public PKCS1Builder setAlgo(String algo) {
        this.algo = algo;
        return this;
    }

    public PKCS1Builder setAlgo(DigestAlgorithm digestAlgorithm) {
        this.algo = digestAlgorithm.getName();
        return this;
    }

    public PKCS1Builder setUserId(byte[] userId) {
        this.signerid = userId;
        return this;
    }


    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }
}
