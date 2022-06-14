package com.syan.netonej.http.client.pcs;

import com.syan.netonej.common.NetonejUtil;
import com.syan.netonej.common.dict.Action;
import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.exception.NetonejException;
import com.syan.netonej.http.client.base.BaseClient;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mmdet
 * @Date 2022-05-05 16:47
 * @Description
 */
public class CMACBuilder extends BaseClient<CMACBuilder> {
    private String id;
    private IdMagic idmagic= IdMagic.KID;
    private String passwd;
    private String factor;//密钥派生因子(BASE64编码）
    private String cipher;//密钥派生算法（比如： sm4, aes128, aes256)等
    private String pubk;//如果设置此项，传入base64格式的客户端的公钥/证书，则服务会用此公钥加密返回密钥派生密钥
    
    public CMACBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public CMACBuilder setIdmagic(IdMagic idmagic) {
        this.idmagic = idmagic;
        return this;
    }

    public CMACBuilder setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public CMACBuilder setFactor(String factor) {
        this.factor = factor;
        return this;
    }

    public CMACBuilder setCipher(String cipher) {
        this.cipher = cipher;
        return this;
    }

    public CMACBuilder setPubk(String pubk) {
        this.pubk = pubk;
        return this;
    }

    @Override
    protected Map<String, String> buildParams() throws NetonejException {
        Map<String, String> params = new HashMap<String, String>();
        if (!NetonejUtil.isEmpty(id)) {
            throw new NetonejException("id not be empty");
        }
        params.put("id", id);

        if (!NetonejUtil.isEmpty(passwd)) {
            params.put("passwd", passwd);
        }
        if (idmagic != null) {
            params.put("idmagic", idmagic.name().toLowerCase());
        }
        params.put("factor", factor);

        params.put("cipher",cipher);

        if (!NetonejUtil.isEmpty(pubk)) {
            params.put("pubk", pubk);
        }
        return params;
    }

    @Override
    protected String buildUrlPath() {
        return Action.PCS_ACTION_CMACKDF;
    }
}
