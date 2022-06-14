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
 * @Date 2022-05-05 15:51
 * @Description
 */
public class PKCS7Builder extends BaseClient<PKCS7Builder> {

    private String id;
    private IdMagic idmagic= IdMagic.KID;
    private String passwd;
    private String data;//BASE64编码的待签名数据，数据长度不限
    private Boolean fullchain;//是否嵌入整个证书链。可以是（0-false,1-true）。缺省=0
    private Boolean attach;//签名结果中是否包含原始数据。可以是（0-false,1-true）。缺省=1
    private String algo;
    private Boolean noattr;//签名结果中是否包含签名时间等属性。可以是（0-false,1-true）。缺省=服务端设置

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
        if (!NetonejUtil.isEmpty(algo)) {
            params.put("algo", algo.trim().toLowerCase());
        }
        if (fullchain != null) {
            params.put("fullchain", fullchain?"1":"0");
        }
        if (attach != null) {
            params.put("attach", attach?"1":"0");
        }
        if (noattr != null) {
            params.put("noattr", noattr?"1":"0");
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_SMP7;
    }

    public PKCS7Builder setId(String id) {
        this.id = id;
        return this;
    }

    public PKCS7Builder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public PKCS7Builder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public PKCS7Builder setData(byte[] data) {
        this.data = Base64.toBase64String(data);
        return this;
    }

    public PKCS7Builder setData(String data) {
        return setData(data.getBytes());
    }

    public PKCS7Builder setBase64Data(String data) {
        this.data = data;
        return this;
    }

    public PKCS7Builder setAlgo(String algo) {
        this.algo = algo;
        return this;
    }

    public PKCS7Builder setAlgo(DigestAlgorithm digestAlgorithm) {
        this.algo = digestAlgorithm.getName();
        return this;
    }

    public PKCS7Builder setFullchain(Boolean fullchain) {
        this.fullchain = fullchain;
        return this;
    }

    public PKCS7Builder setAttach(Boolean attach) {
        this.attach = attach;
        return this;
    }

    public PKCS7Builder setNoattr(Boolean noattr) {
        this.noattr = noattr;
        return this;
    }

    @Override
    public NetonePCS build() throws NetonejException {
        return new NetonePCS(super.build());
    }
}
